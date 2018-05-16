package local;

public class StartMain {
	public static void main(String[] args) {
		LocalServer local = LocalServer.getInstance();
		System.out.println(local);
		local.startLocal();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		local.stopLocal();
	}
}
