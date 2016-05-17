package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import server.database.DataBase;
import server.database.UserData;
import server.ui.ServerFrame;

import java.util.ArrayList;

public class ServerConection implements Runnable{
	
	/*
	 * класс содержит соединение с клиентом и обработку ожидаемых запросов
	 */
	
	private Socket client;
	private BufferedReader inReader;
	private InputStream in;
	private OutputStream out;
	private OutputStreamWriter outWriter;
	private PrintWriter pout;
	private String login;
	private String password;
	private String user;
	private String candidat;
	private String request;
	private UserData userData;
	public final static int FALSE = 0;
	public final static int OK = 1;
	public final static int ERROR = 2;
	private int response;
	
	
	public ServerConection (Socket client){
		this.client = client;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
			try {
								
				in = client.getInputStream();
				inReader = new BufferedReader(new InputStreamReader(in));
				out = client.getOutputStream();
				outWriter = new OutputStreamWriter(out);
				pout = new PrintWriter( outWriter, true);
				ServerFrame.addMessage("Запрос от " + client.toString());
				request = inReader.readLine();
				switch (request) {
				case "enter":
					login = inReader.readLine();
					password = inReader.readLine();
					userData = DataBase.enter(login, password);
					pout.println(userData.getUser());
					pout.println(userData.getCategory());
					pout.println(userData.getLogin());
					break;
				case "registration":
					user = inReader.readLine();
					login = inReader.readLine();
					password = inReader.readLine();
					response = DataBase.registration(user, login, password);
					outWriter.write(response);
					outWriter.flush();
					break;
				case "addCandidat":
					response = FALSE;
					login = inReader.readLine();
					candidat = inReader.readLine();
					if("admin".equals(DataBase.getCategory(login)))
						response = DataBase.addCandidat(candidat);
					outWriter.write(response);
					outWriter.flush();
					break;
				case "getCandidats":
					ArrayList<String> candidats = DataBase.getCandidats();
					for (String c: candidats){
						pout.println(c);
					}
					break;
				case "removeCandidat":
					response = FALSE;
					login = inReader.readLine();
					candidat = inReader.readLine();
					if("admin".equals(DataBase.getCategory(login)))
						response = DataBase.removeCandidat(candidat);
					outWriter.write(response);
					break;
				case "removeAllCandidats":
					response = FALSE;
					login = inReader.readLine();
					if("admin".equals(DataBase.getCategory(login)))
						response = DataBase.removeAllCandidats();
					outWriter.write(response);
					outWriter.flush();
					break;
				case "setDate":
					response = FALSE;
					login = inReader.readLine();
					if("admin".equals(DataBase.getCategory(login))){
						long bTime = Long.valueOf(inReader.readLine());
						long eTime = Long.valueOf(inReader.readLine());
						Date begingDate = new Date(bTime);
						Date endDate = new Date(eTime);
						response = DataBase.setDate(begingDate, endDate);
					}
					outWriter.write(response);
					outWriter.flush();
					break;
				case "resetVoting":
					response = FALSE;
					login = inReader.readLine();
					if("admin".equals(DataBase.getCategory(login))){
						
						DataBase.resetIsVote();
						DataBase.resetVote();
						response = DataBase.delateDate();
					}
					outWriter.write(response);
					outWriter.flush();
					break;
				case "getBegingDate":
					Date begingDate = DataBase.getBegingDate();
					if(begingDate != null)
						pout.println(begingDate.getTime());
					else
						pout.println("empty");
					break;
				case "getEndDate":
					Date endDate = DataBase.getEndDate();
					if(endDate != null)
						pout.println(endDate.getTime());
					else
						pout.println("empty");
					break;
				case "changeLogin":
					login = inReader.readLine();
					String newLogin = inReader.readLine();
					String newPass = inReader.readLine();
					password = inReader.readLine();
					response = DataBase.changeLogin(login, newLogin, newPass, password);
					outWriter.write(response);
					outWriter.flush();
					break;
				case "getRezults":
					String [] [] rezults = DataBase.getRezults();
					for(int i = 0; i < rezults.length; i++){
						for (int k =0 ; k < rezults[i].length; k++){
							pout.println(rezults [i][k]);
						}
					}
					break;
				case "voted":
					
					login = inReader.readLine();
					candidat = inReader.readLine();
					response = DataBase.voted(login, candidat);
					outWriter.write(response);
					outWriter.flush();
					break;
				default:
					break;
				}
				
				client.close();
			} catch (IOException e) {
				ServerFrame.addMessage(e.getMessage());
			}
			
		
	}
	
	
	
	
	
}
