package se.plushogskolan.jersey.model;

public class JerseyUser {
	private String firstname;
	private String lastname;
	private String username;
	private String teamname;
	private String workItemId;
	private String status;

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getUsername() {
		return username;
	}

	public String getTeamname() {
		return teamname;
	}

	public String getWorkItemId() {
		return workItemId;
	}
	
	public String getStatus() {
		return status;
	}
}
