package com.globallogic.newkitchensink.filters;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * Interceptor to inject(and clean) the correlation id into the thread local
 * Correlation Id meant to tie together the same business request across multiple services http interactions
 * Similar interceptor/filters may be used for logging, business context passing, authn, cors ...
 */
@Component
public class CorrelationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String correlationId = request.getHeader("correlation");
        if (correlationId != null) {
            RequestContext.setCorrelationId(correlationId);
        } else {
            RequestContext.setCorrelationId(UUID.randomUUID().toString());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Clear the context once the request is complete
        RequestContext.clear();
    }
}
