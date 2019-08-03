use forum;
set names utf8;

drop table if exists `user`;
set character_set_client=utf8;
create table `user` (
 id int(11) not null auto_increment,
 username varchar(50) default null,
 `password` varchar(50) default null,
 salt varchar(50) default null,
 email varchar(100) default null,
 `type` int(11) default null comment '0-普通用户；1-超级用户；2-版主',
 `status` int (11) default null comment '0-未激活；1-已激活；',
 activation_code varchar(100) default null,
 header_url varchar(200) default null,
 create_time timestamp null default null comment '第一个 null 表示接受 null 值',
 primary key(id),
 key `index_username` (`username`(20)),
 key index_email (email(20))
) engine=InnoDB auto_increment=101 default charset=utf8;



drop table if exists `discuss_post`;
set character_set_client=utf8;
create table discuss_post (
  id int(11) not null auto_increment,
  user_id int(11) default null,
  title varchar(200) default null,
  content text default null,
  `type` int(11) default null comment '0-普通；1-置顶；',
  `status` int(11) default null comment '0-正常；1-精华；2-拉黑；',
  create_time timestamp null default null,
  comment_count int(11) default null,
  score double default null,
  primary key (id),
  key index_user_id (user_id)
) engine=InnoDB default charset=utf8;

DROP TABLE IF EXISTS `login_ticket`;
SET character_set_client = utf8mb4 ;
CREATE TABLE `login_ticket` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `ticket` varchar(45) NOT NULL,
  `status` int(11) DEFAULT '0' COMMENT '0-有效; 1-无效;',
  `expired` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_ticket` (`ticket`(20))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


