package com.eomcs.pms.dao.mariadb;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;

public class ProjectDaoImpl implements ProjectDao {

  SqlSession sqlSession;

  public ProjectDaoImpl(SqlSession sqlSession) throws Exception {
    this.sqlSession = sqlSession;
  }

  @Override
  public int insert(Project project) throws Exception {
    try {
      int count = sqlSession.insert("ProjectMapper.insert", project);

      insertMembers(project.getNo(), project.getMembers());

      sqlSession.commit();
      return count;
    }
    catch (Exception e) {
      sqlSession.rollback();
      throw e;
    }
  }

  @Override
  public List<Project> findByKeyword(String item, String keyword) throws Exception {

    HashMap<String,Object> params = new HashMap<>();
    params.put("item", item);
    params.put("keyword", keyword);

    return sqlSession.selectList("ProjectMapper.findByKeyword", params);
  }

  @Override
  public List<Project> findByKeywords(String title, String owner, String member) throws Exception {

    HashMap<String,Object> params = new HashMap<>();
    params.put("title", title);
    params.put("owner", owner);
    params.put("member", member);

    return sqlSession.selectList("ProjectMapper.findByKeywords", params);
  }

  @Override
  public Project findByNo(int no) throws Exception {
    return sqlSession.selectOne("ProjectMapper.findByNo", no);
  }

  @Override
  public int update(Project project) throws Exception {
    try {
      int count = sqlSession.update("ProjectMapper.update", project);

      deleteMembers(project.getNo());

      insertMembers(project.getNo(), project.getMembers());
      sqlSession.commit();

      return count;
    }
    catch (Exception e) {
      sqlSession.rollback();
      throw e;
    }
  }

  @Override
  public int delete(int no) throws Exception {
    // 1) 프로젝트에 소속된 팀원 정보 삭제

    deleteMembers(no);

    // 2) 프로젝트 삭제
    return sqlSession.delete("ProjectMapper.delete", no);
  }

  @Override
  public int insertMember(int projectNo, int memberNo) throws Exception {
    HashMap<String,Object> params = new HashMap<>();
    params.put("projectNo", projectNo);
    params.put("memberNo", memberNo);
    return sqlSession.insert("ProjectMapper.insertMember", params);
  }

  @Override
  public int insertMembers(int projectNo, List<Member> members) throws Exception {
    HashMap<String,Object> params = new HashMap<>();
    params.put("projectNo", projectNo);
    params.put("members", members);
    int count = sqlSession.insert("ProjectMapper.insertMembers", params);


    return count;
  }

  @Override
  public List<Member> findAllMembers(int projectNo) throws Exception {
    return sqlSession.selectList("ProjectMapper.findAllMembers", projectNo);
  }

  @Override
  public int deleteMembers(int projectNo) throws Exception {

    int count = sqlSession.delete("ProjectMapper.deleteMembers", projectNo);

    if(count == 0) {
      sqlSession.rollback();
      throw new Exception("멤버 정보 제거 실패");
    }

    sqlSession.commit();
    return count;
  }
}












