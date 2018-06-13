package local;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class Controller implements Initializable {
	@FXML private Label notice;
	@FXML private Label env;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public Label getNotice() {
		return this.notice;
	}
	
	public Label getEnv() {
		return this.env;
	}
}
