package vn.vnptnet.archetype.warconfigurable.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;

/**
 * Created by User on 6/29/2017.
 */
public class JWTChecker {
    public static JWTChecker instance = new JWTChecker();
    public static JWTChecker getInstance(){
        return instance;
    }
    private JWTChecker(){}

    public JWTVerifier getVerifier() throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier;
    }

    public DecodedJWT verify(String token) throws UnsupportedEncodingException {
        DecodedJWT jwt = getVerifier().verify(token);
        return jwt;
    }
}
