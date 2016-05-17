package voting.ui;

import java.text.DateFormat;
import java.util.Date;

import javax.swing.JLabel;

public class Clock extends JLabel implements Runnable {
	// "часы"
	// выводят каждую секунду текущие время и дату
	
	private DateFormat simpleDate = DateFormat.getDateInstance(DateFormat.SHORT);
	private DateFormat simpleTime = DateFormat.getTimeInstance(DateFormat.SHORT);
	private Date date;
	Thread clock = new Thread(this);
	Clock(){
		clock.start();
		
	}
	@Override
	public void run() {
		
		while(true){
			date = new Date();
			super.setText("Дата: " + simpleDate.format(date) + "   Время: " + simpleTime.format(date));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}

	}

}