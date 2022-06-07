package com.javid.scheduler.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ibm.bluemix.iam.pepclient.PEPClient;

@Configuration
public class SecurityConfig {

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> registrationBean() throws Exception {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(authorizationFilter());
        registrationBean.addUrlPatterns("/scheduler/*");
        registrationBean.setOrder(0);

        return registrationBean;
    }

    @Bean
    public AuthenticationFilter authorizationFilter() {
        return new AuthenticationFilter();
    }

    @Bean
    public PEPClient pepClient() {
        PEPClient pepClient = PEPClient.builder().pdpUrl("https://iam.test.cloud.ibm.com")
                .jwksEndpoint("https://iam.test.cloud.ibm.com/identity/keys").build();

        return pepClient;
    }

}
