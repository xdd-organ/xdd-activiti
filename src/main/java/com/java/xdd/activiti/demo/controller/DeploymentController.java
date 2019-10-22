package com.java.xdd.activiti.demo.controller;

import com.java.xdd.activiti.demo.service.DeploymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author xdd
 * @date 2019/10/22
 */
@RestController
@RequestMapping("deployment")
public class DeploymentController {

    @Autowired
    private DeploymentService deploymentService;

    @RequestMapping("deploy")
    public String deploy() throws Exception{
        deploymentService.deploy();
        return "success";
    }

    @RequestMapping("startProcess")
    public String startProcess() throws Exception{
        deploymentService.startProcess();
        return "success";
    }
}
