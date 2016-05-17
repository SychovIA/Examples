package voting;

import voting.ui.EnterFrame;

public class Voting {

	public static void main(String[] args) {
		
		//Приложение начинается с открытия первого окна настройки подключения к СУБД.
		
		EnterFrame frame = new EnterFrame();
		frame.setVisible(true);

	}

}
