package com.sr.capital.exception.custom;

public class TaskProcessingException extends CustomException {
    public TaskProcessingException() {
        super("Task still processing! Waiting for next schedule!");
    }

}
