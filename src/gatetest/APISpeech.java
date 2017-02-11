package gatetest;

public class APISpeech {

	public String getUserSpeech(String text) {
		return "[You] : " + text;
	}
	
	public String getBotSpeech(String botAsnwer) {
		return "[SAM] : " + botAsnwer;
	}

}
