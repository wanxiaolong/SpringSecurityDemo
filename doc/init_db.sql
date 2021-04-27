#建库
create database if not exists security;
use security;
#建表
drop table user;
create table user (
  id int auto_increment primary key,
  username varchar(20) not null,
  password varchar(20) not null,
  role varchar(20) not null default 'USER',
  enabled bit not null default 1
);
#唯一键
alter table user add constraint uk_username unique (username);
#普通用户
insert into user(username, `password`, role) values('spring','1','USER');
#管理员
insert into user(username, `password`, role) values('admin','1','ADMIN');
commit;
