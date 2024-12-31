USE dili_uap;

DROP TABLE IF EXISTS `uap_sequence_key`;
CREATE TABLE `uap_sequence_key` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `key` VARCHAR(40) NOT NULL COMMENT 'KEY标识',
  `name` VARCHAR(80) NOT NULL COMMENT 'KEY名称',
  `value` BIGINT NOT NULl COMMENT '起始值',
  `step` TINYINT UNSIGNED NOT NULL COMMENT '步长',
  `pattern` VARCHAR(60) COMMENT 'ID格式', -- ORDER-%d{yyyyMMdd}-%n{4}
  `expired_on` DATE COMMENT '有效日期',
  `version` BIGINT NOT NULL COMMENT '数据版本',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_sequence_key_key` (`key`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_merchant`;
CREATE TABLE `uap_merchant` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `mch_id` BIGINT NOT NULL COMMENT '商户号',
  `parent_id` BIGINT NOT NULL COMMENT '父商户ID',
  `name` VARCHAR(80) NOT NULL COMMENT '商户名称',
  `address` VARCHAR(128) COMMENT '商户地址',
  `linkman` VARCHAR(40) COMMENT '联系人',
  `telephone` VARCHAR(20) COMMENT '电话号码',
  `params` JSON COMMENT '参数配置',
  `state` TINYINT UNSIGNED NOT NULL COMMENT '商户状态',
  `created_time` DATETIME COMMENT '创建时间',
  `modified_time` DATETIME COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_merchant_mchId` (`mch_id`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_branch`;
