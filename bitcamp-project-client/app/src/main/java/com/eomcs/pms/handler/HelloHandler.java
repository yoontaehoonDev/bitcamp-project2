package com.eomcs.pms.handler;

import com.eomcs.stereotype.Component;

@Component(value="/hi")
public class HelloHandler implements Command {

  @Override
  public void service() throws Exception {
    System.out.println("hi");
  }
}
