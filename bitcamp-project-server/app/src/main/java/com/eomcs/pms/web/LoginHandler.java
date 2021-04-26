package com.eomcs.pms.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.service.MemberService;

@WebServlet("/login")
public class LoginHandler extends GenericServlet {


  @Override
  public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {


    MemberService memberService = (MemberService) request.getServletContext().getAttribute("memberService");
    response.setContentType("text/plain;charset=UTF-8");
    PrintWriter out = response.getWriter();

    out.println("[로그인]");


    String email = request.getParameter("email");
    String password = request.getParameter("password");

    try {
      Member member = memberService.get(email, password);
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      if (member == null) {
        out.println("사용자 정보가 맞지 않습니다.");
        // 로그인 실패한다면 세션 객체의 모든 내용을 삭제한다.
        httpRequest.getSession().invalidate(); // 
        return;
      }

      // 로그인 성공한다면, 로그인 사용자 정보를 세션 객체에 보관한다.
      httpRequest.getSession().setAttribute("loginUser", member);

      out.printf("%s 님 환영합니다.\n", member.getName());
    }
    catch (Exception e){
      StringWriter strWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(strWriter);
      e.printStackTrace();

      // StringWriter에 들어 있는 출력 내용을 꺼내 클라이언트로 보낸다.
      out.println(strWriter.toString());
    }
  }

}






