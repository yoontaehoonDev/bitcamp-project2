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

      String name = Prompt.inputString("만든이?(취소: 빈 문자열) ");
      out.writeUTF("member/check");
      out.write(1);
      out.writeUTF(name);
      out.flush();

      String status = in.readUTF();
      in.readInt();

      if(status.equals("error")) {
        System.out.println(in.readUTF());
        return;
      }
      // findByName을 이용해서 입력한 문자열가 멤버인지 확인
      p.setOwner(MemberValidatorHandler.inputMember("만든이?(취소: 빈 문자열) "));
      if (p.getOwner() == null) {
        System.out.println("프로젝트 입력을 취소합니다.");
        return;
      }
      p.setMembers(MemberValidatorHandler.inputMembers("팀원?(완료: 빈 문자열) "));

      out.writeUTF("member/insert");
      out.writeInt(1);
      out.writeUTF(String.format("%s,%s,%s,%s,%s,%s,%s", 
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








