package com.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

@Component
public class JwtUtils {

    private static String SECRET;

    public static final String HEADER_STRING = "Authorization";

    @Value("${app.jwt.secret}")
    public void setSECRET(String SECRET) {
        JwtUtils.SECRET = SECRET;
    }


    public static String generateToken(String userId){
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId",userId);
        String jwt = Jwts.builder().addClaims(params).setExpiration(new Date(System.currentTimeMillis() + 24 * 3600_000L))       //1000hours
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return "Bearer "+jwt;
    }

    public static void validateToken(String token){
        try {
            if (StringUtils.isEmpty(token)) {
                throw new NullPointerException("token can't be null.");
            }
            Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace("Bearer ",""))
                    .getBody();
        }
        catch (Exception e){
            throw new IllegalStateException("Invalid Token. "+e.getMessage());
        }
    }


    public static HttpServletRequest validateTokenAndAddUserToHeader(HttpServletRequest request){

        String token = request.getHeader(HEADER_STRING);
        try {
            if (StringUtils.isEmpty(token)) {
                throw new NullPointerException("token can't be null.");
            }
            Claims body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
            return new CustomHttpServletRequest(request,body);
        }
        catch (Exception e){
            throw new IllegalStateException("Invalid Token. "+e.getMessage());
        }
    }

    /**
     * 自定义请求类
     */
    public static class CustomHttpServletRequest extends HttpServletRequestWrapper{

        private Map<String,String> claims;

        public Map<String, String> getClaims() {
            return claims;
        }

        public CustomHttpServletRequest(HttpServletRequest request, Map<String,?> claims) {
            super(request);
            this.claims = new HashMap<>();
            claims.forEach((k,v) ->this.claims.put(k,String.valueOf(v)));
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if (claims != null && claims.containsKey(name)) {
                return Collections.enumeration(Arrays.asList(claims.get(name)));
            }
            return super.getHeaders(name);
        }
    }
}
