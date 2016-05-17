package voting.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MyCellRenderer extends DefaultTableCellRenderer{
	
	//изменение свойств отдельных строк в таблице
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
		JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		switch (row) {
		case 0:
			label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
			label.setForeground(Color.RED);
			break;
		case 1:
			label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
			label.setForeground(Color.BLACK);
			break;
		case 2:
			label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
			label.setForeground(Color.BLACK);
			break;
		default:
			label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
			label.setForeground(Color.BLACK);
			break;
		}
		if (row % 2 == 0)
			label.setBackground(new Color(230, 230, 250));
		else
			label.setBackground(Color.WHITE);
		if (col  == 0)
			label.setHorizontalAlignment(JLabel.LEFT);
		else
			label.setHorizontalAlignment(JLabel.CENTER);
		
		
		return label;
	}
	

}
