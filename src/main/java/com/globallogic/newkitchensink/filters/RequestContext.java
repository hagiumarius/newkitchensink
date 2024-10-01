package com.globallogic.newkitchensink.filters;

public class RequestContext {
    private static final ThreadLocal<String> correlationId = new ThreadLocal<>();

    public static void setCorrelationId(String user) {
        correlationId.set(user);
    }

    public static void clear() {
        correlationId.remove();
    }
}
