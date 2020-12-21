DROP DATABASE if exists `fishking` ;

CREATE DATABASE IF NOT EXISTS `fishking`
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

use mysql ;

SELECT HOST, USER, PASSWORD FROM user;


DROP USER if EXISTS 'fishking' ;


-- 사용자 계정 생성 'id'@'localhost' 이면 로컬에서만 접속 가능

CREATE USER 'fishking'@'%' IDENTIFIED BY 'fishking12#$';

GRANT ALL PRIVILEGES ON fishking.* TO 'fishking'@'%';


FLUSH PRIVILEGES;

use mysql ;

SELECT HOST, USER, PASSWORD FROM user;


