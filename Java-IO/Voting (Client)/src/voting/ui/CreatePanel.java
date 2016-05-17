package voting.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;

import voting.io.Client;


public class CreatePanel extends JPanel{
	/*
	 * Панель для создания голосования.
	 * Включает в себя:
	 * поля для ввода фамилии и имени кандидата,
	 * кнопки добавления и удаления кандидатов,
	 * панели для установки времени начала и окончания голосования,
	 * панель для вывода времени начала и окончания голосования,
	 * кнопки запуска и отмены голосования, 
	 * список кандидатов.
	 */
	
	private JTextField surnameField;
	private JTextField nameField;
	private JButton addButton;
	private JButton delateButton;
	private JButton createVoting;
	private JButton abortVoiting;
	private JButton clearButton;
	private JList<String> list;
	private ArrayList<String> candidats;
	private Date begingDate = null;
	private Date endDate = null;
	private DateFormat simpleDateTime = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, new Locale("ru"));
	
	CreatePanel(){
		
		//настройка поля ввода фамилии
		Box box1 = Box.createHorizontalBox();
		JLabel surnameLabel = new JLabel("Введите фамилию кандидата:");
		surnameField = new JTextField(15);
		surnameField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		//добавлен фильтр ввода симиволов и указано максимальное кол-во символов в поле
		try {
			((AbstractDocument)(surnameField.getDocument())).setDocumentFilter(
					new MyDocumentFilter(20));
		} catch (BadLocationException e) {}
		
		box1.add(surnameLabel);
		box1.add(Box.createHorizontalStrut(6));
		box1.add(surnameField);
		
		
		
		//настройка поля ввода имени
		Box box2 = Box.createHorizontalBox();
		JLabel nameLabel = new JLabel("Введите имя кандидата:");
		nameField = new JTextField(15);
		nameField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		//добавляем фильтр ввода  симиволов и указываем максимальное кол-во символов в поле
		try {
			((AbstractDocument)(nameField.getDocument())).setDocumentFilter(
					new MyDocumentFilter(20));
		} catch (BadLocationException e) {}
		
		box2.add(nameLabel);
		box2.add(Box.createHorizontalStrut(6));
		box2.add(nameField);
		
		
		// кнопки добавления и удаления кандидатов
		Box box3 = Box.createHorizontalBox();
		addButton = new JButton("Добавить кандидата");
		addButton.addActionListener(e->{
			Client.addCandidat(surnameField.getText(), nameField.getText());
			candidats = Client.getCandidats();
			surnameField.setText("");
			nameField.setText("");
			list.updateUI();
		});
		delateButton = new JButton("Удалить кандидата");
		delateButton.addActionListener(e1->{
			Client.removeCandidat(list.getSelectedValue());
			candidats = Client.getCandidats();
			list.updateUI();
		});
		box3.add(Box.createHorizontalGlue());
		box3.add(addButton);
		box3.add(Box.createHorizontalGlue());
		box3.add(delateButton);
		box3.add(Box.createHorizontalGlue());

		//панель для выбора даты начала голосования
		Box box4 = Box.createHorizontalBox();
		JLabel begingLabel = new JLabel("Время начала голосования:");
		box4.add(begingLabel);
		TimePanel begingPanel = new TimePanel();
		
		//панель для выбора даты окончания голосования
		Box box5 = Box.createHorizontalBox();
		JLabel endLabel = new JLabel("Время окончания голосования:");
		box5.add(endLabel);
		TimePanel endPanel = new TimePanel();
		
		//поле для вывода времени голосования
		Box box6 = Box.createHorizontalBox();
		JTextArea textArea = new JTextArea();
		textArea.setText("Начало голосования:" + "\nОкончание нолосования:");
		textArea.setEditable(false);
		box6.add(textArea);
		textArea.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		//кнопки начала и окончания голосования
		Box box7 = Box.createHorizontalBox();
		createVoting = new JButton("Создать голосование  ");
		createVoting.addActionListener(e2->{
			//проверка правильности даты начала и окончания
			if(begingPanel.getDate().after(endPanel.getDate())){
				JOptionPane.showMessageDialog(null, "<html> <center>Дата окончания голосования должна<br>"
						+ "быть больше даты начала голосования!</center>");
				return;
			}
			/*
			 * установка даты начала и окончания голосования, вывод и информации на экран 
			 * и отключение части компонентов
			 */
			int i  = Client.setDate(begingPanel.getDate(), endPanel.getDate());
			begingDate = Client.getBegingDate();
			endDate = Client.getEndDate();
			if (i == Client.OK){
				textArea.setText("Начало голосования: " + simpleDateTime.format(begingDate) +
						"\nОкончание нолосования: " + simpleDateTime.format(endDate));
				surnameField.setEnabled(false);
				nameField.setEnabled(false);
				addButton.setEnabled(false);
				delateButton.setEnabled(false);
				createVoting.setEnabled(false);
				clearButton.setEnabled(false);
				}else if(i == Client.ERROR_ON_SERVER){
					JOptionPane.showMessageDialog(null, "Ошибка на сервере!", "Ошибка связи", JOptionPane.ERROR_MESSAGE);
				}else if (i == Client.ERROR_CONNECTION){
					JOptionPane.showMessageDialog(null, "Связь сервером отсутствует!", "Ошибка связи", JOptionPane.ERROR_MESSAGE);
				}
			updateUI();
		});
		
		abortVoiting = new JButton("Отменить голосование");
		abortVoiting.addActionListener(e3 ->{
			
			int i  = Client.resetVoting(); // удаление из БД даты голосования, обнуление "проголосовал" в БД, сброс всех голосов кандидатов до 0.
			
			//включение откдюченных компонентов
			
			if ( i == Client.OK){
				textArea.setText("Начало голосования:" + "\nОкончание нолосования:");
				surnameField.setEnabled(true);
				nameField.setEnabled(true);
				addButton.setEnabled(true);
				delateButton.setEnabled(true);
				createVoting.setEnabled(true);
				clearButton.setEnabled(true);
			}else if(i == Client.ERROR_ON_SERVER){
				JOptionPane.showMessageDialog(null, "Ошибка на сервере!", "Ошибка связи", JOptionPane.ERROR_MESSAGE);
			}else if (i == Client.ERROR_CONNECTION){
				JOptionPane.showMessageDialog(null, "Связь сервером отсутствует!", "Ошибка связи", JOptionPane.ERROR_MESSAGE);
			}
			updateUI();
		});
		box7.add(createVoting);
		box7.add(Box.createHorizontalStrut(20));
		box7.add(abortVoiting);
		
		
		nameLabel.setPreferredSize(surnameLabel.getPreferredSize());
		
		
		
		
		
		Box leftBox = Box.createVerticalBox();
		leftBox.add(Box.createVerticalStrut(12));
		leftBox.add(box1);
		leftBox.add(Box.createVerticalStrut(12));
		leftBox.add(box2);
		leftBox.add(Box.createVerticalStrut(12));
		leftBox.add(box3);
		leftBox.add(Box.createVerticalStrut(24));
		leftBox.add(box4);
		leftBox.add(Box.createVerticalStrut(6));
		leftBox.add(begingPanel);
		leftBox.add(Box.createVerticalStrut(12));
		leftBox.add(box5);
		leftBox.add(Box.createVerticalStrut(6));
		leftBox.add(endPanel);
		leftBox.add(Box.createVerticalStrut(12));
		leftBox.add(box6);
		leftBox.add(Box.createVerticalStrut(24));
		leftBox.add(box7);
		leftBox.add(Box.createVerticalGlue());
		
	
		//создание списка кандидатов
		Box box1_right = Box.createHorizontalBox();
		JLabel listLabel = new JLabel("Список кандидатов:");
		box1_right.add(listLabel);
		Box box2_right = Box.createHorizontalBox();
		clearButton = new JButton("Удалить всех"); // кнопка очистки списка кандидатов
		clearButton.addActionListener(e3 ->{
			Client.removeAllCandidats();
			candidats = Client.getCandidats();
			list.updateUI();
		});
		box2_right.add(Box.createHorizontalGlue());
		box2_right.add(clearButton);
		
		
		candidats = Client.getCandidats();
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
		list.setPreferredSize(new Dimension(250,300) );
		
		Box rightBox = Box.createVerticalBox();
		rightBox.add(box1_right);
		rightBox.add(Box.createVerticalStrut(6));
		rightBox.add(new JScrollPane(list));
		rightBox.add(Box.createVerticalStrut(12));
		rightBox.add(box2_right);
		
		
		Box mainBox = Box.createHorizontalBox();
		mainBox.setBorder(new EmptyBorder(12,12,12,12));
		mainBox.add(leftBox);
		mainBox.add(Box.createHorizontalStrut(24));
		mainBox.add(rightBox);
		
		
		add(mainBox);
		
		// если голосование запущено часть компонентов отключаем
		begingDate = Client.getBegingDate();
		endDate = Client.getEndDate();
		if (begingDate != null && endDate != null){
			textArea.setText("Начало голосования: " + simpleDateTime.format(begingDate) +
					"\nОкончание нолосования: " + simpleDateTime.format(endDate));
			surnameField.setEnabled(false);
			nameField.setEnabled(false);
			addButton.setEnabled(false);
			delateButton.setEnabled(false);
			createVoting.setEnabled(false);
		}else {
			textArea.setText("Начало голосования:" + "\nОкончание нолосования:");
			surnameField.setEnabled(true);
			nameField.setEnabled(true);
			addButton.setEnabled(true);
			delateButton.setEnabled(true);
			createVoting.setEnabled(true);
		}
		
		
	}
	
}
