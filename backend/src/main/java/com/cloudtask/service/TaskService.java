package com.cloudtask.service;

import com.cloudtask.model.Task;
import com.cloudtask.model.User;
import com.cloudtask.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Transactional
    public Task createTask(String username, String title, String description, String priority, LocalDateTime dueDate) {
        User user = userService.findByUsername(username);

        Task task = Task.builder()
                .title(title)
                .description(description)
                .status("PENDING")
                .priority(priority != null ? priority : "MEDIUM")
                .dueDate(dueDate)
                .user(user)
                .build();

        return taskRepository.save(task);
    }

    public Page<Task> getTasks(String username, Pageable pageable) {
        User user = userService.findByUsername(username);
        return taskRepository.findByUserAndDeletedAtIsNull(user, pageable);
    }

    public Page<Task> getTasksByStatus(String username, String status, Pageable pageable) {
        User user = userService.findByUsername(username);
        return taskRepository.findByUserAndStatusAndDeletedAtIsNull(user, status, pageable);
    }

    public Page<Task> searchTasks(String username, String title, Pageable pageable) {
        User user = userService.findByUsername(username);
        return taskRepository.searchTasks(user, title, pageable);
    }

    public Task getTaskById(String username, Long taskId) {
        User user = userService.findByUsername(username);
        return taskRepository.findByIdAndUserAndDeletedAtIsNull(taskId, user)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Transactional
    public Task updateTask(String username, Long taskId, String title, String description,
                           String status, String priority, LocalDateTime dueDate) {
        Task task = getTaskById(username, taskId);

        if (title != null) task.setTitle(title);
        if (description != null) task.setDescription(description);
        if (status != null) task.setStatus(status);
        if (priority != null) task.setPriority(priority);
        if (dueDate != null) task.setDueDate(dueDate);

        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(String username, Long taskId) {
        Task task = getTaskById(username, taskId);
        task.setDeletedAt(LocalDateTime.now());
        taskRepository.save(task);
    }

    @Transactional
    public Task completeTask(String username, Long taskId) {
        Task task = getTaskById(username, taskId);
        task.setStatus("COMPLETED");
        task.setCompletedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    @Transactional
    public Task assignTask(String username, Long taskId, Long assignToUserId) {
        Task task = getTaskById(username, taskId);
        User assignTo = userService.findById(assignToUserId);
        task.setAssignedTo(assignTo);
        return taskRepository.save(task);
    }

    public long getTaskCount(String username) {
        User user = userService.findByUsername(username);
        return taskRepository.countTasksByUser(user);
    }

    public long getTaskCountByStatus(String username, String status) {
        User user = userService.findByUsername(username);
        return taskRepository.countTasksByUserAndStatus(user, status);
    }

    public List<Task> getOverdueTasks() {
        return taskRepository.findOverdueTasks(LocalDateTime.now());
    }
}
