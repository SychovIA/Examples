import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import voting.database.DataBaseImpl;
import voting.ui.ServerFrame;

public class ServerRMI {
	// команда для настройки менеджера безопасности -Djava.security.policy=server.policy
	// если ошибка во время запуска тогда -Djava.security.policy=no.policy
	
	
	
	public static void main (String [] args){
		if(System.getSecurityManager()==null)
			//установка менеджера безопасности для RMIсоединения
		System.setSecurityManager(new RMISecurityManager());
		

		new ServerFrame().setVisible(true);
		
		
	}

}
