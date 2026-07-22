package com.errorfreetext.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.errorfreetext.domain.CorrectionTask;
import com.errorfreetext.domain.Language;
import com.errorfreetext.domain.TaskStatus;
import com.errorfreetext.dto.CreateTaskRequest;
import com.errorfreetext.exception.TaskNotFoundException;
import com.errorfreetext.repository.CorrectionTaskRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private CorrectionTaskRepository correctionTaskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTaskPersistsEntityAndReturnsId() {
        when(correctionTaskRepository.save(any(CorrectionTask.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = taskService.createTask(new CreateTaskRequest("hello world", Language.EN));

        ArgumentCaptor<CorrectionTask> captor = ArgumentCaptor.forClass(CorrectionTask.class);
        verify(correctionTaskRepository).save(captor.capture());
        CorrectionTask saved = captor.getValue();
        assertThat(saved.getStatus()).isEqualTo(TaskStatus.NEW);
        assertThat(response.taskId()).isEqualTo(saved.getId());
    }

    @Test
    void getTaskThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(correctionTaskRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTask(id))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void getTaskReturnsCorrectedTextForCompletedTask() {
        CorrectionTask task = new CorrectionTask("helo world", Language.EN);
        task.markCompleted("hello world");
        when(correctionTaskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        var response = taskService.getTask(task.getId());

        assertThat(response.status()).isEqualTo(TaskStatus.COMPLETED);
        assertThat(response.correctedText()).isEqualTo("hello world");
    }
}
