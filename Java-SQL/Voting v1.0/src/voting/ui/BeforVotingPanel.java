package voting.ui;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.text.DateFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import voting.database.DataBase;

public class BeforVotingPanel extends JPanel{
	
	BeforVotingPanel(){
		// панель выводит информацию о датах начала и окончани€ голосовани€
		// информаци€ выводитьс€ в центре панели
		
		GridBagLayout  gridBagLayout= new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {1};
		gridBagLayout.rowHeights = new int[] {1};
		setLayout(gridBagLayout);
		
		
		DateFormat simpleDate = DateFormat.getDateInstance(DateFormat.SHORT);
		DateFormat simpleTime = DateFormat.getTimeInstance(DateFormat.SHORT);
		String begingDate = simpleDate.format(DataBase.getBegingDate());
		String begingTime = simpleTime.format(DataBase.getBegingDate());
		String endDate = simpleDate.format(DataBase.getEndDate());
		String endTime = simpleTime.format(DataBase.getEndDate());
		JLabel textLabel = new JLabel("<html>√олосование начнетс€ "+ begingDate + " в " + begingTime + "<br>"
				+ " <center>и продлитьс€ до " + endDate + ", " + endTime +".</center>");
		textLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
		add(textLabel);
		
	}
}
