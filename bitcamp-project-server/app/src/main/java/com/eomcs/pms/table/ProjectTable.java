package com.eomcs.pms.table;

import java.io.File;
import java.sql.Date;
import java.util.List;
import com.eomcs.pms.domain.Project;
import com.eomcs.util.JsonFileHandler;
import com.eomcs.util.Request;
import com.eomcs.util.Response;

public class ProjectTable implements DataTable {

  File jsonFile = new File("projects.json");
  List<Project> list;

  public ProjectTable() {
    list = JsonFileHandler.loadObjects(jsonFile, Project.class);
  }

  @Override
  public void service(Request request, Response response) throws Exception {

    Project project = null;
    String[] fields = null;
    switch(request.getCommand()) {
      case "project/insert":

        fields = request.getDataList().get(0).split(",");

        project = new Project();

        if(list.size() > 0) {
          project.setNo(list.get(list.size() - 1).getNo() + 1);
        }
        else {
          project.setNo(1);
        }

        project.setTitle(fields[0]);
        project.setContent(fields[1]);
        project.setStartDate(Date.valueOf(fields[2]));
        project.setEndDate(Date.valueOf(fields[3]));
        project.setOwner(fields[4]);
        project.setMembers(fields[5]);

        list.add(project);

        // 게시글을 목록에 추가하는 즉시, List 컬렉션의 전체 데이터를 파일에 저장
        // - 매번 전체 데이터를 파일에 저장하는 것은 비효율적이다.
        // - 효율성에 대한 부분은 무시한다.
        // - 현재 중요한 것은 서버 애플리케이션에서 데이터 파일을 관리한다는 점이다.
        // - 어차피 이 애플리케이션은 진정한 성능을 제공하는 DBMS로 교체할 것이다.
        JsonFileHandler.saveObjects(jsonFile, list);
        break;

      case "project/selectall":
        for(Project p : list) {
          response.appendData(String.format("%d,%s,%s,%s,%s,%s", 
              p.getNo(), 
              p.getTitle(), 
              p.getStartDate(),
              p.getEndDate(), 
              p.getOwner(), 
              p.getMembers()));
        }
        break;
      case "project/select":
        int num = Integer.parseInt(request.getDataList().get(0));

        project = getProject(num);

        if(project != null) {
          response.appendData(String.format("%d,%s,%s,%s,%s,%s,%s", 
              project.getNo(),
              project.getTitle(),
              project.getContent(),
              project.getStartDate().toString(),
              project.getEndDate().toString(),
              project.getOwner(),
              project.getMembers()));
        }
        else {
          throw new Exception("해당 번호의 프로젝트가 없습니다.");
        }
        break;
      case "project/update":
        fields = request.getDataList().get(0).split(",");

        project = getProject(Integer.parseInt(fields[0]));
        if (project == null) {
          throw new Exception("해당 번호의 프로젝트가 없습니다.");
        }

        // 해당 게시물의 제목과 내용을 변경한다.
        // - List에 보관된 객체를 꺼낸 것이기 때문에
        //   그냥 그 객체의 값을 변경하면 된다.
        project.setTitle(fields[1]);
        project.setContent(fields[2]);
        project.setStartDate(Date.valueOf(fields[3]));
        project.setEndDate(Date.valueOf(fields[4]));
        project.setOwner(fields[5]);
        project.setMembers(fields[6]);
        JsonFileHandler.saveObjects(jsonFile, list);
        break;
      case "project/delete":
        num = Integer.parseInt(request.getDataList().get(0));
        project = getProject(num);
        if (project == null) {
          throw new Exception("해당 번호의 프로젝트가 없습니다.");
        }
        list.remove(project);
        JsonFileHandler.saveObjects(jsonFile, list);
        break;
      default:
        throw new Exception("해당 명령을 처리할 수 없습니다.");

    }
  }

  private Project getProject(int projectNum) {
    for(Project p : list) {
      if(p.getNo() == projectNum) {
        return p;
      }
    }
    return null;
  }

}
