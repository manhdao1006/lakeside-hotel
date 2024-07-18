package com.manodrye.lakeside_hotel_server.service;

import java.util.List;

import com.manodrye.lakeside_hotel_server.entity.RoleEntity;
import com.manodrye.lakeside_hotel_server.entity.UserEntity;

public interface IRoleService {

    List<RoleEntity> getRoles();

    RoleEntity createRole(RoleEntity theRole);

    void deleteRole(Long roleId);

    RoleEntity findByName(String name);

    UserEntity removeUserFromRole(Long userId, Long roleId);

    UserEntity assignRoleToUser(Long userId, Long roleId);

    RoleEntity removeAllUsersFromRole(Long roleId);
}
