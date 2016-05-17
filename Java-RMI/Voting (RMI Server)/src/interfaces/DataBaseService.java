package interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import voting.database.UserData;



public interface DataBaseService extends Remote {
	/*
	 * Интерфейс содержит методы вызываемые удаленно
	 */
	
	
	
	// метод для входа в систему голосования
	// возвращает данные пользователя
	public UserData enter (String login, char[] password) throws RemoteException;
	
	//добавление нового пользователя в БД
	public int registration (String user, String login, char[] password) throws RemoteException;
	
	//добавление кандидата
	public  int addCandidat (String login, String candidat) throws RemoteException;
	
	//получение списка всех кандидатов из БД
	public  ArrayList <String> getCandidats() throws RemoteException;
	
	//удаление кандидата
	public  int removeCandidat (String login, String candidat) throws RemoteException;
	
	//удаление всех кандидатов
	public  int removeAllCandidats (String login) throws RemoteException;
	
	//задание даты голосования
	public  int setDate (String login, Date beging_date, Date end_date) throws RemoteException;
	
	//получение даты начала голосования
	public Date getBegingDate() throws RemoteException;
	
	//получение даты окончания голосования
	public Date getEndDate() throws RemoteException;
	
	//изменение логина и пароля
	public int changeLogin(String oldLogin, String newLogin, char[] newPass, char[] oldPass) throws RemoteException;
	
	// получение массива с данными о кандидатах и количестве голосов
	public String [] [] getRezults() throws RemoteException;
	
	// Сброс голосования
	public int resetVoting (String login) throws RemoteException;
	
	//голосование - увеличивает на 1 кол-во голосов у заданного кандидата
	public  int voted(String login, String candidat) throws RemoteException;
	
	
	
	
	

}
