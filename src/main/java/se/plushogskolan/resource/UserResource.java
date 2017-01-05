package se.plushogskolan.resource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import se.plushogskolan.jersey.model.JerseyUser;
import se.plushogskolan.jersey.model.RequestBean;
import se.plushogskolan.jersey.model.UserFinder;
import se.plushogskolan.model.Team;
import se.plushogskolan.model.User;
import se.plushogskolan.model.WorkItem;
import se.plushogskolan.service.TeamService;
import se.plushogskolan.service.UserService;
import se.plushogskolan.service.WorkItemService;

@Component
@Path("users")
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
@Consumes(MediaType.APPLICATION_JSON)
public final class UserResource {

	@Autowired
	private UserService userService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private WorkItemService workItemService;

	@Context
	private UriInfo uriInfo;

	/**
	 * uri: .../users
	 * 
	 * man får skicka "teamname" i http json body:n. Om redan finns ett team med
	 * det här namnet hämtas teamet och sätts till user. Om inget team
	 * med det här namnet finns då skapas ett team med namnet och sätts till
	 * user. Skicka en Json så här:
	 * {
	 *    	"firstname": "xxx",
	 * 		"lastname": "xxx",
	 * 		"username": "xxx", 
	 * 		"teamname": "xxx"
   	 * }
	 */
	@POST
	public Response addUser(JerseyUser user) {

		String teamname = user.getTeamname();
		Team team = teamService.findByName(teamname);
		if (team == null) {
			team = new Team(teamname);
		}
		User newUser = new User(user.getFirstname(),user.getLastname(),user.getUsername(),team);
		newUser.generateUsernumber();
		newUser = userService.createUser(newUser);
		URI location = uriInfo.getAbsolutePathBuilder().path(UserResource.class, "getUserById").build(newUser.getId());
		return Response.created(location).build();
	}

	/**
	 *
	 * Uri: /users?filter=<Type of search>&criteria=<Search criteria>
	 * if no filter specified, all users returned.
	 * 
	 * @param filter
	 * @param criteria
	 * @return
	 */
	
	@GET
	public Response getUser(@BeanParam  RequestBean request) {

		List<User> users = new ArrayList<>();	
		users = userService.getAllUsers();
		if (!request.getFilter().equals("")){
			UserFinder finder = new UserFinder(users, request.getFilter(),request.getCriteria());
		    users = finder.find();
		}
		if (users==null)
			return Response.status(Status.NOT_FOUND).entity("User with given criteria doesn't exist.").build();
		else {
			return Response.ok(users).build();
		}
	}
	
	/**
	 * uri: .../users/p hämtar alla users pagenerad
	 * 
	 * @param page
	 * @param size
	 */
	@GET
	@Path("p")
	public Response getAllUsers(@QueryParam("page") @DefaultValue("0") int page,
			@QueryParam("size") @DefaultValue("10") int size) {

		List<User> users = userService.findAllUsers(page, size);
		return Response.ok(users).build();
	}

	/**
	 * Just an easy way to get a single user based on its id
	 * uri : /users/123
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("{id}")
	public Response getUserById(@PathParam("id") @DefaultValue("1") Long id) {

		User user = userService.getUser(id);
		return Response.ok(user).build();
	}
	
	/**
	 * Url: /users/123 
	 * Method: Put 
	 * Request body parameter:  "workItemId" : 123,  
	 * 							"firstname" : "xxxxx",
	 * 							"status" : "ACTIVE" || "DEACTIVE"
	 * 							"team"  : teamname;
	 * 
	 * @param stringId
	 * @param reqBody
	 * @return
	 */
	@PUT
	@Path("{id}")
	public Response updateUser(@PathParam("id") String id, JerseyUser jerseyUser) {
		User user = userService.getUser(Long.parseLong(id));
		
		if (jerseyUser.getFirstname()!=null){
			user.setFirstname(jerseyUser.getFirstname());
		}
			
		if (jerseyUser.getLastname()!=null){
			user.setLastname(jerseyUser.getLastname());
		}
		
		if (jerseyUser.getTeamname()!=null){
			Team team = teamService.findByName(jerseyUser.getTeamname());
			user.setTeam(team);
		}
		userService.updateUser(user);
		
		if (jerseyUser.getStatus()!=null&&jerseyUser.getStatus().equals("ACTIVE")){
			userService.activateUser(user);
		}
		if (jerseyUser.getStatus()!=null&&jerseyUser.getStatus().equals("DEACTIVE")){
			userService.deactivateUser(user);
		}
		// addWorkItemToUser
		if (jerseyUser.getWorkItemId()!=null){
			WorkItem workItem = workItemService.findById(Long.parseLong(jerseyUser.getWorkItemId()));
		    workItemService.addWorkItemToUser(workItem, user);
		}	
		return Response.ok().build();		
	}
}