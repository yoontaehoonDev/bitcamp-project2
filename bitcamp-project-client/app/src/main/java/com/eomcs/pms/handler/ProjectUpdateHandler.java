package com.eomcs.pms.handler;

import java.util.List;
import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;
import com.eomcs.util.Prompt;

public class ProjectUpdateHandler implements Command {

  MemberValidator memberValidator;
  ProjectDao projectDao;

  public ProjectUpdateHandler(ProjectDao projectDao, MemberValidator memberValidator) {
    this.projectDao = projectDao;
    this.memberValidator = memberValidator;
  }

  @Override
  public void service() throws Exception {
    System.out.println("[프로젝트 변경]");

    int no = Prompt.inputInt("번호? ");

    Project projects = projectDao.findByNo(no);

    if(projects == null) {
      System.out.println("해당 번호의 프로젝트가 존재하지 않습니다.");
      return;
    }

    projects.setTitle(Prompt.inputString(
        String.format("프로젝트명(%s)? ", projects.getTitle())));
    projects.setContent(Prompt.inputString(
        String.format("내용(%s)? ", projects.getContent())));
    projects.setStartDate(Prompt.inputDate(
        String.format("시작일(%s)? ", projects.getStartDate())));
    projects.setEndDate(Prompt.inputDate(
        String.format("종료일(%s)? ", projects.getEndDate())));
    projects.setOwner(memberValidator.inputMember(
        String.format("만든이(%s)?(취소: 빈 문자열) ", projects.getOwner())));

    if (projects.getOwner() == null) {
      System.out.println("프로젝트 변경을 취소합니다.");
      return;
    }

    projects.setMembers(memberValidator.inputMembers(
        String.format("팀원(%s)?(완료: 빈 문자열) ", projects.getMembers())));

    List<Member> list = projectDao.insertMembers();

    String input = Prompt.inputString("정말 변경하시겠습니까?(y/N) ");
    if (!input.equalsIgnoreCase("Y")) {
      System.out.println("프로젝트 변경을 취소하였습니다.");
      return;
    }

    projectDao.update(projects);

    System.out.println("프로젝트을 변경하였습니다.");
  }
}






