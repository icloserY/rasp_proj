package local;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartMain extends Application{
	public static final int PITCHERN0303 = 1;
	static Controller controller = null;
	
	public static void main(String[] args) {
		LocalServer.executorService.submit(() -> {
			LocalServer local = LocalServer.getInstance();
			boolean flag = true;
			
			while(flag) {
				try {
					if(controller != null) {
						local.startLocal(controller);
						flag = false;
					}
					Thread.sleep(1000);
				}
				 catch (Exception e) {
					System.out.println("오류 발생 다시 시작해 주세요.");
					e.printStackTrace();
					flag = false;
				}
			}
		});
		StartMain.launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
		Parent root = (Parent)loader.load();
		System.out.println(getClass().getResource("layout.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		
		primaryStage.show();
		
		controller = (Controller)loader.getController();
	}
}
