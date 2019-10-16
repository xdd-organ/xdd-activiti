package com.java.xdd.activiti.service;

import com.java.xdd.activiti.manager.CustomGroupEntityManager;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 自定义用户管理，用户组session
 * 这里的属性需要与spring-activiti.xml中的该类的property一致
 * 不能使用identityService
 * @author Administrator
 *
 */
@Service
public class CustomGroupEntityManagerFactory implements SessionFactory {
    
    @Autowired
    private CustomGroupEntityManager customGroupEntityManager;
    
    
    @Override
    public Class<?> getSessionType() {
        return GroupIdentityManager.class;
    }

    @Override
    public Session openSession() {
        return customGroupEntityManager;
    }

}