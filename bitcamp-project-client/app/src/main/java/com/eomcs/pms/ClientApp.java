package com.eomcs.pms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.eomcs.util.Prompt;

public class ClientApp {

  String serverAddress;
  int port;

  public static void main(String[] args) {
    ClientApp app = new ClientApp("localhost", 8888);

    try {
      app.execute();

    } catch (Exception e) {
      System.out.println("클라이언트 실행 중 오류 발생!");
      e.printStackTrace();
    }
  }

  public ClientApp(String serverAddress, int port) {
    this.serverAddress = serverAddress;
    this.port = port;
  }


  public void execute() throws Exception {

    try(
        Socket socket = new Socket(serverAddress, port);
        // 2. 데이터 입출력 스트림 객체 준비
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
        ) {
      // Stateful 통신 방식
      // 1. 서버와 연결

      while (true) {
        String command = com.eomcs.util.Prompt.inputString("명령> ");

        if (command.length() == 0) {
          continue;
        }

        out.println(command);
        out.println();
        out.flush();

        String line = null;
        while(true) {
          line = in.readLine();
          if(line.length() == 0) {
            break;
          }
          System.out.println(line);
        }
        System.out.println(); // 이전 명령의 실행을 구분하기 위해 빈 줄 출력

        if(command.equalsIgnoreCase("quit") || 
            command.equalsIgnoreCase("exit") ||
            command.equalsIgnoreCase("serverstop")) {
          System.out.println("안녕!");
          break;
        } 
      }
    } 
    catch (Exception e) {
      System.out.println("통신 오류 발생!");
    }

    Prompt.close();
  }
}
