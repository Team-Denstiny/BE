package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Enumeration;
@Slf4j
public class LoggerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper res = new ContentCachingResponseWrapper((HttpServletResponse) response);

        chain.doFilter(req,res);

        // doFilter 이후의  [request] 정보

        Enumeration<String> headerNames = req.getHeaderNames();
        StringBuilder headerValues = new StringBuilder();

        headerNames.asIterator().forEachRemaining(headerKey -> {
            String headerValue = req.getHeader(headerKey);

            // [authorization-token : ???] [user-agent : ???] , ... 의 형태로 로그 출력
            headerValues
                    .append("[")
                    .append(headerKey)
                    .append(" : ")
                    .append(headerValues)
                    .append("] ");

        });

        String requestBody = new String(req.getContentAsByteArray());
        String uri = req.getRequestURI();
        String method = req.getMethod();

        log.info(">>>>> uri : {} , method : {} , header : {} , body : {}",uri,method, headerValues.toString(),requestBody);

        // [response] 정보
        StringBuilder responseHeaderValues = new StringBuilder();

        res.getHeaderNames().forEach(headerKey ->{
            String headerValue = res.getHeader(headerKey);

            responseHeaderValues
                    .append("[")
                    .append(headerKey)
                    .append(" : ")
                    .append(headerValue)
                    .append("] ");
        });

        String responseBody = new String(res.getContentAsByteArray());

        log.info("<<<<< uri : {}, method : {} , header : {} , body : {}",uri, method, responseHeaderValues, responseBody);

        // 중요! -> 빼먹으면 안 실려서 response로 이동
        res.copyBodyToResponse();

    }
}
