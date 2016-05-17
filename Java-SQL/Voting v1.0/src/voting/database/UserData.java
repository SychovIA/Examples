package voting.database;

public class UserData {
	
	/*
	 * Класс содержит информацию о текущем пользователе:
	 * текущий пользователь,
	 * его категория,
	 * его логин.
	 */
	
	private static String user;
	private static String category;
	private static String login;
	
	
	public static String getUser() {
		return user;
	}
	public static void setUser(String user) {
		UserData.user = user;
	}
	public static String getCategory() {
		return category;
	}
	public static void setCategory(String category) {
		UserData.category = category;
	}
	public static String getLogin() {
		return login;
	}
	public static void setLogin(String login) {
		UserData.login = login;
	}
	public static void setEmpty(){
		setUser(null);
		setCategory(null);
		setLogin(null);
	}
	public static boolean isEmpty(){
		boolean b = false;
		b = (user == null || category == null || login == null);
		return b;
	}

}
