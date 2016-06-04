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
	 * ���� ����� � ������� �����������
	 * �������� ���� ��� ����� ������ � ������,
	 * ������ �����, ������ � �����������.
	 */
	 
	private JTextField loginField;
	private JPasswordField passwordField;
	private JButton okButton;
	private JButton cancelButton;
	private JButton regButton;
	
	
	EnterFrame(){
		// ���������� ���������� � ����������� ����
	super("���� � �������");
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setLocationByPlatform(true);
	
	//������ � ������ � ������� �����
	JMenuBar menuBar = new JMenuBar();
	menuBar.setOpaque(false);
	menuBar.setBorder(null);
	menuBar.add(Box.createHorizontalGlue());
	Clock clock = new Clock();
	menuBar.add(clock);
	setJMenuBar(menuBar);
	
	
	Box box = Box.createHorizontalBox();
	JLabel label = new JLabel("������� ����� � ������");
	label.setFont(new Font("Tahoma", Font.BOLD, 14));
	box.add(label);
	
	//��������� ���� ����� ������
	Box box1 = Box.createHorizontalBox();
	JLabel loginLabel = new JLabel("�����:");
	loginField = new JTextField(15);
	loginField.setBorder(new BevelBorder(BevelBorder.LOWERED));
	//��������� ������ ����� ��������� � ��������� ������������ ���-�� �������� � ����
	try {
		((AbstractDocument)(loginField.getDocument())).setDocumentFilter(
				new MyDocumentFilter(20));
	} catch (BadLocationException e) {e.printStackTrace();}
	box1.add(loginLabel);
	box1.add(Box.createHorizontalStrut(6));
	box1.add(loginField);
	
	//��������� ���� ����� ������
		Box box2 = Box.createHorizontalBox();
	JLabel passwordLabel = new JLabel("������:");
	passwordField = new JPasswordField(15);
	passwordField.setBorder(new BevelBorder(BevelBorder.LOWERED));
	box2.add(passwordLabel);
	box2.add(Box.createHorizontalStrut(6));
	box2.add(passwordField);
	
	// ������ �����������
	Box box3 = Box.createHorizontalBox();
	regButton = new JButton("�����������"); 
	box3.add(regButton);
	box3.add(Box.createHorizontalGlue());
	regButton.addActionListener(e -> {
			// �������� ������ ���� �����������
			RegistrationFrame registrationWindow = new RegistrationFrame();
			registrationWindow.setVisible(true);
			
			// ���������� ��������� ��� ������ ����
			registrationWindow.addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosed(WindowEvent e) {
					// TODO Auto-generated method stub
					setVisible(true); //��� �������� ���� ����������� ���� ����� ����������� �������
				}

				@Override
				public void windowOpened(WindowEvent e) {
					// TODO Auto-generated method stub
					setVisible(false); //��� �������� ���� ����������� ���� ����� ����������� �� �������
				}
			});
	});
	
	// ������ ����� � ������
	Box box4 = Box.createHorizontalBox();
	okButton = new JButton("OK");
	okButton.addActionListener( e ->{
		
		DataBase.enter(loginField.getText(), passwordField.getPassword()); //������ �� ����
		// �������� ������ ������������
		// ���� ����� ��������� �������� ���� ������ ������������ �� �����.
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
				JOptionPane.showMessageDialog(null, "������ �����!", "���� � �������", JOptionPane.ERROR_MESSAGE);
				break;
			}
		}else{
			JOptionPane.showMessageDialog(null, "�������� �����/������!", "���� � �������", JOptionPane.INFORMATION_MESSAGE);
		}
	});
	cancelButton = new JButton("������");
	cancelButton.addActionListener( e1 ->{
		System.exit(0); // �������� ����������
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
