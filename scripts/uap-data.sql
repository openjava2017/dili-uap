USE dili_uap;

INSERT INTO uap_sequence_key (`key`, `name`, `value`, `step`, `pattern`, `expired_on`, `version`)
VALUES ('BRANCH_ID', '组织机构ID', 1000, 20, NULL, NULL, 0);
INSERT INTO uap_sequence_key (`key`, `name`, `value`, `step`, `pattern`, `expired_on`, `version`)
VALUES ('USER_ID', '系统用户ID', 1000, 20, NULL, NULL, 0);
INSERT INTO uap_sequence_key (`key`, `name`, `value`, `step`, `pattern`, `expired_on`, `version`)
VALUES ('MENU_ID', '菜单资源ID', 1000, 20, NULL, NULL, 0);

INSERT INTO uap_user(name, user_name, telephone, email, gender, type, position, branch_id, superior_id, password, secret_key, state, mch_id, description, version, created_time, modified_time)
VALUES('root', '超级管理员', '13688182591', 'huanggang@diligrp.com', '1', 0, 0, 0, NULL, 'c612dfa824b54387bc7afc317683cbe2758a1b00', 'O1yJMqWtY8beGORGlhJlzg==', 1, 0, '超级管理员用于平台维护', 0, now(), now());

INSERT INTO uap_module(module_id, name, type, uri, icon, description, sequence, created_time)
VALUES (1, "平台管理", 0, "https://uap.diligrp.com", null, "管理平台基础数据", 1, now());                                                                                                            VALUES (1, "平台管理", 0, "https://uap.diligrp.com", null, "管理平台基础数据", 1, now());
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, child, uri, icon, module_id, description, sequence, created_time)
VALUES (10, 0, "10", "平台管理", "10", 1, 4, "https://uap.diligrp.com", null, 1, "管理平台基础数据", 1, now());
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, child, uri, icon, module_id, description, sequence, created_time)
VALUES (11, 10, "10-11", "商户管理", "10,11", 2, 0, "/merchant/boss.page", null, 1, "管理平台商户", 1, now());
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, child, uri, icon, module_id, description, sequence, created_time)
VALUES (12, 10, "10-12", "模块管理", "10,12", 2, 0, "/module/boss.page", null, 1, "管理系统模块", 2, now());
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, child, uri, icon, module_id, description, sequence, created_time)
VALUES (13, 10, "10-13", "菜单管理", "10,13", 2, 0, "/menu/boss.page", null, 1, "管理系统菜单", 3, now());
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, child, uri, icon, module_id, description, sequence, created_time)
VALUES (14, 10, "10-14", "管理员管理", "10,14", 2, 0, "/admin/boss.page", null, 1, "管理系统管理员", 4, now());

INSERT INTO uap_module(module_id, name, type, uri, icon, description, sequence, created_time)
VALUES (2, "权限管理", 1, "https://uap.diligrp.com", null, "管理系统权限", 2, now());
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, child, uri, icon, module_id, description, sequence, created_time)
VALUES (20, 0, "20", "权限管理", "20", 1, 3, "https://uap.diligrp.com", null, 2, "管理系统权限", 2, now());
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, child, uri, icon, module_id, description, sequence, created_time)
VALUES (21, 20, "20-21", "用户管理", "20,21", 2, 0, "/user/boss.page", null, 2, "管理系统用户", 1, now());
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, child, uri, icon, module_id, description, sequence, created_time)
VALUES (22, 20, "20-22", "组织机构管理", "20,22", 2, 0, "/branch/boss.page", null, 2, "管理商户分支机构", 2, now());
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, child, uri, icon, module_id, description, sequence, created_time)
VALUES (23, 20, "20-23", "角色管理", "20,23", 2, 0, "/role/boss.page", null, 2, "管理系统角色", 3, now());