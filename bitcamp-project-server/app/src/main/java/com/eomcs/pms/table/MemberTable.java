package com.eomcs.pms.table;

import java.io.File;
import java.sql.Date;
import java.util.List;
import com.eomcs.pms.domain.Member;
import com.eomcs.util.JsonFileHandler;
import com.eomcs.util.Request;
import com.eomcs.util.Response;

public class MemberTable implements DataTable {

  File jsonFile = new File("members.json");
  List<Member> list;

  public MemberTable() {
    list = JsonFileHandler.loadObjects(jsonFile, Member.class);
  }

  @Override
  public void service(Request request, Response response) throws Exception {

    Member member = null;
    String[] fields = null;
    String name = null;
    String temp = null;
    switch(request.getCommand()) {
      case "member/insert":

        fields = request.getDataList().get(0).split(",");

        member = new Member();

        if(list.size() > 0) {
          member.setNo(list.get(list.size() - 1).getNo() + 1);
        }
        else {
          member.setNo(1);
        }

        member.setName(fields[0]);
        member.setEmail(fields[1]);
        member.setPassword(fields[2]);
        member.setPhoto(fields[3]);
        member.setTel(fields[4]);
        member.setRegisteredDate(new Date(System.currentTimeMillis()));

        list.add(member);

        // 게시글을 목록에 추가하는 즉시, List 컬렉션의 전체 데이터를 파일에 저장
        // - 매번 전체 데이터를 파일에 저장하는 것은 비효율적이다.
        // - 효율성에 대한 부분은 무시한다.
        // - 현재 중요한 것은 서버 애플리케이션에서 데이터 파일을 관리한다는 점이다.
        // - 어차피 이 애플리케이션은 진정한 성능을 제공하는 DBMS로 교체할 것이다.
        JsonFileHandler.saveObjects(jsonFile, list);
        break;

      case "member/selectall":
        for(Member m : list) {
          response.appendData(String.format("%d,%s,%s,%s,%s,%s,%s", 
              m.getNo(),
              m.getName(),
              m.getEmail(),
              m.getPassword(),
              m.getPhoto(),
              m.getTel(),
              m.getRegisteredDate()));
        }
        break;

      case "member/select":
        int num = Integer.parseInt(request.getDataList().get(0));

        member = getMember(num);

        if(member != null) {
          response.appendData(String.format("%d,%s,%s,%s,%s,%s,%s", 
              member.getNo(),
              member.getName(),
              member.getEmail(),
              member.getPassword(),
              member.getPhoto(),
              member.getTel(),
              member.getRegisteredDate()));
        }
        else {
          throw new Exception("해당 번호의 멤버가 없습니다.");
        }
        break;

      case "member/selectByName":
        name = request.getDataList().get(0);

        member = getMemberByName(name);

        if(member != null) {
          response.appendData(String.format("%d,%s,%s,%s,%s,%s,%s", 
              member.getNo(),
              member.getName(),
              member.getEmail(),
              member.getPassword(),
              member.getPhoto(),
              member.getTel(),
              member.getRegisteredDate()));
        }
        else {
          throw new Exception("해당 번호의 멤버가 없습니다.");
        }
        break;

      case "member/update":
        fields = request.getDataList().get(0).split(",");

        member = getMember(Integer.parseInt(fields[0]));
        if (member == null) {
          throw new Exception("해당 번호의 멤버가 없습니다.");
        }

        // 해당 게시물의 제목과 내용을 변경한다.
        // - List에 보관된 객체를 꺼낸 것이기 때문에
        //   그냥 그 객체의 값을 변경하면 된다.
        member.setName(fields[1]);
        member.setEmail(fields[2]);
        member.setPassword(fields[3]);
        member.setPhoto(fields[4]);
        member.setTel(fields[5]);
        JsonFileHandler.saveObjects(jsonFile, list);
        break;
      case "member/delete":
        num = Integer.parseInt(request.getDataList().get(0));
        member = getMember(num);
        if (member == null) {
          throw new Exception("해당 번호의 멤버가 없습니다.");
        }
        list.remove(member);
        JsonFileHandler.saveObjects(jsonFile, list);
        break;
      default:
        throw new Exception("해당 명령을 처리할 수 없습니다.");

    }
  }

  private Member getMember(int memberNum) {
    for(Member m : list) {
      if(m.getNo() == memberNum) {
        return m;
      }
    }
    return null;
  }

  private Member getMemberByName(String name) {
    for(Member m : list) {
      if(m.getName().equals(name)) {
        return m;
      }
    }
    return null;
  }



}
