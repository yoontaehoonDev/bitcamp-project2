package com.eomcs.pms.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;

public class ProjectDao {

  Connection con;

  public ProjectDao() throws Exception {
    this.con = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/studydb?user=study&password=1111"
        );
  }

  public int insert(Project p) throws Exception {

    try (PreparedStatement stmt = con.prepareStatement(
        "insert into pms_project(title,content,sdt,edt,owner) values(?,?,?,?,?)",
        Statement.RETURN_GENERATED_KEYS);
        PreparedStatement stmt2 = con.prepareStatement(
            "insert into pms_member_project(member_no,project_no) values(?,?)")) {

      // 수동 커밋으로 설정한다.
      // - pms_project 테이블과 pms_member_project 테이블에 모두 성공적으로 데이터를 저장했을 때 
      //   작업을 완료한다.
      con.setAutoCommit(false); // 의미 => 트랜잭션 시작

      // 1) 프로젝트를 추가한다.
      stmt.setString(1, p.getTitle());
      stmt.setString(2, p.getContent());
      stmt.setDate(3, p.getStartDate());
      stmt.setDate(4, p.getEndDate());
      stmt.setInt(5, p.getOwner().getNo());
      stmt.executeUpdate();

      // 프로젝트 데이터의 PK 값 알아내기
      try (ResultSet keyRs = stmt.getGeneratedKeys()) {
        keyRs.next();
        p.setNo(keyRs.getInt(1)); // 컬럼은 인덱스와 다르게 1부터 시작
      }

      // 2) 프로젝트에 팀원들을 추가한다.
      for (Member member : p.getMembers()) {
        stmt2.setInt(1, member.getNo());
        stmt2.setInt(2, p.getNo());
        stmt2.executeUpdate();
      }
      // 프로젝트 정보 뿐만 아니라 팀원 정보도 정상적으로 입력되었다면,
      // 실제 테이블에 데이터를 적용한다.
      con.commit(); // 의미 : 트랜잭션 종료

      return 1;

    }
  }

  public int delete(int no) throws Exception {

    try (Connection con = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/studydb?user=study&password=1111");
        PreparedStatement stmt = con.prepareStatement(
            "delete pm, p from pms_member_project pm"
                + " left join pms_project p on pm.project_no=p.no where pm.project_no=?");
        ) {

      stmt.setInt(1, no);
      return stmt.executeUpdate();
    }
  }

  public List<Project> findAll() throws Exception {

    try (PreparedStatement stmt = con.prepareStatement(
        "select" 
            + "    p.no,"
            + "    p.title,"
            + "    p.sdt,"
            + "    p.edt,"
            + "    m.no as owner_no,"
            + "    m.name as owner_name"
            + "  from pms_project p"
            + "    inner join pms_member m on p.owner=m.no"
            + "  order by title asc");
        PreparedStatement stmt2 = con.prepareStatement(
            "select" 
                + "    m.no,"
                + "    m.name"
                + " from pms_member_project mp"
                + "     inner join pms_member m on mp.member_no=m.no"
                + " where "
                + "     mp.project_no=?");
        ResultSet rs = stmt.executeQuery();
        ) {

      ArrayList<Project> list = new ArrayList<>();
      while(rs.next()) {
        Project p = new Project();
        p.setNo(rs.getInt("no"));
        p.setTitle(rs.getString("title"));
        p.setStartDate(rs.getDate("sdt"));
        p.setEndDate(rs.getDate("edt"));

        Member owner = new Member();
        owner.setNo(rs.getInt("owner_no"));
        owner.setName(rs.getString("owner_name"));
        p.setOwner(owner);

        stmt2.setInt(1, p.getNo());


        List<Member> members = new ArrayList<>();
        try (ResultSet membersRs = stmt2.executeQuery()) {
          while (membersRs.next()) {
            Member m = new Member();
            m.setNo(membersRs.getInt("no"));
            m.setName(membersRs.getString("name"));
            members.add(m);
          }
          p.setMembers(members);
        }
        list.add(p);
      }
      return list;
    }
  }

  public Project findByNo(int no) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "select"
            + "    p.title,"
            + "    p.no,"
            + "    p.content,"
            + "    p.sdt,"
            + "    p.edt,"
            + "    m.no as owner_no,"
            + "    m.name as owner_name"
            + "  from pms_project p"
            + "    inner join pms_member m on p.owner=m.no"
            + " where p.no=?");
        PreparedStatement stmt2 = con.prepareStatement(
            "select" 
                + "    m.no,"
                + "    m.name"
                + " from pms_member_project mp"
                + "     inner join pms_member m on mp.member_no=m.no"
                + " where "
                + "     mp.project_no=?");
        ) {

      stmt.setInt(1, no);
      stmt2.setInt(1, no);

      try(ResultSet rs = stmt.executeQuery()) {
        if(!rs.next()) {
          return null;
        }

        Project p = new Project();
        p.setNo(rs.getInt("no"));
        p.setTitle(rs.getString("title"));
        p.setContent(rs.getString("content"));
        p.setStartDate(rs.getDate("sdt"));
        p.setEndDate(rs.getDate("edt"));

        Member owner = new Member();
        owner.setNo(rs.getInt("no"));
        owner.setName(rs.getString("name"));
        p.setOwner(owner);

        List<Member> members = new ArrayList<>();
        try (ResultSet membersRs = stmt2.executeQuery()) {
          while (membersRs.next()) {
            Member m = new Member();
            m.setName(membersRs.getString("name"));
            members.add(m);
          }
          p.setMembers(members);
        }

        return p;
      }
    }
  }

  public void update(Project project) throws Exception {

    try (PreparedStatement stmt = con.prepareStatement(
        "update pms_project set"
            + " title=?,"
            + " content=?,"
            + " sdt=?,"
            + " edt=?,"
            + " owner=?"
            + " where no=?");
        PreparedStatement stmt2 = con.prepareStatement( 
            "delete from pms_member_project where project_no=?");
        PreparedStatement stmt3 = con.prepareStatement(
            "insert into pms_member_project(member_no,project_no) values(?,?)")) {

      con.setAutoCommit(false);

      stmt.setString(1, project.getTitle());
      stmt.setString(2, project.getContent());
      stmt.setDate(3, project.getStartDate());
      stmt.setDate(4, project.getEndDate());
      stmt.setInt(5, project.getOwner().getNo());
      stmt.setInt(6, project.getNo());
      stmt.executeUpdate();

      // 5) 프로젝트의 기존 멤버를 삭제한다.

      stmt2.setInt(1, project.getNo());
      stmt2.executeUpdate();

      // 6) 사용자가 선택한 프로젝트 멤버들을 추가한다.
      for (Member member : project.getMembers()) {
        stmt3.setInt(1, member.getNo());
        stmt3.setInt(2, project.getNo());
        stmt3.executeUpdate();
      }

      con.commit();
    }

  }

  public List<Member> insertMembers() {

  }


}
