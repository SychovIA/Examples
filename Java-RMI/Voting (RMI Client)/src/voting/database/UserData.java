package voting.database;
import java.io.Serializable;

public class UserData implements Serializable{
	
	/*
	 * Класс содержит информацию о пользователе:
	 * пользователь,
	 * его категория,
	 * его логин.
	 */
	private String user;
	private String category;
	private String login;
	
	
	UserData(){}
	
	UserData(String user, String category, String login){
		this.user = user;
		this.category = category;
		this.login = login;
	}
	
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public void setEmpty(){
		setUser(null);
		setCategory(null);
		setLogin(null);
	}
	public boolean isEmpty(){
		boolean b = false;
		b = (user == null || category == null || login == null);
		return b;
	}

}
