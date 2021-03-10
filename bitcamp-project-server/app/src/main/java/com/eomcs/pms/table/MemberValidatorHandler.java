package com.eomcs.pms.table;

import java.io.File;
import java.util.List;

import com.eomcs.pms.domain.Member;
import com.eomcs.util.JsonFileHandler;
import com.eomcs.util.Prompt;

public class MemberValidatorHandler {

	List<Member> list;
	File jsonFile = File("members.json");

	public MemberValidatorHandler() {
		list = JsonFileHandler.loadObjects((File)"members.json", Member.class);
	}

	public static String inputMember(String promptTitle) {
		while (true) {
			String name = Prompt.inputString(promptTitle);
			if (name.length() == 0) {
				return null;
			} 
			if (findByName(name) != null) {
				return name;
			}
			System.out.println("등록된 회원이 아닙니다.");
		}
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






