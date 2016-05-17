package voting.ui;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.text.DateFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import voting.io.Client;



public class BeforVotingPanel extends JPanel{
	
	BeforVotingPanel(){
		// ������ ������� ���������� � ����� ������ � ��������� �����������
		// ���������� ���������� � ������ ������
		
		GridBagLayout  gridBagLayout= new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {1};
		gridBagLayout.rowHeights = new int[] {1};
		setLayout(gridBagLayout);
		
		
		DateFormat simpleDate = DateFormat.getDateInstance(DateFormat.SHORT);
		DateFormat simpleTime = DateFormat.getTimeInstance(DateFormat.SHORT);
		String begingDate = simpleDate.format(Client.getBegingDate());
		String begingTime = simpleTime.format(Client.getBegingDate());
		String endDate = simpleDate.format(Client.getEndDate());
		String endTime = simpleTime.format(Client.getEndDate());
		JLabel textLabel = new JLabel("<html>����������� �������� "+ begingDate + " � " + begingTime + "<br>"
				+ " <center>� ���������� �� " + endDate + ", " + endTime +".</center>");
		textLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
		add(textLabel);
		
	}
}
