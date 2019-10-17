package com.java.xdd.activiti.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class CustomGroupEntityManager extends GroupEntityManager {

    private Logger logger = LoggerFactory.getLogger(CustomGroupEntityManager.class);

    public boolean hasUser(String userId) {
        return true;
    }
    
    public List<Group> findGroupsByUser(String userId) {
        logger.info("CustomUserEntityManager  findGroupsByUser userId:" + userId);
        if (userId == null)
            return null;
        boolean hasUser = hasUser(userId);
        if (!hasUser)return null;
        List<Map<String,Object>> roleList = getRoleList(userId);
        List<Group> groupEntitys = new ArrayList<Group>();
        if(null != roleList) {
            for (Map<String, Object> role : roleList) {
                String roleCode = null == role.get("roleCode")?"":role.get("roleCode").toString();
                String roleName = null == role.get("roleName")?"":role.get("roleName").toString();
                GroupEntity groupEntity = toActivitiGroup(roleCode,roleName);
                groupEntitys.add(groupEntity);
            }
        }
        return groupEntitys;
    }

    public static GroupEntity toActivitiGroup(String roleCode,String roleName) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setRevision(1);
        groupEntity.setType("assignment");
        groupEntity.setId(roleCode);
        groupEntity.setName(roleName);
        return groupEntity;
    }
    
    public List<Map<String, Object>> getRoleList(String userId) {
//      List<Map<String,Object>> roleList = new ArrayList<>();
//      List<MisUserRole> misUserRoleList = misUserRoleDao.findByUserId(userId);
//      for (MisUserRole misUserRole : misUserRoleList) {
//          final String roleId = misUserRole.getRoleId();
//          boolean isExitRole = misRoleDao.findRoleById(roleId) != null && misRoleDao.findRoleById(roleId).size()>0;
//          MisRole role = isExitRole ? misRoleDao.findRoleById(roleId).get(0) : null;
//          Map<String, Object> roleMap = new HashMap<>();
//          if(role != null){
//              roleMap.put("roleCode", role.getCode());
//              roleMap.put("roleName", role.getName());
//              
//          }
//          roleList.add(roleMap);
//      }
//      return roleList;
        List<Map<String,Object>> roleList = new ArrayList<>();
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("roleCode", "role_code");
        roleMap.put("roleName", "角色名称");
        roleList.add(roleMap);
        return roleList;
    }
}
