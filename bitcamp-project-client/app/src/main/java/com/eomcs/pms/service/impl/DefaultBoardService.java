package com.eomcs.pms.service.impl;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.eomcs.pms.dao.BoardDao;
import com.eomcs.pms.domain.Board;
import com.eomcs.pms.service.BoardService;


public class DefaultBoardService implements BoardService {

  SqlSession sqlSession;
  BoardDao boardDao;

  public DefaultBoardService(SqlSession sqlSession, BoardDao boardDao) {
    this.sqlSession = sqlSession;
    this.boardDao = boardDao;
  }


  @Override
  public int add(Board board) throws Exception {
    int count = boardDao.insert(board);
    sqlSession.commit();
    return count;
  }

  @Override
  public List<Board> list() throws Exception {
    return boardDao.findByKeyword(null);
  }

  @Override
  public Board get(int no) throws Exception {
    Board board = boardDao.findByNo(no);

    if(board != null) {
      boardDao.updateViewCount(no);
      sqlSession.commit();
    }
    return board;
  }

  @Override
  public int update(Board board) throws Exception {
    int count = boardDao.update(board);
    sqlSession.commit();
    return count;
  }

  @Override
  public int delete(int no) throws Exception {
    int count = boardDao.delete(no);
    sqlSession.commit();
    return count;
  }

  @Override
  public List<Board> search(String keyword) throws Exception {
    return boardDao.findByKeyword(keyword);
  }
}
