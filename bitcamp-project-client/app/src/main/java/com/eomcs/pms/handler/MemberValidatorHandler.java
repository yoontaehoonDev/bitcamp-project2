package com.eomcs.pms.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MemberValidatorHandler implements Command {

	@Override
	public void service(DataInputStream in, DataOutputStream out) {

		out.writeUTF("member/check");
		out.writeInt(1);
		out.writeUTF(promptTitle);
		out.flush();

		String status = in.readUTF();
		in.readInt();
		String data = in.readUTF();
		System.out.printf("status : %s\n", status);
		System.out.printf("in.readInt : %d\n", in.readInt());

		if(status.equals("error")) {
			System.out.println(data);
			return null;
		}
		return promptTitle;
		//		while (true) {
		//			String name = Prompt.inputString(promptTitle);
		//			if (name.length() == 0) {
		//				return null;
		//			} 
		//			if (findByName(name) != null) {
		//				return name;
		//			}
		//			System.out.println("등록된 회원이 아닙니다.");
		//		}
	}

	public static String inputMembers(String promptTitle) {
		String members = "";
		while (true) {
			String name = inputMember(promptTitle);
			if (name == null) {
				return members;
			} else {
				if (!members.isEmpty()) {
					members += ",";
				}
				members += name;
			}
		}
	}
}






