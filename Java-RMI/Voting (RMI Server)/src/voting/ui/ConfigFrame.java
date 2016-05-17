package voting.ui;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import voting.database.DataBaseImpl;



public class ConfigFrame extends JFrame{
	/*
	 *  Окно настройки подключения к СУБД.
	 *  Содержит поля для ввода
	 *  имени сервера,
	 *  номера порта,
	 *  имени пользователя СУБД,
	 *  пароля,
	 *  и текущей базы данных.
	 *  после подключения к СУБД открывается окно входа в систему голосования и данное окно закрывается
	 */
	
	private JTextField hostField;
	private JTextField portField;
	private JTextField userField;
	private JTextField dbField;
	private JPasswordField passField;
	private JButton okButton;
	private JButton cancelButton;
	
	public ConfigFrame(){
		//добавляем необходимые компоненты и настраиваем окно
		
		super("Подключение к MySQL");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		
		Box box1 = Box.createHorizontalBox();
		JLabel hostLabel = new JLabel("Имя (IP) Сервера:");
		hostField = new JTextField(10);
		hostField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		hostField.setText("localhost");
		box1.add(hostLabel);
		box1.add(Box.createHorizontalStrut(6));
		box1.add(hostField);
		
		Box box2 = Box.createHorizontalBox();
		JLabel portLabel = new JLabel("Номер порта:");
		portField = new JTextField(10);
		portField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		portField.setText("3306");
		box2.add(portLabel);
		box2.add(Box.createHorizontalStrut(6));
		box2.add(portField);
		
		Box box3 = Box.createHorizontalBox();
		JLabel userLabel = new JLabel("Имя пользователя:");
		userField = new JTextField(10);
		userField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		box3.add(userLabel);
		box3.add(Box.createHorizontalStrut(6));
		box3.add(userField);
		
		Box box4 = Box.createHorizontalBox();
		JLabel passLabel = new JLabel("Пароль:");
		passField = new JPasswordField(10);
		passField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		box4.add(passLabel);
		box4.add(Box.createHorizontalStrut(6));
		box4.add(passField);
		
		Box box5 = Box.createHorizontalBox();
		JLabel bdLabel = new JLabel("База данных:");
		dbField = new JTextField(10);
		dbField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		box5.add(bdLabel);
		box5.add(Box.createHorizontalStrut(6));
		box5.add(dbField);
		
		Box box6 = Box.createHorizontalBox();
		okButton = new JButton("OK");
		okButton.addActionListener(e->{
			DataBaseImpl.setHost(hostField.getText());
			DataBaseImpl.setPort(portField.getText());
			DataBaseImpl.setUserName(userField.getText());
			DataBaseImpl.setPassword(passField.getPassword());
			DataBaseImpl.setDb(dbField.getText());
			dispose();
							
		});
		cancelButton = new JButton("Отмена");
		cancelButton.addActionListener( e1 ->{
			dispose();
		});
		box6.add(Box.createHorizontalGlue());
		box6.add(okButton);
		box6.add(Box.createHorizontalStrut(12));
		box6.add(cancelButton);
		
		hostLabel.setPreferredSize(userLabel.getPreferredSize());
		portLabel.setPreferredSize(userLabel.getPreferredSize());
		passLabel.setPreferredSize(userLabel.getPreferredSize());
		bdLabel.setPreferredSize(userLabel.getPreferredSize());
		
		Box mainBox = Box.createVerticalBox();
		mainBox.setBorder(new EmptyBorder(12,12,12,12));
		mainBox.add(box1);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(box2);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(box3);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(box4);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(box5);
		mainBox.add(Box.createVerticalStrut(20));
		mainBox.add(box6);
		setContentPane(mainBox);
		pack();
		setResizable(false);
		
	}
}
