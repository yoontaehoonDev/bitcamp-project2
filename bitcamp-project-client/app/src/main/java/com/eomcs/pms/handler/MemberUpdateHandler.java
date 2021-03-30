package com.eomcs.pms.handler;

import com.eomcs.pms.dao.MemberDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.util.Prompt;

public class MemberUpdateHandler implements Command {

  MemberDao memberDao;

  public MemberUpdateHandler(MemberDao memberDao) {
    this.memberDao = memberDao;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[회원 변경]");

    int no = Prompt.inputInt("번호? ");

    Member m = memberDao.findByNo(no);

    if(m == null) {
      System.out.println("해당하는 번호의 회원이 없습니다.");
      return;
    }

    String input = Prompt.inputString("정말 변경하시겠습니까?(y/N) ");
    if (!input.equalsIgnoreCase("Y")) {
      System.out.println("회원 정보 변경을 취소하였습니다.");
      return;
    }

    m.setName(Prompt.inputString(String.format("이름(%s)? ", m.getName())));
    m.setEmail(Prompt.inputString(String.format("이메일(%s)? ", m.getEmail())));
    m.setPassword(Prompt.inputString("암호? "));
    m.setPhoto(Prompt.inputString(String.format("사진(%s)? ", m.getPhoto())));
    m.setTel(Prompt.inputString(String.format("전화번호(%s)? ", m.getTel())));

    memberDao.update(m);

    System.out.println("회원 정보를 변경하였습니다.");
  }
}



