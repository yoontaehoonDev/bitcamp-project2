package com.eomcs.pms.dao;

import java.util.List;
import com.eomcs.pms.domain.Member;

public interface MemberDao {


  int insert(Member m) throws Exception;

  List<Member> findAll() throws Exception;

  Member findByNo(int no) throws Exception;

  int update(Member m) throws Exception;

  int delete(int no) throws Exception;

  Member findByName(String name) throws Exception;
}
