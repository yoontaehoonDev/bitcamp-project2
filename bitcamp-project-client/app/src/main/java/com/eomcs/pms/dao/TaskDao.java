package com.eomcs.pms.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.eomcs.pms.domain.Project;
import com.eomcs.pms.domain.Task;

public class TaskDao {
	Connection con;

	public TaskDao() throws Exception {
		this.con = DriverManager.getConnection(
				"jdbc:mariadb://localhost:3306/studydb?user=study&password=1111"
				);
	}

	public List<Project> findAllProjects() throws Exception {
		List<Project> projects = new ArrayList<>();
		try(PreparedStatement stmt = con.prepareStatement(
				"select no,title from pms_project order by title asc");
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				Project p = new Project();
				p.setNo(rs.getInt("no"));
				p.setTitle(rs.getString("title"));
				projects.add(p);
			}

			return projects;
		}
	}

	public int insert(Task task) throws Exception {
		try(PreparedStatement stmt = con.prepareStatement(
				"insert into pms_task(content, deadline, owner, status)"
						+ " values(?,?,?,?)"
				);

				) {
			stmt.setString(1, task.getContent());
			stmt.setDate(2, task.getDeadline());
			stmt.setString(3, task.getOwner().getName());
			stmt.setString(4, Task.getStatusLabel(task.getStatus()));
			return stmt.executeUpdate();
		}
	}

}
