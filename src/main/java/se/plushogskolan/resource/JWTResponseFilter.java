package se.plushogskolan.resource;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

@Secured
public class JWTResponseFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		final Logger log = Logger.getLogger(CustomerRequestFilter.class.getName());
		log.info("in response filter");
		String username = (String) requestContext.getProperty("username");
		String accessToken = JWTTokenGenerator.generateAccessToken(username);
		responseContext.getHeaders().add("Access_token", accessToken);
		
	}


}
