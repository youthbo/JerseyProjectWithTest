package se.plushogskolan.resources;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TeamResourceTest {
private static Client client;
	
	@BeforeClass
	public static void setUp(){
		client = ClientBuilder.newClient();
	}
	
	@Test
	public void canCreateTeam() {

		String input = "{\"name\":\"team 1 from test\"}";
        assertEquals(400, client.target("http://127.0.0.1:8080/teams")
        		.request(MediaType.APPLICATION_JSON_VALUE).post(Entity.json(input)).getStatus());

    }
	
	@Test
	public void canUpdateTeam() {
	
		String input = "{\"name\":\"team 4 from test\"}";
        assertEquals(200, client.target("http://127.0.0.1:8080/teams/3")
        		.request(MediaType.APPLICATION_JSON_VALUE)
        		.header("auth-token", "authorized").put(Entity.json(input)).getStatus());
        input = "{\"name\":\"team 2 from test\"}";
        assertEquals(500, client.target("http://127.0.0.1:8080/teams/3")
        		.request(MediaType.APPLICATION_JSON_VALUE)
        		.header("auth-token", "authorized").put(Entity.json(input)).getStatus());

    }
	
	@Test
	public void canGetAllTeams() {
        assertEquals(200, client.target("http://127.0.0.1:8080/teams")
        		.request(MediaType.APPLICATION_JSON_VALUE).header("auth-token", "authorized").get().getStatus());

    }
}
