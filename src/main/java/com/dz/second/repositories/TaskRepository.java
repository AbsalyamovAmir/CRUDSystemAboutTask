package com.dz.second.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dz.second.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

}
