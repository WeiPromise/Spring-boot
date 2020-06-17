-- schema data
CREATE TABLE IF NOT EXISTS `roles` (
`roles_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
`roles_name` varchar(20) NOT NULL COMMENT '角色名',
`status` int(1) NOT NULL DEFAULT '1' COMMENT '角色权限表',
 PRIMARY KEY (`roles_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `users` (
`user_id` int(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
`user_name` varchar(30) NOT NULL COMMENT '用户名',
`user_password` varchar(50) NOT NULL COMMENT '用户密码',
`user_email` varchar(50) NOT NULL COMMENT '用户邮箱',
`status` int(1) NOT NULL DEFAULT '1' COMMENT '此条数据是否有效',
`user_type` int(5) NOT NULL DEFAULT '1' COMMENT '用户类型0为',
PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;