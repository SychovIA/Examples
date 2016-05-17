package server.ui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import server.ServerConection;
import server.database.DataBase;

public class ServerFrame extends JFrame implements Runnable, ActionListener{
	
	/*
	 * ���� �������.
	 * �������� ���� ��� ������ ���������,
	 * ������ ������� �������, ��������� ���������� � ��, �������� ������.
	 * ��� ������� ������ � ������� ����������� ���������� ������ ������� (������� ���� - ������ �������, ������� - ���)
	 */
	
	private static JTextArea textArea;
	private JButton startButton;
	private JButton configButton;
	private JButton hideButton;
	private PopupMenu popupMenu;
	private MenuItem runItem;
	private MenuItem hideItem;
	private MenuItem exitItem;
	private TrayIcon trayIcon;
	private File file1 = new File ("images/IconGreen.png");
	private File file2 = new File ("images/IconRed.png");
	private boolean running;
	private Thread thread;
	private ServerSocket ss;
	private BufferedImage image;
	private static DateFormat simpleTime = DateFormat.getTimeInstance(DateFormat.SHORT, new Locale("ru"));


	public ServerFrame()  {
		//���������� � ��������� ����������� ���� 
		super("������");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setSize(400, 400);
		setMinimumSize(new Dimension (300, 200));
		
		// ������ � ��������
		Box box = Box.createHorizontalBox();
		startButton = new JButton("Run");
		startButton.setToolTipText("������ �������");
		startButton.addActionListener(this);
		configButton = new JButton("��������� ��");
		configButton.setToolTipText("��������� ����������� � ���� ������");
		configButton.addActionListener(this);
		hideButton = new JButton("������� �����");
		hideButton.setToolTipText("������� � ������� �����");
		hideButton.addActionListener(this);
		box.setBorder(new EmptyBorder(6, 6, 6, 6));
		box.add(startButton);
		box.add(Box.createHorizontalStrut(6));
		box.add(configButton);
		box.add(Box.createHorizontalGlue());
		box.add(hideButton);
		getContentPane().add(box, BorderLayout.NORTH);
		
		//���� ��� ������ ���������
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setMargin(new Insets(6, 6, 6, 6));
		JScrollPane scrollPane = new JScrollPane(textArea);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		
		
		// ��������� ������������ ���� � ������ �������
		popupMenu = new PopupMenu();
		runItem = new MenuItem("���������...");
		runItem.addActionListener(this);
		popupMenu.add(runItem);
		hideItem = new MenuItem("������� ����������");
		hideItem.addActionListener(this);
		popupMenu.add(hideItem);
		exitItem = new MenuItem("�����");
		exitItem.addActionListener(e ->{
			System.exit(0);
		});
		popupMenu.add(exitItem);
		image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		trayIcon = new TrayIcon(image, "������ (Offline)", popupMenu);
		trayIcon.addActionListener(this);

	}
	
	
	

	@Override
	public void run() {
		// ����� ��� �������� �������� ������� � ��������
		Executor executor = Executors.newFixedThreadPool(5);
		try {
			ss = new ServerSocket(3000);
			while (running)
				executor.execute(new ServerConection(ss.accept()));
			
			
		} catch (IOException e) {
			
			addMessage("Server Error: " + e.getMessage());
			running = false;
		}
	}
	
	
	
	// ���������� ��������� � textArea
		public static void addMessage(String message){
			textArea.append(simpleTime.format(new Date()) + ": " +message + "\n");
		}

		
		
		
	private void start(){ 
		// ������ ������
		if(!running){
			addMessage("Connecting to Database...");
			if (DataBase.createTable()){
				addMessage("Database connected!");
				configButton.setEnabled(false);
				running = true;
				thread = new Thread(this);
				thread.start();
				addMessage("Server running...");
				
			} else
			return;
		}else{
			JOptionPane.showMessageDialog(null, "������ ��� �������!", "������", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	
private void hideFrame(){
		
		//������ �������� ������
		
		
				setVisible(false);
			if(running){
				
				try {
					image = ImageIO.read(file1);
					trayIcon.setImage(image);
					trayIcon.setToolTip("������ (Online)");
					
					
					SystemTray.getSystemTray().add(trayIcon);
				} catch (Exception e1) {
					
					setVisible(true);
				}
			}else{
				
				try {
					image = ImageIO.read(file2);
					trayIcon.setImage(image);
					trayIcon.setToolTip("������ (Offline)");
					SystemTray.getSystemTray().add(trayIcon);
				} catch (Exception e1) {
					
					setVisible(true);
				}
			}
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(startButton)){
			start();
		}else if(e.getSource().equals(configButton)){
			JFrame config = new ConfigFrame();
			config.setVisible(true);
		}else if(e.getSource().equals(hideButton)){
			hideFrame();
		}else if(e.equals(runItem)){
			start();
			if(running){
				try {
					image = ImageIO.read(file1);
					trayIcon.setImage(image);
					trayIcon.setToolTip("������ (Online)");
				} catch (Exception e1) {
					setVisible(true);
					SystemTray.getSystemTray().remove(trayIcon);	
				}
			}else
			JOptionPane.showMessageDialog(null, "������ ��� ������� �������!", "������", JOptionPane.INFORMATION_MESSAGE);
						
		}else if(e.getSource().equals(hideItem) || e.getSource().equals(trayIcon)){
			setVisible(true);
			SystemTray.getSystemTray().remove(trayIcon);
		}else if(e.getSource().equals(exitItem))
			System.exit(0);
		
	}
	
}
