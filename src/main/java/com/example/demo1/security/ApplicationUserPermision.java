package com.example.demo1.security;

public enum ApplicationUserPermision {
    STUDENT_READ("student:read"),
    STUDENT_WRITE( "student:write"),
    COURSE_READ ("course:read"),
    COURSE_WRITE("course:write");

    private final String permission;

    ApplicationUserPermision(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
