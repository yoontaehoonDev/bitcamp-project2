package com.eomcs.pms.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberListHandler implements Command {

  @Override
  public void service() throws Exception {
    System.out.println("[회원 목록]");

    try(Connection con = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/studydb?user=study&password=1111"
        );
        PreparedStatement stmt = con.prepareStatement(
            "select no, name, email, photo, tel, cdt from pms_member order by no"
            );
        ResultSet rs = stmt.executeQuery()
        ) {

      while(rs.next()) {
        System.out.printf("번호 : %d 이름 : %s 이메일 : %s 사진 : %s 번호 : %s 가입일 : %s\n",
            rs.getInt("no"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("photo"),
            rs.getString("tel"),
            rs.getDate("cdt")
            );
      }
    }
  }
}






