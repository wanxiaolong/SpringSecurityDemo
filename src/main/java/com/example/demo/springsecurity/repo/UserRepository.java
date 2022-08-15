package com.example.demo.springsecurity.repo;

import com.example.demo.springsecurity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 都是简单查询，JPA会通过解析方法名生成查询语句，
     * 只需要按规则声明方法名就好，不用在方法上写@Query
     */
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);

    @Query(value =
            "select distinct p.code from user u " +
            "join user_role ur on ur.user_id = u.id " +
            "join role r on r.id = ur.role_id " +
            "join role_permission rp on rp.role_id = r.id " +
            "join permission p on p.id = rp.permission_id " +
            "where u.username = :username", nativeQuery = true)
    List<String> findAuthoritiesBy(@Param("username") String username);
}
