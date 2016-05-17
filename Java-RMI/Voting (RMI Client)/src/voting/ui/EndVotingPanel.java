package voting.ui;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.text.DateFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import voting.database.DataBase;


public class EndVotingPanel extends JPanel{
	EndVotingPanel(){
		// панель выводит информацию о дате окончани€ голосовани€
		// информаци€ выводитьс€ в центре панели
		
		GridBagLayout  gridBagLayout= new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {1};
		gridBagLayout.rowHeights = new int[] {1};
		setLayout(gridBagLayout);
		
		
		DateFormat simpleDate = DateFormat.getDateInstance(DateFormat.SHORT);
		DateFormat simpleTime = DateFormat.getTimeInstance(DateFormat.SHORT);
		String date = simpleDate.format(DataBase.getEndDate());
		String time = simpleTime.format(DataBase.getEndDate());
		JLabel textLabel = new JLabel("√олосование окончитьс€ "+ date + " в " + time + ". ");
		textLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 25));
		add(textLabel);
		
	}
}
