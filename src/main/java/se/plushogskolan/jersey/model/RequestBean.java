package se.plushogskolan.jersey.model;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class RequestBean {
	
	
	@QueryParam(value = "filter")
	@DefaultValue("")
	private String filter; 
	
	@QueryParam(value = "criteria")
	@DefaultValue("")
	private String criteria; 

	public String getFilter() {
		return filter;
	}

	public String getCriteria() {
		return criteria;
	}

	
	
	
}
