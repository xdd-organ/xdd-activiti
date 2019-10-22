package com.java.xdd.activiti.config;

import com.java.xdd.activiti.manager.CustomGroupEntityManager;
import com.java.xdd.activiti.manager.CustomUserEntityManager;
import com.java.xdd.activiti.service.CustomGroupEntityManagerFactory;
import com.java.xdd.activiti.service.CustomUserEntityManagerFactory;
import org.activiti.engine.*;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xdd
 * @date 2019/10/16
 */
@Configuration
public class ActivitiConfig {

    @Autowired
    private CustomUserEntityManagerFactory userEntityManagerFactory;

    @Autowired
    private CustomGroupEntityManagerFactory groupEntityManagerFactory;

    @Bean
    public SpringProcessEngineConfiguration engineConfiguration() {
        SpringProcessEngineConfiguration engineConfiguration = new SpringProcessEngineConfiguration();

        engineConfiguration.setDbIdentityUsed(false);//是否创建identity用户相关表
        engineConfiguration.setDatabaseSchemaUpdate("false");//自动创建数据表
        engineConfiguration.setJobExecutorActivate(false);//是否激活Activiti的任务调度
        engineConfiguration.setProcessDefinitionCacheLimit(10);
        engineConfiguration.setActivityFontName("宋体");//生成流程图的字体
        engineConfiguration.setLabelFontName("宋体");//生成流程图的字体
        engineConfiguration.setCreateDiagramOnDeploy(true);//在部署时生成流程图

        engineConfiguration.setDataSource(dataSource());
        engineConfiguration.setTransactionManager(transactionManager());

        engineConfiguration.setCustomSessionFactories(factoryList());//指定自定义用户管理工厂，包括：用户管理和组管理

        return engineConfiguration;
    }

    @Bean
    public ProcessEngineFactoryBean engineFactory() {
        ProcessEngineFactoryBean engineFactoryBean = new ProcessEngineFactoryBean();
        engineFactoryBean.setProcessEngineConfiguration(engineConfiguration());
        return engineFactoryBean;
    }

//    activiti的7大服务接口
    @Bean
    public RepositoryService repositoryService() throws Exception {
        RepositoryService repositoryService = engineFactory().getObject().getRepositoryService();
        return repositoryService;
    }
    @Bean
    public RuntimeService runtimeService() throws Exception {
        RuntimeService runtimeService = engineFactory().getObject().getRuntimeService();
        return runtimeService;
    }
    @Bean
    public TaskService taskService() throws Exception {
        TaskService taskService = engineFactory().getObject().getTaskService();
        return taskService;
    }
    @Bean
    public FormService formService() throws Exception {
        FormService formService = engineFactory().getObject().getFormService();
        return formService;
    }
    @Bean
    public HistoryService historyService() throws Exception {
        HistoryService historyService = engineFactory().getObject().getHistoryService();
        return historyService;
    }
    @Bean
    public ManagementService managementService() throws Exception {
        ManagementService managementService = engineFactory().getObject().getManagementService();
        return managementService;
    }
//    @Bean
//    public IdentityService identityService() throws Exception {
//        IdentityService identityService = engineFactory().getObject().getIdentityService();
//        return identityService;
//    }


    //数据库连接池
    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/activiti");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

    //事物管理
    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    //
    @Bean
    public List<SessionFactory> factoryList() {
        List<SessionFactory> factoryList = new ArrayList<>();
        factoryList.add(userEntityManagerFactory);
        factoryList.add(groupEntityManagerFactory);
        return factoryList;
    }

}
