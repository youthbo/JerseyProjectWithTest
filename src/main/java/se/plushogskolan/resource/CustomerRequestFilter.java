package se.plushogskolan.resource;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@PreMatching
public class CustomerRequestFilter implements ContainerRequestFilter{

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		final Logger log = Logger.getLogger(CustomerRequestFilter.class.getName());
		log.info("in request filter");
		if (requestContext.getHeaders().containsKey("auth-token")){
			String auth_token = requestContext.getHeaderString("auth-token");
			if (!"authorized".equals(auth_token)){
			  log.info("Authentication failed with auth-token "+auth_token);
			  requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
			}
		}else{
			log.warning("Authentication token misses.");
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
		} 
		
	}

}