CREATE TABLE `uap_branch` (
  `id` BIGINT NOT NULL COMMENT '非自增主键ID',
  `mch_id` BIGINT NOT NULL COMMENT '商户号',
  `parent_id` BIGINT NOT NULL COMMENT '父级机构ID',
  `name` VARCHAR(80) NOT NULL COMMENT '名称',
  `path` VARCHAR(40) NOT NULL COMMENT '机构路径', -- 格式: id1,id2,id3,id4
  `type` TINYINT UNSIGNED NOT NULL COMMENT '类型', -- 业务部门，行政部门等
  `level` TINYINT UNSIGNED NOT NULL COMMENT '层级', -- 分支机构树层级
  `children` SMALLINT UNSIGNED NOT NULL COMMENT '子节点数量', -- 是否为叶子节点
  `state` TINYINT UNSIGNED NOT NULL COMMENT '状态', -- 保留字段
  `description` VARCHAR(128) COMMENT '备注',
  `version` INTEGER UNSIGNED NOT NULL COMMENT '数据版本号',
  `created_time` DATETIME COMMENT '创建时间',
  `modified_time` DATETIME COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_branch_code` (`code`) USING BTREE,
  KEY `idx_branch_parentId` (`parent_id`, `created_time`) USING BTREE,
  KEY `idx_branch_mchId` (`mch_id`, `level`) USING BTREE,
  KEY `idx_branch_name` (`name`) USING BTREE,
  KEY `idx_branch_createdTime` (`created_time`, `level`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_user`;
CREATE TABLE `uap_user` (
  `id` BIGINT NOT NULL COMMENT '非自增主键ID',
  `name` VARCHAR(40) NOT NULL COMMENT '用户账号',
  `user_name` VARCHAR(40) NOT NULL COMMENT '真实姓名',
  `telephone` VARCHAR(20) NOT NULL COMMENT '电话号码',
  `email` VARCHAR(40) NOT NULL COMMENT '邮箱地址',
  `gender` TINYINT UNSIGNED COMMENT '性别',
  `type` TINYINT UNSIGNED NOT NULL COMMENT '用户类型',
  `position` TINYINT UNSIGNED COMMENT '职位',
  `branch_id` BIGINT NOT NULL COMMENT '分支机构ID',
  `superior_id` BIGINT COMMENT '上级用户',
  `password` VARCHAR(40) NOT NULL COMMENT '交易密码',
  `secret_key` VARCHAR(60) NOT NULL COMMENT '安全密钥',
  `locked_time` DATETIME COMMENT '锁定时间',
  `session_id` VARCHAR(40) COMMENT '登录会话',
  `online_time` DATETIME COMMENT '登陆时间', -- 最近登录时间
  `state` TINYINT UNSIGNED NOT NULL COMMENT '账号状态', -- 待激活 正常 锁定 禁用
  `mch_id` BIGINT NOT NULL COMMENT '商户ID',
  `description` VARCHAR(128) COMMENT '备注',
  `version` INTEGER UNSIGNED NOT NULL COMMENT '数据版本号',
  `created_time` DATETIME COMMENT '创建时间',
  `modified_time` DATETIME COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_name` (`name`) USING BTREE,
  KEY `idx_user_userName` (`user_name`) USING BTREE,
  KEY `idx_user_telephone` (`telephone`) USING BTREE,
  KEY `idx_user_email` (`email`) USING BTREE,
  KEY `idx_user_branchId` (`branch_id`) USING BTREE,
  KEY `idx_user_superiorId` (`superior_id`) USING BTREE,
  KEY `idx_user_createdTime` (`created_time`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_user_role`;
CREATE TABLE `uap_user_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '账号ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `created_time` DATETIME COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_role_userId` (`user_id`) USING BTREE,
  KEY `idx_user_role_roleId` (`role_id`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_user_authority`;
CREATE TABLE `uap_user_authority` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `resource_id` BIGINT NOT NULL COMMENT '资源ID',
  `code` VARCHAR(40) COMMENT '资源编码', -- 冗余
  `type` TINYINT UNSIGNED NOT NULL COMMENT '资源类型',
  `bitmap` INTEGER NOT NULL COMMENT '子权限位图',
  `created_time` DATETIME COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_authority_userId` (`user_id`, `resource_id`, `type`) USING BTREE,
  KEY `idx_user_authority_resourceId` (`resource_id`, `type`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_role`;
CREATE TABLE `uap_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(60) NOT NULL COMMENT '角色名称',
  `mch_id` BIGINT NOT NULL COMMENT '商户ID',
  `description` VARCHAR(128) COMMENT '备注',
  `version` INTEGER UNSIGNED NOT NULL COMMENT '数据版本号',
  `created_time` DATETIME COMMENT '创建时间',
  `modified_time` DATETIME COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_role_name` (`name`, `mch_id`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_role_authority`;
CREATE TABLE `uap_role_authority` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `resource_id` BIGINT NOT NULL COMMENT '资源ID',
  `code` VARCHAR(40) COMMENT '资源编码', -- 冗余
  `type` TINYINT UNSIGNED NOT NULL COMMENT '资源类型',
  `bitmap` INTEGER NOT NULL COMMENT '子权限位图',
  `created_time` DATETIME COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_authority_roleId` (`role_id`, `resource_id`, `type`) USING BTREE,
  KEY `idx_role_authority_resourceId` (`resource_id`, `type`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_menu_resource`;
CREATE TABLE `uap_menu_resource` (
  `id` BIGINT NOT NULL COMMENT '非自增主键ID', -- 防止菜单ID和页面元素ID重复，不便于构建树形结构数据
  `parent_id` BIGINT NOT NULL COMMENT '父节点ID',
  `code` VARCHAR(40) NOT NULL COMMENT '菜单编码',
  `name` VARCHAR(60) NOT NULL COMMENT '菜单名称',
  `path` VARCHAR(40) NOT NULL COMMENT '菜单路径', -- 格式：id1,id2,id3,id4
  `level` TINYINT UNSIGNED NOT NULL COMMENT '菜单层级',
  `children` TINYINT UNSIGNED NOT NULL COMMENT '子节点数量', -- 用来标注目录/页面
  `uri` VARCHAR(60) NOT NULL COMMENT '相对路径', -- /user/page.do
  `icon` VARCHAR(60) COMMENT '菜单图标', -- DFS中的fileId
  `module_id` BIGINT NOT NULL COMMENT '所属模块ID',
  `description` VARCHAR(128) COMMENT '备注',
  `sequence` TINYINT UNSIGNED NOT NULL COMMENT '顺序号',
  `created_time` DATETIME COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_menu_resource_code` (`code`) USING BTREE,
  UNIQUE KEY `uk_menu_resource_path` (`path`) USING BTREE,
  KEY `idx_menu_resource_moduleId` (`module_id`, `level`) USING BTREE,
  KEY `idx_menu_resource_parentId` (`parent_id`, `sequence`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_menu_element`;
CREATE TABLE `uap_menu_element` (
  `id` BIGINT NOT NULL COMMENT '非自增主键ID', -- 防止菜单ID和页面元素ID重复，不便于构建树形结构数据
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  `name` VARCHAR(60) NOT NULL COMMENT '元素名称', -- 新增 修改 查询
  `offset` TINYINT UNSIGNED NOT NULL COMMENT '权限偏移量',
  `description` VARCHAR(128) COMMENT '备注',
  `sequence` TINYINT UNSIGNED NOT NULL COMMENT '顺序号',
  `created_time` DATETIME COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_page_element_menuId` (`menu_id`, `sequence`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_module`;
CREATE TABLE `uap_module` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `module_id` BIGINT NOT NULL COMMENT '模块号', -- 可以用来做license
  `name` VARCHAR(60) NOT NULL COMMENT '模块名称',
  `type` TINYINT UNSIGNED NOT NULL COMMENT '模块类型', -- Platform Native PC App MiniPro
  `uri` VARCHAR(60) NOT NULL COMMENT '绝对路径',
  `icon` VARCHAR(60) COMMENT '模块图标', -- DFS中fileId
  `description` VARCHAR(128) COMMENT '备注',
  `sequence` TINYINT UNSIGNED NOT NULL COMMENT '顺序号',
  `created_time` DATETIME COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_module_moduleId` (`module_id`) USING BTREE
) ENGINE=InnoDB;