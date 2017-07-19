package vn.vnptnet.archetype.warconfigurable.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.json.JSONObject;
import vn.vnptnet.archetype.warconfigurable.security.JWTChecker;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 5/30/2017.
 */

@WebFilter(filterName="jwtFilter")
public class JWTFilter implements Filter  {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String clientIp = request.getRemoteAddr();
        HttpServletRequest httpRequest = (HttpServletRequest) request;


        String authorizationHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorizationHeader == null) {
            reject(response, "not found http header "+HttpHeaders.AUTHORIZATION);
            return;
        }else if(!authorizationHeader.startsWith("Bearer ")){
            reject(response, HttpHeaders.AUTHORIZATION + " header is not correct");
            return;
        }
        String token = authorizationHeader.substring("Bearer ".length());

        try {
            DecodedJWT jwt = JWTChecker.getInstance().verify(token);

            Map<String, Claim> claims = jwt.getClaims();
            if(claims.containsKey("ip_address")) {
                Claim tables = claims.get("ip_address");
                List<String> ip_address = tables.asList(String.class);
                if(!ip_address.contains(clientIp)){
                    reject(response, "un-authorized ip address");
                }
            }

            chain.doFilter(request, response);
        } catch (UnsupportedEncodingException exception){
            //UTF-8 encoding not supported
            exception.printStackTrace();
            reject(response, "Unsupported encoding");
            return;
        } catch(TokenExpiredException exception){
            reject(response, "Token expired");
            return;
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            exception.printStackTrace();
            reject(response, "Token invalid");
            return;
        } catch (Exception exception){
            exception.printStackTrace();
            reject(response, exception.getMessage());
            return;
        }
    }
    @Override
    public void destroy() {
    }

    private void reject(ServletResponse response, String message) throws IOException {
        JSONObject jo = new JSONObject(){
            {
                put("message",message);
            }
        };
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, jo.toString());

    }
    /*
    private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
            .entity("You cannot access this resource").build();
    private static final Response TOKEN_EXPIRED = Response.status(Response.Status.UNAUTHORIZED)
            .entity("Your token is expired").build();

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        String authorizationHeader =
                containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            containerRequestContext.abortWith(ACCESS_DENIED);
            return;
        }

        String token = authorizationHeader.substring("Bearer ".length());
        //System.out.println("token = " + token);
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm)
                    //.withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);

            Map<String, Claim> claims = jwt.getClaims();
            if(claims.containsKey("ip_address")) {
                Claim tables = claims.get("ip_address");
                List<String> ip_address = tables.asList(String.class);

            }
        } catch (UnsupportedEncodingException exception){
            //UTF-8 encoding not supported
            exception.printStackTrace();
            containerRequestContext.abortWith(ACCESS_DENIED);
            return;
        } catch(TokenExpiredException exception){
            containerRequestContext.abortWith(TOKEN_EXPIRED);
            return;
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            exception.printStackTrace();
            containerRequestContext.abortWith(ACCESS_DENIED);
            return;
        } catch (Exception exception){
            exception.printStackTrace();
            containerRequestContext.abortWith(ACCESS_DENIED);
            return;
        }
    }*/
}
