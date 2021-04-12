package com.eomcs.pms.service.impl;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.dao.TaskDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import com.eomcs.pms.service.ProjectService;


public class DefaultProjectService implements ProjectService {

  SqlSession sqlSession;
  ProjectDao projectDao;
  TaskDao taskDao;

  public DefaultProjectService(SqlSession sqlSession, ProjectDao projectDao, TaskDao taskDao) {
    this.sqlSession = sqlSession;
    this.projectDao = projectDao;
    this.taskDao = taskDao;
  }

  @Override
  public int add(Project project) throws Exception {
    try {
      int count = projectDao.insert(project);

      HashMap<String,Object> params = new HashMap<>();
      params.put("projectNo", project.getNo());
      params.put("members", project.getMembers());

      projectDao.insertMembers(params);

      sqlSession.commit();
      return count;
    }
    catch (Exception e) {
      sqlSession.rollback();
      throw e;
    }
  }

  @Override
  public List<Project> list() throws Exception {
    return projectDao.findByKeyword(null);
  }

  @Override
  public Project get(int no) throws Exception {
    return projectDao.findByNo(no);
  }

  @Override
  public int update(Project project) throws Exception {
    try {
      int count = projectDao.update(project);
      projectDao.deleteMembers(project.getNo());


      HashMap<String, Object> params = new HashMap<>();
      params.put("projectNo", project.getNo());
      params.put("members", project.getMembers());

      projectDao.insertMembers(params);

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
    try {

      taskDao.deleteByProjectNo(no);

      projectDao.deleteMembers(no);

      int count = projectDao.delete(no);
      sqlSession.commit();
      return count;
    }
    catch (Exception e) {
      sqlSession.rollback();
      throw e;
    }
  }

  @Override
  public List<Project> search(String item, String keyword) throws Exception {
    HashMap<String,Object> params = new HashMap<>();
    params.put("item", item);
    params.put("keyword", keyword);

    return projectDao.findByKeyword(params);
  }

  @Override
  public List<Project> search(String title, String owner, String member) throws Exception {
    HashMap<String,Object> params = new HashMap<>();
    params.put("title", title);
    params.put("owner", owner);
    params.put("member", member);
    return projectDao.findByKeywords(params);
  }

  @Override
  public List<Member> findAllMembers(int projectNo) throws Exception {
    return sqlSession.selectList("ProjectMapper.findAllMembers", projectNo);
  }

  @Override
  public int deleteMembers(int projectNo) throws Exception {
    int count = sqlSession.delete("ProjectMapper.deleteMembers", projectNo);
    sqlSession.commit();
    return count;
  }

  @Override
  public int updateMembers(int projectNo, List<Member> members) throws Exception {
    try {
      projectDao.deleteMembers(projectNo);

      HashMap<String,Object> params = new HashMap<>();
      params.put("projectNo", projectNo);
      params.put("members", members);

      int count = projectDao.insertMembers(params);
      sqlSession.commit();
      return count;
    }
    catch (Exception e) {
      sqlSession.rollback();
      throw e;
    }
  }
}
