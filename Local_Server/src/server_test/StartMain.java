package server_test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class StartMain {
	public static void main(String[] args) throws IOException {
	//	Client_test client = new StartMain().new Client_test();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			System.out.println("명령어를 입력 하세요. : 1.st(start) 2.sto(stop) 3.sd(send)");
			String command = reader.readLine();
			if(command.equalsIgnoreCase("sto")) {
				System.out.println("종료합니다.");
	//			client.stopClient();
				break;
			}
			if(command.startsWith("st")) {
	//			client.startClient();
				continue;
			}
			if(command.startsWith("sd")) {
				String message = null;
				try {
					message = command.split(" \"", 0)[1];
				} catch(Exception e) { 
					printHelp(); 
					continue;
				}
	//			client.send(message);
			}
		}
	}
	
	private static void printHelp() {
		System.out.println();
		System.out.println("잘못된 명령입니다. 아래 명령어 사용법을 확인하세요.");
		System.out.println("명령어 사용법:");
		System.out.println("st");
		System.out.println("sto");
		System.out.println("sd" + " \" " + "message" + " \" ");
		System.out.println();
	}
	
}
