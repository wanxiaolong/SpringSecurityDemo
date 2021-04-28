package com.example.demo.springsecurity.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 这些字段与数据表的列一一对应，因此可以不用在字段上写@Column
 */
@Entity
public class User {
    @Id
    private Integer id;

    private String username;

    private String password;

    private boolean enabled;

    private String role;

    private String authority;

    public String getAuthority() {
        return authority;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getRole() {
        return role;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
