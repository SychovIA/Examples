package voting.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;


public class Client {
	
	/*
	 * Класс содержит клиентские запросы на сервер
	 */
	
	private static Socket server;
	private static InputStream in;
	private static InputStreamReader inReader;
	private static OutputStream out;
	private static PrintWriter pout;
	private static BufferedReader buf;
	private static String response;
	public final static int FALSE = 0;
	public final static int OK = 1;
	public final static int ERROR_ON_SERVER = 2;
	public final static int ERROR_CONNECTION = 3;
	
	private Client(){}
	
	//запрос на вход
	public static int enter (String login, char[] password){
		int i = FALSE;
		try {
			server = new Socket("localhost", 3000);
			in = server.getInputStream();
			out = server.getOutputStream();
			pout = new PrintWriter(out, true);
			pout.println("enter");
			pout.println(login);
			pout.println(new String (password));
			buf = new BufferedReader(new InputStreamReader(in));
			response = buf.readLine();
			UserData.setUser(response);
			response = buf.readLine();
			UserData.setCategory(response);
			response = buf.readLine();
			UserData.setLogin(response);
			i = OK;
			server.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		} catch (IOException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		}
		return i;

	}
	
		// добавление нового пользователя
	public static int registration(String lName, String fName, String login, char[] password){
		int i = FALSE;
		String pass = new String(password);
		try {
			server = new Socket("localhost", 3000);
			in = server.getInputStream();
			out = server.getOutputStream();
			pout = new PrintWriter(out, true);
			pout.println("registration");
			pout.println(lName + fName);
			pout.println(login);
			pout.println(pass);
			inReader = new InputStreamReader(in); 
			i = inReader.read();
			
			
			server.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		} catch (IOException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		}

	
		
		return i;
	}
	
	//добавление кандидата
	public static  int addCandidat(String lName, String fName){
		int i = FALSE;
		try {
			server = new Socket("localhost", 3000);
			in = server.getInputStream();
			out = server.getOutputStream();
			pout = new PrintWriter(out, true);
			pout.println("addCandidat");
			pout.println(UserData.getLogin());
			pout.println(lName + " " + fName);
			inReader = new InputStreamReader(in); 
			i = inReader.read();
			server.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		} catch (IOException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		}
		return i;
	}
	
