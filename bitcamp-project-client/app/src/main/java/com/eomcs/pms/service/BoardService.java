package com.eomcs.pms.service;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.eomcs.pms.dao.BoardDao;
import com.eomcs.pms.domain.Board;


public class BoardService {

  SqlSession sqlSession;
  BoardDao boardDao;

  public BoardService(SqlSession sqlSession, BoardDao boardDao) {
    this.sqlSession = sqlSession;
    this.boardDao = boardDao;
  }

  public int add(Board board) throws Exception {
    int count = boardDao.insert(board);
    sqlSession.commit();
    return count;
  }

  public List<Board> list() throws Exception {
    return boardDao.findByKeyword(null);
  }

  public Board detail(int no) throws Exception {
    Board board = boardDao.findByNo(no);

    if(board != null) {
      boardDao.updateViewCount(no);
      sqlSession.commit();
    }
    return board;
  }

  public int update(Board board) throws Exception {
    int count = boardDao.update(board);
    sqlSession.commit();
    return count;
  }

  public int delete(int no) throws Exception {
    int count = boardDao.delete(no);
    sqlSession.commit();
    return count;
  }

  public List<Board> search(String keyword) throws Exception {
    return boardDao.findByKeyword(keyword);
  }
}
