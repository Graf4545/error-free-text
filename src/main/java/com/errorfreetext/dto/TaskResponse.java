package com.errorfreetext.dto;

import com.errorfreetext.domain.TaskStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskResponse {
    private UUID taskId;
    private TaskStatus status;
    private String correctedText;
    private String errorMessage;

    public TaskResponse(UUID taskId, TaskStatus status, String correctedText, String errorMessage) {
        this.taskId = taskId;
        this.status = status;
        this.correctedText = correctedText;
        this.errorMessage = errorMessage;
    }

    public UUID taskId() {
        return taskId;
    }

    public TaskStatus status() {
        return status;
    }

    public String correctedText() {
        return correctedText;
    }

    public String errorMessage() {
        return errorMessage;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getCorrectedText() {
        return correctedText;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setCorrectedText(String correctedText) {
        this.correctedText = correctedText;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
