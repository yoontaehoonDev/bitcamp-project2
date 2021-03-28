package com.eomcs.pms.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.eomcs.pms.domain.Task;

public class TaskListHandler implements Command {

	@Override
	public void service() throws Exception {
		System.out.println("[작업 목록]");

		try (Connection con = DriverManager.getConnection(
				"jdbc:mariadb://localhost:3306/studydb?user=study&password=1111");
				PreparedStatement stmt = con.prepareStatement(
						"select t.no, t.content, t.deadline, m.name as admin_name, t.status"
								+ " from pms_task t"
								+ " inner join pms_member m on m.no=t.owner"
								+ " order by t.no desc"
						);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				System.out.printf("%d, %s, %s, %s, %s\n", 
						rs.getInt("no"), 
						rs.getString("content"), 
						rs.getDate("deadline"),
						rs.getString("admin_name"),
						Task.getStatusLabel(rs.getInt("status")));
			}
		}
	}
}
