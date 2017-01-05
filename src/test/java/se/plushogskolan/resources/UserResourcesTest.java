package se.plushogskolan.resources;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserResourcesTest {

    private static Client client;
	
	@BeforeClass
	public static void setUp(){
		client = ClientBuilder.newClient();
	}
	
	@Test
	public void canCreateUser() {
		String input = "{\"firstname\":\"Bo\""+","
				       +"\"lastname\":\"Yang\""+","
				       +"\"username\":\"Boyang12345\""+","
				       +"\"teamname\":\"IT\"}";
        assertEquals(201, client.target("http://127.0.0.1:8080/users")
        		.request(MediaType.APPLICATION_JSON_VALUE).header("auth-token", "authorized").post(Entity.json(input)).getStatus());

	}
	
	@Test
	public void canUpdateUser() {
		String input = "{\"firstname\":\"Bonnie\""+","
				       +"\"lastname\":\"Yang\""+","
				       +"\"teamname\":\"IT\"}";
        assertEquals(200, client.target("http://127.0.0.1:8080/users/3")
        		.request(MediaType.APPLICATION_JSON_VALUE)
        		.header("auth-token", "authorized")
        		.put(Entity.json(input)).getStatus());

	}
	
	@Test
	public void canGetUsers() {
        assertEquals(200, client.target("http://127.0.0.1:8080/users?filter=firstname&criteria=Bo")
        		.request(MediaType.APPLICATION_JSON_VALUE)
        		.header("auth-token", "authorized")
        		.get().getStatus());
        assertEquals(200, client.target("http://127.0.0.1:8080/users?filter=teamname&criteria=IT")
        		.request(MediaType.APPLICATION_JSON_VALUE)
        		.header("auth-token", "authorized")
        		.get().getStatus());

	}

}
