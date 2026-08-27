package com.cloudtask.controller;

import com.cloudtask.dto.TaskRequest;
import com.cloudtask.dto.TaskResponse;
import com.cloudtask.model.Task;
import com.cloudtask.model.User;
import com.cloudtask.service.TaskService;
import com.cloudtask.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .createdBy(task.getUser().getUsername())
                .assignedTo(task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : null)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .completedAt(task.getCompletedAt())
                .build();
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request,
                                                    Authentication authentication) {
        String username = authentication.getName();
        Task task = taskService.createTask(
                username,
                request.getTitle(),
                request.getDescription(),
                request.getPriority(),
                request.getDueDate()
        );

        if (request.getAssignedToUserId() != null) {
            task = taskService.assignTask(username, task.getId(), request.getAssignedToUserId());
        }

        return ResponseEntity.ok(mapToResponse(task));
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getTasks(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        
        String username = authentication.getName();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Task> tasks;

        if (search != null && !search.isEmpty()) {
            tasks = taskService.searchTasks(username, search, pageable);
        } else if (status != null && !status.isEmpty()) {
            tasks = taskService.getTasksByStatus(username, status, pageable);
        } else {
            tasks = taskService.getTasks(username, pageable);
        }

        return ResponseEntity.ok(tasks.map(this::mapToResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id,
                                                     Authentication authentication) {
        String username = authentication.getName();
        Task task = taskService.getTaskById(username, id);
        return ResponseEntity.ok(mapToResponse(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,
                                                    @Valid @RequestBody TaskRequest request,
                                                    Authentication authentication) {
        String username = authentication.getName();
        Task task = taskService.updateTask(
                username,
                id,
                request.getTitle(),
                request.getDescription(),
                request.getStatus(),
                request.getPriority(),
                request.getDueDate()
        );

        if (request.getAssignedToUserId() != null) {
            task = taskService.assignTask(username, task.getId(), request.getAssignedToUserId());
        }

        return ResponseEntity.ok(mapToResponse(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id,
                                           Authentication authentication) {
        String username = authentication.getName();
        taskService.deleteTask(username, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(@PathVariable Long id,
                                                     Authentication authentication) {
        String username = authentication.getName();
        Task task = taskService.completeTask(username, id);
        return ResponseEntity.ok(mapToResponse(task));
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getTaskStats(Authentication authentication) {
        String username = authentication.getName();
        long total = taskService.getTaskCount(username);
        long pending = taskService.getTaskCountByStatus(username, "PENDING");
        long inProgress = taskService.getTaskCountByStatus(username, "IN_PROGRESS");
        long completed = taskService.getTaskCountByStatus(username, "COMPLETED");

        return ResponseEntity.ok(new Object() {
            public final long totalTasks = total;
            public final long pendingTasks = pending;
            public final long inProgressTasks = inProgress;
            public final long completedTasks = completed;
        });
    }
}
