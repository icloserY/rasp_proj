package local;

public class WatchEnvironmentService implements Runnable {
	private Environment env;
	
	public WatchEnvironmentService(Environment env) {
		this.env = env;
	}
	
	@Override
	public void run() {
		//온도, 습도 감시
		System.out.println("온도, 습도 감지 시작");
	}
	
}
