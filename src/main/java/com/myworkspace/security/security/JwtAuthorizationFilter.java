package com.myworkspace.security.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.myworkspace.security.db.UserRepository;
import com.myworkspace.security.model.UserEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.HEADER);
        if(null == header || !header.startsWith(JwtProperties.TOKEN_PREFIX)){
                    chain.doFilter(request,response);
                    return;
        }

        //if header is present and contains the token then try grap userPrincipal from database and perform
        //authorization
        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request,response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {

            String token = request.getHeader(JwtProperties.HEADER);
            if(token != null){
                //parse the token and validate it
                String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
                        .build()
                        .verify(token.replace(JwtProperties.TOKEN_PREFIX,""))
                        .getSubject();
                if(null != username){
                    UserEntity user = userRepository.getByUsername(username);
                    UserPrincipal principal = new UserPrincipal(user);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username,null,principal.getAuthorities());
                    return auth;
                }
            }
            return null;

    }
}
