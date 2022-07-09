package com.sm.finchley.loadbalance.support;


/**
 * @author lmwl
 */
public class GrayFilterContextHolder {

    /**
     * Stores the {@link GrayFilterContext} for current thread.
     */
    private static final ThreadLocal<GrayFilterContext> contextHolder = ThreadLocal.withInitial(() -> new DefaultGrayFilterContext());

    /**
     * Retrieves the current thread bound instance of {@link GrayFilterContext}.
     *
     * @return the current context
     */
    public static GrayFilterContext getCurrentContext() {
        return contextHolder.get();
    }

    /**
     * Clears the current context.
     */
    public static void clearCurrentContext() {
        contextHolder.remove();
    }
}