package se.plushogskolan.resource;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TeamExceptionMapper implements ExceptionMapper<TeamException>  {

	@Override
	public Response toResponse(TeamException e) {
		return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
	}

}
