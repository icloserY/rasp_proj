package watchTest;

import com.pi4j.component.button.Button;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.Parent;

public class FxTest extends Application{
	
	@Override
	public void init() throws Exception {
		// TODO Auto-generated method stub
		super.init();
		
		System.out.println(Thread.currentThread().getName()+": init() 호출");
	}
	
	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		VBox root = new VBox();
		root.setPrefWidth(600);
		root.setPrefHeight(350);
		root.setAlignment(Pos.CENTER);
		root.setSpacing(20);
		
		Label label = new Label();
		label.setText("도서관 환경 관리 시스템");
		label.setFont(new Font(50));
		
		javafx.scene.control.Button button = new javafx.scene.control.Button();
		button.setText("확인");
		button.setOnAction(event ->Platform.exit());
		
		root.getChildren().add(label);
		root.getChildren().add(button);
		
		Scene scene = new Scene(root);
		
		arg0.setTitle("appMain");
		arg0.setScene(scene);
		arg0.show();
		
		System.out.println(Thread.currentThread().getName()+": start() 호출");
		
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		super.stop();
		
		System.out.println(Thread.currentThread().getName()+": stop() 호출");
	}



	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getName()+": main() 호출");
		launch(args);
	}
}
