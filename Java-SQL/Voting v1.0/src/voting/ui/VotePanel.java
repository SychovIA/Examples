package voting.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import voting.database.DataBase;

public class VotePanel extends JPanel{
	/*
	 * Панель для голосования.
	 * Содержит:
	 * список кандидатов,
	 * кнопку для голосования.
	 * Все компоненты расположены в центре панели.
	 */

	private JList<String> list;
	private ArrayList<String> candidats;
	private JButton voteButton;
	
	VotePanel(){
		GridBagLayout  gridBagLayout= new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {1};
		gridBagLayout.rowHeights = new int[] {1};
		setLayout(gridBagLayout);
		
		Box box1_left = Box.createHorizontalBox();
		JLabel listLabel = new JLabel("Список кандидатов:");
		listLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		box1_left.add(listLabel);
		
		
		candidats = DataBase.getCandidats();
		list = new JList<String>();
		list.setFont(new Font("Tahoma", Font.PLAIN, 16));
		list.setModel(new AbstractListModel<String>() {
			
			public int getSize() {
				return candidats.size();
			}
			public String getElementAt(int index) {
				return candidats.get(index);
			}
		});
		
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(250,300));
		Box leftBox = Box.createVerticalBox();
		leftBox.add(box1_left);
		leftBox.add(Box.createVerticalStrut(12));
		leftBox.add(scrollPane);
		
		Box rightBox = Box.createVerticalBox();
		JLabel textLabel1 = new JLabel("Выберите нужного кандидата");
		JLabel textLabel2 = new JLabel("и нажмите кнопку \"Голосовать\"");
		textLabel1.setFont(new Font("Tahoma", Font.BOLD, 15));
		textLabel2.setFont(new Font("Tahoma", Font.BOLD, 15));
		textLabel1.setPreferredSize(textLabel2.getPreferredSize());
		voteButton = new JButton("Голосовать");
		voteButton.addActionListener(e -> {
			if(!list.isSelectionEmpty())
			DataBase.voted(list.getSelectedValue());
			else
				JOptionPane.showMessageDialog(null, "Выберите кандидата!", "Голосование", JOptionPane.INFORMATION_MESSAGE);
		});
		rightBox.add(textLabel1);
		rightBox.add(textLabel2);
		rightBox.add(Box.createVerticalStrut(12));
		rightBox.add(voteButton);
		
		Box mainBox = Box.createHorizontalBox();
		mainBox.setBorder(new EmptyBorder(12,12,12,12));
		mainBox.add(leftBox);
		mainBox.add(Box.createHorizontalStrut(24));
		mainBox.add(rightBox);
		add(mainBox);
	}
}