package server_test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StartMain {
	public static void main(String[] args) throws IOException {
		System.out.println("START");
		Server_test server = new Server_test();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			System.out.println("명령어를 입력 하세요. : 1.st(start) 2.sto(stop)");
			String command = reader.readLine();
			if(command.equalsIgnoreCase("sto")) {
				System.out.println("종료합니다.");
				server.stopServer();
				break;
			}
			if(command.startsWith("st")) {
				server.startServer();
				continue;
			}
		}
	}
}
