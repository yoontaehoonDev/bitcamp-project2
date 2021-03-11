package com.eomcs.pms.handler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import com.eomcs.util.Prompt;

public class TaskDetailHandler implements Command {

  @Override
  public void service(DataInputStream in, DataOutputStream out) throws Exception {
    System.out.println("[작업 상세보기]");

    int no = Prompt.inputInt("번호? ");
    out.writeUTF("task/select");
    out.writeInt(1);
    out.writeUTF(Integer.toString(no));

    String status = in.readUTF();
    in.readInt();

    if(status.equals("error")) {
      System.out.println(in.readUTF());
      return;
    }

    String[] fields = in.readUTF().split(",");

    System.out.printf("내용: %s\n", fields[1]);
    System.out.printf("마감일: %s\n", fields[2]);
    System.out.printf("상태: %s\n", getStatusLabel(fields[3]));
    System.out.printf("담당자: %s\n", fields[4]);
  }

  private String getStatusLabel(String fields) {
    switch (fields) {
      case "1":
        return "진행중";
      case "2":
        return "완료";
      default:
        return "신규";
    }
  }
}
