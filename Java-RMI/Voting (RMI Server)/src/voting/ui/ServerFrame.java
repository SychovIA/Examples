package voting.ui;


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

import java.net.MalformedURLException;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import voting.database.DataBaseImpl;



public class ServerFrame extends JFrame implements ActionListener{
	
	/*
	 * ���� �������.
	 * �������� ���� ��� ������ ���������,
	 * ������ ������� � ��������� �������, ��������� ���������� � ��, �������� ������.
	 * ��� ������� ������ � ������� ����������� ���������� ������ ������� (������� ���� - ������ �������, ������� - ���)
	 */
	
	private static JTextArea textArea;
	Registry reg;
	private JButton startButton;
	private JButton stopButton;
	private JButton configButton;
	private JButton hideButton;
	private PopupMenu popupMenu;
	private MenuItem startItem;
	private MenuItem stopItem;
	private MenuItem showItem;
	private MenuItem exitItem;
	private TrayIcon trayIcon;
	private File file1 = new File ("images/IconGreen.png");
	private File file2 = new File ("images/IconRed.png");
	private boolean running;
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
		Box box1 = Box.createHorizontalBox();
		startButton = new JButton("Start");
		startButton.setToolTipText("������ �������");
		startButton.addActionListener(this);
		stopButton = new JButton("Stop");
		stopButton.setToolTipText("��������� �������");
		stopButton.addActionListener(this);
		configButton = new JButton("��������� ��");
		configButton.setToolTipText("��������� ����������� � ���� ������");
		configButton.addActionListener(this);
		
		box1.setBorder(new EmptyBorder(6, 6, 6, 6));
		box1.add(startButton);
		box1.add(Box.createHorizontalStrut(6));
		box1.add(stopButton);
		box1.add(Box.createHorizontalStrut(12));
		box1.add(configButton);
		
		
		Box box2 = Box.createHorizontalBox();
		hideButton = new JButton("������� �����");
		hideButton.setToolTipText("������� � ������� �����");
		hideButton.addActionListener(this);
		box2.setBorder(new EmptyBorder(6, 6, 6, 6));
		box2.add(Box.createHorizontalGlue());
		box2.add(hideButton);
		
		
		getContentPane().add(box1, BorderLayout.NORTH);
		getContentPane().add(box2, BorderLayout.SOUTH);
		
		//���� ��� ������ ���������
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setMargin(new Insets(6, 6, 6, 6));
		JScrollPane scrollPane = new JScrollPane(textArea);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		
		
		// ��������� ������������ ���� � ������ �������
		popupMenu = new PopupMenu();
		startItem = new MenuItem("���������");
		startItem.addActionListener(this);
		popupMenu.add(startItem);
		stopItem = new MenuItem("����������");
		stopItem.addActionListener(this);
		popupMenu.add(stopItem);
		showItem = new MenuItem("������� ����������");
		showItem.addActionListener(this);
		popupMenu.add(showItem);
		exitItem = new MenuItem("�����");
		exitItem.addActionListener(e ->{
			System.exit(0);
		});
		popupMenu.add(exitItem);
		image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		trayIcon = new TrayIcon(image, "������ (Offline)", popupMenu);
		trayIcon.addActionListener(this);

	}
	
	

	// ���������� ��������� � textArea
		public static void addMessage(String message){
			textArea.append(simpleTime.format(new Date()) + ": " +message + "\n");
		}

		
		
		
	private void start(){ 
		// ������ �������
		if(!running){
			addMessage("Connecting to Database...");
				
				
				try {
					DataBaseImpl dataBase = new DataBaseImpl();
					if (dataBase.createTable()){
						addMessage("Database connected!");
						addMessage("RMI server starting... ");
					} else
						return;
					reg = LocateRegistry.createRegistry(1099);
					addMessage("RMI registry created!");
					Naming.rebind("rmi://localhost:1099/DataService", dataBase);
					configButton.setEnabled(false);
					running = true;
					addMessage("RMI server started success! ");
				} catch (RemoteException | MalformedURLException e) {
					addMessage("ERROR: RMI server exeption: " + e);
				}
			
			
		}else{
			JOptionPane.showMessageDialog(null, "������ ��� �������!", "������", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private void stop(){ // ��������� �������
		if(running){
			addMessage("RMI server stoping..");
			try {
				
				Naming.unbind("rmi://localhost:1099/DataService");
				UnicastRemoteObject.unexportObject(reg, true);
				running = false;
				configButton.setEnabled(true);
				addMessage("RMI server stoped success!");
			} catch (RemoteException | MalformedURLException | NotBoundException e) {
				addMessage("ERROR: RMI server exeption: " + e);
			}
		}else{
			JOptionPane.showMessageDialog(null, "������ ��� ����������!", "������", JOptionPane.INFORMATION_MESSAGE);
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
					e1.printStackTrace();
					setVisible(true);
				}
			}else{
				
				try {
					image = ImageIO.read(file2);
					trayIcon.setImage(image);
					trayIcon.setToolTip("������ (Offline)");
					SystemTray.getSystemTray().add(trayIcon);
				} catch (Exception e1) {
					e1.printStackTrace();
					setVisible(true);
				}
			}
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(startButton)){
			start();
		}else if (e.getSource().equals(stopButton)){
			stop();
		}else if(e.getSource().equals(configButton)){
			JFrame config = new ConfigFrame();
			config.setVisible(true);
		}else if(e.getSource().equals(hideButton)){
			hideFrame();
		}else if(e.getSource().equals(startItem)){
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
						
		}else if(e.getSource().equals(stopItem)){
			stop();
			if(!running){
				try {
					image = ImageIO.read(file2);
					trayIcon.setImage(image);
					trayIcon.setToolTip("������ (Offline)");
				} catch (Exception e1) {
					setVisible(true);
					SystemTray.getSystemTray().remove(trayIcon);	
				}
			}else
			JOptionPane.showMessageDialog(null, "������ ��� ������� �������!", "������", JOptionPane.INFORMATION_MESSAGE);
						
		}else if(e.getSource().equals(showItem) || e.getSource().equals(trayIcon)){
			setVisible(true);
			SystemTray.getSystemTray().remove(trayIcon);
		}else if(e.getSource().equals(exitItem))
			System.exit(0);
		
	}

	
}
