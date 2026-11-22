package org.example.servlet;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(
        value = "/internal/*",
        dispatcherTypes = {
                DispatcherType.REQUEST,
                DispatcherType.FORWARD
        }
)
public class InternalServletFilter implements Filter {

    private static final String INTERNAL_DISPATCH_ATTRIBUTE =
            "internalDispatch";

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        boolean internalDispatch = Boolean.TRUE.equals(httpRequest.getAttribute(INTERNAL_DISPATCH_ATTRIBUTE));

        if (!internalDispatch) {
            httpResponse.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Direct access to internal endpoints is forbidden"
            );

            return;
        }

        chain.doFilter(request, response);
    }
}