package com.java.xdd.activiti.demo.bean;

/**
 * @author xdd
 * @date 2019/10/18
 */
public class CusUserTask {

    public static final String TYPE_ASSIGNEE = "1";
    public static final String TYPE_CANDIDATEUSER = "2";
    public static final String TYPE_CANDIDATEGROUP = "3";

    private String taskDefKey;
    private String group_id;
    private String taskType;
    private String candidate_ids;

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getCandidate_ids() {
        return candidate_ids;
    }

    public void setCandidate_ids(String candidate_ids) {
        this.candidate_ids = candidate_ids;
    }
}
