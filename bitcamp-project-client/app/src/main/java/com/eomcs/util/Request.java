package com.eomcs.util;

import java.util.List;

public class Request {
  private String command;
  private List<String> dataList;



  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public List<String> getDataList() {
    return dataList;
  }

  public void setDataList(List<String> dataList) {
    this.dataList = dataList;
  }


  public void appendData(String data) {
    this.dataList.add(data);
  }
}
