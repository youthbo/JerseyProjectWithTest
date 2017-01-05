package se.plushogskolan.jersey.model;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import se.plushogskolan.model.Team;
import se.plushogskolan.model.User;

public class UserFinder {
	
   private String filter;
   private String criteria;
   List<User> users;
   public UserFinder(List<User> users,String filter, String criteria) {
	 this.users = users;
	 this.filter = filter;
	 this.criteria = criteria;
   }
   
   public List<User> find(){
	   List<User> userList = null;
	   switch (filter) {
		case "username":
			userList = users.stream().filter(u->u.getUsername().equals(criteria)).collect(Collectors.toList());
			break;
		case "usernumber":
			userList = users.stream().filter(u->u.getUsernumber().equals(criteria)).collect(Collectors.toList());
			break;
		case "firstname":
			userList = users.stream().filter(u->u.getFirstname().equals(criteria)).collect(Collectors.toList());
			break;
		case "lastname":
			userList = users.stream().filter(u->u.getLastname().equals(criteria)).collect(Collectors.toList());
			break;
		case "teamname":
			userList = users.stream().filter(u->u.getTeam().getName().equals(criteria)).collect(Collectors.toList());
			break;
		default:
			return null;
		}
	   return userList;
   }
}
