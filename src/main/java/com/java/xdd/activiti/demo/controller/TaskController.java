package com.java.xdd.activiti.demo.controller;

import com.java.xdd.activiti.demo.service.TaskToService;
import org.activiti.bpmn.converter.EndEventXMLConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xdd
 * @date 2019/10/22
 */
@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskToService taskService;

    @RequestMapping("queryTask")
    public String queryTask() {
        taskService.queryTask();
        return "success";
    }

    @RequestMapping("completeTask")
    public String completeTask() {
        taskService.completeTask();
        return "success";
    }

    @RequestMapping("queryFinishedTask")
    public String queryFinishedTask() {
        taskService.queryFinishedTask();
        return "success";
    }

    @RequestMapping("queryFinishedTask2")
    public String queryFinishedTask2() {
        taskService.queryFinishedTask2();
        return "success";
    }

    @RequestMapping("queryActiveActivity")
    public String queryActiveActivity(HttpServletResponse response) throws Exception{
        taskService.queryActiveActivity(response);
        return "success";
    }

    @RequestMapping("getBpmnImg")
    public String getBpmnImg(HttpServletResponse response) throws Exception{
        taskService.getBpmnImg(response);
        return "success";
    }
}
