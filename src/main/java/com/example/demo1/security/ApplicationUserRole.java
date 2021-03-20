package com.example.demo1.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo1.security.ApplicationUserPermision.*;

public enum ApplicationUserRole {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ,STUDENT_WRITE)),
    ADMINTRAINEE(Sets.newHashSet(COURSE_READ, STUDENT_READ));

    private final Set<ApplicationUserPermision> permissions;

    ApplicationUserRole(Set<ApplicationUserPermision> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermision> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority>getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions=getPermissions().stream()

                .map(permission->new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE"+this.name()));
        return permissions;

    }
}
