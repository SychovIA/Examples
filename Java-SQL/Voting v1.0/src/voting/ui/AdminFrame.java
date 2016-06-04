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

public class AdminFrame extends JFrame implements Runnable{
	/*
	 * —оздание окна дл€ администратора.
	 * ќкно содержит 3 вкладки: создание голосовани€, 
	 * вывод результатов голосовани€, смена парол€.
	 * »нформаци€ выводима€ в результатах голосовани€ измен€етс€
	 * в зависимости от состо€ни€ голосовани€.
	 */
	private JPanel contentPane;
	private JButton exitButton;
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	private JTabbedPane tabbedPane;
	private Thread thread;
	private boolean running;
	private Date begingDate;
	private Date endDate;
	
	AdminFrame(){
		// добавол€ем компоненты и настраиваем окно
		super("Voting v1.0: јдминистратор");
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
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(12, 12, 12, 12));
		contentPane.setLayout(new BorderLayout(12, 12));
		setContentPane(contentPane);
		
		//панель с вкладками
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		panel1 = new CreatePanel();
		tabbedPane.addTab("—оздать голосование", panel1);
		
		panel2 = new JPanel();
		tabbedPane.addTab("–езультаты голосовани€", panel2);
		
		panel3 = new LoginPanel();
		tabbedPane.addTab("—менить логин и пароль", panel3);
		
		Box box = Box.createHorizontalBox();
		exitButton = new JButton("¬ыход");  // кнопка дл€ возврата в окно входа
		exitButton.addActionListener(e -> {
			updateStop();  //перед закрытием окна убивыем дополнительный поток
			UserData.setEmpty();//обнул€ем информацию о пользователе
			dispose();
			new EnterFrame().setVisible(true);
		});
		box.add(Box.createHorizontalGlue());
		box.add(exitButton);
		box.add(Box.createHorizontalStrut(12));
		contentPane.add(box, BorderLayout.SOUTH);
		
		pack();
		setResizable(false);
		
		
		updateStart(); //запуск дополнительного потока
		
		
		
		
	}
	
	
	/*создание дополнительного потока дл€ проверки состо€ни€ голосовани€ и
	 *измени€ содержани€ вкладки "–езультаты голосовани€"
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
				if(panel2.getClass() != NoVotePanel.class){
					panel2 = new NoVotePanel();
					tabbedPane.setComponentAt(1, panel2);
				}
			}else{
				
				if (begingDate.after(new Date()) & panel2.getClass()!= BeforVotingPanel.class){
					panel2 = new BeforVotingPanel();
					tabbedPane.setComponentAt(1, panel2);
				}
				if (begingDate.before(new Date()) & endDate.after(new Date()) & panel2.getClass() != EndVotingPanel.class){
					panel2 = new EndVotingPanel();
					tabbedPane.setComponentAt(1, panel2);
				}
				
				if(endDate.before(new Date()) & panel2.getClass() != RezultsPanel.class){
					panel2 = new RezultsPanel();
					tabbedPane.setComponentAt(1, panel2);
				}
			}
			
		try {
			Thread.sleep(1000); // периодичность потока 1 секунда
		} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	
	private void updateStart(){ // запуск потока
		if(!running){
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}
	
	
	private void updateStop(){ //остановка потока
			running = false;
	}

}
