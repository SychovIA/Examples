package voting.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;

import voting.database.DataBase;

public class RegistrationFrame extends JFrame implements ActionListener{
	
	/*
	 * Окно регистрации пользователя
	 * Содержит: 
	 * поля для ввода фамилии, имени, логина, пароля и повтора пароля;
	 * кнопки входа, отмены и регистрации.
	 */
	
	private JTextField surnameField;
	private JTextField nameField;
	private JTextField loginField;
	private JPasswordField passwordField;
	private JPasswordField repeatPasswordField;
	private JButton ok;
	private JButton cancel;
	
	 
	RegistrationFrame(){
		//добавляем необходимые компоненты и настраиваем окно
		
	super("Voting v1.0: Регистрация");
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	setLocationByPlatform(true);
	
	JMenuBar menuBar = new JMenuBar();
	setJMenuBar(menuBar);
	
	//настройка поля фамилии
	Box box1 = Box.createHorizontalBox();
	JLabel surnameLabel = new JLabel("Введите вашу фамилию:");
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
	
	
	//настройка поля имени
	Box box2 = Box.createHorizontalBox();
	JLabel nameLabel = new JLabel("Введите ваше имя:");
	nameField = new JTextField(15);
	nameField.setBorder(new BevelBorder(BevelBorder.LOWERED));
	//добавлен фильтр ввода симиволов и указано максимальное кол-во символов в поле
	try {
		((AbstractDocument)(nameField.getDocument())).setDocumentFilter(
				new MyDocumentFilter(20));
	} catch (BadLocationException e) {}
	box2.add(nameLabel);
	box2.add(Box.createHorizontalStrut(6));
	box2.add(nameField);
	
	
	//настройка поля логина
	Box box3 = Box.createHorizontalBox();
	JLabel loginLabel = new JLabel("Введите ваш логин:");
	loginField = new JTextField(15);
	loginField.setBorder(new BevelBorder(BevelBorder.LOWERED));
	//добавлен фильтр ввода симиволов и указано максимальное кол-во символов в поле
	try {
		((AbstractDocument)(loginField.getDocument())).setDocumentFilter(
				new MyDocumentFilter(20));
	} catch (BadLocationException e) {}
	box3.add(loginLabel);
	box3.add(Box.createHorizontalStrut(6));
	box3.add(loginField);

	
	//настройка поля пароля
	Box box4 = Box.createHorizontalBox();
	JLabel passwordLabel = new JLabel("Введите пароль:");
	passwordField = new JPasswordField(15);
	passwordField.setBorder(new BevelBorder(BevelBorder.LOWERED));
	box4.add(passwordLabel);
	box4.add(Box.createHorizontalStrut(6));
	box4.add(passwordField);
	
	
	//настройка поля повтора пароля
	Box box5 = Box.createHorizontalBox();
	JLabel repeatPasswordLabel = new JLabel("Подтвердите пароль:");
	repeatPasswordField = new JPasswordField(15);
	repeatPasswordField.setBorder(new BevelBorder(BevelBorder.LOWERED));
	box5.add(repeatPasswordLabel);
	box5.add(Box.createHorizontalStrut(6));
	box5.add(repeatPasswordField);

	
	// кнопки ОК и Отмена
	Box box6 = Box.createHorizontalBox();
	ok = new JButton("OK");
	ok.addActionListener(this);
	cancel = new JButton("Отмена");
	cancel.addActionListener( e ->{
		dispose();
		
	});
	box6.add(Box.createHorizontalGlue());
	box6.add(ok);
	box6.add(Box.createHorizontalStrut(12));
	box6.add(cancel);

	loginLabel.setPreferredSize(surnameLabel.getPreferredSize());
	nameLabel.setPreferredSize(surnameLabel.getPreferredSize());
	loginLabel.setPreferredSize(surnameLabel.getPreferredSize());
	passwordLabel.setPreferredSize(surnameLabel.getPreferredSize());
	repeatPasswordLabel.setPreferredSize(surnameLabel.getPreferredSize());

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


	@Override
	public void actionPerformed(ActionEvent e) {
		
		//проверка коректности введеной в поля информации
		if(surnameField.getText().equals("")){
			JOptionPane.showMessageDialog(null, "Введите фамилию!", "Регистрация", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if (nameField.getText().equals("")){
			JOptionPane.showMessageDialog(null, "Введите имя!", "Регистрация", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if(loginField.getText().equals("")){
			JOptionPane.showMessageDialog(null, "Введите логин!", "Регистрация", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if (passwordField.getPassword().length == 0){
			JOptionPane.showMessageDialog(null, "Введите пароль!", "Регистрация", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if (repeatPasswordField.getPassword().length == 0){
			JOptionPane.showMessageDialog(null, "Подтвердите пароль!", "Регистрация", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		if (!Arrays.equals(passwordField.getPassword(), repeatPasswordField.getPassword())){
			JOptionPane.showMessageDialog(null, "Пароли не совпадают!", "Регистрация", JOptionPane.INFORMATION_MESSAGE);
			passwordField.setText("");
			repeatPasswordField.setText("");
			return;
		}
		
			
		boolean b = DataBase.registation(surnameField.getText(), nameField.getText(), loginField.getText(), passwordField.getPassword()); //регистрация
		
		if (b){ 
			dispose(); //при успешной регистрации окно закрывается
			}
		
	}
	
	

}
