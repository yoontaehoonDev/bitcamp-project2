package com.eomcs.pms.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.eomcs.pms.domain.Project;
import com.eomcs.util.Prompt;

public class ProjectAddHandler implements Command {

	@Override
	public void service(DataInputStream in, DataOutputStream out) {
		try {
			System.out.println("[프로젝트 등록]");

			Project p = new Project();

			p.setNo(Prompt.inputInt("번호? "));
			p.setTitle(Prompt.inputString("프로젝트명? "));
			p.setContent(Prompt.inputString("내용? "));
			p.setStartDate(Prompt.inputDate("시작일? "));
			p.setEndDate(Prompt.inputDate("종료일? "));

			p.setOwner(Prompt.inputString("만든이?(취소 : 빈 문자열) "));
			//			if (p.getOwner().length() == 0) {
			//				System.out.println("프로젝트 입력을 취소합니다.");
			//				return;
			//			}
			//			out.writeUTF("member/inputMember");
			//			out.writeInt(1);
			//			out.writeUTF(p.getOwner());
			//			out.flush();
			//
			//			String status = in.readUTF();
			//			in.readInt();
			//
			//			if(status.equals("error")) {
			//				System.out.println(in.readUTF());
			//				return;
			//			}
			p.setMembers(Prompt.inputString(("팀원?(완료: 빈 문자열) ")));
			//			out.writeUTF("member/inputMembers");
			//			out.writeInt(1);
			//			out.writeUTF(p.getMembers());
			//			out.flush();
			//
			//			status = in.readUTF();
			//			in.readInt();
			//
			//			if(status.equals("error")) {
			//				System.out.println(in.readUTF());
			//				return;
			//			}

			out.writeUTF("project/insert");
			out.writeInt(1);
			out.writeUTF(String.format("%s,%s,%s,%s,%s,%s", 
					p.getTitle(),
					p.getContent(),
					p.getStartDate().toString(),
					p.getEndDate().toString(),
					p.getOwner(),
					p.getMembers().replace(",", "|")));

			System.out.println("프로젝트를 등록했습니다.");
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}








