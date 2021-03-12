package com.eomcs.pms.handler;

import java.util.Iterator;
import com.eomcs.driver.Statement;

public class BoardListHandler implements Command {

  Statement stmt;

  public BoardListHandler(Statement stmt) {
    this.stmt = stmt;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[게시글 목록]");

    // 서버에 게시글 목록을 달라고 요청한다.
    Iterator<String> results = stmt.executeQuery("board/selectall");

    while(results.hasNext()) {
      String[] fields = results.next().split(",");
      System.out.printf("%s, %s, %s, %s, %s\n", 
          fields[0],
          fields[1],
          fields[2],
          fields[3],
          fields[4]);
    }
  }
}






