package com.java.xdd.activiti.demo.service;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.engine.*;
import org.activiti.engine.history.*;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xdd
 * @date 2019/10/22
 */
@Service
public class TaskToService {

    private Logger logger = LoggerFactory.getLogger(TaskToService.class);

    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    public void queryTask() {
        //根据指定用户查询
        List<Task> management = taskService.createTaskQuery().taskAssignee("kermit").list();
        for (Task task : management) {
            logger.info("Task available: " + task.getName() + ";id=" + task.getId());
        }
    }

    //完成任务
    public void completeTask() {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("kermit").list();
        Task task = tasks.get(0);
        taskService.complete(task.getId());
    }

    public void queryFinishedTask() {
        HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        /** 根据历史流程实例ID查询历史任务实例 */
        List<HistoricTaskInstance> htiLists = taskInstanceQuery
                .processDefinitionId("process:1:40004")
//                .processInstanceId("42501")
                .finished()
                .orderByTaskCreateTime().asc().list();
        for (HistoricTaskInstance htiList : htiLists) {
            logger.info("完成的实例id：{}，完成时间：{}", htiList.getId(), htiList.getEndTime());
        }
    }

    public void queryFinishedTask2() {
        HistoricProcessInstanceQuery taskInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        /** 根据历史流程实例ID查询历史任务实例 */
        List<HistoricProcessInstance> htiLists = taskInstanceQuery
                .processDefinitionId("process:1:40004")
                .finished()
                .orderByProcessInstanceId().asc().list();
        for (HistoricProcessInstance htiList : htiLists) {
            logger.info("完成的实例id：{}，完成时间：{}", htiList.getId(), htiList.getEndTime());
        }
    }

    //获取当前流程实例所有的活动的节点，并在活动节点标识
    public void queryActiveActivity(HttpServletResponse response) throws Exception{
        List<String> activityIds = runtimeService.getActiveActivityIds("42501");//act_hi_procinst表id
        logger.info("所有的活动的节点:{}", activityIds);

        BpmnModel bpmnModel = repositoryService.getBpmnModel("process:1:40004");//act_re_procdef表id

        /** 获取流程定义图片对应的输入流 */
        InputStream inputStream = repositoryService.getProcessDiagram("process:1:40004");
        /** 流程定义图片对应的输入流转换成缓冲流图片 */
        BufferedImage iamge = ImageIO.read(inputStream);
        /** 获取图片的画笔 */
        Graphics2D g = (Graphics2D) iamge.getGraphics();
        /** 设置画笔颜色 */
        g.setColor(Color.RED);
        /** 设置画笔粗细 */
        g.setStroke(new BasicStroke(3.0f));
        /** 设置消除锯齿 */
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        for (String activityId : activityIds) {
            GraphicInfo gi = bpmnModel.getGraphicInfo(activityId);
            logger.info("x:{},y:{},宽：{}，高：{}", gi.getX(), gi.getY(), gi.getWidth(), gi.getHeight());
            /** 绘制圆角矩形 */
            g.drawRoundRect((int)gi.getX(), (int)gi.getY(),
                    (int)gi.getWidth(), (int)gi.getHeight(), 10, 10);
        }

        /** 设置浏览器响应数据类型 */
        response.setContentType("image/png");
        /** 向浏览器输出图片 */
        ImageIO.write(iamge, "png", response.getOutputStream());
    }

    public void getBpmnImg(HttpServletResponse response) throws Exception {
        //当前正在执行任务
        List<String> currentActs = runtimeService.getActiveActivityIds("50001");//act_hi_procinst表id

        //历史完成的任务
        List<HistoricActivityInstance> finishedInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId("50001").finished().list();
        for (HistoricActivityInstance hai : finishedInstances) {
            currentActs.add(hai.getActivityId());
        }

        //已执行过的箭头
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition("process:1:40004");
        List<String> highLightedFlows = new ArrayList<>();
        getHighLightedFlows(processDefinitionEntity.getActivities(), highLightedFlows, currentActs);

        logger.info("所有的活动的节点:{}", currentActs);

        //获取BPMN模型对象
        BpmnModel model = repositoryService.getBpmnModel("process:1:40004");//act_re_procdef表id

        //使用宋体
        String fontName = "宋体";

        InputStream imageStream = processEngineConfiguration
                .getProcessDiagramGenerator()
                .generateDiagram(model, "png", currentActs, highLightedFlows,
                        fontName, fontName, fontName, null, 1.0);


        // 输出资源内容到相应对象
        BufferedImage iamge = ImageIO.read(imageStream);

        response.setContentType("image/png");
        ImageIO.write(iamge, "png", response.getOutputStream());
    }

    private void getHighLightedFlows(List<ActivityImpl> activityList, List<String> highLightedFlows, List<String> historicActivityInstanceList) {
        for (ActivityImpl activity : activityList) {
            if (activity.getProperty("type").equals("subProcess")) {
                // get flows for the subProcess
                getHighLightedFlows(activity.getActivities(), highLightedFlows, historicActivityInstanceList);
            }

            if (historicActivityInstanceList.contains(activity.getId())) {
                List<PvmTransition> pvmTransitionList = activity.getOutgoingTransitions();
                for (PvmTransition pvmTransition : pvmTransitionList) {
                    String destinationFlowId = pvmTransition.getDestination().getId();
                    if (historicActivityInstanceList.contains(destinationFlowId)) {
                        highLightedFlows.add(pvmTransition.getId());
                    }
                }
            }
        }
    }
}
