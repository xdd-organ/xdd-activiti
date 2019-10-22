package com.java.xdd.activiti.demo.service;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
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
}
