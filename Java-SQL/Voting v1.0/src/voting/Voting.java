package voting;

import voting.ui.ConfigFrame;

public class Voting {

	public static void main(String[] args) {
		
		//Приложение начинается с открытия первого окна настройки подключения к СУБД.
		ConfigFrame configFrame = new ConfigFrame();
		configFrame.setVisible(true);

	}

}