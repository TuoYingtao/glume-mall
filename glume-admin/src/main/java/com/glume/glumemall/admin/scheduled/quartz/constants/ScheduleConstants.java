package com.glume.glumemall.admin.scheduled.quartz.constants;

/**
 * 定时任务常量
 * @author tuoyingtao
 * @create 2022-04-20 11:23
 */
public class ScheduleConstants {

    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    /** 执行目标key */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /** 并发执行任务 */
    public static final String TASK_CONCURRENT = "0";

    /** 非并发任务 */
    public static final String TASK_NOT_CONCURRENT = "1";

    /** 默认 */
    public static final String MISFIRE_DEFAULT = "0";

    /** 立即触发执行 */
    public static final String MISFIRE_IGNORE_MISFIRES = "1";

    /** 触发执行一次任务 */
    public static final String MISFIRE_FIRE_AND_PROCEED = "2";

    /** 不触发立即执行 */
    public static final String MISFIRE_DO_NOTHING = "3";

    /** 成功标识 */
    public static final String SUCCESS = "0";

    /** 失败标识 */
    public static final String FAIL = "1";

    public enum Status {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private Integer value;

        Status(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

}
