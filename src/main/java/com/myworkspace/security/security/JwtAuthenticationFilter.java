package com.myworkspace.security.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myworkspace.security.model.LoginViewModel;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager manager){
        this.authenticationManager = manager;
    }

    //Triggers when we issue POST request to login. The token generated here will be used by Spring Security to validate the credentails when the request to login is made.
    /*{
        "username":"priyank",
        "password":"joshi"
    }
    in the request body
    */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
    throws AuthenticationServiceException {
        LoginViewModel credentials = null;
        try {
            credentials = new ObjectMapper().readValue(request.getInputStream(),LoginViewModel.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException(e.getLocalizedMessage());
        }
        if(null !=credentials){
            //create login token
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    credentials.getUsername(),
                    credentials.getPassword(),
                    new ArrayList<>());
            //Authenticate user
            Authentication auth = authenticationManager.authenticate(authenticationToken);
            return auth;
        }
        return null;
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request,
                                         HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        //Grab Principal
        UserPrincipal principal =(UserPrincipal) authResult.getPrincipal();

        //Create JWT token -
        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()));

        //Add token In response
             response.addHeader(JwtProperties.HEADER,JwtProperties.TOKEN_PREFIX +token);
    }

    }
