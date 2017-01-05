package se.plushogskolan.resource;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;


public class IssueException extends WebApplicationException{

	private static final long serialVersionUID = 1522401509511202156L;

	public IssueException(String message) {
		super(Response.status(Status.CONFLICT)
				.entity(message).type(MediaType.TEXT_PLAIN).build());
	}
}
