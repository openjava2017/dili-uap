USE dili_uap;

DROP TABLE IF EXISTS `uap_merchant`;
CREATE TABLE `uap_merchant` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` BIGINT NOT NULL COMMENT '父商户ID',
  `code` VARCHAR(20) NOT NULL COMMENT '商户编码',
  `name` VARCHAR(80) NOT NULL COMMENT '商户名称',
  `address` VARCHAR(128) COMMENT '商户地址',
  `linkman` VARCHAR(40) COMMENT '联系人',
  `telephone` VARCHAR(20) COMMENT '电话号码',
  `state` TINYINT UNSIGNED NOT NULL COMMENT '商户状态',
  `created_time` DATETIME COMMENT '创建时间',
  `modified_time` DATETIME COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_merchant_code` (`code`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_branch`;
CREATE TABLE `uap_branch` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `mch_id` BIGINT NOT NULL COMMENT '商户ID',
  `parent_id` BIGINT NOT NULL COMMENT '父节点ID',
  `code` VARCHAR(40) NOT NULL COMMENT '编码', -- 格式: 分支机构ID-分支机构ID-分支机构ID
  `name` VARCHAR(80) NOT NULL COMMENT '名称',
  `type` TINYINT UNSIGNED NOT NULL COMMENT '类型', -- 分公司，业务部门，行政部门等
  `level` TINYINT UNSIGNED NOT NULL COMMENT '层级', -- 分支机构树层级
  `children` SMALLINT UNSIGNED NOT NULL COMMENT '子节点数量', -- 是否为叶子节点
  `state` TINYINT UNSIGNED NOT NULL COMMENT '状态', -- 软删除
  `version` INTEGER UNSIGNED NOT NULL COMMENT '数据版本号',
  `created_time` DATETIME COMMENT '创建时间',
  `modified_time` DATETIME COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_branch_code` (`code`) USING BTREE,
  KEY `idx_branch_parentId` (`parent_id`, `state`) USING BTREE,
  KEY `idx_branch_name` (`name`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_user`;
CREATE TABLE `uap_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(40) NOT NULL COMMENT '用户名',
  `user_name` VARCHAR(40) NOT NULL COMMENT '真实姓名',
  `telephone` VARCHAR(20) NOT NULL COMMENT '电话号码',
  `email` VARCHAR(40) NOT NULL COMMENT '邮箱地址',
  `gender` TINYINT UNSIGNED COMMENT '性别',
  `position` TINYINT UNSIGNED NOT NULL COMMENT '职位',
  `branch_id` BIGINT COMMENT '分支机构ID',
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
  `code` VARCHAR(20) COMMENT '资源编码', --冗余
  `type` TINYINT UNSIGNED NOT NULL COMMENT '资源类型',
  `permission` INTEGER NOT NULL COMMENT '资源权限',
  `created_time` DATETIME COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_authority_userId` (`user_id`) USING BTREE,
  KEY `idx_user_authority_resourceId` (`resource_id`) USING BTREE
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
  `code` VARCHAR(20) COMMENT '资源编码', --冗余
  `type` TINYINT UNSIGNED NOT NULL COMMENT '资源类型',
  `permission` INTEGER NOT NULL COMMENT '资源权限',
  `created_time` DATETIME COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_role_authority_roleId` (`role_id`) USING BTREE,
  KEY `idx_role_authority_resourceId` (`resource_id`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_menu_resource`;
CREATE TABLE `uap_menu_resource` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` VARCHAR(40) NOT NULL COMMENT '菜单编码', -- 格式：父菜单编码1-父菜单编码2-编码
  `name` VARCHAR(60) NOT NULL COMMENT '菜单名称',
  `parent_id` BIGINT NOT NULL COMMENT '父菜单',
  `level` TINYINT UNSIGNED NOT NULL COMMENT '菜单层级',
  `type` TINYINT UNSIGNED NOT NULL COMMENT '菜单类型-目录 页面', -- 是否有子节点
  `uri` VARCHAR(60) NOT NULL COMMENT '相对路径', --/user/page.do
  `icon` VARCHAR(60) COMMENT '菜单图标',
  `module_id` BIGINT NOT NULL COMMENT '所属模块ID',
  `sequence` TINYINT UNSIGNED NOT NULL COMMENT '顺序号',
  `created_time` DATETIME COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_menu_resource_code` (`code`) USING BTREE,
  KEY `idx_menu_resource_moduleId` (`module_id`, `sequence`) USING BTREE,
  KEY `idx_menu_resource_parentId` (`parent_id`, `sequence`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_page_element`;
CREATE TABLE `uap_page_element` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  `code` VARCHAR(40) NOT NULL COMMENT '元素编码', -- Add Modify List
  `name` VARCHAR(60) NOT NULL COMMENT '元素名称', -- 新增 修改 查询
  `offset` TINYINT UNSIGNED NOT NULL COMMENT '偏离量',
  `sequence` TINYINT UNSIGNED NOT NULL COMMENT '顺序号',
  PRIMARY KEY (`id`),
  KEY `idx_page_element_menuId` (`menu_id`, `sequence`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_module`;
CREATE TABLE `uap_module` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` VARCHAR(60) NOT NULL COMMENT '模块编码',
  `name` VARCHAR(60) NOT NULL COMMENT '模块名称',
  `type` TINYINT UNSIGNED NOT NULL COMMENT '模块类型', -- Native App MiniPro
  `uri` VARCHAR(60) NOT NULL COMMENT '绝对路径',
  `icon` VARCHAR(60) COMMENT '模块图标',
  `description` VARCHAR(128) COMMENT '备注',
  `sequence` TINYINT UNSIGNED NOT NULL COMMENT '顺序号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_module_code` (`code`) USING BTREE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `uap_scope_resource`;
CREATE TABLE `uap_scope_resource` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` VARCHAR(40) NOT NULL COMMENT '范围编码', -- 个人，所有
  `name` VARCHAR(60) NOT NULL COMMENT '范围名称', -- 个人，所有
  `sequence` TINYINT UNSIGNED NOT NULL COMMENT '顺序号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;