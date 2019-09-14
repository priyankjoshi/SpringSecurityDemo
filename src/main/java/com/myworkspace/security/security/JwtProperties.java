package com.myworkspace.security.security;

public class JwtProperties {
    public static final String SECRET="priyankjoshi123";
    public static final int EXPIRATION_TIME = 864000000; //10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
}
