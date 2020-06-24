INSERT INTO report.report_data (id, sql_content, file_name, file_type, create_date, update_date,
                                               is_valid, task_id, data_source)
VALUES (1, 'select  1 as 标题1, 2 as 标题2, now() as 时间 from dual', '测试', 'xls', now(), now(), '1', 1, 'DATA');
INSERT INTO report_task (id, create_date, update_date, is_valid, cron, name) VALUES (1, now(), now(), '1', '0 0 8 ?  * * *', '测试');
INSERT INTO report.report_target (id, create_date, update_date, is_valid, target_type, target_address, message, task_id) VALUES (1, now(), now(), '1', 'mail', 'yz576965161@126.com', '测试', 1);