package de.hetzge.sgame.network;

public class Test {

	public static void main(String[] args) throws InterruptedException {

		NetworkConfig.INSTANCE.clientLifecycle = new IF_ClientLifecycle() {
			@Override
			public void onClientRegisterSuccess(Object registerAnswer) {
				System.out.println(registerAnswer);
			}
		};

		NetworkConfig.INSTANCE.serverLifecycle = new IF_ServerLifecycle() {
			@Override
			public void onClientRegister(Object message) {
				System.out.println(message);
			}

			@Override
			public void onClientConnected() {
			}
		};

		new Thread(() -> {
			Server server = new Server();
			server.connect();
		}).start();

		Thread.sleep(100);

		new Thread(() -> {
			Client client = new Client();
			client.connect();
			client.sendMessage("Hello");
		}).start();

	}
}
