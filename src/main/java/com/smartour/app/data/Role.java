package com.smartour.app.data;

public enum Role {
    USER("user"), ADMIN("admin");

    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

}
