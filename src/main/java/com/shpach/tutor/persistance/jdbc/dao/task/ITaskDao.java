package com.shpach.tutor.persistance.jdbc.dao.task;

import java.sql.Connection;
import java.util.List;

import com.shpach.tutor.persistance.entities.Task;

public interface ITaskDao {
	public Task addOrUpdate(Task task);
	public Task addOrUpdate(Task task,  Connection connection);
	public List<Task> findTaskByTestIdAndUserId(int testId, int userId);
	public List<Task> findTaskByTestId(int testId);
	List<Task> findTaskByUserId(int userId);
	public List<Task> findAllTasks();
}
