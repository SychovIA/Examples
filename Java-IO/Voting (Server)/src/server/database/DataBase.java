package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import server.ServerConection;
import server.ui.ServerFrame;

public class DataBase {
	
	//����� �������� ������ ��� ����������� � ���� MySQL, �������� ������ � �� � �� ���������.

	private static String host;
	private static String port;
	private static String userName;
	private static char [] password;
	private static String db;
	private static Connection connection;
	private static Statement create;
	private static PreparedStatement pStatement;
	
	// ��������� ���������� � ����
	private static Connection getConnection() {
		
		try{
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, userName, new String(password));
			return conn;
		} catch(Exception e){
			ServerFrame.addMessage("ERROR: Connecting to Database FALSE!");
		}
			//JOptionPane.showMessageDialog(null, "����� � ����� ������ �����������!", "������ ����������� � ��", JOptionPane.WARNING_MESSAGE);}
		return null;
	}
	
	//�������� ������
	public static boolean  createTable() {
		
		boolean b = false;
		
		connection = getConnection();
		if(connection!=null)
		try {
			
			//������� �������������
			create = connection.createStatement();
			create.execute("CREATE TABLE IF NOT EXISTS users (users_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
							+ "login VARCHAR(20) NOT NULL UNIQUE, "
							+ "password VARCHAR(20) NOT NULL, "
							+ "user VARCHAR (60) NOT NULL, "
							+ "category VARCHAR(10) NOT NULL DEFAULT 'elector', "
							+ "is_voted BOOLEAN NOT NULL DEFAULT '0')");
				
			//������� ����������
			create.execute("CREATE TABLE IF NOT EXISTS candidats (candidats_id INT AUTO_INCREMENT PRIMARY KEY, "
							+ "candidat VARCHAR(60) NOT NULL UNIQUE, "
							+ "vote INT NOT NULL DEFAULT '0')");
					
			//������� ���� �����������
			create.execute("CREATE TABLE IF NOT EXISTS dates (date_id INT AUTO_INCREMENT PRIMARY KEY, "
							+ "beging_date DATETIME, "
							+ "end_date DATETIME)");
			
			//�� ��������� �������� ������ �������������� ����  ��� �� ���������� 
			create.executeUpdate("INSERT IGNORE INTO users "
					+ "(users_id,login, password, user, category) "
					+ "VALUES "
					+ "(1, 'admin', 'admin', 'admin', 'admin')");
			
			//���������� ������ ������ ����
			create.executeUpdate("INSERT IGNORE INTO dates VALUES (1, null, null)");
				
			b = true;
			
		} catch (Exception e) {
			ServerFrame.addMessage("ERROR: Tables not created  on Database!");
			//JOptionPane.showMessageDialog(null, "������ ��������� ������!", "������ ���� ������", JOptionPane.ERROR_MESSAGE);
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
	
	// ����� ��� ����� � ������� �����������
	// ���������� ������ ������������
	public static UserData enter (String login, String password){
		
		UserData user = new UserData();
		connection = getConnection();
		if(connection!=null)
		try {
			
			pStatement = connection.prepareStatement("SELECT user, category "
					+ "FROM users "
					+ "WHERE login = ? AND password = ?");
			pStatement.setString(1, login);
			pStatement.setString(2, password);
			
			ResultSet rs = pStatement.executeQuery();
			 
			 if (rs.next()) {
				 user.setUser(rs.getString("user"));
				 user.setCategory(rs.getString("category"));
				 user.setLogin(login);
			}
		
			
		} catch (Exception e){e.printStackTrace();
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

	//���������� ������ ������������ � ��
	public static int registration(String user, String login, String password){
		int i = ServerConection.FALSE;
		
	
		connection = getConnection();
		if (connection!=null)
			try {
				pStatement = connection.prepareStatement("INSERT IGNORE INTO users "
						+ "(login, password, user) "
						+ "VALUES "
						+ "(?,?,?)");
				pStatement.setString(1, login);
				pStatement.setString(2, password);
				pStatement.setString(3, user);
				i = pStatement.executeUpdate();
				
	
		
			} catch (Exception e){i = ServerConection.ERROR; 
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
	
	//���������� ���������
	public static  int addCandidat(String candidat){
		int i = ServerConection.FALSE;
		
		connection = getConnection();
		if (connection != null)
		try {
			pStatement = connection.prepareStatement("INSERT IGNORE INTO candidats "
					+ "(candidat) "
					+ "VALUES (?)");
			pStatement.setString(1, candidat);
			i = pStatement.executeUpdate();
			
		} catch (Exception e){i = ServerConection.ERROR; 
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
	
	//��������� ������ ���� ���������� �� ��
	public static  ArrayList <String> getCandidats(){
		
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
		} catch (Exception e){
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
	
	//�������� ���������
	public static  int removeCandidat(String candidat){
		int i = ServerConection.FALSE;

				
		connection = getConnection();
		if (connection != null)
		try {
			
			pStatement = connection.prepareStatement("DELETE FROM candidats WHERE candidat = ?");
			pStatement.setString(1, candidat);
			i = pStatement.executeUpdate();
			
		} catch (Exception e){i = ServerConection.ERROR;
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
	
	//�������� ���� ����������
	public static  int removeAllCandidats(){
		int i = ServerConection.FALSE;
		
		connection = getConnection();
		if(connection != null)
		try {
			create = connection.createStatement();
			i = create.executeUpdate("DELETE FROM candidats");
			
		} catch (Exception e){i = ServerConection.ERROR;
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
	
	
	//������� ���� �����������
	public static  int setDate(Date beging_date, Date end_date){
		int i = ServerConection.FALSE;
		connection = getConnection();
		if (connection != null)
		try {
			pStatement = connection.prepareStatement("UPDATE dates  SET beging_date = ?, end_date = ? WHERE date_id = '1'");
			java.sql.Timestamp begingTS = new java.sql.Timestamp(beging_date.getTime());
			java.sql.Timestamp endTS = new java.sql.Timestamp(end_date.getTime());
			pStatement.setTimestamp(1, begingTS);
			pStatement.setTimestamp(2, endTS);
			i = pStatement.executeUpdate();
			
			
			
		} catch (Exception e){i = ServerConection.ERROR;
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
	
	
	//�������� ���� �����������
	public static int delateDate(){
		int i = ServerConection.FALSE;
		connection = getConnection();
		if(connection != null)
		try {
			create = connection.createStatement();
			i = create.executeUpdate("UPDATE dates  SET beging_date = null, end_date = null WHERE date_id = '1'");
			
		} catch (Exception e){i = ServerConection.ERROR;
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
	
	//��������� ���� ������ �����������
	public static Date getBegingDate(){
		
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
	
	
	//��������� ���� ��������� �����������
	public static Date getEndDate(){
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
	
	//��������� ������ � ������
	public static int changeLogin(String oldLogin, String newLogin, String newPass, String oldPass){
		int i = ServerConection.FALSE;
		connection = getConnection();
		if (connection != null)
			
		try {
			pStatement = connection.prepareStatement("UPDATE users SET login = ?, password = ? "
					+ "WHERE login = ? and password = ?");
			pStatement = connection.prepareStatement("UPDATE users SET login = ?, password = ? "
					+ "WHERE login = ? and password = ?");
			pStatement.setString(1, newLogin);
			pStatement.setString(2, newPass);
			pStatement.setString(3, oldLogin);
			pStatement.setString(4, oldPass);
			i = pStatement.executeUpdate();
			
			
		} catch (Exception e){i = ServerConection.ERROR;
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
	
	
	// ��������� ������� � ������� � ���������� � ���������� �������
	// ���������� ������ �� 5 ���������� � ���������� ���-��� ������� � ������� �������� �������
	public static String [] [] getRezults(){
		
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
	
	
	//����� ��������� "������������" � �������� false
	public static int resetIsVote(){
		int i = ServerConection.FALSE;
		connection = getConnection();
		if (connection!= null)
		try {
			create = connection.createStatement();
			i = create.executeUpdate("UPDATE users  SET is_voted = '0'");
			
		} catch (Exception e){i = ServerConection.ERROR;
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
	
	//����� ���� ������� � ���������� �� 0
	public static int resetVote(){
			int i = ServerConection.FALSE;
		
		connection = getConnection();
		if (connection!= null)
		try {
			create = connection.createStatement();
			i = create.executeUpdate("UPDATE candidats  SET vote = '0'");
			
		} catch (Exception e){i = ServerConection.ERROR;
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
	
	//����������� - ����������� �� 1 ���-�� ������� � ��������� ���������
	public static  int voted(String login, String candidat){
		int i = ServerConection.FALSE;
		boolean b = true;
		
		connection = getConnection();
		if (connection != null)
		try {
			//�������� ��������� �� ������������
			pStatement = connection.prepareStatement("SELECT is_voted FROM users WHERE login = ?");
			pStatement.setString(1, login);
			ResultSet rs = pStatement.executeQuery();

			//���� ������ �� ������, �������
	        if (rs.next()){
	        b = rs.getBoolean("is_voted");
	        }
			
	        if(!b){
			
	        	//���� �� ������������ ���������� ������ ��������� � ������� ��������� "������������" �� true
	        pStatement = connection.prepareStatement("UPDATE candidats  SET vote = vote + 1 "
					+ "WHERE candidat = ?");
			
	        pStatement.setString(1, candidat);
	        pStatement.executeUpdate();
			
	        pStatement = connection.prepareStatement("UPDATE users SET is_voted = 1 WHERE login = ?");
	        pStatement.setString(1, login);
	        i = pStatement.executeUpdate();
	        
	        }
			
		} catch (Exception e){i = ServerConection.ERROR;
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
	
	//��������� ��������� ������������
	public static  String getCategory(String login){
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
	
	//������ ��� �������
	public static void setHost(String host) {
		DataBase.host = host;
	}
	
	//������ ����� �����
	public static void setPort(String port) {
		DataBase.port = port;
	}
	
	//������ ��� ������������ ����
	public static void setUserName(String userName) {
		DataBase.userName = userName;
	}
	
	//������ ������
	public static void setPassword(char [] password) {
		DataBase.password = password;
	}
	
	//������ ��
	public static void setDb(String db) {
		DataBase.db = db;
	}

}
