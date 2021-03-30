package com.eomcs.pms.handler;

import java.util.List;
import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.domain.Project;

public class ProjectListHandler implements Command {

  ProjectDao projectDao;

  public ProjectListHandler(ProjectDao projectDao) throws Exception {
    this.projectDao = projectDao;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[프로젝트 목록]");


    List<Project> list = projectDao.findAll();

    for(Project p : list) {
      System.out.printf("%d, %s, %s, %s, %s, %s\n",
          p.getNo(),
          p.getTitle(),
          p.getStartDate(),
          p.getEndDate(),
          p.getOwner(),
          p.getMembers()
          );
    }
  }
}








