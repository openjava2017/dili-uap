USE dili_uap;

INSERT INTO uap_sequence_key (`key`, `name`, `value`, `step`, `pattern`, `expired_on`, `version`)
VALUES ('BRANCH_ID', '组织机构ID', 1000, 20, NULL, NULL, 0);
INSERT INTO uap_sequence_key (`key`, `name`, `value`, `step`, `pattern`, `expired_on`, `version`)
VALUES ('USER_ID', '系统用户ID', 1000, 20, NULL, NULL, 0);
INSERT INTO uap_sequence_key (`key`, `name`, `value`, `step`, `pattern`, `expired_on`, `version`)
VALUES ('MENU_ID', '菜单资源ID', 1000, 20, NULL, NULL, 0);

set @when = now();
INSERT INTO uap_user(id, name, user_name, telephone, email, gender, type, position, branch_id, superior_id, password, secret_key, state, mch_id, description, version, created_time, modified_time)
VALUES(1, 'root', '超级管理员', '13688182591', 'huanggang@diligrp.com', '1', 0, 0, 0, NULL, 'c612dfa824b54387bc7afc317683cbe2758a1b00', 'O1yJMqWtY8beGORGlhJlzg==', 1, 0, '超级管理员用于平台维护', 0, @when, @when);

INSERT INTO uap_module(module_id, name, type, uri, icon, description, sequence, created_time)
VALUES (20, "平台管理", 0, "https://uap.diligrp.com", null, "管理平台基础数据", 1, @when);
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, children, uri, icon, module_id, description, sequence, created_time)
VALUES (20, 0, "20", "平台管理", "20", 1, 4, "https://uap.diligrp.com", null, 20, "管理平台基础数据", 1, @when);
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, children, uri, icon, module_id, description, sequence, created_time)
VALUES (21, 20, "20-21", "商户管理", "20,21", 2, 0, "/merchant/boss.page", null, 20, "管理平台商户", 1, @when);
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, children, uri, icon, module_id, description, sequence, created_time)
VALUES (22, 20, "20-22", "模块管理", "20,22", 2, 0, "/module/boss.page", null, 20, "管理系统模块", 2, @when);
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, children, uri, icon, module_id, description, sequence, created_time)
VALUES (23, 20, "20-23", "菜单管理", "20,23", 2, 0, "/menu/boss.page", null, 20, "管理系统菜单", 3, @when);
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, children, uri, icon, module_id, description, sequence, created_time)
VALUES (24, 20, "10-14", "管理员管理", "20,24", 2, 0, "/admin/boss.page", null, 20, "管理系统管理员", 4, @when);

INSERT INTO uap_module(module_id, name, type, uri, icon, description, sequence, created_time)
VALUES (40, "权限管理", 1, "https://uap.diligrp.com", null, "管理系统权限", 2, @when);
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, children, uri, icon, module_id, description, sequence, created_time)
VALUES (40, 0, "40", "权限管理", "40", 1, 3, "https://uap.diligrp.com", null, 40, "管理系统权限", 2, @when);
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, children, uri, icon, module_id, description, sequence, created_time)
VALUES (41, 40, "40-41", "用户管理", "40,41", 2, 0, "/user/boss.page", null, 40, "管理系统用户", 1, @when);
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, children, uri, icon, module_id, description, sequence, created_time)
VALUES (42, 40, "40-42", "组织机构管理", "40,42", 2, 0, "/branch/boss.page", null, 40, "管理商户分支机构", 2, @when);
INSERT INTO uap_menu_resource(id, parent_id, code, name, path, level, children, uri, icon, module_id, description, sequence, created_time)
VALUES (43, 40, "40-43", "角色管理", "40,43", 2, 0, "/role/boss.page", null, 40, "管理系统角色", 3, @when);