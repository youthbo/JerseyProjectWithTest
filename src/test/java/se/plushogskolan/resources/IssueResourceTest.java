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
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
//@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class IssueResourceTest {
	
	private static Client client;
	
	@BeforeClass
	public static void setUp(){
		client = ClientBuilder.newClient();
	}
	
	@Test
	public void canCreateIssue() {
	
		String input = "{\"description\":\"issue 1 from test\"}";
        assertEquals(201, client.target("http://127.0.0.1:8080/issues")
        		.request(MediaType.APPLICATION_JSON_VALUE).header("auth-token", "authorized").post(Entity.json(input)).getStatus());

    }
	
	@Test
	public void createDuplicatedIssue() {
		String input = "{\"description\":\"issue 2 from test\"}";
		assertEquals(201, client.target("http://127.0.0.1:8080/issues")
        		.request(MediaType.APPLICATION_JSON_VALUE).header("auth-token", "authorized").post(Entity.json(input)).getStatus());
	 	assertEquals(409,client.target("http://127.0.0.1:8080/issues")
        		.request(MediaType.APPLICATION_JSON_VALUE).header("auth-token", "authorized").post(Entity.json(input)).getStatus());

    }
	
	@Test
	public void canUpdateIssue(){
		String input = "{\"description\":\"issue 5 from test\"}";
		assertEquals(200, client.target("http://127.0.0.1:8080/issues/23")
        		.request(MediaType.APPLICATION_JSON_VALUE).header("auth-token", "authorized").put(Entity.json(input)).getStatus());
		assertEquals(409,client.target("http://127.0.0.1:8080/issues/23")
        		.request(MediaType.APPLICATION_JSON_VALUE).header("auth-token", "authorized").put(Entity.json(input)).getStatus());
	
	}

}
