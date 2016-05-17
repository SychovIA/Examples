package voting;

import java.rmi.RMISecurityManager;
import java.util.Date;

import voting.database.DataBase;
import voting.ui.EnterFrame;


public class Voting {
	
	// ������� ��� ��������� ��������� ������������ -Djava.security.policy=client.policy
	// ���� ������ �� ����� ������� ����� -Djava.security.policy=no.policy

	public static void main(String[] args) {
		if(System.getSecurityManager()==null)
			//��������� ��������� ������������ ��� RMI����������
			System.setSecurityManager(new RMISecurityManager());
		
		
		new EnterFrame().setVisible(true);
	}

}