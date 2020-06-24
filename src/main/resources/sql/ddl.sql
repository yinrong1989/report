create database report;
use report;
create table report_data
(
    id          bigint auto_increment
        primary key,
    sql_content varchar(5000)                         not null,
    file_name   varchar(200)                          not null,
    file_type   varchar(20) default 'xls'             not null comment 'xls,csv,xlsx',
    create_date datetime    default CURRENT_TIMESTAMP not null,
    update_date datetime    default CURRENT_TIMESTAMP not null,
    is_valid    char        default '1'               not null,
    task_id     bigint                                null,
    data_source varchar(20)                           null
);

create table report_target
(
    id             bigint auto_increment
        primary key,
    create_date    datetime    default CURRENT_TIMESTAMP not null,
    update_date    datetime    default CURRENT_TIMESTAMP not null,
    is_valid       char        default '1'               not null,
    target_type    varchar(20) default 'email'           not null comment 'email,wechatMessage',
    target_address varchar(500)                          null comment '机器人token或者邮箱地址',
    message        varchar(20)                           null comment '前置消息',
    task_id        bigint                                null
);

create table report_task
(
    id          bigint auto_increment
        primary key,
    create_date datetime     default CURRENT_TIMESTAMP not null,
    update_date datetime     default CURRENT_TIMESTAMP not null,
    is_valid    char         default '1'               not null,
    cron        varchar(20)                            null,
    name        varchar(200) default ''                not null
);
