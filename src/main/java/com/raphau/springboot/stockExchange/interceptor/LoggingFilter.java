package com.raphau.springboot.stockExchange.interceptor;

import com.google.gson.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.list;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {
    public static String SANITIZED_VALUE = "****";

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        ContentCachingResponseWrapper responseWrapper = responseWrapper(response);
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        log.info("Incoming request:\n" +
                        "ID: {}\n" +
                        "Method: {}\n" +
                        "URI: {}\n" +
                        "Headers: [{}]\n" +
                        "Body: [{}]",
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                headersToString(list(request.getHeaderNames()), request::getHeader),
                sanitizeJson(getRequestBody(request)));

        try {
            chain.doFilter(request, responseWrapper);
        } finally {
            log.info("Outgoing response:\n" +
                            "ID: {}\n" +
                            "Headers: [{}]\n" +
                            "Body: [{}]",
                    requestId,
                    headersToString(response.getHeaderNames(), response::getHeader),
                    sanitizeJson(new String(responseWrapper.getContentAsByteArray())));
            responseWrapper.copyBodyToResponse();
        }
    }

    private String getRequestBody(ServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    private ContentCachingRequestWrapper requestWrapper(ServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper requestWrapper) {
            return requestWrapper;
        }
        return new ContentCachingRequestWrapper((HttpServletRequest) request);
    }

    private ContentCachingResponseWrapper responseWrapper(ServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper responseWrapper) {
            return responseWrapper;
        }
        return new ContentCachingResponseWrapper((HttpServletResponse) response);
    }

    private String headersToString(Collection<String> headerNames, Function<String, String> headerValueResolver) {
        return headerNames.stream()
                .map(headerName -> {
                    String header = headerValueResolver.apply(headerName);
                    if (headerName.toLowerCase().endsWith("token")) {
                        header = SANITIZED_VALUE;
                    }
                    return String.format("\"%s=%s\"", headerName, header);
                })
                .collect(Collectors.joining(", "));
    }

    private String sanitizeJson(String body) {
        if (body.isEmpty()) return body;
        try {
            JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();
            maskSensitiveData(jsonObject, "password");
            maskSensitiveData(jsonObject, "token");

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(jsonObject);
        } catch (JsonSyntaxException e) {
            return body;
        }
    }

    private void maskSensitiveData(JsonObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            jsonObject.addProperty(key, SANITIZED_VALUE);
        }
    }
}

