package com.eomcs.pms.util.concurrent;

import java.util.LinkedList;
import java.util.List;

// Thread Pool 역할을 수행한다.
// 쓰레드를 생성하고 유지한다.
public class ThreadPool {

  int threadCount;

  boolean isStop;

  // 쓰레드에게 작업을 주면, 실행을 하도록 기존 쓰레드를 커스텀마이징을 한다.
  class Executor extends Thread {

    Runnable task;

    public Executor(int threadNo) {
      super(threadNo + " 번");
    }

    public void setTask(Runnable task) {
      // 쓰레드에 작업을 할당하면 작업 개시 알림을 보낸다.
      synchronized(this) {
        this.task = task;
        this.notify();
      }
    }

    @Override
    public void run() {
      while(true) {
        synchronized(this) {
          try {
            // 작업 개시 알림이 올 때까지 기다린다.
            this.wait();
            if(isStop) {
              break; // 쓰레드는 멈춘다.
            }
          }
          catch (Exception e) {
            System.out.println("쓰레드를 대기시키는 중에 오류 발생");
            break;
          }
          // 작업을 실행한다.
          task.run();
          // 쓰레드의 작업이 끝난 후, 쓰레드를 종료하라는 상태면,
          // 즉시 run() 메소드를 나간다.
          if(isStop) {
            break;
          }
          // 쓰레드를 종료하라는 상태라면, 클라이언트 작업을 완료한 후,
          // 그대로 쓰레드를 종료한다.

          // 작업이 끝난 후, 쓰레드풀로 돌려 보낸다.
          returnExecutor(this);

        }
      }

      System.out.println(this.getName() + " 쓰레드 종료");
    }
  }

  // 쓰레드 목록
  List<Executor> executors = new LinkedList<>();

  public void execute(Runnable task) {
    // 쓰레드 목록에서 쓰레드를 꺼낸다.
    Executor executor = getExecutor();
    System.out.println(executor.getName() + " 번 쓰레드 사용");

    // 쓰레드에 작업을 할당한다.
    executor.setTask(task);
  }

  private Executor getExecutor() {
    if(executors.size() == 0) {
      // 현재 쓰레드 목록에 쓰레드가 없다면, 새 쓰레드를 생성하고 리턴한다.
      Executor executor = new Executor(++threadCount);
      System.out.println(threadCount + " 번 쓰레드 생성");

      // 새로 만든 쓰레드를 시작시킨다.
      // 실제 쓰레드는 작업을 할당하기 전까지 기다릴 것이다.
      executor.start();
      try { Thread.sleep(300); } catch (Exception e) {}

      return executor;
    }

    // 쓰레드 목록에 기존에 반납된 쓰레드가 있다면, 그 쓰레드를 리턴한다.
    return executors.remove(0);
  }

  private void returnExecutor(Executor executor) {
    executors.add(executor);
  }

  public void shutdown() {
    // 쓰레드의 실행 상태를 종료 상태로 설정한다.
    isStop = true;

    // 쓰레드 목록에 들어 있는 모든 쓰레드를 깨운다.
    // 쓰레드가 깨어나면, 제일 먼저 isStop 변수의 값을 검사하여
    // true면 쓰레드를 멈출 것이다.
    for(Executor executor : executors) {
      executor.setTask(null);
    }
  }
}