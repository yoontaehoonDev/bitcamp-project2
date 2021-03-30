package com.eomcs.pms.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.eomcs.pms.domain.Member;

public class MemberDao {

  Connection con;

  public MemberDao() throws Exception {
    this.con = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/studydb?user=study&password=1111"
        );
  }


  public int insert(Member m) throws Exception {
    try(PreparedStatement stmt = con.prepareStatement(
        "insert into pms_member(name, email, password, photo, tel)"
            + " values(?, ?, password(?), ?, ?)"
        )
        ) {

      stmt.setString(1, m.getName());
      stmt.setString(2, m.getEmail());
      stmt.setString(3, m.getPassword());
      stmt.setString(4, m.getPhoto());
      stmt.setString(5, m.getTel());
      return stmt.executeUpdate();
    }
  }

  public  List<Member> findAll() throws Exception {
    try(PreparedStatement stmt = con.prepareStatement(
        "select no, name, email, photo, tel, cdt from pms_member order by name asc;"
        );
        ResultSet rs = stmt.executeQuery()
        ) {
      ArrayList<Member> list = new ArrayList<>();

      while(rs.next()) {
        Member m = new Member();
        m.setNo(rs.getInt("no"));
        m.setName(rs.getString("name"));
        m.setEmail(rs.getString("email"));
        m.setPhoto(rs.getString("photo"));
        m.setTel(rs.getString("tel"));
        m.setRegisteredDate(rs.getDate("cdt"));
        list.add(m);
      }
      return list;
    }
  }

  public  Member findByNo(int no) throws Exception {
    try(PreparedStatement stmt = con.prepareStatement(
        "select no, name, email, photo, tel, cdt from pms_member where no = ?"
        )
        ) {

      stmt.setInt(1, no);

      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) {
          return null;
        }

        Member m = new Member();
        m.setNo(rs.getInt("no"));
        m.setName(rs.getString("name"));
        m.setEmail(rs.getString("email"));
        m.setPhoto(rs.getString("photo"));
        m.setTel(rs.getString("tel"));
        m.setRegisteredDate(rs.getDate("cdt"));

        return m;
      }
    }
  }

  public  int update(Member m) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "update pms_member set name=?, email=?, password=password(?), photo=?, tel=? where no=?")) {


      stmt.setString(1, m.getName());
      stmt.setString(2, m.getEmail());
      stmt.setString(3, m.getPassword());
      stmt.setString(4, m.getPhoto());
      stmt.setString(5, m.getTel());
      stmt.setInt(6, m.getNo());
      return stmt.executeUpdate();

    }
  }

  public  int delete(int no) throws Exception {
    try(PreparedStatement stmt = con.prepareStatement(
        "delete from pms_member where no = ?"
        )) {

      stmt.setInt(1, no);

      return stmt.executeUpdate();
    }
  }

  public Member findByName(String name) throws Exception {
    try(PreparedStatement stmt = con.prepareStatement(
        "select * from pms_member where name = ?"
        )) {
      stmt.setString(1, name);

      ResultSet rs = stmt.executeQuery();

      if(!rs.next()) {
        return null;
      }

      Member m = new Member();
      m.setNo(rs.getInt("no"));
      m.setName(rs.getString("name"));
      m.setEmail(rs.getString("email"));
      m.setPhoto(rs.getString("photo"));
      m.setTel(rs.getString("tel"));
      m.setRegisteredDate(rs.getDate("cdt"));

      return m;
    }
  }
}
