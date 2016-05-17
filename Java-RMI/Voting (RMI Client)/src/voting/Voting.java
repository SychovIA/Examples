package voting;

import java.rmi.RMISecurityManager;
import java.util.Date;

import voting.database.DataBase;
import voting.ui.EnterFrame;


public class Voting {
	
	// команда для настройки менеджера безопасности -Djava.security.policy=client.policy
	// если ошибка во время запуска тогда -Djava.security.policy=no.policy

	public static void main(String[] args) {
		if(System.getSecurityManager()==null)
			//установка менеджера безопасности для RMIсоединения
			System.setSecurityManager(new RMISecurityManager());
		
		
		new EnterFrame().setVisible(true);
	}

}