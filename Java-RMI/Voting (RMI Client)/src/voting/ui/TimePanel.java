package voting.ui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

public class TimePanel extends JPanel{
	
	/*
	 * Компоент для задания даты.
	 * содержит 3 элемента JSpinner для  дней, часов и минут
	 * и метод возвращающий значение даты
	 */
	private JSpinner dateSpinner;
	private JSpinner hourSpinner;
	private JSpinner minuteSpinner;
	private Calendar calendar;
	TimePanel(){
		setLayout(new FlowLayout());
		JLabel dateLabael = new JLabel("День:");
		dateSpinner = new JSpinner();
		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.AM_PM, 0);
		dateSpinner.setFont(new Font("Tahoma", Font.PLAIN, 14));
		dateSpinner.setModel(new SpinnerDateModel(calendar.getTime(), calendar.getTime(), null, Calendar.DAY_OF_YEAR));
		DateFormat df = new SimpleDateFormat("dd MMM yyyy г. ", new Locale("ru"));
		DateFormatter dff = new DateFormatter(df);
		DefaultFormatterFactory factory = new DefaultFormatterFactory(dff);
		JSpinner.DefaultEditor dateEditor = new JSpinner.DefaultEditor(dateSpinner);
		dateEditor.getTextField().setFormatterFactory(factory);
		dateSpinner.setEditor(dateEditor);
		
		dateSpinner.setBorder(new BevelBorder(BevelBorder.LOWERED));
		dateSpinner.getEditor().setLocale(new Locale("ru"));
		
		
		JLabel hourLabael = new JLabel("ч.");
		hourSpinner = new JSpinner();
		hourSpinner.setModel(new SpinnerNumberModel(0, 0, 24, 1));
		hourSpinner.setFont(new Font("Tahoma", Font.PLAIN, 14));
		JSpinner.NumberEditor hourEditor = new JSpinner.NumberEditor(hourSpinner);
		hourEditor.getTextField().setEditable(false);
		hourSpinner.setEditor(hourEditor);
		hourSpinner.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		JLabel minuteLabael = new JLabel("мин.");
		minuteSpinner = new JSpinner();
		minuteSpinner.setModel(new SpinnerNumberModel(0, 0, 60, 1));
		minuteSpinner.setFont(new Font("Tahoma", Font.PLAIN, 14));
		JSpinner.NumberEditor minuteEditor = new JSpinner.NumberEditor(minuteSpinner);
		minuteEditor.getTextField().setEditable(false);
		minuteSpinner.setEditor(minuteEditor);
		minuteSpinner.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		add(dateLabael);
		add(dateSpinner);
		add(hourLabael);
		add(hourSpinner);
		add(minuteLabael);
		add(minuteSpinner);
		
		
	}
	Date getDate(){
		
		Date date = (Date)dateSpinner.getValue();
		Calendar currentCalendar = Calendar.getInstance();
		currentCalendar.setTime(date);
		currentCalendar.add(Calendar.HOUR,(Integer) hourSpinner.getValue());
		currentCalendar.add(Calendar.MINUTE,(Integer) minuteSpinner.getValue());
		date = currentCalendar.getTime();
		return date;
		
	}
	
	
}
