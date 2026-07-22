package com.errorfreetext.scheduler;

import com.errorfreetext.domain.CorrectionTask;
import com.errorfreetext.domain.TaskStatus;
import com.errorfreetext.exception.SpellerApiException;
import com.errorfreetext.repository.CorrectionTaskRepository;
import com.errorfreetext.service.TaskService;
import com.errorfreetext.service.TextCorrectionService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskProcessingScheduler {

    private static final Logger log = LoggerFactory.getLogger(TaskProcessingScheduler.class);

    private final CorrectionTaskRepository correctionTaskRepository;
    private final TextCorrectionService textCorrectionService;
    private final TaskService taskService;
    private final int batchSize;

    public TaskProcessingScheduler(
            CorrectionTaskRepository correctionTaskRepository,
            TextCorrectionService textCorrectionService,
            TaskService taskService,
            @Value("${app.scheduler.batch-size}") int batchSize
    ) {
        this.correctionTaskRepository = correctionTaskRepository;
        this.textCorrectionService = textCorrectionService;
        this.taskService = taskService;
        this.batchSize = batchSize;
    }

    @Scheduled(fixedDelayString = "${app.scheduler.fixed-delay}")
    public void processNewTasks() {
        List<CorrectionTask> tasks = correctionTaskRepository.findByStatusOrderByCreatedAtAsc(
                TaskStatus.NEW,
                PageRequest.of(0, batchSize)
        );

        if (tasks.isEmpty()) {
            return;
        }

        log.debug("Processing {} new task(s)", tasks.size());
        for (CorrectionTask task : tasks) {
            processTask(task);
        }
    }

    private void processTask(CorrectionTask task) {
        taskService.markInProgress(task);
        try {
            String corrected = textCorrectionService.correct(task.getOriginalText(), task.getLanguage());
            taskService.markCompleted(task, corrected);
        } catch (SpellerApiException ex) {
            taskService.markFailed(task, ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error while processing task {}", task.getId(), ex);
            taskService.markFailed(task, "Unexpected error during text correction");
        }
    }
}
