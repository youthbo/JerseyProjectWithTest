package se.plushogskolan.resources;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class WorkItemResourceTest {

    private static Client client;
	
	@BeforeClass
	public static void setUp(){
		client = ClientBuilder.newClient();
	}
	
	@Test
	public void canCreateWorkItem() {
		String input = "{\"title\":\"workitem test 1\""+","
				       +"\"description\":\"this is from test.\"}";
        assertEquals(201, client.target("http://127.0.0.1:8080/workitems")
        		.request(MediaType.APPLICATION_JSON).header("auth-token", "authorized").post(Entity.json(input)).getStatus());

	}
	
	@Test
	public void canUpdateWorkItem() {
		String input = "{\"status\":\"Done\""+","
				       +"\"issueId\":\"23\"}";
        assertEquals(200, client.target("http://127.0.0.1:8080/workitems/1")
        		.request(MediaType.APPLICATION_JSON)
        		.header("auth-token", "authorized")
        		.put(Entity.json(input)).getStatus());

	}
	
	@Test
	public void canGetWorkItem() {
        assertEquals(200, client.target("http://127.0.0.1:8080/workitems?filter=status&criteria=Done")
        		.request(MediaType.APPLICATION_JSON)
        		.header("auth-token", "authorized")
        		.get().getStatus());
        
        assertEquals(200, client.target("http://127.0.0.1:8080/workitems?filter=issue&criteria=22")
        		.request(MediaType.APPLICATION_JSON)
        		.header("auth-token", "authorized")
        		.get().getStatus());

	}
	

}
