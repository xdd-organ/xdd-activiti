package com.java.xdd.activiti.demo.service;

import com.java.xdd.activiti.demo.bean.CusUserTask;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xdd
 * @date 2019/10/18
 */
@Service
public class CusUserTaskService {
    public List<CusUserTask> findByProcDefKey(String businessKey) {
        List<CusUserTask> res = new ArrayList<>();
        CusUserTask cusUserTask = new CusUserTask();
        cusUserTask.setCandidate_ids("001");
        cusUserTask.setGroup_id("01");
        cusUserTask.setTaskDefKey("taskDefKey");
        cusUserTask.setTaskType("1");

        res.add(cusUserTask);
        return res;
    }
}
