package com.java.xdd.activiti.demo.service;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xdd
 * @date 2019/10/22
 */
@Service
public class DeploymentService {

    private Logger logger = LoggerFactory.getLogger(DeploymentService.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    public void deploy() throws Exception{
        String path = "E:\\yidong\\xdd-activiti\\src\\main\\resources\\process.bpmn";
        String png = "E:\\yidong\\xdd-activiti\\src\\main\\resources\\process.png";
//        InputStream inputStream = new FileInputStream(path);
//        InputStream fileInputStream = new FileInputStream(png);

        repositoryService.createDeployment().name("deployment_name")
//                .addInputStream("active_name", inputStream)//bpmn文件
//                .addInputStream("png_name", fileInputStream)//流程图
                .addClasspathResource("process.bpmn20.xml")
                .addClasspathResource("process.png")
                .deploy();
    }

    //启动流程实例
    public void startProcess() throws Exception {
        ProcessInstance myProcess_1 = runtimeService.startProcessInstanceById("process:1:40004");
        logger.info("部署DeploymentId：{}", myProcess_1.getDeploymentId());
        logger.info("部署name：{}", myProcess_1.getName());
    }


}
