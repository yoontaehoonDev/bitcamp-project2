package com.eomcs.pms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import com.eomcs.pms.table.DataTable;
import com.eomcs.util.Request;
import com.eomcs.util.Response;

// 클라이언트 측의 Statement 와 연결된 후,
// 그 요청 처리를 별도의 실행 흐름으로 분리한다.
public class StatementHandlerThread extends Thread {
  Socket socket;
  HashMap<String, DataTable> tableMap = new HashMap<>();

  public StatementHandlerThread(Socket socket, HashMap<String, DataTable> tableMap) {
    this.socket = socket;
    this.tableMap = tableMap;
  }

  @Override
  public void run() {
    // 별도의 실행 흐름에서 수행할 작업이 있다면, 이 메소드에 기술한다. 
    try(DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

      while(true) {

        Request request = receiveRequest(in);

        log(request);

        if(request.getCommand().equals("quit")) {
          sendResponse(out, "success");
          break;
        }

        DataTable dataTable = findDataTable(request.getCommand());

        if(dataTable != null) {
          Response response = new Response();
          try {
            dataTable.service(request, response);
            sendResponse(out, "success", response.getDataList().toArray(new String[response.getDataList().size()]));
          }
          catch (Exception e) {
            e.printStackTrace();
            sendResponse(out, "error", e.getMessage());
          }
        }
        else {
          sendResponse(out, "error", "해당 요청을 처리할 수 없습니다.");
        }
        out.flush();
      }
    }

    catch (Exception e) {
      e.printStackTrace();
    }

  }

  private DataTable findDataTable(String command) {
    Set<String> keySet = tableMap.keySet();
    for(String key : keySet) {
      if(command.startsWith(key)) {
        return tableMap.get(key);
      }
    }
    return null;
  }

  private Request receiveRequest(DataInputStream in) throws Exception {
    Request request = new Request();

    request.setCommand(in.readUTF());
    int length = in.readInt();

    ArrayList<String> data = null;
    if(length > 0) {
      data = new ArrayList<>();
      for(int i = 0; i < length; i++) {
        data.add(in.readUTF());
      }
      request.setDataList(data);
    }
    return request;
  }

  private void sendResponse(DataOutputStream out, String status, String... data) throws Exception {
    out.writeUTF(status);
    out.writeInt(data.length);
    for(int i = 0; i< data.length; i++) {
      out.writeUTF(data[i]);
    }
    out.flush();
  }

  private void log(Request request) {
    System.out.println("============================");
    System.out.printf("명령 : %s\n", request.getCommand());
    List<String> data = request.getDataList();
    System.out.printf("데이터 개수 : %d\n", data == null ? 0 : data.size());
    if(data != null) {
      System.out.println("데이터 : ");
      for(String str : data) {
        System.out.println(str);
      }
    }
  }

}
