package com.eomcs.pms.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.eomcs.util.Prompt;

public class MemberValidator  {


  public String inputMember(String promptTitle) throws SQLException {

    String name = Prompt.inputString(promptTitle);

    if(name.length() == 0) {
      return null;
    }

    try(Connection con = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/studydb?user=study&password=1111"
        );
        PreparedStatement stmt = con.prepareStatement(
            "select name from pms_member");
        ResultSet rs = stmt.executeQuery()
        ) {

      while(rs.next()) {
        if(name.equals(rs.getString("name"))) {
          return name;
        }
      }

    }
    System.out.println("등록된 회원이 아닙니다.");
    return null;
  }

  public String inputMembers(String promptTitle) throws SQLException {

    String members = "";
    int flag = 0;

    try(Connection con = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/studydb?user=study&password=1111"
        );
        PreparedStatement stmt = con.prepareStatement(
            "select name from pms_member"
            );
        ) {
      while(true) {
        String name = Prompt.inputString("멤버 : ");
        try(ResultSet rs = stmt.executeQuery()) {
          while(rs.next()) {
            if(name.equals(rs.getString("name"))) {
              flag = 1;
              if (!members.isEmpty()) {
                members += "/";
                System.out.printf("값 : %s\n", name);
              }
              members += name;
              System.out.printf("값 : %s\n", members);
            }
          }
        }

        if(name.length() == 0) {
          return members;
        }
        if(flag == 0) {
          System.out.println("등록된 회원이 아닙니다.");  
        }
        flag = 0;
      }
    }
  }

}





