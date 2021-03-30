package com.eomcs.pms.handler;

import java.util.List;
import com.eomcs.pms.dao.BoardDao;
import com.eomcs.pms.domain.Board;
import com.eomcs.util.Prompt;

public class BoardSearchHandler implements Command {

  BoardDao boardDao;

  public BoardSearchHandler(BoardDao boardDao) {
    this.boardDao = boardDao;
  }

  @Override
  public void service() throws Exception {
    String keyword = Prompt.inputString("검색어? ");

    if(keyword.length() == 0) {
      System.out.println("검색어를 입력하세요.");
      return;
    }
    List<Board> list = boardDao.findByKeyword(keyword);

    for(Board b : list) {
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






