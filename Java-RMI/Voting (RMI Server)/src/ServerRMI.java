import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import voting.database.DataBaseImpl;
import voting.ui.ServerFrame;

public class ServerRMI {
	// ������� ��� ��������� ��������� ������������ -Djava.security.policy=server.policy
	// ���� ������ �� ����� ������� ����� -Djava.security.policy=no.policy
	
	
	
	public static void main (String [] args){
		if(System.getSecurityManager()==null)
			//��������� ��������� ������������ ��� RMI����������
		System.setSecurityManager(new RMISecurityManager());
		

		new ServerFrame().setVisible(true);
		
		
	}

}
