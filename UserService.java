package thw.edu.javaII.port.warehouse.ui.common;

import java.util.ArrayList;
import java.util.List;

public class UserService {
	private List<User> users;
	
	public UserService() {
		users = new ArrayList<User>();
		users.add(new User("user","pass"));
		users.add(new User("jsh","jsh123"));
		users.add(new User("mmn","mmn123"));
	}
	
	public boolean checkLogin(String user, String pass) {
		for(User u : users) {
			if(u.getUserName().equals(user) && u.getPassword().equals(pass)) {
				return true;
			}
		}
		return false;
	}

}

class User {
	private String userName;
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

}
