package com.eomcs.pms.handler;

import java.util.List;
import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import com.eomcs.util.Prompt;

public class ProjectDetailSearchHandler implements Command {

  ProjectDao projectDao;

  public ProjectDetailSearchHandler(ProjectDao projectDao) {
    this.projectDao = projectDao;
  }
  // title like, owner =, member = 
  @Override
  public void service() throws Exception {
    System.out.println("[프로젝트 검색]");

    String title = Prompt.inputString("프로젝트명?(제외 : 빈 문자열) : ");
    if(title.length() == 0) {
      title = null;
    }
    String owner = Prompt.inputString("관리자명?(제외 : 빈 문자열) : ");
    if(owner.length() == 0) {
      owner = null;
    }
    String member = Prompt.inputString("팀원명?(제외 : 빈 문자열) : ");
    if(member.length() == 0) {
      member = null;
    }

    List<Project> projects = projectDao.findByKeywords(title, owner, member);

    for (Project p : projects) {

      StringBuilder strBuilder = new StringBuilder();
      List<Member> members = p.getMembers();
      for (Member m : members) {
        if (strBuilder.length() > 0) {
          strBuilder.append("/");
        }
        strBuilder.append(m.getName());
      }

      // 2) 프로젝트 정보를 출력
      System.out.printf("%d, %s, %s, %s, %s, [%s]\n", 
          p.getNo(), 
          p.getTitle(), 
          p.getStartDate(),
          p.getEndDate(),
          p.getOwner().getName(),
          strBuilder.toString());
    }
  }
}








