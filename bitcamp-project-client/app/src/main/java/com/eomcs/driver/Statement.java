package com.eomcs.driver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Statement implements AutoCloseable {
	Socket socket;
	DataInputStream in;
	DataOutputStream out;

	public Statement(String host, int port) throws Exception {
		socket = new Socket(host, port);
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
	}

	public void executeUpdate(String command, String... args) throws Exception {
		request(command, args);

		String status = in.readUTF();
		in.readInt();
		if(status.equals("error")) {
			throw new Exception(in.readUTF());
		}
	}

	public Iterator<String> executeQuery(String command, String... args) throws Exception {
		request(command, args);

		String status = in.readUTF();
		int length = in.readInt();

		if(status.equals("error")) {
			throw new Exception(in.readUTF());
		}

		ArrayList<String> results = new ArrayList<>();

		for(int i = 0; i < length; i++) {
			results.add(in.readUTF());
		}

		return results.iterator();
	}

	private void request(String command, String... args) throws Exception {
		out.writeUTF(command);
		out.writeInt(args.length);
		for(String data : args) {
			out.writeUTF(data);
		}
		out.flush();
	}

	@Override
	public void close() {
		try {in.close();} catch (Exception e) {}
		try {out.close();} catch (Exception e) {}
		try {socket.close();} catch (Exception e) {}
	}
}
