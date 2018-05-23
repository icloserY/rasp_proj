package local;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class StartMain {
	public static final int PITCHERN0303 = 1;
	
	public static void main(String[] args) {
		LocalServer local = LocalServer.getInstance();
		//LocalServerVer2 local = null;
		boolean serverStatus = false;
		boolean flag = true;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));
		
		while(flag) {
			System.out.println("명령을 입력하세요.");
			System.out.println("1.start(sta), 2.stop(sto)");
			String command = "NOTHING";
			try {
				command = reader.readLine();
				if(!command.equals("NOTHING")) {
					if(command.equalsIgnoreCase("sta")  && !serverStatus) {
						local.startLocal();
						serverStatus = true;
					} else if(command.equalsIgnoreCase("sto") && serverStatus) {
						local.stopLocal();
						serverStatus = false;
					} else if(command.equalsIgnoreCase("sta") && serverStatus) {
						System.out.println("서버가 이미 동작 중입니다.");
					} else {
						System.out.println("잘못된 명령어 입니다.");
					}
				}
				Thread.sleep(100);
			} catch (Exception e) {
				System.out.println("오류 발생 다시 시작해 주세요.");
				e.printStackTrace();
				flag = false;
			}
		}
	}
}
