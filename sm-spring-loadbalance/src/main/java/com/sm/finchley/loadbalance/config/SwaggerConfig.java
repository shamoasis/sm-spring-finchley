package com.sm.finchley.loadbalance.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;


/**
 * @author lmwl
 */
@Configuration
@EnableSwagger2
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SwaggerConfig {
    /**
     * 配置swagger2
     * 注册一个bean属性
     * swagger2其实就是重新写一个构造器，因为他没有get set方法\
     * enable() 是否启用swagger false swagger不能再浏览器中访问
     * groupName()配置api文档的分组  那就注册多个Docket实例 相当于多个分组
     *
     * @return
     */
    @Bean
    public Docket docket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("default")
                .enable(true)
                .select()
                /**
                 * RequestHandlerSelectors配置扫描接口的方式
                 *      basePackage 配置要扫描的包
                 *      any 扫描全部
                 *      none 不扫描
                 *      withClassAnnotation 扫描类上的注解
                 *      withMethodAnnotation 扫描方法上的注解
                 */
                .apis(RequestHandlerSelectors.basePackage("com.sm.sc"))
                /**
                 * paths() 扫描过滤方式
                 *      any过滤全部
                 *      none不过滤
                 *      regex正则过滤
                 *      ant过滤指定路径
                 */
                //                .paths(PathSelectors.ant("/login/**"))
                .build();
    }

    /**
     * 配置swagger2信息 =apiInfo
     *
     * @return
     */
    public ApiInfo apiInfo() {
        /*作者信息*/
        //        Contact contact = new Contact("XXX", "http://baidu.com", "email");
        Contact contact = new Contact("damo", "https://www.damo.icu/", "lmwl@live.com");
        return new ApiInfo(
                "SpringCloudAPI接口",
                "微服务接口",
                "V4.0",
                "urn:toVs",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
}
