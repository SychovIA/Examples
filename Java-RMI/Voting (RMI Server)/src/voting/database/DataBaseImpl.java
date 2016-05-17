package voting.database;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import interfaces.DataBaseService;
import voting.ui.ServerFrame;



public class DataBaseImpl extends UnicastRemoteObject implements DataBaseService {
	
	/*
	 * Имплементация интерфейса DataBase плюс допополнительные методы для соединения с БД и настройки соединения 
	 */
	
	private static String host;
	private static String port;
	private static String userName;
	private static char [] password;
	private static String db;
	private static Connection connection;
	private static Statement create;
	private static PreparedStatement pStatement;
	private final static int FALSE = 0;
	private final static int OK = 1;
	private final static int ERROR = 2;

	public DataBaseImpl() throws RemoteException {
		
		super();
		
		
		 
		
	}
	
	
	// Соединение с БД
	private static Connection getConnection() {
		
		
		try{
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, userName, new  String (password));
			return conn;
		} catch(Exception e){
			ServerFrame.addMessage("ERROR: Connecting to Database FALSE!");
		}
			
		return null;
	}
	
	
	//Создание таблиц
		public static boolean  createTable() {
			
			boolean b = false;
			
			connection = getConnection();
			if(connection!=null)
			try {
				
				//таблица пользователей
				create = connection.createStatement();
				create.execute("CREATE TABLE IF NOT EXISTS users (users_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
								+ "login VARCHAR(20) NOT NULL UNIQUE, "
								+ "password VARCHAR(20) NOT NULL, "
								+ "user VARCHAR (60) NOT NULL, "
								+ "category VARCHAR(10) NOT NULL DEFAULT 'elector', "
								+ "is_voted BOOLEAN NOT NULL DEFAULT '0')");
					
				//таблица кандидитов
				create.execute("CREATE TABLE IF NOT EXISTS candidats (candidats_id INT AUTO_INCREMENT PRIMARY KEY, "
								+ "candidat VARCHAR(60) NOT NULL UNIQUE, "
								+ "vote INT NOT NULL DEFAULT '0')");
						
				//таблица даты голосования
				create.execute("CREATE TABLE IF NOT EXISTS dates (date_id INT AUTO_INCREMENT PRIMARY KEY, "
								+ "beging_date DATETIME, "
								+ "end_date DATETIME)");
				
				//по умолчанию создание записи администратора если  она не существует 
				create.executeUpdate("INSERT IGNORE INTO users "
						+ "(users_id,login, password, user, category) "
						+ "VALUES "
						+ "(1, 'admin', 'admin', 'admin', 'admin')");
				
				//добавление пустой строки даты
				create.executeUpdate("INSERT IGNORE INTO dates VALUES (1, null, null)");
					
				b = true;
				
			} catch (Exception e) {
				ServerFrame.addMessage("ERROR: Tables not created  on Database!");
			}
			finally {
				 if (create != null) {
			        	
				        try {
								create.close();
						} catch (SQLException e) {}
				  }
				  if (connection != null) {
				        try {
				            	connection.close();
						} catch (SQLException e) {}
				  }
			}
		      return b;
		}
	
	
	

	@Override
	public UserData enter(String login, char[] password) throws RemoteException {
		// метод для входа в систему голосования
		// возвращает данные пользователя
		
		UserData user = null;
		connection = getConnection();
		if(connection!=null)
		try {
			
			pStatement = connection.prepareStatement("SELECT user, category "
					+ "FROM users "
					+ "WHERE login = ? AND password = ?");
			pStatement.setString(1, login);
			pStatement.setString(2, new String(password));
			
			ResultSet rs = pStatement.executeQuery();
			 
			 if (rs.next()) {
				 user = new UserData();
				 user.setUser(rs.getString("user"));
				 user.setCategory(rs.getString("category"));
				 user.setLogin(login);
			}
		
			
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if (pStatement != null) {
		        try {
		        	pStatement.close();
					} catch (SQLException e) {}
			 }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {}
	        }
		}
		
		return user;
	}

	@Override
	public int registration(String user, String login, char[] password) throws RemoteException{
		//добавление нового пользователя в БД
		
		int i = FALSE;
		
		connection = getConnection();
		if (connection!=null)
			try {
				pStatement = connection.prepareStatement("INSERT IGNORE INTO users "
						+ "(login, password, user) "
						+ "VALUES "
						+ "(?,?,?)");
				pStatement.setString(1, login);
				pStatement.setString(2, new String(password));
				pStatement.setString(3, user);
				i = pStatement.executeUpdate();
				
	
		
			} catch (Exception e){i = ERROR; 
			} finally {
				if (pStatement != null) {
			        try {
			        	pStatement.close();
						} catch (SQLException e) {}
			        }
			        if (connection != null) {
			            try {
			            	connection.close();
						} catch (SQLException e) {}
			        }
			}
		return i;
	}

	@Override
	public int addCandidat(String login, String candidat) throws RemoteException{
		//добавление кандидата
		
		int i = FALSE;
		if ("admin".equals(getCategory(login))){
		
			connection = getConnection();
			if (connection != null)
			try {
				pStatement = connection.prepareStatement("INSERT IGNORE INTO candidats "
						+ "(candidat) "
						+ "VALUES (?)");
				pStatement.setString(1, candidat);
				i = pStatement.executeUpdate();
				
			} catch (Exception e){
				i = ERROR;
			} finally {
				if (pStatement != null) {
		        	
			        try {
			        	pStatement.close();
						} catch (SQLException e) {}
			        }
			        if (connection != null) {
			            try {
			            	connection.close();
						} catch (SQLException e) {}
			        }
			}
		}
		return i;
		
	}

	@Override
	public ArrayList<String> getCandidats() throws RemoteException{
		//получение списка всех кандидатов из БД
		
		ArrayList <String> candidats = new ArrayList<>();
		
		connection = getConnection();
		if (connection != null)
		try {
			 create = connection.createStatement();
			 ResultSet rs = create.executeQuery("SELECT candidat "
						+ "FROM candidats "
						+ "ORDER BY candidat");
		        		        
		        while (rs.next()){
		        	candidats.add(rs.getString("candidat"));
		        }
		        return candidats;
		} catch (Exception e){e.printStackTrace();
		} finally {
			if (create != null) {
	        	
		        try {
						create.close();
					} catch (SQLException e) {}
		        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {}
	        }
		}
		return candidats;
	}

	@Override
	public int removeCandidat(String login, String candidat) throws RemoteException{
		//удаление кандидата
		
		int i = FALSE;
		if ("admin".equals(getCategory(login))){

		
			connection = getConnection();
			if (connection != null)
			try {
				
				pStatement = connection.prepareStatement("DELETE FROM candidats WHERE candidat = ?");
				pStatement.setString(1, candidat);
				i = pStatement.executeUpdate();
				
			} catch (Exception e){
				i = ERROR;
			} finally {
				if (pStatement != null) {
		        	
			        try {
			        	pStatement.close();
						} catch (SQLException e) {}
			        }
			        if (connection != null) {
			            try {
			            	connection.close();
						} catch (SQLException e) {}
			        }
			}
		}
		return i;
	}

	@Override
	public int removeAllCandidats(String login) throws RemoteException{
		//удаление всех кандидатов
		
		int i = FALSE;
		if ("admin".equals(getCategory(login))){
			connection = getConnection();
			if(connection != null)
			try {
				create = connection.createStatement();
				i = create.executeUpdate("DELETE FROM candidats");
				
			} catch (Exception e){
				i = ERROR;
			} finally {
				if (create != null) {
			        try {
							create.close();
						} catch (SQLException e) {}
			        }
		        if (connection != null) {
		            try {
		            	connection.close();
					} catch (SQLException e) {}
		        }
			}
		}
		return i;
	}

	@Override
	public int setDate(String login, Date beging_date, Date end_date) throws RemoteException{
		//задание даты голосования
		int i = FALSE;
		if ("admin".equals(getCategory(login))){
			connection = getConnection();
			if (connection != null)
			try {
				pStatement = connection.prepareStatement("UPDATE dates  SET beging_date = ?, end_date = ? WHERE date_id = '1'");
				java.sql.Timestamp begingTS = new java.sql.Timestamp(beging_date.getTime());
				java.sql.Timestamp endTS = new java.sql.Timestamp(end_date.getTime());
				pStatement.setTimestamp(1, begingTS);
				pStatement.setTimestamp(2, endTS);
				i = pStatement.executeUpdate();
				
				
				
			} catch (Exception e){
				i = ERROR;
			} finally {
				if (pStatement != null) {
		        	
			        try {
			        	pStatement.close();
						} catch (SQLException e) {}
			        }
			        if (connection != null) {
			            try {
			            	connection.close();
						} catch (SQLException e) {}
			        }
			}
		}
		return i;
		
	}

	
	

	@Override
	public Date getBegingDate() throws RemoteException{
		//получение даты начала голосования
		
		Date date = null;
		java.sql.Timestamp begingTS;
		
		
		connection = getConnection();
		if(connection != null)
		try {
			create = connection.createStatement();
			ResultSet rs = create.executeQuery("SELECT beging_date FROM dates WHERE date_id = '1' and beging_date IS NOT NULL and end_date IS NOT NULL");
			if (rs.next()){
			begingTS = rs.getTimestamp("beging_date");
			date =  new Date(begingTS.getTime());
			
			}			
			
		} catch (Exception e){e.printStackTrace();
		} finally {
			if (create != null) {
	        	
		        try {
						create.close();
					} catch (SQLException e) {}
		        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {}
	        }
		}
		
		return date;
	}

	@Override
	public Date getEndDate() throws RemoteException{
		//получение даты окончания голосования
		
		Date date = null;
		java.sql.Timestamp endTS;
		
		
		connection = getConnection();
		if(connection != null)
		try {
			create = connection.createStatement();
			ResultSet rs = create.executeQuery("SELECT end_date FROM dates WHERE date_id = '1' and beging_date IS NOT NULL and end_date IS NOT NULL");
			if (rs.next()){
			endTS = rs.getTimestamp("end_date");
			date =  new Date(endTS.getTime());
			}			
			
		} catch (Exception e){e.printStackTrace();
		} finally {
			if (create != null) {
	        	
		        try {
						create.close();
					} catch (SQLException e) {}
		        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {}
	        }
		}
		
		return date;
	}

	@Override
	public int changeLogin(String oldLogin, String newLogin, char[] newPass, char[] oldPass) throws RemoteException{
		//изменение логина и пароля
		
		int i = FALSE;
		connection = getConnection();
		if (connection != null)
			
		try {
			pStatement = connection.prepareStatement("UPDATE users SET login = ?, password = ? "
					+ "WHERE login = ? and password = ?");
			pStatement = connection.prepareStatement("UPDATE users SET login = ?, password = ? "
					+ "WHERE login = ? and password = ?");
			pStatement.setString(1, newLogin);
			pStatement.setString(2, new String(newPass));
			pStatement.setString(3, oldLogin);
			pStatement.setString(4, new String(oldPass));
			i = pStatement.executeUpdate();
			
			
		} catch (Exception e){i = ERROR;
		} finally {
			if (pStatement != null) {
	        	
		        try {
		        	pStatement.close();
					} catch (SQLException e) {}
		        }
		        if (connection != null) {
		            try {
		            	connection.close();
					} catch (SQLException e) {}
		        }
		}
		return i;
	}

	@Override
	public String[][] getRezults() throws RemoteException{
		// получение массива с данными о кандидатах и количестве голосов
		// возвращает массив из 5 кандидатов с наибольшим кол-вом голосов в порядке убывания голосов
		
		String [] [] rezults = new String [5] [2];
		for (int i = 0; i < rezults.length; i++ ){
			for(int j = 0; j < rezults[i].length; j++)
				rezults[i][j] = "";
		}
		int i = 0;
		connection = getConnection();
		if (connection!=null)
		try {
			
			create = connection.createStatement();
			ResultSet rs = create.executeQuery("SELECT candidat, vote FROM candidats ORDER BY vote DESC LIMIT 5");
			
			
			while (rs.next()){
				rezults [i] [0] = rs.getString("candidat");
				rezults [i] [1] = rs.getString("vote");
				i++;
			}
			
			
		} catch (Exception e){e.printStackTrace();
		} finally {
			if (create != null) {
	        	
		        try {
						create.close();
					} catch (SQLException e) {}
		        }
		        if (connection != null) {
		            try {
		            	connection.close();
					} catch (SQLException e) {}
		        }
		}
		return rezults;
	}

	
	
	
	
	

	@Override
	public int voted(String login, String candidat) throws RemoteException{
		//голосование - увеличивает на 1 кол-во голосов у заданного кандидата
		int i = FALSE;
		boolean b = true;
		
		connection = getConnection();
		if (connection != null)
		try {
			//проверка голосовал ли пользователь
			pStatement = connection.prepareStatement("SELECT is_voted FROM users WHERE login = ?");
			pStatement.setString(1, login);
			ResultSet rs = pStatement.executeQuery();

			//если запрос не прошел, возврат
	        if (rs.next()){
	        b = rs.getBoolean("is_voted");
	        }
			
	        if(!b){
			
	        	//если не проголосовал добавление голоса кандидату и измение параметра "проголосовал" на true
	        pStatement = connection.prepareStatement("UPDATE candidats  SET vote = vote + 1 "
					+ "WHERE candidat = ?");
			
	        pStatement.setString(1, candidat);
	        pStatement.executeUpdate();
			
	        pStatement = connection.prepareStatement("UPDATE users SET is_voted = 1 WHERE login = ?");
	        pStatement.setString(1, login);
	        i = pStatement.executeUpdate();
	        
	        }
			
		} catch (Exception e){i = ERROR;
		} finally {
			 if (pStatement != null) {
		        	
			        try {
			        	pStatement.close();
						} catch (SQLException e) {}
			        }
			        if (connection != null) {
			            try {
			            	connection.close();
						} catch (SQLException e) {}
			        }
		}
		return i;
		
	}

	@Override
	public int resetVoting(String login) throws RemoteException{
		// Сброс голосования
		
		int i = FALSE;
		if ("admin".equals(getCategory(login))){
			
			resetIsVote();
			resetVote();
			i = delateDate();
			
		}
		return i;
		
	}
	
	private int delateDate() {
		//удаление даты голосования
		
		int i = FALSE;
		connection = getConnection();
		if(connection != null)
		try {
			create = connection.createStatement();
			i = create.executeUpdate("UPDATE dates  SET beging_date = null, end_date = null WHERE date_id = '1'");
			
		} catch (Exception e){
			i = ERROR;
		} finally {
			if (create != null) {
	        	
		        try {
						create.close();
					} catch (SQLException e) {}
		        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {}
	        }
		}
		return i;
		
	}
	
	private void resetIsVote() {
		//сброс параметра "проголосовал" к значению false
		
		connection = getConnection();
		if (connection!= null)
		try {
			create = connection.createStatement();
			create.executeUpdate("UPDATE users  SET is_voted = '0'");
			
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if (create != null) {
	        	
		        try {
						create.close();
					} catch (SQLException e) {}
		        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {}
	        }
		}
		
	}
	
	
	private void resetVote() {
		//сброс всех голосов у кандидатов до 0
		
		connection = getConnection();
		if (connection!= null)
		try {
			create = connection.createStatement();
			create.executeUpdate("UPDATE candidats  SET vote = '0'");
			
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if (create != null) {
	        	
		        try {
						create.close();
					} catch (SQLException e) {}
		        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {}
	        }
		}
		
	}
	
	
	private  String getCategory(String login){
		//получение категории пользователя
		
		String category = null; 
				
		connection = getConnection();
		if (connection!= null)
		try {
			create = connection.createStatement();
			ResultSet rs = create.executeQuery("SELECT category FROM  users  WHERE login = '" + login+ "'");
			if(rs.next()){
				category = rs.getString("category");
				
			}
			
			
		} catch (Exception e){e.printStackTrace();
		} finally {
			 if (create != null) {
		        	
			        try {
							create.close();
						} catch (SQLException e) {}
			        }
			        if (connection != null) {
			            try {
			            	connection.close();
						} catch (SQLException e) {}
			   }
		}
		return category;
	
		
	}

	//задать имя сервера
	public static void setHost(String host) {
		DataBaseImpl.host = host;
	}

	//задать номер порта
	public static void setPort(String port) {
		DataBaseImpl.port = port;
	}

	//задать имя пользователя СУБД
	public static void setUserName(String userName) {
		DataBaseImpl.userName = userName;
	}

	//задать пароль
	public static void setPassword(char[] password) {
		DataBaseImpl.password = password;
	}

	//задать БД
	public static void setDb(String db) {
		DataBaseImpl.db = db;
	}

	

}
