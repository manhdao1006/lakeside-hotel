package com.manodrye.lakeside_hotel_server.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.manodrye.lakeside_hotel_server.entity.RoleEntity;
import com.manodrye.lakeside_hotel_server.entity.UserEntity;
import com.manodrye.lakeside_hotel_server.exception.RoleAlreadyExistException;
import com.manodrye.lakeside_hotel_server.exception.UserAlreadyExistsException;
import com.manodrye.lakeside_hotel_server.repository.RoleRepository;
import com.manodrye.lakeside_hotel_server.repository.UserRepository;
import com.manodrye.lakeside_hotel_server.service.IRoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public List<RoleEntity> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public RoleEntity createRole(RoleEntity theRole) {
        String roleName = "ROLE_" + theRole.getName().toUpperCase();
        RoleEntity role = new RoleEntity(roleName);
        if (roleRepository.existsByName(role)) {
            throw new RoleAlreadyExistException(theRole.getName() + " role already exists");
        }
        
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        this.removeAllUsersFromRole(roleId);
        roleRepository.deleteById(roleId);
    }

    @Override
    public RoleEntity findByName(String name) {
        return roleRepository.findByName(name).get();
    }

    @Override
    public UserEntity removeUserFromRole(Long userId, Long roleId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        Optional<RoleEntity> role = roleRepository.findById(roleId);
        if (role.isPresent() && role.get().getUsers().contains(user.get())) {
            role.get().removeUserFromRole(user.get());
            roleRepository.save(role.get());
            
            return user.get();
        }

        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public UserEntity assignRoleToUser(Long userId, Long roleId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        Optional<RoleEntity> role = roleRepository.findById(roleId);
        if (user.isPresent() && user.get().getRoles().contains(role.get())) {
            throw new UserAlreadyExistsException(user.get().getFirstName() + " is already assigned to the " + role.get().getName() + " role");
        }

        if (role.isPresent()) {
            role.get().assignRoleToUser(user.get());
            roleRepository.save(role.get());
        }

        return user.get();
    }

    @Override
    public RoleEntity removeAllUsersFromRole(Long roleId) {
        Optional<RoleEntity> role = roleRepository.findById(roleId);
        role.ifPresent(RoleEntity::removeAllUsersFromRole);

        return roleRepository.save(role.get());
    }

}
