package thw.edu.javaII.port.warehouse.ui;

import thw.edu.javaII.port.warehouse.ui.common.Session;

public class Starter extends Thread {
	
	private Session ses;
	
	public Starter(Session ses) {
		this.ses = ses;
	}
	
	public void run() {
		while(!ses.isLogin()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		LagerUI.run(ses);
	}

}
