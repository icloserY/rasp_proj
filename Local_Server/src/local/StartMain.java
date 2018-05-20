package local;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class StartMain {
	public static final int PITCHERN0303 = 1;
	
	public static void main(String[] args) {
		boolean flag = true;
		LocalServer local = LocalServer.getInstance();
		System.out.println(local);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));
		
		while(flag) {
			System.out.println("명령을 입력하세요.");
			System.out.println("1.start(sta), 2.stop(sto)");
			String command = "NOTHING";
			try {
				command = reader.readLine();
				if(!command.equals("NOTHING") && flag) {
					if(command.equalsIgnoreCase("sta")) {
						local.startLocal();
						flag = true;
					}
					else if(command.equalsIgnoreCase("sto")) {
						local.stopLocal();
						flag = false;
					}
					else {
						System.out.println("잘못된 명령어 입니다.");
					}
				}
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("오류 발생 다시 시작해 주세요.");
				flag = false;
			}
		}
	}
}
