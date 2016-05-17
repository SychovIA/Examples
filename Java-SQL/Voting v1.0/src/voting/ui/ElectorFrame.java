package voting.ui;

import java.awt.BorderLayout;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import voting.database.DataBase;
import voting.database.UserData;

public class ElectorFrame extends JFrame implements Runnable{
	
	/*
	 * ќкно пользовател€.
	 * —одержит 2 вкладки:
	 * "√олосование",
	 * "—менить логин и пароль".
	 * »нформаци€ выводима€ в вкладке "√олосование" измен€етс€
	 * в зависимости от состо€ни€ голосовани€.
	 */
	private JPanel contentPane;
	private JPanel panel1;
	private JPanel panel2;
	private JTabbedPane tabbedPane;
	private Thread thread;
	private boolean running;
	private Date begingDate;
	private Date endDate;
	
	ElectorFrame(){
		// добавол€ем компоненты и настраиваем окно
		super("Voting v1.0:" + " " + UserData.getUser());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setSize(600, 500);
		//панель с часами и текущей датой
		JMenuBar menuBar = new JMenuBar();
		menuBar.setOpaque(false);
		menuBar.setBorder(null);
		menuBar.add(Box.createHorizontalGlue());
		Clock clock = new Clock();
		menuBar.add(clock);
		setJMenuBar(menuBar);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(12, 12, 12, 12));
		contentPane.setLayout(new BorderLayout(12, 12));
		setContentPane(contentPane);
		
		
		//панель с вкладками
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		panel1 = new JPanel();
		tabbedPane.addTab("√олосование", panel1);
		
		
		panel2 = new LoginPanel();
		tabbedPane.addTab("—менить логин и пароль", panel2);
		
		
		Box box = Box.createHorizontalBox();
		JButton exitButton = new JButton("¬ыход");  // кнопка дл€ возврата в окно входа
		exitButton.addActionListener(e -> {
			updateStop();  //перед закрытием окна убивыем дополнительный поток
			UserData.setEmpty(); //обнул€ем информацию о пользователе
			dispose();
			new EnterFrame().setVisible(true);
			
		});
		box.add(Box.createHorizontalGlue());
		box.add(exitButton);
		box.add(Box.createHorizontalStrut(12));
		contentPane.add(box, BorderLayout.SOUTH);
		
		
		
		setResizable(false);
		
		updateStart();  //запуск дополнительного потока
		
	}
	
	/*создание дополнительного потока дл€ проверки состо€ни€ голосовани€ и
	 *измени€ содержани€ вкладки "√олосование"
	 */

	@Override
	public void run() {
		while(running){
			begingDate = DataBase.getBegingDate();
			endDate = DataBase.getEndDate();
			/*
			 * проверка пол€ даты голосовани€ и в зависитости от полученных данных
			 * изменение панели выводимой на экран
			 * что бы не перересовавыть панель каждый раз,
			 * происходит проверка на текущую панель
			 */
			if (begingDate==null||endDate==null){
				if(panel1.getClass() != NoVotePanel.class){
					panel1 = new NoVotePanel();
					tabbedPane.setComponentAt(0, panel1);
					tabbedPane.setSelectedIndex(0);
				}
			}else{
				if (begingDate.after(new Date()) & panel1.getClass()!= BeforVotingPanel.class){
					panel1 = new BeforVotingPanel();
					tabbedPane.setComponentAt(0, panel1);
					tabbedPane.setSelectedIndex(0);
				}
				if (begingDate.before(new Date()) & endDate.after(new Date()) & panel1.getClass() != VotePanel.class){
					panel1 = new VotePanel();
					tabbedPane.setComponentAt(0, panel1);
					tabbedPane.setSelectedIndex(0);
				}
				
				if(endDate.before(new Date()) & panel1.getClass() != RezultsPanel.class){
					panel1 = new RezultsPanel();
					tabbedPane.setComponentAt(0, panel1);
					tabbedPane.setSelectedIndex(0);
				}
			}
			
		try {
			Thread.sleep(1000); // периодичность потока 1 секунда
		} catch (InterruptedException e) {}
		}
	}
	
	private void updateStart(){ //запуск потока
		if(!running){
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}
	
	private void updateStop(){  //остановка потока
			running = false;
	}
	
}