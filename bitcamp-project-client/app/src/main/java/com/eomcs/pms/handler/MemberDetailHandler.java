package com.eomcs.pms.handler;

import com.eomcs.pms.dao.MemberDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.util.Prompt;

public class MemberDetailHandler implements Command {

  MemberDao memberDao;

  public MemberDetailHandler(MemberDao memberDao) {
    this.memberDao = memberDao;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[회원 상세보기]");

    int no = Prompt.inputInt("번호? ");

    Member member = memberDao.findByNo(no);

    if(member == null) {
      System.out.println("해당 번호의 회원이 존재하지 않습니다.");
      return;
    }

    System.out.printf("번호 : %d\n", member.getNo());
    System.out.printf("이름: %s\n", member.getName());
    System.out.printf("이메일: %s\n", member.getEmail());
    System.out.printf("사진: %s\n", member.getPhoto());
    System.out.printf("전화: %s\n", member.getTel());
    System.out.printf("가입일: %s\n", member.getRegisteredDate());
  }
}






