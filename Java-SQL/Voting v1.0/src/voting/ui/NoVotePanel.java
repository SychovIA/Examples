package voting.ui;

import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class NoVotePanel extends JPanel{
	
	NoVotePanel(){
		GridBagLayout  gridBagLayout= new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {1};
		gridBagLayout.rowHeights = new int[] {1};
		setLayout(gridBagLayout);
		
		JLabel textLabel = new JLabel("Голосование еще не обьявлено! ");
		textLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 25));
		add(textLabel);
	}

}
