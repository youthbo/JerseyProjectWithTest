package se.plushogskolan.resource;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import se.plushogskolan.jersey.model.KeyGenerator;
import se.plushogskolan.service.UserService;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class CustomerRequestFilter implements ContainerRequestFilter{
	@Autowired
	UserService userService;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		final Logger log = Logger.getLogger(CustomerRequestFilter.class.getName());
		log.info("in request filter");
		if (!requestContext.getHeaders().containsKey("Authorization")) {
			log.warning("Authentication token misses.");
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Authentication token misses.").build());
			return;
		}

		String auth_token = requestContext.getHeaderString("Authorization");
		if (!auth_token.startsWith("Bearer")){
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Authentication token format is wrong.").build());
			return;
		}
		
		auth_token = auth_token.substring("Bearer".length()).trim();	
		try {
		    Claims claims = Jwts.parser().setSigningKey(KeyGenerator.getKey())
		    		        .parseClaimsJws(auth_token).getBody();
		    requestContext.setProperty("username", claims.getSubject() );
		} catch (ExpiredJwtException e) {			
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Authentication token is expired").build());			
		}catch(Exception e){
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Authentication token is invalid").build());
		}

		
	}

}
