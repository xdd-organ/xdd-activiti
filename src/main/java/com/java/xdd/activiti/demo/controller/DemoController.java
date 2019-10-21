package com.java.xdd.activiti.demo.controller;

import com.java.xdd.activiti.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xdd
 * @date 2019/10/18
 */
@RestController
@RequestMapping("demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @RequestMapping("demo")
    public Object demo () throws Exception {
        demoService.demo();
        return "success";
    }

    @RequestMapping("findProcessDefinition")
    public Object findProcessDefinition () throws Exception {
        demoService.findProcessDefinition();
        return "success";
    }

    @RequestMapping("getBpmnImg")
    public Object getBpmnImg (HttpServletResponse response) throws Exception {
        demoService.getBpmnImg(response);
        return "success";
    }

    @RequestMapping("startProcess")
    public Object startProcess() throws Exception {
        demoService.startProcess();
        return "success";
    }

    @RequestMapping("getHistoryTask")
    public Object getHistoryTask() throws Exception {
        demoService.getHistoryTask();
        return "success";
    }


    @RequestMapping("queryTask")
    public Object queryTask() throws Exception {
        demoService.queryTask();
        return "success";
    }

    @RequestMapping("completeTask")
    public Object completeTask() throws Exception {
        demoService.completeTask();
        return "success";
    }
}
