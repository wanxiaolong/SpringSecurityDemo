package com.example.demo.springsecurity.repo;

import com.example.demo.springsecurity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 都是简单查询，JPA会通过解析方法名生成查询语句，
     * 只需要按规则声明方法名就好，不用在方法上写@Query
     */

    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
}
