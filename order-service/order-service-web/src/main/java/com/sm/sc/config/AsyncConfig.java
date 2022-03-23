package com.sm.sc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lmwl
 */
@Configuration
@Slf4j
public class AsyncConfig {
    private static final String ASYNC_EXECUTOR_NAME = "asyncExecutor";

    @Bean(name = ASYNC_EXECUTOR_NAME)
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(ASYNC_EXECUTOR_NAME);
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(200);
        executor.setThreadNamePrefix("asyncConfigTask-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean，这样这些异步任务的销毁就会先于Redis线程池的销毁
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        executor.setAwaitTerminationSeconds(60);
        // 这里在每次异步调用的时候, 会包装一下.
        executor.setTaskDecorator(runnable -> {
            // 这个时候还是同步状态
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            log.info("executed by AsyncConfig");
            // 返回的这个 runnable对象 才是去调用线程池.
            return () -> {
                try {
                    // 我们set 进去 ,其实是一个ThreadLocal维护的.
                    RequestContextHolder.setRequestAttributes(requestAttributes);
                    runnable.run();
                } finally {
                    // 最后记得释放内存
                    RequestContextHolder.resetRequestAttributes();
                }
            };
        });
        return executor;
    }
}
