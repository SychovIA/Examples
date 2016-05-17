package interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import voting.database.UserData;



public interface DataBaseService extends Remote {
	/*
	 * ��������� �������� ������ ���������� ��������
	 */
	
	
	
	// ����� ��� ����� � ������� �����������
	// ���������� ������ ������������
	public UserData enter (String login, char[] password) throws RemoteException;
	
	//���������� ������ ������������ � ��
	public int registration (String user, String login, char[] password) throws RemoteException;
	
	//���������� ���������
	public  int addCandidat (String login, String candidat) throws RemoteException;
	
	//��������� ������ ���� ���������� �� ��
	public  ArrayList <String> getCandidats() throws RemoteException;
	
	//�������� ���������
	public  int removeCandidat (String login, String candidat) throws RemoteException;
	
	//�������� ���� ����������
	public  int removeAllCandidats (String login) throws RemoteException;
	
	//������� ���� �����������
	public  int setDate (String login, Date beging_date, Date end_date) throws RemoteException;
	
	//��������� ���� ������ �����������
	public Date getBegingDate() throws RemoteException;
	
	//��������� ���� ��������� �����������
	public Date getEndDate() throws RemoteException;
	
	//��������� ������ � ������
	public int changeLogin(String oldLogin, String newLogin, char[] newPass, char[] oldPass) throws RemoteException;
	
	// ��������� ������� � ������� � ���������� � ���������� �������
	public String [] [] getRezults() throws RemoteException;
	
	// ����� �����������
	public int resetVoting (String login) throws RemoteException;
	
	//����������� - ����������� �� 1 ���-�� ������� � ��������� ���������
	public  int voted(String login, String candidat) throws RemoteException;
	
	
	
	
	

}
