package org.pozvezd.springuserapi.service;

import org.pozvezd.springuserapi.exception.RoleNotFoundException;
import org.pozvezd.springuserapi.model.entity.RoleEntity;
import org.pozvezd.springuserapi.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleEntity findRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + roleName));
    }

    public RoleEntity getRoleReference(UUID roleUuid) {
        return roleRepository.getReferenceById(roleUuid);
    }

    public boolean roleExists(String roleName) {
        return roleRepository.existsByRoleName(roleName);
    }
}
