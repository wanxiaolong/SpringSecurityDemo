#建库
drop database if exists security;
create database security;
use security;
#建表。为了简便，把以下的多个表设计成一个表：
#1用户<---->2用户角色<---->3角色<---->4角色权限<---->5权限
#这几个表都是关联的
drop table if exists user;
create table user (
      id int auto_increment primary key,
      username varchar(20) not null,
      password varchar(20) not null,
      role varchar(20) not null default 'USER',#角色信息
      authority varchar(20) not null default 'USER',#权限信息
      enabled bit not null default 1
);
#唯一键
alter table user add constraint uk_username unique (username);
#普通用户
insert into user(username, `password`, role, authority) values('spring','1','USER','USER');
#管理员
insert into user(username, `password`, role, authority) values('admin','1','ADMIN','ADMIN');
commit;