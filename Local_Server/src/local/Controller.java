package local;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class Controller implements Initializable {
	@FXML private Label notice_db;
	@FXML private Label notice_env;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public Label getNotice_db() {
		return this.notice_db;
	}
	
	public Label getNotice_env() {
		return this.notice_env;
	}
}
