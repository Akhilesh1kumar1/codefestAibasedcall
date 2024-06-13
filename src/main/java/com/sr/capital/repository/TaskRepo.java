package com.sr.capital.repository;


import com.sr.capital.entity.Task;
import com.sr.capital.helpers.enums.TaskStatus;
import com.sr.capital.helpers.enums.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    @Query("SELECT t from Task t where t.groupId = :groupId order by t.auditData.createdAt desc")
    List<Task> findByGroupId(Long groupId);

    @Query("SELECT t from Task t where t.groupId = :groupId and t.type = :taskType order by t.auditData.createdAt desc")
    Task findByGroupIdAndTaskType(Long groupId, TaskType taskType);

    @Query("SELECT t from Task t where t.status = :status AND t.lastTryAt < :timestamp order by t.lastTryAt asc")
    List<Task> findByStatusAndLastTryAt(TaskStatus status, LocalDateTime timestamp);
}
