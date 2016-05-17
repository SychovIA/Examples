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
	 * ������ ��� �������� �����������.
	 * �������� � ����:
	 * ���� ��� ����� ������� � ����� ���������,
	 * ������ ���������� � �������� ����������,
	 * ������ ��� ��������� ������� ������ � ��������� �����������,
	 * ������ ��� ������ ������� ������ � ��������� �����������,
	 * ������ ������� � ������ �����������, 
	 * ������ ����������.
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
		
		//��������� ���� ����� �������
		Box box1 = Box.createHorizontalBox();
		JLabel surnameLabel = new JLabel("������� ������� ���������:");
		surnameField = new JTextField(15);
		surnameField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		//�������� ������ ����� ��������� � ������� ������������ ���-�� �������� � ����
		try {
			((AbstractDocument)(surnameField.getDocument())).setDocumentFilter(
					new MyDocumentFilter(20));
		} catch (BadLocationException e) {}
		
		box1.add(surnameLabel);
		box1.add(Box.createHorizontalStrut(6));
		box1.add(surnameField);
		
		
		
		//��������� ���� ����� �����
		Box box2 = Box.createHorizontalBox();
		JLabel nameLabel = new JLabel("������� ��� ���������:");
		nameField = new JTextField(15);
		nameField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		//��������� ������ �����  ��������� � ��������� ������������ ���-�� �������� � ����
		try {
			((AbstractDocument)(nameField.getDocument())).setDocumentFilter(
					new MyDocumentFilter(20));
		} catch (BadLocationException e) {}
		
		box2.add(nameLabel);
		box2.add(Box.createHorizontalStrut(6));
		box2.add(nameField);
		
		
		// ������ ���������� � �������� ����������
		Box box3 = Box.createHorizontalBox();
		addButton = new JButton("�������� ���������");
		addButton.addActionListener(e->{
			Client.addCandidat(surnameField.getText(), nameField.getText());
			candidats = Client.getCandidats();
			surnameField.setText("");
			nameField.setText("");
			list.updateUI();
		});
		delateButton = new JButton("������� ���������");
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

		//������ ��� ������ ���� ������ �����������
		Box box4 = Box.createHorizontalBox();
		JLabel begingLabel = new JLabel("����� ������ �����������:");
		box4.add(begingLabel);
		TimePanel begingPanel = new TimePanel();
		
		//������ ��� ������ ���� ��������� �����������
		Box box5 = Box.createHorizontalBox();
		JLabel endLabel = new JLabel("����� ��������� �����������:");
		box5.add(endLabel);
		TimePanel endPanel = new TimePanel();
		
		//���� ��� ������ ������� �����������
		Box box6 = Box.createHorizontalBox();
		JTextArea textArea = new JTextArea();
		textArea.setText("������ �����������:" + "\n��������� �����������:");
		textArea.setEditable(false);
		box6.add(textArea);
		textArea.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		//������ ������ � ��������� �����������
		Box box7 = Box.createHorizontalBox();
		createVoting = new JButton("������� �����������  ");
		createVoting.addActionListener(e2->{
			//�������� ������������ ���� ������ � ���������
			if(begingPanel.getDate().after(endPanel.getDate())){
				JOptionPane.showMessageDialog(null, "<html> <center>���� ��������� ����������� ������<br>"
						+ "���� ������ ���� ������ �����������!</center>");
				return;
			}
			/*
			 * ��������� ���� ������ � ��������� �����������, ����� � ���������� �� ����� 
			 * � ���������� ����� �����������
			 */
			int i  = Client.setDate(begingPanel.getDate(), endPanel.getDate());
			begingDate = Client.getBegingDate();
			endDate = Client.getEndDate();
			if (i == Client.OK){
				textArea.setText("������ �����������: " + simpleDateTime.format(begingDate) +
						"\n��������� �����������: " + simpleDateTime.format(endDate));
				surnameField.setEnabled(false);
				nameField.setEnabled(false);
				addButton.setEnabled(false);
				delateButton.setEnabled(false);
				createVoting.setEnabled(false);
				clearButton.setEnabled(false);
				}else if(i == Client.ERROR_ON_SERVER){
					JOptionPane.showMessageDialog(null, "������ �� �������!", "������ �����", JOptionPane.ERROR_MESSAGE);
				}else if (i == Client.ERROR_CONNECTION){
					JOptionPane.showMessageDialog(null, "����� �������� �����������!", "������ �����", JOptionPane.ERROR_MESSAGE);
				}
			updateUI();
		});
		
		abortVoiting = new JButton("�������� �����������");
		abortVoiting.addActionListener(e3 ->{
			
			int i  = Client.resetVoting(); // �������� �� �� ���� �����������, ��������� "������������" � ��, ����� ���� ������� ���������� �� 0.
			
			//��������� ����������� �����������
			
			if ( i == Client.OK){
				textArea.setText("������ �����������:" + "\n��������� �����������:");
				surnameField.setEnabled(true);
				nameField.setEnabled(true);
				addButton.setEnabled(true);
				delateButton.setEnabled(true);
				createVoting.setEnabled(true);
				clearButton.setEnabled(true);
			}else if(i == Client.ERROR_ON_SERVER){
				JOptionPane.showMessageDialog(null, "������ �� �������!", "������ �����", JOptionPane.ERROR_MESSAGE);
			}else if (i == Client.ERROR_CONNECTION){
				JOptionPane.showMessageDialog(null, "����� �������� �����������!", "������ �����", JOptionPane.ERROR_MESSAGE);
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
		
	
		//�������� ������ ����������
		Box box1_right = Box.createHorizontalBox();
		JLabel listLabel = new JLabel("������ ����������:");
		box1_right.add(listLabel);
		Box box2_right = Box.createHorizontalBox();
		clearButton = new JButton("������� ����"); // ������ ������� ������ ����������
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
		
		// ���� ����������� �������� ����� ����������� ���������
		begingDate = Client.getBegingDate();
		endDate = Client.getEndDate();
		if (begingDate != null && endDate != null){
			textArea.setText("������ �����������: " + simpleDateTime.format(begingDate) +
					"\n��������� �����������: " + simpleDateTime.format(endDate));
			surnameField.setEnabled(false);
			nameField.setEnabled(false);
			addButton.setEnabled(false);
			delateButton.setEnabled(false);
			createVoting.setEnabled(false);
		}else {
			textArea.setText("������ �����������:" + "\n��������� �����������:");
			surnameField.setEnabled(true);
			nameField.setEnabled(true);
			addButton.setEnabled(true);
			delateButton.setEnabled(true);
			createVoting.setEnabled(true);
		}
		
		
	}
	
}
