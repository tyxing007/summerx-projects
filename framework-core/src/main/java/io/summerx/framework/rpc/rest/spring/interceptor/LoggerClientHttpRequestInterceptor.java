package io.summerx.framework.rpc.rest.spring.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * LoggerInterceptor
 *
 * @author summerx
 * @Date 2016-07-14
 */
public class LoggerClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = null;
        try {
            response = execution.execute(request, body);
            return response;
        } catch (Throwable th) {
            logger.info("=== 请求异常");
            logger.info(th.getMessage());
            throw th;
        } finally {
            logger.info("=== 响应");
            if (response != null && response.getBody() != null) {
                logger.info(String.format("%s %s", response.getRawStatusCode(), response.getStatusText()));
            }
        }
    }
}
