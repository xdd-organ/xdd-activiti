package com.java.xdd.activiti.demo.controller;

import com.java.xdd.activiti.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
