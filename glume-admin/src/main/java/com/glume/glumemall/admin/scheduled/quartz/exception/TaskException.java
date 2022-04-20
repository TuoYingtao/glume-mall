package com.glume.glumemall.admin.scheduled.quartz.exception;

/**
 * 任务策略异常
 * @author tuoyingtao
 * @create 2022-04-20 11:49
 */
public class TaskException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Code code;

    public TaskException(String message, Code code) {
        this(message,code, null);
    }

    public TaskException(String message, Code code, Exception nestedEx) {
        super(message, nestedEx);
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public enum Code {
        TASK_EXISTS, NO_TASK_EXISTS, TASK_ALREADY_STARTED, UNKNOWN, CONFIG_ERROR, TASK_NODE_NOT_AVAILABLE
    }
}
