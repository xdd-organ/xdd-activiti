package com.java.xdd.activiti.demo.service;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @author xdd
 * @date 2019/10/18
 */
@Service
public class DemoService {

    private Logger logger = LoggerFactory.getLogger(DemoService.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    //部署流程定义
    public void demo() throws Exception{
        //流程图
        String path = "E:\\yidong\\xdd-activiti\\src\\main\\resources\\demo3.bpmn";

        InputStream inputStream = new FileInputStream(path);

        //流程部署
        Deployment deploy = repositoryService.createDeployment().name("审批流程")
                .addClasspathResource("demo3.bpmn").deploy();
        logger.info(deploy.getId());
        logger.info("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());
    }


    //查询流程定义
    public void findProcessDefinition(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        if(list != null && list.size()>0){
            for (ProcessDefinition definition : list) {
                logger.info("流程定义名称：{}", definition.getName());
                logger.info("部署对象ID：{}", definition.getId());
            }
        }
    }

    //TODO
    public void getBpmnImg(HttpServletResponse response) throws Exception{
//        String s = "vacationRequest:1:15003";//流程定义id（act_re_procdef表id）
//        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId("20001").singleResult();
//        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionId(s).singleResult();
//        BpmnModel bpmnModel = repositoryService.getBpmnModel(s);
//        List<String> activeActivityIds = runtimeService.getActiveActivityIds("20001");
//        List<String> highLightedFlows = getHighLightedFlows(processDefinition, processInstance.getId());
//        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
////        InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds, highLightedFlows);
//        String activityFontName=processEngineConfiguration.getActivityFontName();
//        String labelFontName=processEngineConfiguration.getLabelFontName();
//        InputStream imageStream =diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds, highLightedFlows,activityFontName,labelFontName,null, null, 1.0);
//        // 输出资源内容到相应对象
//        byte[] b = new byte[1024];
//        int len;
//        while ((len = imageStream.read(b, 0, 1024)) != -1) {
//            response.getOutputStream().write(b, 0, len);
//        }

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("20001").singleResult();
        String processDefinitionId = processInstance.getProcessDefinitionId();

        //使用宋体
        String fontName = "宋体";
        //获取BPMN模型对象
        BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
        //获取流程实例当前的节点，需要高亮显示
        List<String> currentActs = Collections.EMPTY_LIST;
        if (processInstance != null)
            currentActs = runtimeService.getActiveActivityIds(processInstance.getId());

        InputStream imageStream = processEngineConfiguration
                .getProcessDiagramGenerator()
                .generateDiagram(model, "png", currentActs, new ArrayList<String>(),
                        fontName, fontName, fontName, null, 1.0);

        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    //启动流程实例
    public void startProcess() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("employeeName", "admin");
        params.put("numberOfDays", "3");
        params.put("vacationMotivation", "原因是：。。。。");
        ProcessInstance myProcess_1 = runtimeService.startProcessInstanceById("vacationRequest:1:15003", params);

        logger.info("部署DeploymentId：{}", myProcess_1.getDeploymentId());
        logger.info("部署name：{}", myProcess_1.getName());

    }

    // 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
    public void getHistoryTask() {
        String s = "vacationRequest:1:15003";//流程定义id（act_re_procdef表id）
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processDefinitionId(s)
                .orderByHistoricActivityInstanceId().asc().list();
        int index = 1;
        for (HistoricActivityInstance activityInstance : list) {
            logger.info("第[" + index + "]个已执行节点=" + activityInstance.getActivityId() + " : " +activityInstance.getActivityName());
            index++;
        }
    }

    //查询用户组任务
    public void queryTask() {
        //根据用户组查询
        List<Task> management = taskService.createTaskQuery().taskCandidateGroup("management").list();
        //根据指定用户查询
//        List<Task> management = taskService.createTaskQuery().taskAssignee("admin").list();
        for (Task task : management) {
            logger.info("Task available: " + task.getName() + ";id=" + task.getId());
        }
    }

    //完成任务
    public void completeTask() {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("management").list();
        Task task = tasks.get(0);
        Map<String, Object> taskVariables = new HashMap<String, Object>();
        taskVariables.put("vacationApproved", "false");
        taskVariables.put("managerMotivation", "We have a tight deadline!");
        taskService.complete(task.getId(), taskVariables);
    }





    private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinition, String processInstanceId) {
        List<String> highLightedFlows = new ArrayList<String>();
        List<HistoricActivityInstance> historicActivityInstances = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().asc().list();

        List<String> historicActivityInstanceList = new ArrayList<String>();
        for (HistoricActivityInstance hai : historicActivityInstances) {
            historicActivityInstanceList.add(hai.getActivityId());
        }

        // add current activities to list
        List<String> highLightedActivities = runtimeService.getActiveActivityIds(processInstanceId);
        historicActivityInstanceList.addAll(highLightedActivities);

        // activities and their sequence-flows
        for (ActivityImpl activity : processDefinition.getActivities()) {
            int index = historicActivityInstanceList.indexOf(activity.getId());

            if (index >= 0 && index + 1 < historicActivityInstanceList.size()) {
                List<PvmTransition> pvmTransitionList = activity
                        .getOutgoingTransitions();
                for (PvmTransition pvmTransition : pvmTransitionList) {
                    String destinationFlowId = pvmTransition.getDestination().getId();
                    if (destinationFlowId.equals(historicActivityInstanceList.get(index + 1))) {
                        highLightedFlows.add(pvmTransition.getId());
                    }
                }
            }
        }
        return highLightedFlows;
    }

}
