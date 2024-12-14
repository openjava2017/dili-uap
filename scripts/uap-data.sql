USE dili_uap;

INSERT INTO uap_sequence_key (`key`, `name`, `value`, `step`, `pattern`, `expired_on`, `version`)
VALUES ('BRANCH_ID', '组织机构ID', 1000, 20, NULL, NULL, 0);
INSERT INTO uap_sequence_key (`key`, `name`, `value`, `step`, `pattern`, `expired_on`, `version`)
VALUES ('USER_ID', '系统用户ID', 1000, 20, NULL, NULL, 0);
INSERT INTO uap_sequence_key (`key`, `name`, `value`, `step`, `pattern`, `expired_on`, `version`)
VALUES ('MENU_ID', '菜单资源ID', 1000, 20, NULL, NULL, 0);

INSERT INTO uap_user(name, user_name, telephone, email, gender, type, position, branch_id, superior_id, password, secret_key, state, mch_id, description, version, created_time, modified_time)
VALUES('root', '超级管理员', '13688182591', 'huanggang@diligrp.com', '1', 0, 0, 0, NULL, 'c612dfa824b54387bc7afc317683cbe2758a1b00', 'O1yJMqWtY8beGORGlhJlzg==', 1, 0, '超级管理员用于平台维护', 0, now(), now());