package voting.ui;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
import voting.database.UserData;

public class EnterFrame extends JFrame{
	/*
	 * Окно входа в систему голосования
	 * Содержит поля для ввода логина и пароля,
	 * кнопки входа, отмены и регистрации.
	 */
	 
	private JTextField loginField;
	private JPasswordField passwordField;
	private JButton okButton;
	private JButton cancelButton;
	private JButton regButton;
	
	
	EnterFrame(){
		// добаволяем компоненты и настраиваем окно
	super("Вход в систему");
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setLocationByPlatform(true);
	
	//панель с часами и текущей датой
	JMenuBar menuBar = new JMenuBar();
	menuBar.setOpaque(false);
	menuBar.setBorder(null);
	menuBar.add(Box.createHorizontalGlue());
	Clock clock = new Clock();
	menuBar.add(clock);
	setJMenuBar(menuBar);
	
	
	Box box = Box.createHorizontalBox();
	JLabel label = new JLabel("Введите логин и пароль");
	label.setFont(new Font("Tahoma", Font.BOLD, 14));
	box.add(label);
	
	//настройка поля ввода логина
	Box box1 = Box.createHorizontalBox();
	JLabel loginLabel = new JLabel("Логин:");
	loginField = new JTextField(15);
	loginField.setBorder(new BevelBorder(BevelBorder.LOWERED));
	//добавляем фильтр ввода симиволов и указываем максимальное кол-во символов в поле
	try {
		((AbstractDocument)(loginField.getDocument())).setDocumentFilter(
				new MyDocumentFilter(20));
	} catch (BadLocationException e) {e.printStackTrace();}
	box1.add(loginLabel);
	box1.add(Box.createHorizontalStrut(6));
	box1.add(loginField);
	
	//настройка поля ввода пароля
		Box box2 = Box.createHorizontalBox();
	JLabel passwordLabel = new JLabel("Пароль:");
	passwordField = new JPasswordField(15);
	passwordField.setBorder(new BevelBorder(BevelBorder.LOWERED));
	box2.add(passwordLabel);
	box2.add(Box.createHorizontalStrut(6));
	box2.add(passwordField);
	
	// кнопка регистрации
	Box box3 = Box.createHorizontalBox();
	regButton = new JButton("Регистрация"); 
	box3.add(regButton);
	box3.add(Box.createHorizontalGlue());
	regButton.addActionListener(e -> {
			// создание нового окна регистрации
			RegistrationFrame registrationWindow = new RegistrationFrame();
			registrationWindow.setVisible(true);
			
			// добавление слушателя для нового окна
			registrationWindow.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosed(WindowEvent e) {
					// TODO Auto-generated method stub
					setVisible(true); //при закрытии окна регистрации окно входа становиться видимым
				}

				@Override
				public void windowOpened(WindowEvent e) {
					// TODO Auto-generated method stub
					setVisible(false); //при открытии окна регистрации окно входа становиться не видимым
				}
			});
	});
	
	// кнопки входа и отмены
	Box box4 = Box.createHorizontalBox();
	okButton = new JButton("OK");
	okButton.addActionListener( e ->{
		
		DataBase.enter(loginField.getText(), passwordField.getPassword()); //запрос на вход
		// проверка данных пользователя
		// вход будет считаться успешным если данные пользователя не пусты.
		if(!UserData.isEmpty()){
			String category = UserData.getCategory();
			switch (category) {
			
			case "elector":
				new ElectorFrame().setVisible(true);
				dispose();
				break;
			case "admin":
				new AdminFrame().setVisible(true);
				dispose();
				break;
			default:
				System.out.println();
				JOptionPane.showMessageDialog(null, "Ошибка входа!", "Вход в систему", JOptionPane.ERROR_MESSAGE);
				break;
			}
		}else{
			JOptionPane.showMessageDialog(null, "Неверный логин/пароль!", "Вход в систему", JOptionPane.INFORMATION_MESSAGE);
		}
	});
	cancelButton = new JButton("Отмена");
	cancelButton.addActionListener( e1 ->{
		System.exit(0); // Закрытие приложения
	});
	box4.add(Box.createHorizontalGlue());
	box4.add(okButton);
	box4.add(Box.createHorizontalStrut(12));
	box4.add(cancelButton);

	
	
	loginLabel.setPreferredSize(passwordLabel.getPreferredSize());

	Box mainBox = Box.createVerticalBox();
	mainBox.setBorder(new EmptyBorder(12,12,12,12));
	mainBox.add(box);
	mainBox.add(Box.createVerticalStrut(20));
	mainBox.add(box1);
	mainBox.add(Box.createVerticalStrut(12));
	mainBox.add(box2);
	mainBox.add(Box.createVerticalStrut(12));
	mainBox.add(box3);
	mainBox.add(Box.createVerticalStrut(20));
	mainBox.add(box4);
	setContentPane(mainBox);
	pack();
	setResizable(false);
	
	
	}
	

}
