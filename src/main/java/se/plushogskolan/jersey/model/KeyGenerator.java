package se.plushogskolan.jersey.model;

import java.security.Key;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class KeyGenerator {
	
private static final Key SIGNING_KEY = MacProvider.generateKey(SignatureAlgorithm.HS256);
	
	public static Key getKey(){
	      return SIGNING_KEY;
	}
}
