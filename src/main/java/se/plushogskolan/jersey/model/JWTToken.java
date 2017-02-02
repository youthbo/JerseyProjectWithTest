package se.plushogskolan.jersey.model;

public class JWTToken {
	private String accessToken;
	private String refreshToken;

	public JWTToken(String accessToken, String refreshToken) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}

}
