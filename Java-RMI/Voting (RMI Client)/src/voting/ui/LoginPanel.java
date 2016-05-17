package voting.ui;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;

import voting.database.DataBase;





public class LoginPanel extends JPanel implements ActionListener{
	
	/*
	 * Панель для смены логина и пароля.
	 * Содержит: 
	 * поля для ввода новых логина и пароля, старого пароля и повтора пароля;
	 * кнопки ОК и Отмена.
	 * Все компоненты расположены в центре панели.
	 */
	
	private JTextField 	newLoginField;
	private JPasswordField oldPasswordField;
	private JPasswordField newPasswordField;
	private JPasswordField repeatPassField;
	private JButton okButton;
	private JButton cancelButton;
	
	LoginPanel(){
		
		GridBagLayout  gridBagLayout= new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {1};
		gridBagLayout.rowHeights = new int[] {1};
		setLayout(gridBagLayout);
		
		Box box = Box.createHorizontalBox();
		JLabel label = new JLabel("Смена логина");
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		box.add(label);
		
		//настройка поля ввода нового логина
		Box box1 = Box.createHorizontalBox();
		JLabel newLoginLabel = new JLabel("Новый логин:");
		newLoginField = new JTextField(15);
		newLoginField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		//добавлен фильтр ввода симиволов и указано максимальное кол-во символов в поле
		try {
			((AbstractDocument)(newLoginField.getDocument())).setDocumentFilter(
					new MyDocumentFilter(20));
		} catch (BadLocationException e) {}
		box1.add(newLoginLabel);
		box1.add(Box.createHorizontalStrut(6));
		box1.add(newLoginField);
		
		//настройка поля ввода старого пароля
 		Box box2 = Box.createHorizontalBox();
		JLabel oldPasswordLabel = new JLabel("Старый пароль:");
		oldPasswordField = new JPasswordField(15);
		oldPasswordField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		box2.add(oldPasswordLabel);
		box2.add(Box.createHorizontalStrut(6));
		box2.add(oldPasswordField);
		
		//настройка поля ввода нового пароля
		Box box3 = Box.createHorizontalBox();
		JLabel newPasswordLabel = new JLabel("Новый пароль:");
		newPasswordField = new JPasswordField(15);
		newPasswordField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		box3.add(newPasswordLabel);
		box3.add(Box.createHorizontalStrut(6));
		box3.add(newPasswordField);
		
		//настройка поля ввода повтора пароля
		Box box4 = Box.createHorizontalBox();
		JLabel repeatPassLabel = new JLabel("Повторите пароль:");
		repeatPassField = new JPasswordField(15);
		repeatPassField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		box4.add(repeatPassLabel);
		box4.add(Box.createHorizontalStrut(6));
		box4.add(repeatPassField);
		
		// кнопки ОК и Отмена
		Box box5 = Box.createHorizontalBox();
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		cancelButton = new JButton("Отмена");
		cancelButton.addActionListener(e1 -> {
			
			newLoginField.setText("");
			oldPasswordField.setText("");
			newPasswordField.setText("");
			repeatPassField.setText("");
		});
		box5.add(Box.createHorizontalGlue());
		box5.add(okButton);
		box5.add(Box.createHorizontalStrut(12));
		box5.add(cancelButton);
		
		newLoginLabel.setPreferredSize(repeatPassLabel.getPreferredSize());
		oldPasswordLabel.setPreferredSize(repeatPassLabel.getPreferredSize());
		newPasswordLabel.setPreferredSize(repeatPassLabel.getPreferredSize());
		
		
		
		Box mainBox = Box.createVerticalBox();
		mainBox.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(12, 12, 12, 12)) );
		mainBox.add(box);
		mainBox.add(Box.createVerticalStrut(20));
		mainBox.add(box1);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(box2);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(box3);
		mainBox.add(Box.createVerticalStrut(12));
		mainBox.add(box4);
		mainBox.add(Box.createVerticalStrut(20));
		mainBox.add(box5);
		
		add(mainBox);
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//проверка коректности введеной в поля информации
		if("".equals(newLoginField.getText())){
		JOptionPane.showMessageDialog(null, "Введите фамилию!", "Смена логина", JOptionPane.INFORMATION_MESSAGE);
		return;
		}
		if (oldPasswordField.getPassword().length == 0){
		JOptionPane.showMessageDialog(null, "Введите старый пароль!", "Смена логина", JOptionPane.INFORMATION_MESSAGE);
		return;
		}
	
		if (newPasswordField.getPassword().length == 0){
		JOptionPane.showMessageDialog(null, "Введите новый пароль!", "Смена логина", JOptionPane.INFORMATION_MESSAGE);
		return;
		}
		if (repeatPassField.getPassword().length == 0){
		JOptionPane.showMessageDialog(null, "Подтвердите пароль!", "Смена логина", JOptionPane.INFORMATION_MESSAGE);
		return;
		}
		if (!Arrays.equals(newPasswordField.getPassword(), repeatPassField.getPassword())){
			JOptionPane.showMessageDialog(null, "Пароли не совпадают!");
			newPasswordField.setText("");
			repeatPassField.setText("");
			return;
		}
		
		int i = DataBase.changeLogin(DataBase.getCurrentUser().getLogin(), newLoginField.getText(), newPasswordField.getPassword(), oldPasswordField.getPassword()); //смена логина и пароля
		if(i == DataBase.OK)
			JOptionPane.showMessageDialog(null, "Логин и пароль измнены", "Смена логина", JOptionPane.INFORMATION_MESSAGE);
		else if (i == DataBase.ERROR_ON_SERVER)
			JOptionPane.showMessageDialog(null, "Логин уже существует", "Смена логина", JOptionPane.INFORMATION_MESSAGE);		
		else if(i == DataBase.FALSE)
			JOptionPane.showMessageDialog(null, "Неверный пароль", "Смена логина", JOptionPane.INFORMATION_MESSAGE);
		else 
			JOptionPane.showMessageDialog(null, "Связь сервером отсутствует!", "Ошибка связи", JOptionPane.ERROR_MESSAGE);
	}
}
