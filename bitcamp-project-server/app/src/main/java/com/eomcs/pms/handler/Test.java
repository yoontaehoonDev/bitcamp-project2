package com.eomcs.pms.handler;

import java.io.PrintWriter;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.service.MemberService;
import com.eomcs.stereotype.Component;
import com.eomcs.util.CommandRequest;
import com.eomcs.util.CommandResponse;
import com.eomcs.util.Prompt;

@Component("/test")
public class Test implements Command {

  MemberService memberService;

  public Test(MemberService memberService) {
    this.memberService = memberService;
  }

  @Override
  public void service(CommandRequest request, CommandResponse response) throws Exception {

    PrintWriter out = response.getWriter();
    Prompt prompt = request.getPrompt();
    out.println("[유저 정보]");

    int number = prompt.inputInt("사용자 번호 : ");
    Member m = memberService.get(number);
    if(m == null) {
      out.println("존재하지 않는 유저 입니다.");
      return;
    }

    m = (Member) request.getSession().getAttribute("loginUser");
    if(m.getNo() != number) {
      out.println("로그인 하지 않았습니다.");
      return;
    }

    out.printf("사용자 번호 : %d\n", m.getNo());
    out.printf("이름 : %s\n", m.getName());
    out.printf("이메일 : %s\n", m.getEmail());
    out.printf("사진 : %s\n", m.getPhoto());

  }
}
