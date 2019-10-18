package com.java.xdd.activiti.demo.service;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @author xdd
 * @date 2019/10/18
 */
@Service
public class DemoService {

    private Logger logger = LoggerFactory.getLogger(DemoService.class);

    @Autowired
    private RepositoryService repositoryService;

    //部署流程定义
    public void demo() throws Exception{
        //流程图
        String path = "E:\\yidong\\xdd-activiti\\src\\main\\resources\\demo2.bpmn";

        InputStream inputStream = new FileInputStream(path);

        //流程部署
        Deployment deploy = repositoryService.createDeployment().name("审批流程")
                .addInputStream("demo2", inputStream).deploy();
        logger.info(deploy.getId());
    }


    public void findProcessDefinition(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc()
                .list();
        if(list != null && list.size()>0){
            for (ProcessDefinition definition : list) {
                logger.info("流程定义名称：{}", definition.getName());
                logger.info("流程定义的key：{}", definition.getKey());
                logger.info("部署对象ID：{}", definition.getDeploymentId());
                logger.info("资源名称png文件：{}", definition.getDiagramResourceName());
            }
        }
    }
}
