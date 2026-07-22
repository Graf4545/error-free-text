package com.errorfreetext.service;

import com.errorfreetext.domain.CorrectionTask;
import com.errorfreetext.domain.TaskStatus;
import com.errorfreetext.dto.CreateTaskRequest;
import com.errorfreetext.dto.CreateTaskResponse;
import com.errorfreetext.dto.TaskResponse;
import com.errorfreetext.exception.TaskNotFoundException;
import com.errorfreetext.repository.CorrectionTaskRepository;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final CorrectionTaskRepository correctionTaskRepository;

    public TaskService(CorrectionTaskRepository correctionTaskRepository) {
        this.correctionTaskRepository = correctionTaskRepository;
    }

    @Transactional
    public CreateTaskResponse createTask(CreateTaskRequest request) {
        CorrectionTask task = new CorrectionTask(request.text(), request.language());
        correctionTaskRepository.save(task);
        log.info("Created correction task {}", task.getId());
        return new CreateTaskResponse(task.getId());
    }

    @Transactional(readOnly = true)
    public TaskResponse getTask(UUID taskId) {
        CorrectionTask task = correctionTaskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        return switch (task.getStatus()) {
            case COMPLETED -> new TaskResponse(task.getId(), task.getStatus(), task.getCorrectedText(), null);
            case FAILED -> new TaskResponse(task.getId(), task.getStatus(), null, task.getErrorMessage());
            case NEW, IN_PROGRESS -> new TaskResponse(task.getId(), task.getStatus(), null, null);
        };
    }

    @Transactional
    public void markInProgress(CorrectionTask task) {
        task.markInProgress();
        correctionTaskRepository.save(task);
    }

    @Transactional
    public void markCompleted(CorrectionTask task, String correctedText) {
        task.markCompleted(correctedText);
        correctionTaskRepository.save(task);
        log.info("Task {} completed", task.getId());
    }

    @Transactional
    public void markFailed(CorrectionTask task, String errorMessage) {
        task.markFailed(errorMessage);
        correctionTaskRepository.save(task);
        log.warn("Task {} failed: {}", task.getId(), errorMessage);
    }

    @Transactional(readOnly = true)
    public CorrectionTask getTaskEntity(UUID taskId) {
        return correctionTaskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }
}
