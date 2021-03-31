package com.eomcs.pms.dao.mariadb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.eomcs.pms.dao.BoardDao;
import com.eomcs.pms.domain.Board;
import com.eomcs.pms.domain.Member;

public class BoardDaoImpl implements BoardDao {

  Connection con;

  public BoardDaoImpl(Connection con) throws Exception {
    this.con = con;
  }

  @Override
  public int insert(Board board) throws Exception {

    try (PreparedStatement stmt =
        con.prepareStatement("insert into pms_board(title, content, writer) values(?,?,?)");) {

      stmt.setString(1, board.getTitle());
      stmt.setString(2, board.getContent());
      stmt.setInt(3, board.getWriter().getNo());

      return stmt.executeUpdate();
    }
  }

  @Override
  public List<Board> findAll() throws Exception {
    ArrayList<Board> list = new ArrayList<>();
    try (PreparedStatement stmt = con.prepareStatement( //
        "select b.no, b.title, b.writer, b.cdt, b.vw_cnt, m.no writer_no, m.name writer_name"
        + " from pms_board b"
        + " inner join pms_member m on m.no=b.writer"
        + " order by b.no desc");
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        Board board = new Board();
        board.setNo(rs.getInt("no"));
        board.setTitle(rs.getString("title"));
        board.setRegisteredDate(rs.getDate("cdt"));
        board.setViewCount(rs.getInt("vw_cnt"));

        Member writer = new Member();
        writer.setNo(rs.getInt("writer_no"));
        writer.setName(rs.getString("writer_name"));
        board.setWriter(writer);

        System.out.printf("%s, %s, %s, %s, %s\n", 
            rs.getInt("no"),
            rs.getString("title"),
            rs.getString("writer_name"),
            rs.getDate("cdt"),
            rs.getInt("vw_cnt"));
      }
      return list;
    }
  }

  @Override
  public Board findByNo(int no) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement( //
        "select b.no, b.title, b.content, b.writer, b.cdt, b.vw_cnt, b.like_cnt,"
        + " m.no writer_no, m.name writer_name"
        + " from pms_board b"
        + " inner join pms_member m on b.writer=m.no"
        + " where b.no = ?")) {

      stmt.setInt(1, no);

      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) {
          return null;
        }

        Board board = new Board();
        board.setNo(rs.getInt("no"));
        board.setTitle(rs.getString("title"));
        board.setContent(rs.getString("content"));
        board.setRegisteredDate(new Date(rs.getTimestamp("cdt").getTime()));
        board.setViewCount(rs.getInt("vw_cnt"));

        Member writer = new Member();
        writer.setNo(rs.getInt("writer_no"));
        writer.setName(rs.getString("writer_name"));
        board.setWriter(writer);
        board.setLike(rs.getInt("like_cnt"));

        return board;
      }
    }
  }

  @Override
  public int update(Board board) throws Exception {

    try (PreparedStatement stmt = con.prepareStatement(
        "update pms_board set title=?, content=? where no=?")) {

      // 3) DBMS에게 게시글 변경을 요청한다.
      stmt.setString(1, board.getTitle());
      stmt.setString(2, board.getContent());
      stmt.setInt(3, board.getNo());

      return stmt.executeUpdate();
    }
  }

  @Override
  public int delete(int no) throws Exception {

    try (PreparedStatement stmt = con.prepareStatement( //
        "delete from pms_board where no=?")) {

      stmt.setInt(1, no);
      return stmt.executeUpdate();
    }
  }


  @Override
  public List<Board> findByKeyword(String keyword) throws Exception {
    ArrayList<Board> list = new ArrayList<>();

    try (PreparedStatement stmt = con.prepareStatement(
        "select"
            + " b.no,"
            + " b.title,"
            + " b.cdt,"
            + " b.vw_cnt,"
            + " b.like_cnt,"
            + " m.no as writer_no,"
            + " m.name as writer_name"
            + " from pms_board b"
            + "   inner join pms_member m on m.no=b.writer"
            + " where b.title like concat('%',?,'%')"
            + "   or b.content like concat('%',?,'%')"
            + "   or m.name like concat('%',?,'%')"
            + " order by b.no desc")) {

      stmt.setString(1, keyword);
      stmt.setString(2, keyword);
      stmt.setString(3, keyword);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        Board board = new Board();
        board.setNo(rs.getInt("no"));
        board.setTitle(rs.getString("title"));
        board.setRegisteredDate(rs.getDate("cdt"));
        board.setViewCount(rs.getInt("vw_cnt"));

        Member writer = new Member();
        writer.setNo(rs.getInt("writer_no"));
        writer.setName(rs.getString("writer_name"));
        board.setWriter(writer);

        list.add(board);
      }
    }

    return list;
  }

  @Override
  public int updateViewCount(int no) throws Exception {

    try (PreparedStatement stmt = con.prepareStatement(
        "update pms_board set vw_cnt=vw_cnt+1 where no=?")) {

      stmt.setInt(1, no);
      return stmt.executeUpdate();
    }
  }
}
