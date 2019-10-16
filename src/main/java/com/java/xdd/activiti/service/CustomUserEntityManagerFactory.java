package com.java.xdd.activiti.service;

import javax.annotation.Resource;

import com.java.xdd.activiti.manager.CustomUserEntityManager;
import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 自定义用户管理，用户session
 * 这里的属性需要与`spring-activiti.xml`中的该类的property一致
 * 不能使用identityService
 * @author Administrator
 *
 */
@Service
public class CustomUserEntityManagerFactory implements SessionFactory {
    
    @Autowired
    private CustomUserEntityManager customUserEntityManager;
    

    @Override
    public Class<?> getSessionType() {
        return UserIdentityManager.class;
    }

    @Override
    public Session openSession() {
        return customUserEntityManager;
    }


}
