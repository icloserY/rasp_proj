package local;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

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
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));
			LocalServer local = LocalServer.getInstance();
			boolean serverStatus = false;
			boolean flag = true;
			
			while(flag) {
				System.out.println("명령을 입력하세요.");
				System.out.println("1.start(sta), 2.stop(sto)");
				String command = "NOTHING";
				try {
					//command = reader.readLine();
					command = "sta";
					if(!command.equals("NOTHING")) {
						if(command.equalsIgnoreCase("sta")  && !serverStatus) {
							if(controller != null) {
								local.startLocal(controller);
								serverStatus = true;
								flag = false;
							}
						} else if(command.equalsIgnoreCase("sto") && serverStatus) {
							local.stopLocal();
							serverStatus = false;
						} else if(command.equalsIgnoreCase("sta") && serverStatus) {
							System.out.println("서버가 이미 동작 중입니다.");
						} else {
							System.out.println("잘못된 명령어 입니다.");
						}
					}
					Thread.sleep(1000);
				} catch (Exception e) {
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
