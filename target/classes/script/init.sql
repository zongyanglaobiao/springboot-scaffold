drop  database if exists test_database;
create database  test_database;
use test_database;
create table test_user(
    id varchar(50) primary key,
    username varchar(50),
    passwd varchar(50)
)engine = innodb charset = UTF8;

insert into test_user(id, username, passwd)
values ('1','迪丽热巴','123'),
       ('2','古力娜扎','456'),
       ('3','欧阳娜娜','789');