	//получение списка кандидатов
	public static  ArrayList <String> getCandidats(){
		ArrayList <String> candidats = new ArrayList<>();
		try {
			server = new Socket("localhost", 3000);
			in = server.getInputStream();
			out = server.getOutputStream();
			pout = new PrintWriter(out, true);
			pout.println("getCandidats");
			buf = new BufferedReader(new InputStreamReader(in));
			response = buf.readLine();
			while(response != null){
			candidats.add(response);
			response = buf.readLine();
			}
			server.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return candidats;
		
	}
	
	//удаление кандидата
	public static  int removeCandidat(String candidat){
		int i = FALSE;
		try {
			server = new Socket("localhost", 3000);
			in = server.getInputStream();
			out = server.getOutputStream();
			pout = new PrintWriter(out, true);
			pout.println("removeCandidat");
			pout.println(UserData.getLogin());
			pout.println(candidat);
			inReader = new InputStreamReader(in); 
			i = inReader.read();
			server.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		} catch (IOException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		}
		return i;
	}
	
	//удаление всех кандидатов
	public static  int removeAllCandidats(){
		int i = FALSE;
		try {
			server = new Socket("localhost", 3000);
			in = server.getInputStream();
			out = server.getOutputStream();
			pout = new PrintWriter(out, true);
			pout.println("removeAllCandidats");
			pout.println(UserData.getLogin());
			inReader = new InputStreamReader(in); 
			i = inReader.read();
			server.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		} catch (IOException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		}
		return i;
	}
	
	
	//установить дату голосования
	public static  int setDate(Date begingDate, Date endDate){
		int i = FALSE;
		try {
			server = new Socket("localhost", 3000);
			in = server.getInputStream();
			out = server.getOutputStream();
			pout = new PrintWriter(out, true);
			pout.println("setDate");
			pout.println(UserData.getLogin());
			pout.println(begingDate.getTime());
			pout.println(endDate.getTime());
			inReader = new InputStreamReader(in); 
			i = inReader.read();
			server.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		} catch (IOException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		}
		return i;
	}
	
	//сброс голосования 
	public static  int resetVoting(){
		int i = FALSE;
		try {
			server = new Socket("localhost", 3000);
			in = server.getInputStream();
			out = server.getOutputStream();
			pout = new PrintWriter(out, true);
			pout.println("resetVoting");
			pout.println(UserData.getLogin());
			inReader = new InputStreamReader(in); 
			i = inReader.read();
			
			server.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		} catch (IOException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		}
		return i;
	}
	
	//получить дату начала голосования
	public static Date getBegingDate(){
		Date date = null;
		try {
			server = new Socket("localhost", 3000);
			in = server.getInputStream();
			out = server.getOutputStream();
			pout = new PrintWriter(out, true);
			pout.println("getBegingDate");
			BufferedReader buf = new BufferedReader(new InputStreamReader(in)); 
			response = buf.readLine();
			if ("empty".equals(response)){
				date = null;
			}
			else{
				long time = Long.valueOf(response);
				date = new Date(time);
			}
			server.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		return date;
	}
	
	//получить дату окончания голосования
	public static Date getEndDate(){
		Date date = null;
		try {
			server = new Socket("localhost", 3000);
			in = server.getInputStream();
			out = server.getOutputStream();
			pout = new PrintWriter(out, true);
			pout.println("getEndDate");
			buf = new BufferedReader(new InputStreamReader(in));
			response = buf.readLine();
			if ("empty".equals(response)){
				date = null;
			}
			else{
				long time = Long.valueOf(response);
				date = new Date(time);
			}
			server.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		return date;
	}
	
	//смена логина и пароля
	public static int changeLogin(String newLogin, char[] newPass, char[] oldPass){
		int i = FALSE;
		try {
			server = new Socket("localhost", 3000);
			in = server.getInputStream();
			out = server.getOutputStream();
			pout = new PrintWriter(out, true);
			pout.println("changeLogin");
			pout.println(UserData.getLogin());
			pout.println(newLogin);
			pout.println(new String(newPass));
			pout.println(new String(oldPass));
			inReader = new InputStreamReader(in); 
			i = inReader.read();
			if(i == OK)
				UserData.setLogin(newLogin);
			server.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		} catch (IOException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		}
		
		return i;
		
	}
	
	// получение списка кандидатов с наибольшим кол-вом голосов (5 лучших)
	public static String [] [] getRezults(){
		String [][] rezults = new String [5][2];
		try {
			server = new Socket("localhost", 3000);
			in = server.getInputStream();
			out = server.getOutputStream();
			pout = new PrintWriter(out, true);
			pout.println("getRezults");
			BufferedReader buf = new BufferedReader(new InputStreamReader(in));
			for(int i = 0; i < rezults.length; i++){
				for (int k =0 ; k < rezults[i].length; k++){
					rezults [i][k] = buf.readLine();
				}
			}
			server.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rezults;
		
	}
	
	//голосование
	public static int voted(String candidat){
		int i = FALSE;
		try {
			server = new Socket("localhost", 3000);
			in = server.getInputStream();
			out = server.getOutputStream();
			pout = new PrintWriter(out, true);
			pout.println("voted");
			pout.println(UserData.getLogin());
			pout.println(candidat);
			inReader = new InputStreamReader(in); 
			i = inReader.read();
			server.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		} catch (IOException e) {
			e.printStackTrace();
			i = ERROR_CONNECTION;
		}
		
		return i;
	}
	
}
