package voting.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.TableColumn;

import voting.database.DataBase;

public class RezultsPanel extends JPanel{
	/*
	 * Панель с результатами голосования.
	 * Содержит таблицу для вывода 5 лучших результатовю
	 * В таблице 2 поля: "Кандидат", "Голоса"
	 * Все компоненты расположены в центре панели.
	 */
	
	
	RezultsPanel(){
		
		GridBagLayout  gridBagLayout= new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {1};
		gridBagLayout.rowHeights = new int[] {1};
		setLayout(gridBagLayout);
		
		
		//Создание таблицы
		String [] head = new String[]{"Кандидат:","Голоса:"};
		String [] [] data = DataBase.getRezults();
		JTable table = new JTable(data,head);
		TableColumn column1 = table.getColumnModel().getColumn(0);
		TableColumn column2 = table.getColumnModel().getColumn(1);
		column1.setCellRenderer(new MyCellRenderer());
		column1.setPreferredWidth(300);
		column2.setCellRenderer(new MyCellRenderer());
		column2.setPreferredWidth(50);
		table.setRowHeight(30);
		table.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 18));
		table.setEnabled(false);
		JScrollPane scrollPaneTable = new JScrollPane(table);
		scrollPaneTable.setBorder(new LineBorder(Color.BLACK));
		scrollPaneTable.setPreferredSize(new Dimension(350, 178));
		
		
		Box box = Box.createVerticalBox();
		
		
		JLabel nameTableLabael = new JLabel("Пять лучших результатов:");
		nameTableLabael.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		
		box.add(nameTableLabael);
		box.add(Box.createVerticalStrut(12));
		box.add(scrollPaneTable);
		add(box);
	}

}


