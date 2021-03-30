package com.eomcs.pms.handler;

import java.util.List;
import com.eomcs.pms.dao.MemberDao;
import com.eomcs.pms.domain.Member;

public class MemberListHandler implements Command {

  MemberDao memberDao;

  public MemberListHandler(MemberDao memberDao) {
    this.memberDao = memberDao;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[회원 목록]");

    List<Member> list = memberDao.findAll();

    for(Member m : list) {
      System.out.printf("번호 : %d 이름 : %s 이메일 : %s 사진 : %s 번호 : %s 가입일 : %s\n",
          m.getNo(),
          m.getName(),
          m.getEmail(),
          m.getPhoto(),
          m.getTel(),
          m.getRegisteredDate()
          );
    }
  }
}






