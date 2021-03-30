package com.eomcs.pms.handler;

import java.util.List;
import com.eomcs.pms.dao.BoardDao;
import com.eomcs.pms.domain.Board;

public class BoardListHandler implements Command {

  BoardDao boardDao;

  public BoardListHandler(BoardDao boardDao) {
    this.boardDao = boardDao;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[게시글 목록]");

    // 서버에 게시글 목록을 달라고 요청한다.
    List<Board> boards = boardDao.findAll();

    for(Board b : boards) {
      System.out.printf("%d, %s, %s, %s, %d\n",
          b.getNo(),
          b.getTitle(),
          b.getWriter().getName(),
          b.getRegisteredDate(),
          b.getViewCount()
          );
    }
  }
}






