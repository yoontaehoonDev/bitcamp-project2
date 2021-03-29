package com.eomcs.pms.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.eomcs.pms.domain.Board;
import com.eomcs.pms.domain.Member;
import com.eomcs.util.Prompt;

public class BoardAddHandler implements Command {

  @Override
  public void service() throws Exception {

    System.out.println("[게시글 등록]");

    Board b = new Board();

    b.setTitle(Prompt.inputString("제목? "));
    b.setContent(Prompt.inputString("내용? "));


    try (Connection con = DriverManager.getConnection( //
        "jdbc:mariadb://localhost:3306/studydb?user=study&password=1111");
        PreparedStatement stmt =
            con.prepareStatement("insert into pms_board(title, content, writer) values(?,?,?)");
        PreparedStatement stmt2 = con.prepareStatement(
            "select no from pms_member where no = ?"
            );
        ) {
      int num = Prompt.inputInt("회원 번호?(취소 : 빈 문자열) ");

      stmt2.setInt(1, num);

      try(ResultSet rs = stmt2.executeQuery()) {
        if(!rs.next()) {
          System.out.println("존재하지 않는 회원입니다.");
          return;
        }
      }

      Member m = new Member();
      m.setNo(num);

      stmt.setString(1, b.getTitle());
      stmt.setString(2, b.getContent());
      stmt.setInt(3, m.getNo());

      stmt.executeUpdate();
    }



    System.out.println("게시글을 등록하였습니다.");

  }
}






