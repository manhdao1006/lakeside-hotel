package com.manodrye.lakeside_hotel_server.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity> users = new HashSet<>();

    public void assignRoleToUser(UserEntity userEntity) {
        userEntity.getRoles().add(this);
        this.getUsers().add(userEntity);
    }

    public void removeUserFromRole(UserEntity userEntity) {
        userEntity.getRoles().remove(this);
        this.getUsers().remove(userEntity);
    }

    public void removeAllUserFromRole() {
        if (this.getUsers() != null) {
            List<UserEntity> roleUsers = this.getUsers().stream().toList();
            roleUsers.forEach(this :: removeUserFromRole);
        }
    }

    public String getName() {
        return name != null ? name : "";
    }
}
