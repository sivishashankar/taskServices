package com.fse.capsule.taskmgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fse.capsule.taskmgr.model.ParentTask;

public interface ParentTaskRepository extends JpaRepository<ParentTask, Integer> {

}
