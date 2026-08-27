package com.cloudtask.repository;

import com.cloudtask.model.Task;
import com.cloudtask.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByUserAndDeletedAtIsNull(User user, Pageable pageable);

    Page<Task> findByUserAndStatusAndDeletedAtIsNull(User user, String status, Pageable pageable);

    Page<Task> findByUserAndPriorityAndDeletedAtIsNull(User user, String priority, Pageable pageable);

    Optional<Task> findByIdAndUserAndDeletedAtIsNull(Long id, User user);

    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.deletedAt IS NULL " +
           "AND (:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    Page<Task> searchTasks(@Param("user") User user,
                           @Param("title") String title,
                           Pageable pageable);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.user = :user AND t.deletedAt IS NULL")
    long countTasksByUser(@Param("user") User user);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.user = :user AND t.status = :status AND t.deletedAt IS NULL")
    long countTasksByUserAndStatus(@Param("user") User user, @Param("status") String status);

    List<Task> findByAssignedToAndDeletedAtIsNull(User assignedTo);

    @Query("SELECT t FROM Task t WHERE t.dueDate < :date AND t.status != 'COMPLETED' AND t.deletedAt IS NULL")
    List<Task> findOverdueTasks(@Param("date") LocalDateTime date);
}
