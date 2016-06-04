package voting.database;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import interfaces.DataBaseService;

public class DataBase{
	
	private static DataBaseService dataBase;
	private static UserData currentUser;
	public final static int FALSE = 0;
	public final static int OK = 1;
	public final static int ERROR_ON_SERVER = 2;
	public final static int CONNECTION_ERROR = 3;
	
	private DataBase(){}
	
	private static DataBaseService getDataBase() {
		dataBase = null;
		try {
			dataBase = (DataBaseService) Naming.lookup("rmi://localhost:1099/DataService");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		return dataBase;
	}
	
	public static int  enter (String login, char[] password){
		
		
		int i = FALSE;
		UserData user = null;
		dataBase = null;
		dataBase = getDataBase();
		if(dataBase != null)
		try {
			user = dataBase.enter(login, password);
			i = OK;
		} catch (RemoteException e) {
			e.printStackTrace();
			i = CONNECTION_ERROR;	
		}
		else
			i = CONNECTION_ERROR;
		setCurrentUser(user);
		return i;
	}
	
	public static int registration(String lName, String fName, String login, char[] password) {
		int i = FALSE;
		String user = lName + " " + fName;
		dataBase = null;
		dataBase = getDataBase();
		if(dataBase != null)
		try {
			i = dataBase.registration(user, login, password);
		} catch (Exception e) {
			e.printStackTrace();
			i = CONNECTION_ERROR;
		}else
			i = CONNECTION_ERROR;
		return i;
	}

	public static int addCandidat(String login, String lName, String fName) {
		int i = FALSE;
		String candidat = lName + " " + fName;
		dataBase = null;
		dataBase = getDataBase();
		if(dataBase != null)
		try {
			i = dataBase.addCandidat(login, candidat);
		} catch (Exception e) {
			e.printStackTrace();
			i = CONNECTION_ERROR;
			//JOptionPane.showMessageDialog(null, "Отсутсвует связь с сервером", "Ошибка соединения", JOptionPane.ERROR_MESSAGE);
		}
		else
			i = CONNECTION_ERROR;
		return i;
		
	}

	public static ArrayList<String> getCandidats() {
		ArrayList<String> list = new ArrayList<>();
		dataBase = null;
		dataBase = getDataBase();
		if(dataBase != null)
		try {
			list = dataBase.getCandidats();
		} catch (Exception e) {}
		
		return list;
	}
	
	public static int removeCandidat(String login, String candidat){
		int i = FALSE;
		dataBase = null;
		dataBase = getDataBase();
		if(dataBase != null)
		try {
			i = dataBase.removeCandidat(login, candidat);
		} catch (Exception e) {
			e.printStackTrace();
			i = CONNECTION_ERROR;
		}else
			i = CONNECTION_ERROR;
		return i;
		
	}

	public static int removeAllCandidats(String login) {
		int i = FALSE;
		dataBase = null;
		dataBase = getDataBase();
		if(dataBase != null)
		try {
			i = dataBase.removeAllCandidats(login);
		} catch (Exception e) {
			e.printStackTrace();
			i = CONNECTION_ERROR;
		}else
			i = CONNECTION_ERROR;
		return i;
		
	}

	public static int setDate(String login, Date beging_date, Date end_date){
		int i = FALSE;
		dataBase = null;
		dataBase = getDataBase();
		if(dataBase != null)
		try {
			i = dataBase.setDate(login, beging_date, end_date);
		} catch (Exception e) {
			e.printStackTrace();
			i = CONNECTION_ERROR;
		}else
			i = CONNECTION_ERROR;
		return i;
		
	}

	public static Date getBegingDate() {
		Date date = null;
		dataBase = null;
		dataBase = getDataBase();
		if(dataBase != null)
		try {
			date = dataBase.getBegingDate();
		} catch (Exception e) {}
		return date;
	}
	
	public static Date getEndDate() {
		Date date = null;
		dataBase = null;
		dataBase = getDataBase();
		if(dataBase != null)
		try {
			date = dataBase.getEndDate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static int changeLogin(String oldLogin, String newLogin, char[] newPass, char[] oldPass) {
		int i = FALSE;
		dataBase = null;
		dataBase = getDataBase();
		if(dataBase != null)
		try {
			i = dataBase.changeLogin(oldLogin, newLogin, newPass, oldPass);
		} catch (Exception e) {
			e.printStackTrace();
			i = CONNECTION_ERROR;
		}
		else
			i = CONNECTION_ERROR;
		return i;
	}
	
	public static String[][] getRezults() {
		String[][] rezults = new String[5][2];
		dataBase = null;
		dataBase = getDataBase();
		if(dataBase != null)
		try {
			rezults = dataBase.getRezults();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Отсутсвует связь с сервером", "Ошибка соединения", JOptionPane.ERROR_MESSAGE);
		}
		
		return rezults;
	}
	
	public static int resetVoting(String login) {
		int i = FALSE;
		dataBase = null;
		dataBase = getDataBase();
		if(dataBase != null)
		try {
			i = dataBase.resetVoting(login);
		} catch (Exception e) {
			e.printStackTrace();
			i = CONNECTION_ERROR;
		}
		else
			i = CONNECTION_ERROR;
		return i;
		
	}
	
	public static int voted(String login, String candidat) {
		int i = FALSE;
		dataBase = null;
		dataBase = getDataBase();
		if(dataBase != null)
		try {
			i = dataBase.voted(login, candidat);
		} catch (Exception e) {
			e.printStackTrace();
			i = CONNECTION_ERROR;
		}
		else
			i = CONNECTION_ERROR;
		return i;
	} 
	
	public static UserData getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(UserData currentUser) {
		DataBase.currentUser = currentUser;
	}

}
