package com.javid.scheduler.config;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.ibm.bluemix.iam.pepclient.PEPClient;
import com.ibm.bluemix.iam.pepclient.client.v2.model.Subject;

import pepclientshadowed.com.auth0.jwt.exceptions.TokenExpiredException;

public class AuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private PEPClient pepClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String serviceId = System.getenv("SERVICE_ID");
        String bearerToken = getBearerTokenFromRequest(request);
        try {
            if (StringUtils.hasText(bearerToken) && isAuthenticated(bearerToken, serviceId)) {
                log.info("Request is authenticated");
            } else {
                log.info("Request is not authenticated");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header");
            }
        } catch (Exception ex) {
            log.error("Not Authenticated {}", ex);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization");
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthenticated(String bearerToken, String serviceId) {

        try {

            Subject subject = pepClient.getSubjectAsIamIdClaim(bearerToken);

            if (Objects.isNull(subject.getAttributes())) {
                throw new TokenExpiredException("The Token has expired");
            }

            String iamId = subject.getAttributes().get("id");

            String incomingServId = iamId.substring(4, iamId.length());

            if (serviceId.equals(incomingServId)) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            logger.error("Error occurred while validating token {}", e);
            return false;
        }

    }

    private String getBearerTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}
