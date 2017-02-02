package se.plushogskolan.resource;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import se.plushogskolan.jersey.model.KeyGenerator;

public class JWTTokenGenerator {

	public static String generateAccessToken(String username) {
		Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (1000 * 60));
		String compactJwt = Jwts.builder()
		  .setSubject(username)
		  .setIssuedAt(now)
		  .setExpiration(exp)
		  .signWith(SignatureAlgorithm.HS256, KeyGenerator.getKey())
		  .compact();
		return compactJwt;
	}

	public static String generateRefreshToken(String username,String hashedPassword) {
		Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (1000 * 6000));
		String compactJwt = Jwts.builder()
		  .setSubject(username)
		  .setIssuedAt(now)
		  .setId(hashedPassword.substring(0, 10))
		  .setExpiration(exp)
		  .signWith(SignatureAlgorithm.HS256, KeyGenerator.getKey())
		  .compact();
		return compactJwt;
	}

}
