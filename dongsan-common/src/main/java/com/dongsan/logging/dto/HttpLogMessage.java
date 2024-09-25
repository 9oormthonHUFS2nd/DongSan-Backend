package com.dongsan.logging.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.Collections;
import java.util.stream.Collectors;

@Builder
public record HttpLogMessage(
        String httpMethod,
        String requestURI,
        HttpStatus httpStatus,
        String clientIp,
        double elapsedTime,
        String headers,
        String requestParam,
        String requestBody,
        String responseBody
) {
    public static HttpLogMessage createInstance(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper, double elapsedTime){
        String headers = Collections.list(requestWrapper.getHeaderNames())
                .stream()
                .map(header -> header + " : "+requestWrapper.getHeader(header))
                .collect(Collectors.joining(", "));

        String requestBody = new String(requestWrapper.getContentAsByteArray());
        String responseBody = new String(responseWrapper.getContentAsByteArray());

        return HttpLogMessage.builder()
                .httpMethod(requestWrapper.getMethod())
                .requestURI(requestWrapper.getRequestURI())
                .httpStatus(HttpStatus.valueOf(responseWrapper.getStatus()))
                .clientIp(requestWrapper.getRemoteAddr())
                .elapsedTime(elapsedTime)
                .headers(headers)
                .requestParam(requestWrapper.getQueryString())
                .requestBody(requestBody)
                .responseBody(responseBody)
                .build();
    }

    public String toPrettierLog(){
        return String.format(
                """
                     
                |[REQUEST] %s %s %s (%.3f)
                |>> CLIENT_IP: %s
                |>> HEADERS: %s
                |>> REQUEST_PARAM: %s
                |>> REQUEST_BODY: %s
                |>> RESPONSE_BODY: %s
                """,
                this.httpMethod,
                this.requestURI,
                this.httpStatus,
                this.elapsedTime,
                this.clientIp,
                this.headers,
                this.requestParam,
                this.requestBody,
                this.responseBody
        );
    }
}
