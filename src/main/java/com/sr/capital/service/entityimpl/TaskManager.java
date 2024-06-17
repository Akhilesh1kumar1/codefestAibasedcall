package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.primary.Task;
import com.sr.capital.helpers.enums.TaskStatus;
import com.sr.capital.helpers.enums.TaskType;
import com.sr.capital.repository.primary.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskManager {

    @Autowired
    private TaskRepo taskRepo;

    public Task saveTask(Task task) {
        return taskRepo.save(task);
    }

    public List<Task> saveAllTasks(List<Task> taskList) {
        return taskRepo.saveAll(taskList);
    }

    public Task findTaskByTaskId(Long taskId) {
        Optional<Task> taskOptional = taskRepo.findById(taskId);
        return taskOptional.orElse(null);
    }

    public Task findTaskByVerificationIdAndTaskType(Long verificationId, TaskType taskType) {
        return taskRepo.findByGroupIdAndTaskType(verificationId, taskType);
    }

    public List<Task> findTasksByGroupId(Long groupId) {
        return taskRepo.findByGroupId(groupId);
    }

    public List<Task> findProcessingTasksTriedBeforeTime(LocalDateTime time) {
        return taskRepo.findByStatusAndLastTryAt(TaskStatus.PROCESSING, time);
    }

}
