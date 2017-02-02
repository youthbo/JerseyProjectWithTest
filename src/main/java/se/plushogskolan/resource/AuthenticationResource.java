package se.plushogskolan.resource;


import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import se.plushogskolan.jersey.model.Credentials;
import se.plushogskolan.jersey.model.JWTToken;
import se.plushogskolan.jersey.model.KeyGenerator;
import se.plushogskolan.model.User;
import se.plushogskolan.service.UserService;

@Component
@Path("auth")
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationResource {
	
	@Autowired
	UserService userService;
	
	@Context
	HttpHeaders headers;
	
	@POST
	public Response authentication(Credentials credentials) {		
		String username = credentials.getUsername();
		String password = credentials.getPassword();
		User  user = userService.getUserByUsername(username);
		if (user==null){
			return Response.status(Status.UNAUTHORIZED).entity("User doesn't exist.").build();
		}		
		String storedSalt = user.getSalt();
		String storedPassword = user.getPassword();
		String hashedPassword = user.hashPassword(password.toCharArray(),Base64.decodeBase64(storedSalt));
		
		if(storedPassword.equals(hashedPassword)){
			String accessToken = JWTTokenGenerator.generateAccessToken(username);
			String refreshToken = JWTTokenGenerator.generateRefreshToken(username,hashedPassword);			
			return Response.ok(new JWTToken(accessToken,refreshToken)).build();
		}		
		return Response.status(Status.UNAUTHORIZED).build();
	}
	
    @GET
    @Path("token")
    public Response getNewAccessToken(){
    	
    	String authentication = headers.getHeaderString("Authorization");   	
    	if (authentication == null){
    		return Response.status(Status.UNAUTHORIZED).entity("No authentication infomation in header.").build();
    	}
    	String refreshToken = authentication.substring("Bearer".length()).trim();
    	HashMap<String,String> userInfo = parseToken(refreshToken);
        String username= userInfo.get("username");
        String tokenId = userInfo.get("id");
    	if (userInfo.isEmpty()){
    		return Response.status(Status.UNAUTHORIZED).entity("Refresh token is invalid").build();
    	}
    	User user = userService.getUserByUsername(username);
		if (user==null){
			return Response.status(Status.UNAUTHORIZED).entity("User doesn't exist.").build();
		}
		if (!user.getPassword().substring(0, 10).equals(tokenId)){
			return Response.status(Status.UNAUTHORIZED).entity("Invalid token.").build();
		}
		String accessToken = JWTTokenGenerator.generateAccessToken(username);
		refreshToken = JWTTokenGenerator.generateRefreshToken(username,user.getPassword());
		return Response.ok(new JWTToken(accessToken,refreshToken)).build();
    }
    
	private HashMap<String,String> parseToken(String refreshToken) {
		HashMap<String,String> userInfo = new HashMap<>();
		try{
		    Claims claims = Jwts.parser().setSigningKey(KeyGenerator.getKey())
		    		        .parseClaimsJws(refreshToken).getBody();
		    userInfo.put("username",claims.getSubject());
		    userInfo.put("id",claims.getId());		    
		    return userInfo;
		}catch(Exception e){
			return userInfo;
		}
	}

}
