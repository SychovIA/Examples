package voting.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DataBase {
	
	//����� �������� ������ ��� ����������� � ���� MySQL, �������� ������ � �� � �� ���������.
	
	private static String host; //��� �������
	private static String port; //����� �����
	private static String userName; //��� ������������
	private static char[] password; //������
	private static String db; //��� ���� ������
	private static Connection connection;
	private static Statement create;
	private static PreparedStatement pStatement;
	
	private DataBase(){}
	
	// ��������� ���������� � ����
public static Connection getConnection() {
		
		try{
			
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, userName, new String (password));
			
			return conn;
		} catch(Exception e){JOptionPane.showMessageDialog(null, "����� � ����� ������ �����������!", "������ ����������� � ��", JOptionPane.WARNING_MESSAGE);}
		return null;
	}
	

	//�������� ������
	public static boolean  createTable() {
	
	boolean b = false;
	
	connection = getConnection();
	if(connection!=null)
	try {
		create = connection.createStatement();
		
			//������� �������������
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
		
	} catch (Exception e) {e.printStackTrace();
	}
	finally {
        if (create != null) {
        	
        try {
				create.close();
			} catch (SQLException e) {e.printStackTrace();}
        }
        if (connection != null) {
            try {
            	connection.close();
			} catch (SQLException e) {e.printStackTrace();}
        }
	}
      return b;
}
	
	
	//����� ��� ����� � ������� �����������
	public static void enter(String login, char [] pass){
		
		
		
		connection = getConnection();
		if (connection != null)
		try {
			
			pStatement = connection.prepareStatement("SELECT user, category "
					+ "FROM users "
					+ "WHERE login = ? AND password = ?");
			pStatement.setString(1, login);
			pStatement.setString(2, new String (pass));
			
			ResultSet rs = pStatement.executeQuery();
			 
			//���� SQL ������ ������ �������� ���������� ������ � ����� UserData
			 if (rs.next()) {
				 UserData.setUser(rs.getString("user"));
				 UserData.setCategory(rs.getString("category"));
				 UserData.setLogin(login);
			    }
		
			
		} catch (Exception e){System.out.println(e.getMessage());
		} finally {
			 if (pStatement != null) {
		        try {
		        	pStatement.close();
					} catch (SQLException e) {e.printStackTrace();}
			 }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
		}
	
		
	}
	
	
	
	
	
	//���������� ������ ������������ � ��
	public static boolean registation(String lName, String fName, String login, char [] pass){
		boolean b = false;
		String user = lName + " " + fName;
		
		
		connection = getConnection();
		if (connection!=null)
		try {
			pStatement = connection.prepareStatement("INSERT IGNORE INTO users "
					+ "(login, password, user) "
					+ "VALUES "
					+ "(?,?,?)");
			pStatement.setString(1, login);
			pStatement.setString(2, new String (pass));
			pStatement.setString(3, user);
			int i = pStatement.executeUpdate();
			b = i>0;
			if (b)
				JOptionPane.showMessageDialog(null, "����������� ������ �������!", "�����������", JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(null, "������ ����� ��� ����������!", "�����������", JOptionPane.INFORMATION_MESSAGE);
			return b;
		
			
		} catch (Exception e){e.printStackTrace();
		} finally {
	        if (pStatement != null) {
	        try {
	        	pStatement.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
		}
		return b;
	}
	
	
	
	
	
	
	//���������� ���������
	public static  void addCandidat(String lName, String fName){
		
		
		connection = getConnection();
		if (connection != null)
		try {
			pStatement = connection.prepareStatement("INSERT IGNORE INTO candidats "
					+ "(candidat) "
					+ "VALUES (?)");
			pStatement.setString(1, lName + " "+ fName);
			pStatement.executeUpdate();
			
		} catch (Exception e){e.printStackTrace();
		} finally {
	        if (pStatement != null) {
	        	
	        try {
	        	pStatement.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
		}
		
	}
	
	
	//��������� ������ ���� ���������� �� ��
	public static  ArrayList <String> getCandidats(){
				
		connection = getConnection();
		if (connection != null)
		try {
			 create = connection.createStatement();
			 ResultSet rs = create.executeQuery("SELECT candidat "
						+ "FROM candidats "
						+ "ORDER BY candidat");
		        ArrayList <String> candidats = new ArrayList<>(); 
		        
		        while (rs.next()){
		        	candidats.add(rs.getString("candidat"));
		        }
		        return candidats;
		} catch (Exception e){e.printStackTrace();
		} finally {
			if (create != null) {
	        	
		        try {
						create.close();
					} catch (SQLException e) {e.printStackTrace();}
		        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
		}
		return null;
		
	}
	
	//�������� ���������
	public static  void removeCandidat(String candidat){
		
		connection = getConnection();
		if (connection != null)
		try {
			pStatement = connection.prepareStatement("DELETE FROM candidats WHERE candidat = ?");
			pStatement.setString(1, candidat);
			pStatement.executeUpdate();
			
		} catch (Exception e){System.out.println(e.getMessage());
		} finally {
	        if (pStatement != null) {
	        	
	        try {
	        	pStatement.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
		}
		
	}
	
	
	//�������� ���� ����������
	public static  void removeAllCandidats(){
		
		connection = getConnection();
		if (connection != null)
		try {
			create = connection.createStatement();
			create.executeUpdate("DELETE FROM candidats");
			
		} catch (Exception e){System.out.println(e.getMessage());
		} finally {
			if (create != null) {
		        try {
						create.close();
					} catch (SQLException e) {e.printStackTrace();}
		        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
		}
		
	}
	
	
	//������� ���� �����������
	public static  void setDate(Date beging_date, Date end_date){
		
		
		connection = getConnection();
		if (connection != null)
		try {
			pStatement = connection.prepareStatement("UPDATE dates  SET beging_date = ?, end_date = ? WHERE date_id = '1'");
			java.sql.Timestamp begingTS = new java.sql.Timestamp(beging_date.getTime());
			java.sql.Timestamp endTS = new java.sql.Timestamp(end_date.getTime());
			pStatement.setTimestamp(1, begingTS);
			pStatement.setTimestamp(2, endTS);
			pStatement.executeUpdate();
			
			
			
		} catch (Exception e){e.printStackTrace();
		} finally {
	        if (pStatement != null) {
	        	
	        try {
	        	pStatement.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
		}
		
	}
	
	
	//�������� ���� �����������
	public static void delateDate(){
				
		connection = getConnection();
		if (connection != null)
		try {
			create = connection.createStatement();
			create.executeUpdate("UPDATE dates  SET beging_date = null, end_date = null WHERE date_id = '1'");
			
			
			
		} catch (Exception e){e.printStackTrace();
		} finally {
			if (create != null) {
	        	
		        try {
						create.close();
					} catch (SQLException e) {e.printStackTrace();}
		        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
		}
		
		
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
					} catch (SQLException e) {e.printStackTrace();}
		        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {e.printStackTrace();}
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
					} catch (SQLException e) {e.printStackTrace();}
		        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
		}
		
		return date;
	}
	
	//����������� - ����������� �� 1 ���-�� ������� � ��������� ���������
	public static  void voted(String candidat){
		
		boolean b = true;
		connection = getConnection();
		if (connection != null)
		try {
			//�������� ��������� �� ������������
			pStatement = connection.prepareStatement("SELECT is_voted FROM users WHERE login = ?");
			pStatement.setString(1, UserData.getLogin());
			pStatement.executeQuery();
			ResultSet rs = pStatement.executeQuery();

	        //���� ������ �� ������, �������
	        if (rs.next()){
	        	b = rs.getBoolean("is_voted");
	        }else
	        	return;
			
	        if(b){
	        	//���� ��� ������������ ����� ��������� � �������
	        	JOptionPane.showMessageDialog(null, "�� ��� �������������! �������� ��������� �����������!", "�����������", JOptionPane.INFORMATION_MESSAGE);
	        	return;
	        }else{
			
	        	//���� �� ������������ ���������� ������ ��������� � ������� ��������� "������������" �� true
	        pStatement = connection.prepareStatement("UPDATE candidats  SET vote = vote + 1 "
					+ "WHERE candidat = ?");
			
	        pStatement.setString(1, candidat);
	        pStatement.executeUpdate();
			
	        pStatement = connection.prepareStatement("UPDATE users SET is_voted = 1 WHERE login = ?");
	        pStatement.setString(1, UserData.getLogin());
	        pStatement.executeUpdate();
	        JOptionPane.showMessageDialog(null, "������� �� ��� �����!", "�����������", JOptionPane.INFORMATION_MESSAGE);
	        }
			
		} catch (Exception e){e.printStackTrace();
		} finally {
	        if (pStatement != null) {
	        	
	        try {
	        	pStatement.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
		}
		
	}
	
	
	//��������� ������ � ������
	public static void changeLogin(String newLogin, char [] newPass, char [] oldPass){
		
		String oldLogin = UserData.getLogin();
		
		
		connection = getConnection();
		if (connection != null)
		try {
			pStatement = connection.prepareStatement("UPDATE IGNORE users SET login = ?, password = ? "
					+ "WHERE login = ? and password = ?");
			pStatement.setString(1, newLogin);
			pStatement.setString(2, new String (newPass));
			pStatement.setString(3, oldLogin);
			pStatement.setString(4, new String (oldPass));
			int i = pStatement.executeUpdate();
			if (i>0){
				UserData.setLogin(newLogin);
				JOptionPane.showMessageDialog(null, "����� �������!", "����� ������", JOptionPane.INFORMATION_MESSAGE);}
			else
				JOptionPane.showMessageDialog(null, "�������� ������ ���� ����� ��� ����������!",  "����� ������", JOptionPane.INFORMATION_MESSAGE);
		
		
			
		} catch (Exception e){e.printStackTrace();
		} finally {
	        if (pStatement != null) {
	        	
	        try {
	        	pStatement.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
		}
	}
	
	// ��������� ������� � ������� � ���������� � ���������� �������
	// ���������� ������ �� 5 ���������� � ���������� ���-��� ������� � ������� �������� �������
	public static String [] [] getRezults(){
		
		String [] [] rezults = new String [5] [2];
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
				} catch (SQLException e) {e.printStackTrace();}
	        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
		}
		return rezults;
		
	}
	
	
	//����� ��������� "������������" � �������� false
	public static void resetIsVote(){
		
		
		connection = getConnection();
		if (connection!= null)
		try {
			create = connection.createStatement();
			create.executeUpdate("UPDATE users  SET is_voted = '0'");
			
		} catch (Exception e){e.printStackTrace();
		} finally {
			if (create != null) {
	        	
		        try {
						create.close();
					} catch (SQLException e) {e.printStackTrace();}
		        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
		}
		
		
	}
	
	//����� ���� ������� � ���������� �� 0
	public static void resetVote(){
		
		connection = getConnection();
		
		if (connection!= null)
		try {
			create = connection.createStatement();
			create.executeUpdate("UPDATE candidats  SET vote = '0'");
			
		} catch (Exception e){e.printStackTrace();
		} finally {
			if (create != null) {
	        	
		        try {
						create.close();
					} catch (SQLException e) {e.printStackTrace();}
		        }
	        if (connection != null) {
	            try {
	            	connection.close();
				} catch (SQLException e) {e.printStackTrace();}
	        }
		}
	}
	
	//������ ��� �������
	public static void setHost(String h){
		host = h;
	}
	
	//������ ����� �����
	public static void setPort(String p){
		port = p;
	}
	
	//������ ��� ������������ ����
	public static void setUserName(String u){
		userName = u;
	}
	
	//������ ������
	public static void setPassword(char[] pass){
		password = pass;
	}
	
	//������ ��
	public static void setDb(String b){
		db = b;
	}
	
	
}