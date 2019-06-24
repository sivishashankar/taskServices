package com.fse.capsule.taskmgr.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fse.capsule.taskmgr.dao.TaskManagerDao;
import com.fse.capsule.taskmgr.model.ParentTask;
import com.fse.capsule.taskmgr.model.Task;
import com.fse.capsule.taskmgr.service.TaskManagerService;

@Service
public class TaskManagerServiceImpl implements TaskManagerService {

	
		@Autowired
		TaskManagerDao taskManagerDao;
		
		final String DATE_PATTERN = "yyyy-MM-dd";

		/**
		 * This method is used to fetch all the tasks
		 * 
		 * @return list of tasks
		 */
		@Override
		public List<Task> fetchAllTasks() {
	
			List<Task> taskList = taskManagerDao.fetchAllTasks();
	
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
	
			taskList.forEach(task -> {
	
				String startDate = simpleDateFormat.format(task.getStartDate());
				String endDate = simpleDateFormat.format(task.getEndDate());
	
				task.setStartDateStr(startDate);
				task.setEndDateStr(endDate);
	
				task.setParentTaskName("This task has no Parent");
				if (null != task.getParentTask()) {
					task.setParentTaskName(task.getParentTask().getParentTaskName());
					task.setParentId(task.getParentTask().getId());
				}
			});
	
			return taskList;
		}
	
		/**
		 * This method is used to add / update a task
		 * 
		 * @param task
		 * @throws ParseException
		 */
		@Override
		public void saveTask(Task task) throws ParseException {
	
			Date stDate = new SimpleDateFormat(DATE_PATTERN).parse(task.getStartDateStr());
			Date endDate = new SimpleDateFormat(DATE_PATTERN).parse(task.getEndDateStr());
	
			task.setStartDate(stDate);
			task.setEndDate(endDate);
	
			if (null != task.getParentId()) {	
				saveParentTask(task);
			}
	
			taskManagerDao.saveTask(task);
		}
	
		/**
		 * This method is used to end a task
		 * 
		 * @param id
		 */
		@Override
		@Transactional
		public void endTask(Integer taskId) {
	
			taskManagerDao.endTask(taskId);
		}
	
		/**
		 * This method is used to fetch specific task by id
		 * 
		 * @param id
		 * @return Task
		 */
		@Override
		public Task fetchTaskById(Integer taskId) {
	
			Task task = taskManagerDao.fetchTaskById(taskId);
	
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
	
			String startDate = simpleDateFormat.format(task.getStartDate());
			String endDate = simpleDateFormat.format(task.getEndDate());
	
			task.setStartDateStr(startDate);
			task.setEndDateStr(endDate);
	
			task.setParentTaskName("This task has no Parent");
			
			if (null != task.getParentTask()) {
				task.setParentTaskName(task.getParentTask().getParentTaskName());
				task.setParentId(task.getParentTask().getId());
			}
	
			return task;
		}
		
		protected void saveParentTask(Task task) {
			
			ParentTask parent = new ParentTask();
			
			Integer parentId = taskManagerDao.fetchParentTask(task.getParentTaskName());

			if (null != parentId) {

				parent.setId(parentId);

			} else {
				parent.setParentTaskName(task.getParentTaskName());
				parent = taskManagerDao.saveParentTask(parent);

			}

			task.setParentTask(parent);
		}
}
