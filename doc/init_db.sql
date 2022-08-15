#建库
drop database if exists security;
create database security;
use security;
#1用户表
drop table if exists user;
create table user (
    id int auto_increment primary key,
    username varchar(20) not null,
    password varchar(20) not null,
    fullname varchar(20) not null,
    enabled bit not null default 1
);
alter table user add constraint uk_username unique (username);
insert into user(username, password, fullname) values ('zhangsan','1','张三');#普通用户
insert into user(username, password, fullname) values ('lisi','1','李四');#管理员


#2角色表
drop table if exists role;
create table role (
    id int auto_increment primary key,
    role_name varchar(20) not null,
    enabled bit not null default 1
);
insert into role(role_name) values ('ADMIN');#1是管理员
insert into role(role_name) values ('USER');#2是普通用户


#3用户角色表
drop table if exists user_role;
create table user_role (
    id int auto_increment primary key,
    user_id int not null,
    role_id int not null
);
alter table user_role add constraint fk_userrole_user foreign key (user_id) references user(id);
alter table user_role add constraint fk_userrole_role foreign key (role_id) references role(id);
insert into user_role(user_id, role_id) values (1, 2);#张三是普通用户
insert into user_role(user_id, role_id) values (2, 1),(2,2);#李四是管理员和普通用户


#4权限表
drop table if exists permission;
create table permission (
    id int auto_increment primary key,
    code varchar(20) not null,
    description varchar(20) not null
);
insert into permission(code, description) VALUES ('p1', '测试资源1');
insert into permission(code, description) VALUES ('p2', '测试资源2');


#5角色权限表
drop table if exists role_permission;
create table role_permission (
    id int auto_increment primary key,
    role_id int not null,
    permission_id int not null
);
alter table role_permission add constraint fk_rolepermission_role foreign key (role_id) references role(id);
alter table role_permission add constraint fk_rolepermission_permission foreign key (permission_id) references permission(id);
insert into role_permission(role_id, permission_id) VALUES (1, 1),(1,2);#角色1是管理员，有所有资源的权限
insert into role_permission(role_id, permission_id) VALUES (2, 1);#角色2是普通用户，只有p1
commit;