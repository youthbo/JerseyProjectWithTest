package se.plushogskolan.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import se.plushogskolan.jersey.model.JerseyTeam;
import se.plushogskolan.model.Status;
import se.plushogskolan.model.Team;
import se.plushogskolan.service.ServiceException;
import se.plushogskolan.service.TeamService;

@Component
@Path("teams")
@Produces({ MediaType.APPLICATION_JSON})
@Consumes({ MediaType.APPLICATION_JSON})
public final class TeamResource {

	@Autowired
	TeamService teamService;

	@Context
	private UriInfo uriInfo;

	/**
	 * URL:.../teams 
	 * body: 
	 * { 
	 * "name":"TeamName" 
	 * }
	 * @throws TeamException 
	 */
	@POST
	public Response addTeam(JerseyTeam jerseyteam) throws TeamException {
		Team team = new Team(jerseyteam.getName());
		try {
			
			team = teamService.createTeam(team);
		} catch (ServiceException e) {
			throw new TeamException("Team already exists!");
		}
		URI location = uriInfo.getAbsolutePathBuilder().path(TeamResource.class, "getTeam").build(team.getId());
		return Response.created(location).build();
	}

	/**
	 * URL:.../teams/123 
	 * Method: Put 
	 * * Request body parameter: "name" :"TeamName" || 
	 * 							"status" : "INACTIVE" || 
	 * 							"userId" : "123"
	 *
	 * 
	 * @param id
	 * @param reqBody
	 * 
	 */
	@PUT
	@Path("{id}")
	public Response uppdate(@PathParam("id") Long id, JerseyTeam team) {
		

		if (team.getName()!=null) {

			String status = team.getStatus();
			try {
				Status.valueOf(status);
			} catch (IllegalArgumentException e) {
				return Response.status(400).type(MediaType.TEXT_PLAIN)
						.entity("Status has to be either of: ACTIVE or INACTIVE").build();
			}
			teamService.updateStatusTeam(id, Status.valueOf(status));
			return Response.ok(teamService.findOne(id)).build();
		}

		if (team.getName()!=null) {
			String teamName = team.getName();
			teamService.uppdateTeam(id, teamName);
			return Response.ok().build();

		}
		if (team.getUserId()!=null) {
			Long userId = Long.parseLong(team.getUserId());
			teamService.assigneUserToTeam(id, userId);
			return Response.ok().build();

		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	/**
	 * URL:.../teams 
	 * Method: Get
	 * 
	 * @return all teams
	 */
	@GET
	public Response getAllTeams() {
		return Response.ok(teamService.findAllTeams()).build();
	}

	/**
	 * URL:.../teams/123 
	 * Method: Get
	 * 
	 * @param id
	 * @return team with id
	 */
	@GET
	@Path("{id}")
	public Response getTeam(@PathParam("id") Long id) {

		return Response.ok(teamService.findOne(id)).build();
	}

}