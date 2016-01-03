package com.hantsylabs.restexample.springmvc.test.integration;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Base64;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * copy from https://github.com/izeye/samples-spring-boot-branches/blob/rest-and-actuator-with-security/src/main/java/samples/springboot/util/BasicAuthRestTemplate.java
 * 
 * Spring Boot itself provides a simpler TestRestTemplate for the test purpose.
 * 
 * @author hantsy
 */
public class BasicAuthRestTemplate extends RestTemplate {

    public BasicAuthRestTemplate(String username, String password) {
        addAuthentication(username, password);
    }

    private void addAuthentication(String username, String password) {
        if (username == null) {
            return;
        }
        List<ClientHttpRequestInterceptor> interceptors = Collections
                .<ClientHttpRequestInterceptor>singletonList(
                        new BasicAuthorizationInterceptor(username, password));
        setRequestFactory(new InterceptingClientHttpRequestFactory(getRequestFactory(),
                interceptors));
    }

    private static class BasicAuthorizationInterceptor implements
            ClientHttpRequestInterceptor {

        private final String username;

        private final String password;

        public BasicAuthorizationInterceptor(String username, String password) {
            this.username = username;
            this.password = (password == null ? "" : password);
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                ClientHttpRequestExecution execution) throws IOException {
            byte[] token = Base64.getEncoder().encode(
                    (this.username + ":" + this.password).getBytes());
            request.getHeaders().add("Authorization", "Basic " + new String(token));
            return execution.execute(request, body);
        }

    }
}
