package com.example.demo.springsecurity.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 这些字段与数据表的列一一对应，因此可以不用在字段上写@Column
 */
@Entity
@Data
public class User {
    @Id
    private Integer id;
    private String username;
    private String password;
    private String fullname;
    private boolean enabled;
}
