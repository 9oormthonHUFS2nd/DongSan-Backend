package com.dongsan.logging;

import com.dongsan.logging.dto.HttpLogMessage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class ReqResLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper cachingResponseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(cachingRequestWrapper, cachingResponseWrapper);
        long endTime = System.currentTimeMillis();

        try{
            HttpLogMessage httpLogMessage = HttpLogMessage.createInstance(cachingRequestWrapper, cachingResponseWrapper, (endTime-startTime)/1000.0);
            log.info(httpLogMessage.toPrettierLog());
        } catch (Exception e){
            log.warn("[Logging] " + this.getClass().getSimpleName() + "Logging 실패");
        }
    }
}
