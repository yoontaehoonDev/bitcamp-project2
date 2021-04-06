package com.eomcs.pms.dao.mariadb;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;

// 한 번에 4번째 단계까지 가지말고 일단 3번째와 4번째 단계 사이에 있는 정도로 구현을 해보자.
// - 각 DAO 클래스는 Connection 객체를 공유하기 위해 인스턴스 필드로 선언한다.
// - 각 DAO 클래스는 DAO 인스턴스가 생성될 때 Connection 객체를 만든다.
public class ProjectDaoImpl implements ProjectDao {

  SqlSession sqlSession;

  public ProjectDaoImpl(SqlSession sqlSession) throws Exception {
    this.sqlSession = sqlSession;
  }

  // 이제 메서드들은 인스턴스 필드에 들어있는 Connection 객체를 사용해야 하기 때문에
  // 스태틱 메서드가 아닌 인스턴스 메서드로 선언해야 한다.
  @Override
  public int insert(Project project) throws Exception {
    sqlSession.insert("ProjectMapper.insert", project);
    for(Member m : project.getMembers()) {
      HashMap<String, Integer> map = new HashMap<>();
      map.put("member", m.getNo());
      map.put("project", project.getNo());
      sqlSession.insert("ProjectMapper.insertMember", map);
    }
    return 0;
  }

  @Override
  public List<Project> findAll() throws Exception {

    sqlSession.selectList("ProjectMapper.findAll");
  }

  @Override
  public Project findByNo(int no) throws Exception {
    return sqlSession.selectOne("ProjectMapper.findByNo", no);
  }

  @Override
  public List<Member> findAllMembers(int projectNo) throws Exception {
    return sqlSession.selectList("ProjectMapper.findAllMembers", projectNo);

  }

  @Override
  public int update(Project project) throws Exception {
    sqlSession.update("ProjectMapper.update", project);
    sqlSession.delete("ProjectMapper.deleteMembers", project.getNo());

    for(Member m : project.getMembers()) {
      HashMap<String, Integer> map = new HashMap<>();
      map.put("member", m.getNo());
      map.put("project", project.getNo());
      sqlSession.insert("ProjectMapper.insertMember", map);
    }
    return 0;
  }

  @Override
  public int delete(int no) throws Exception {
    return sqlSession.delete("ProjectMapper.delete", no);
  }

  @Override
  public int deleteMembers(int projectNo) throws Exception {
    return sqlSession.delete("ProjectMapper.deleteMembers", projectNo);
  }

}












