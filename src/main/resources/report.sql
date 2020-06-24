create table nflow_monitor.report_data
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

create table nflow_monitor.report_target
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

create table nflow_monitor.report_task
(
    id          bigint auto_increment
        primary key,
    create_date datetime     default CURRENT_TIMESTAMP not null,
    update_date datetime     default CURRENT_TIMESTAMP not null,
    is_valid    char         default '1'               not null,
    cron        varchar(20)                            null,
    name        varchar(200) default ''                not null
);

INSERT INTO nflow_monitor.report_task (id, create_date, update_date, is_valid, cron, name) VALUES (1, '2020-06-17 13:23:58', '2020-06-17 13:23:58', '1', '0 0 8 ?  * * *', '财务报表');

11,"		-- 还银行
SELECT
DATE(a.bk_claim_date) '实际还款日',
b.channel ,
c.mer_name '产品类型',

SUM(a.act_total_amt)'实际还款总额',

SUM( CASE WHEN a.opt_status='T' THEN  a.act_total_amt ELSE 0 END) '提前还款总额',

SUM( CASE WHEN a.opt_status IN('J','Q','K') THEN  a.act_total_amt ELSE 0 END) '提前结清总额',

SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status ='N'  THEN  a.act_total_amt ELSE 0 END) '正常还款总额',

SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status IN ('O','D')  THEN  a.act_total_amt ELSE 0 END) '逾期还款总额'
FROM loan_replan a
INNER JOIN loan b ON a.loan_id=b.id
INNER JOIN mer_info c ON b.pub_mer_no=c.mer_no
WHERE  a.is_valid='Y'
AND a.bk_claim_status<>'N'
 AND b.channel='srcb'
AND DATE(b.loan_time) >= '2019-11-01'
GROUP BY c.mer_no,b.channel,DATE(a.bk_claim_date)
 ORDER BY DATE(a.bk_claim_date) DESC,b.channel ASC;
",农商行还银行11-1后,xls,2020-06-17 16:09:44,2020-06-17 16:09:44,1,1,nfsp
12,"		-- 还银行
SELECT
DATE(a.bk_claim_date) '实际还款日',
b.channel ,
c.mer_name '产品类型',

SUM(a.act_total_amt)'实际还款总额',

SUM( CASE WHEN a.opt_status='T' THEN  a.act_total_amt ELSE 0 END) '提前还款总额',

SUM( CASE WHEN a.opt_status IN('J','Q','K') THEN  a.act_total_amt ELSE 0 END) '提前结清总额',

SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status ='N'  THEN  a.act_total_amt ELSE 0 END) '正常还款总额',

SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status IN ('O','D')  THEN  a.act_total_amt ELSE 0 END) '逾期还款总额'
FROM loan_replan a
INNER JOIN loan b ON a.loan_id=b.id
INNER JOIN mer_info c ON b.pub_mer_no=c.mer_no
WHERE  a.is_valid='Y'
AND a.bk_claim_status<>'N'
 AND b.channel='srcb'
AND DATE(b.loan_time) < '2019-11-01'
GROUP BY c.mer_no,b.channel,DATE(a.bk_claim_date)
 ORDER BY DATE(a.bk_claim_date) DESC,b.channel ASC;
",农商行还银行11-1前,xls,2020-06-17 16:09:44,2020-06-17 16:09:44,1,1,nfsp
2,"
SELECT a.* ,a.京东扣款总额-a.融担公司通道费-a.信用保证险-a.京东代扣手续费 AS '风险计划服务费' FROM
(SELECT
	m.mer_name '产品',
	l.id loanId,
	real_name '姓名',
	u.identity_no '身份证号',
	u.phone '手机号',
	l.periods '借款期数',
	l.loan_money '借款本金',
	l.interest AS '利息总和',
p.amount AS '京东扣款总额',
CASE
		p.STATUS
		WHEN 'S' THEN '支付成功'
		WHEN 'P' THEN '支付中'
 		 ELSE '支付失败'
	END '支付状态',
	DATE(l.loan_time) '放款日期',
	DATE_FORMAT(l.loan_time,'%T')  '放款时间',
CASE
		l.channel
		WHEN '10009' THEN
		'光大苏分'
		WHEN 'jcfc' THEN
		'晋商消金'
		WHEN 'jcfc-x' THEN
		'晋商融担'
		WHEN 'wsm' THEN
		'微神马'
		WHEN 'citic' THEN
		'中信消费金融'
		WHEN 'kcbe' THEN
		'金城银行'
		WHEN 'citic' THEN
		'中信消费金融'
		WHEN 'kcbe' THEN
		'金城银行'
		WHEN 'gdxt' THEN
		'光大信托'
			WHEN 'srcb' THEN
		'上海农商'
			WHEN 'zjts' THEN
		'中金同盛'
		WHEN 'klb' THEN '昆仑银行'
		WHEN 'ccab' THEN '长安银行'
		WHEN 'ljbc' THEN '龙江银行'
		WHEN 'lx2b' THEN '龙信2B'
		WHEN 'lx2c' THEN '龙信2C'
		ELSE NULL
	END '资金渠道',
	p.pay_order_no '交易号',
	'' AS '扣款方式',
	CASE
		p.STATUS
		WHEN 'S' THEN '否'
		WHEN 'P' THEN '未知'
 		 ELSE '是'
	END '是否追偿',
	'' AS '追偿日期',
-- 	CASE
-- 		pr.policy_product_code
-- 		WHEN 'hexierongdan' THEN '和谐融担'
-- 		WHEN 'shenguorongrongdan' THEN '深国融担'
--  		 ELSE '其他' END '融担公司',
		-- m.pre_account '融担公司',
-- 		pc.channel_name '融担公司',
-- 		pc.channel_mch_id '扣款账号',

p.amount*0.035 '融担公司通道费',
p.amount*0.01 '融担公司通道费1',
TRUNCATE(CEIL(if(l.channel ='jcfc-x',0,CASE
		l.periods
		WHEN 3 THEN
		( l.loan_money + l.interest ) * 0.5 / 100
		WHEN 6 THEN
		( l.loan_money + l.interest ) * 1 / 100
		WHEN 9 THEN
		( l.loan_money + l.interest ) * 1.5 / 100
		WHEN 12 THEN
		( l.loan_money + l.interest ) * 2 / 100
		ELSE 0  END) *100)/100,2)AS '信用保证险',
p.amount*2.5/1000  '京东代扣手续费'

FROM
	loan l
	LEFT JOIN loan_detail p ON p.loan_id = l.id
AND p.type IN ('01','04','02')
	LEFT JOIN USER u ON u.id = l.user_id
	LEFT JOIN mer_info m ON m.mer_no = l.pub_mer_no AND m.STATUS = 'Y'
	LEFT JOIN nfsp.mer_policy_router pr ON l.policy_router_id=pr.id
  -- LEFT JOIN charge_account_setting cs ON cs.business_mode_id=pr.business_mode_id AND cs.mer_no=l.pub_mer_no

	WHERE
	l.loan_status IN ( 'S', 'R' )
  -- AND cs.delete_flag=0
AND DATE(l.loan_time)  = DATE_SUB(CURRENT_DATE,INTERVAL 1 DAY)
	AND l.channel='zjts'
ORDER BY
	p.create_date DESC
	)a
	GROUP BY a.loanId;

",保费-中金同盛,xls,2020-06-17 16:04:56,2020-06-17 16:04:56,1,1,nfsp
3,"
SELECT a.* ,a.京东扣款总额-a.融担公司通道费-a.信用保证险-a.京东代扣手续费 AS '风险计划服务费' FROM
(SELECT
	m.mer_name '产品',
	l.id loanId,
	real_name '姓名',
	u.identity_no '身份证号',
	u.phone '手机号',
	l.periods '借款期数',
	l.loan_money '借款本金',
	l.interest AS '利息总和',
p.amount AS '京东扣款总额',
CASE
		p.STATUS
		WHEN 'S' THEN '支付成功'
		WHEN 'P' THEN '支付中'
 		 ELSE '支付失败'
	END '支付状态',
	DATE(l.loan_time) '放款日期',
	DATE_FORMAT(l.loan_time,'%T')  '放款时间',
CASE
		l.channel
		WHEN '10009' THEN
		'光大苏分'
		WHEN 'jcfc' THEN
		'晋商消金'
		WHEN 'jcfc-x' THEN
		'晋商融担'
		WHEN 'wsm' THEN
		'微神马'
		WHEN 'citic' THEN
		'中信消费金融'
		WHEN 'kcbe' THEN
		'金城银行'
		WHEN 'citic' THEN
		'中信消费金融'
		WHEN 'kcbe' THEN
		'金城银行'
		WHEN 'gdxt' THEN
		'光大信托'
			WHEN 'srcb' THEN
		'上海农商'
			WHEN 'zjts' THEN
		'中金同盛'
		WHEN 'klb' THEN '昆仑银行'
		WHEN 'ccab' THEN '长安银行'
		WHEN 'ljbc' THEN '龙江银行'
		WHEN 'lx2b' THEN '龙信2B'
		WHEN 'lx2c' THEN '龙信2C'
		ELSE NULL
	END '资金渠道',
	p.pay_order_no '交易号',
	'' AS '扣款方式',
	CASE
		p.STATUS
		WHEN 'S' THEN '否'
		WHEN 'P' THEN '未知'
 		 ELSE '是'
	END '是否追偿',
	'' AS '追偿日期',
-- 	CASE
-- 		pr.policy_product_code
-- 		WHEN 'hexierongdan' THEN '和谐融担'
-- 		WHEN 'shenguorongrongdan' THEN '深国融担'
--  		 ELSE '其他' END '融担公司',
		-- m.pre_account '融担公司',
		pc.channel_name '融担公司',
		pc.channel_mch_id '扣款账号',

p.amount*0.035 '融担公司通道费',
p.amount*0.01 '融担公司通道费1',
TRUNCATE(CEIL(if(l.channel ='jcfc-x',0,CASE
		l.periods
		WHEN 3 THEN
		( l.loan_money + l.interest ) * 0.5 / 100
		WHEN 6 THEN
		( l.loan_money + l.interest ) * 1 / 100
		WHEN 9 THEN
		( l.loan_money + l.interest ) * 1.5 / 100
		WHEN 12 THEN
		( l.loan_money + l.interest ) * 2 / 100
		ELSE 0  END) *100)/100,2)AS '信用保证险',
p.amount*2.5/1000  '京东代扣手续费'

FROM
	loan l
	LEFT JOIN loan_detail p ON p.loan_id = l.id
AND p.type IN ('01','04')
	LEFT JOIN USER u ON u.id = l.user_id
	LEFT JOIN mer_info m ON m.mer_no = l.pub_mer_no AND m.STATUS = 'Y'
	LEFT JOIN nfsp.mer_policy_router pr ON l.policy_router_id=pr.id
  LEFT JOIN charge_account_setting cs ON cs.business_mode_id=pr.business_mode_id AND cs.mer_no=l.pub_mer_no
	LEFT JOIN `nflow_common`.pay_channel pc ON cs.pre_account = pc.account

	WHERE
	l.loan_status IN ( 'S', 'R' )
  AND cs.delete_flag=0
-- 	AND DATE(l.loan_time)  >= '2019-12-13'

 AND DATE(l.loan_time)  = DATE_SUB(CURRENT_DATE,INTERVAL 1 DAY)
	AND l.pub_mer_no NOT IN ( '10006', '10002' )
ORDER BY
	p.create_date DESC
	)a
	GROUP BY a.loanId;

",保费清单,xls,2020-06-17 16:04:56,2020-06-17 16:04:56,1,1,nfsp
1,"-- 客户实际还款
SELECT
DATE(a.act_repay_date) '实际还款日',
b.channel ,
c.mer_name '产品类型',

SUM(a.act_total_amt)'实际还款总额',

SUM( CASE WHEN a.opt_status='T' THEN  a.act_total_amt ELSE 0 END) '提前还款总额',

SUM( CASE WHEN a.opt_status IN('J','Q','K') THEN  a.act_total_amt ELSE 0 END) '提前结清总额',

SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status ='N'  THEN  a.act_total_amt ELSE 0 END) '正常还款总额',

SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status IN ('O','D')  THEN  a.act_total_amt ELSE 0 END) '逾期还款总额'
FROM loan_replan a
INNER JOIN loan b ON a.loan_id=b.id
INNER JOIN mer_info c ON b.pub_mer_no=c.mer_no
WHERE  a.is_valid='Y' AND a.opt_status <> 'N'
-- AND a.bk_claim_status<>'N'
 AND b.channel='srcb'
AND DATE(b.loan_time)< '2019-11-01'
GROUP BY c.mer_no,b.channel,DATE(a.act_repay_date)
 ORDER BY DATE(a.act_repay_date) DESC,b.channel ASC;",农商行实际还款11-1前,xls,2020-06-17 13:22:07,2020-06-17 13:22:07,1,1,nfsp
15,"-- 客户实际还款
SELECT
DATE(a.act_repay_date) '实际还款日',
b.channel ,
c.mer_name '产品类型',

SUM(a.act_total_amt)'实际还款总额',

SUM( CASE WHEN a.opt_status='T' THEN  a.act_total_amt ELSE 0 END) '提前还款总额',

SUM( CASE WHEN a.opt_status IN('J','Q','K') THEN  a.act_total_amt ELSE 0 END) '提前结清总额',

SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status ='N'  THEN  a.act_total_amt ELSE 0 END) '正常还款总额',

SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status IN ('O','D')  THEN  a.act_total_amt ELSE 0 END) '逾期还款总额'
FROM loan_replan a
INNER JOIN loan b ON a.loan_id=b.id
INNER JOIN mer_info c ON b.pub_mer_no=c.mer_no
WHERE  a.is_valid='Y' AND a.opt_status <> 'N'
-- AND a.bk_claim_status<>'N'
 AND b.channel='srcb'
AND DATE(b.loan_time)>= '2019-11-01'
GROUP BY c.mer_no,b.channel,DATE(a.act_repay_date)
 ORDER BY DATE(a.act_repay_date) DESC,b.channel ASC;",农商行实际还款11-1后,xls,2020-06-17 16:09:44,2020-06-17 16:09:44,1,1,nfsp
13,"-- 计划还款数据
SELECT
   DATE(a.cul_repay_date) repayDate,
    b.channel channel,
    c.mer_name merName,
		-- SUM(a.cul_total_amt)'计划总额',
-- SUM(a.cul_principal) '计划本金',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_total_amt ELSE a.cul_total_amt END) '计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_principal ELSE a.cul_principal END) '计划本金',
-- 提前还款
SUM( CASE WHEN a.opt_status='T' THEN  a.act_principal ELSE 0 END) '提前还款本金',

-- 提前结清
SUM( CASE WHEN a.opt_status IN('J','Q','K') THEN  a.act_principal ELSE 0 END) '提前结清本金',

-- 正常已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status ='N'  THEN  a.act_principal ELSE 0 END) '正常还款本金',


-- 逾期已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status IN ('O','D')  THEN  a.act_principal ELSE 0 END) '逾期还本金'
    FROM loan_replan a
    INNER JOIN loan b ON a.loan_id=b.id
    INNER JOIN mer_info c ON b.pub_mer_no=c.mer_no
    WHERE   a.is_valid='Y'
      AND b.channel='srcb'
      AND DATE(b.loan_time) >= '2019-11-01'
    GROUP BY c.mer_no, b.channel, DATE(a.cul_repay_date)
    ORDER BY DATE(a.cul_repay_date) DESC,b.channel ASC;
",农商行计划还款11-1后,xls,2020-06-17 16:09:44,2020-06-17 16:09:44,1,1,nfsp
14,"-- 计划还款数据
SELECT
   DATE(a.cul_repay_date) repayDate,
    b.channel channel,
    c.mer_name merName,
		-- SUM(a.cul_total_amt)'计划总额',
-- SUM(a.cul_principal) '计划本金',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_total_amt ELSE a.cul_total_amt END) '计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_principal ELSE a.cul_principal END) '计划本金',
-- 提前还款
SUM( CASE WHEN a.opt_status='T' THEN  a.act_principal ELSE 0 END) '提前还款本金',

-- 提前结清
SUM( CASE WHEN a.opt_status IN('J','Q','K') THEN  a.act_principal ELSE 0 END) '提前结清本金',

-- 正常已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status ='N'  THEN  a.act_principal ELSE 0 END) '正常还款本金',


-- 逾期已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status IN ('O','D')  THEN  a.act_principal ELSE 0 END) '逾期还本金'
    FROM loan_replan a
    INNER JOIN loan b ON a.loan_id=b.id
    INNER JOIN mer_info c ON b.pub_mer_no=c.mer_no
    WHERE   a.is_valid='Y'
      AND b.channel='srcb'
      AND DATE(b.loan_time) <'2019-11-01'
    GROUP BY c.mer_no, b.channel, DATE(a.cul_repay_date)
    ORDER BY DATE(a.cul_repay_date) DESC,b.channel ASC;
",农商行计划还款11-1前,xls,2020-06-17 16:09:44,2020-06-17 16:09:44,1,1,nfsp
4,"-- 计划还款数据
SELECT
   DATE(a.cul_repay_date) repayDate,b.channel channel,c.mer_name merName,
		-- SUM(a.cul_total_amt)'计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_total_amt ELSE a.cul_total_amt END) '计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_principal ELSE a.cul_principal END) '计划本金',
-- 提前还款
SUM( CASE WHEN a.opt_status='T' THEN  a.act_principal ELSE 0 END) '提前还款本金',
-- 提前结清
SUM( CASE WHEN a.opt_status IN('J','Q','K') THEN  a.act_principal ELSE 0 END) '提前结清本金',
-- 正常已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status ='N'  THEN  a.act_principal ELSE 0 END) '正常还款本金',
-- 逾期已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status IN ('O','D')  THEN  a.act_principal ELSE 0 END) '逾期还本金'
    FROM loan_replan a
    INNER JOIN loan b ON a.loan_id=b.id
    INNER JOIN mer_info c ON b.pub_mer_no=c.mer_no
    WHERE   a.is_valid='Y'
      AND b.channel ='gdxt'
  --  AND DATE(a.cul_repay_date)>='2019-12-04'
    GROUP BY c.mer_no, b.channel, DATE(a.cul_repay_date)
    ORDER BY DATE(a.cul_repay_date) DESC,b.channel ASC;",计划还款-光大信托,xls,2020-06-17 16:04:56,2020-06-17 16:04:56,1,1,nfsp
5,"-- 计划还款数据
SELECT
   DATE(a.cul_repay_date) repayDate,b.channel channel,c.mer_name merName,
		-- SUM(a.cul_total_amt)'计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_total_amt ELSE a.cul_total_amt END) '计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_principal ELSE a.cul_principal END) '计划本金',
-- 提前还款
SUM( CASE WHEN a.opt_status='T' THEN  a.act_principal ELSE 0 END) '提前还款本金',
-- 提前结清
SUM( CASE WHEN a.opt_status IN('J','Q','K') THEN  a.act_principal ELSE 0 END) '提前结清本金',
-- 正常已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status ='N'  THEN  a.act_principal ELSE 0 END) '正常还款本金',
-- 逾期已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status IN ('O','D')  THEN  a.act_principal ELSE 0 END) '逾期还本金'
    FROM loan_replan a
    INNER JOIN loan b ON a.loan_id=b.id
    INNER JOIN mer_info c ON b.pub_mer_no=c.mer_no
    WHERE   a.is_valid='Y'
      AND b.channel ='kcbe'
  --  AND DATE(a.cul_repay_date)>='2019-12-04'
    GROUP BY c.mer_no, b.channel, DATE(a.cul_repay_date)
    ORDER BY DATE(a.cul_repay_date) DESC,b.channel ASC;",计划还款-金城银行,xls,2020-06-17 16:04:56,2020-06-17 16:04:56,1,1,nfsp
6,"-- 计划还款数据
SELECT
   DATE(a.cul_repay_date) repayDate,b.channel channel,c.mer_name merName,
		-- SUM(a.cul_total_amt)'计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_total_amt ELSE a.cul_total_amt END) '计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_principal ELSE a.cul_principal END) '计划本金',
-- 提前还款
SUM( CASE WHEN a.opt_status='T' THEN  a.act_principal ELSE 0 END) '提前还款本金',
-- 提前结清
SUM( CASE WHEN a.opt_status IN('J','Q','K') THEN  a.act_principal ELSE 0 END) '提前结清本金',
-- 正常已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status ='N'  THEN  a.act_principal ELSE 0 END) '正常还款本金',
-- 逾期已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status IN ('O','D')  THEN  a.act_principal ELSE 0 END) '逾期还本金'
    FROM loan_replan a
    INNER JOIN loan b ON a.loan_id=b.id
    INNER JOIN mer_info c ON b.pub_mer_no=c.mer_no
    WHERE   a.is_valid='Y'
      AND b.channel ='jcfc-x'
  --  AND DATE(a.cul_repay_date)>='2019-12-04'
    GROUP BY c.mer_no, b.channel, DATE(a.cul_repay_date)
    ORDER BY DATE(a.cul_repay_date) DESC,b.channel ASC;",计划还款-晋商融担,xls,2020-06-17 16:04:56,2020-06-17 16:04:56,1,1,nfsp
7,"-- 计划还款数据
SELECT
   DATE(a.cul_repay_date) repayDate,b.channel channel,c.mer_name merName,
		-- SUM(a.cul_total_amt)'计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_total_amt ELSE a.cul_total_amt END) '计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_principal ELSE a.cul_principal END) '计划本金',
-- 提前还款
SUM( CASE WHEN a.opt_status='T' THEN  a.act_principal ELSE 0 END) '提前还款本金',
-- 提前结清
SUM( CASE WHEN a.opt_status IN('J','Q','K') THEN  a.act_principal ELSE 0 END) '提前结清本金',
-- 正常已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status ='N'  THEN  a.act_principal ELSE 0 END) '正常还款本金',
-- 逾期已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status IN ('O','D')  THEN  a.act_principal ELSE 0 END) '逾期还本金'
    FROM loan_replan a
    INNER JOIN loan b ON a.loan_id=b.id
    INNER JOIN mer_info c ON b.pub_mer_no=c.mer_no
    WHERE   a.is_valid='Y'
      AND b.channel ='klb'
  --  AND DATE(a.cul_repay_date)>='2019-12-04'
    GROUP BY c.mer_no, b.channel, DATE(a.cul_repay_date)
    ORDER BY DATE(a.cul_repay_date) DESC,b.channel ASC;",计划还款-昆仑银行,xls,2020-06-17 16:04:56,2020-06-17 16:04:56,1,1,nfsp
8,"-- 计划还款数据
SELECT
   DATE(a.cul_repay_date) repayDate,b.channel channel,c.mer_name merName,
		-- SUM(a.cul_total_amt)'计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_total_amt ELSE a.cul_total_amt END) '计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_principal ELSE a.cul_principal END) '计划本金',
-- 提前还款
SUM( CASE WHEN a.opt_status='T' THEN  a.act_principal ELSE 0 END) '提前还款本金',
-- 提前结清
SUM( CASE WHEN a.opt_status IN('J','Q','K') THEN  a.act_principal ELSE 0 END) '提前结清本金',
-- 正常已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status ='N'  THEN  a.act_principal ELSE 0 END) '正常还款本金',
-- 逾期已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status IN ('O','D')  THEN  a.act_principal ELSE 0 END) '逾期还本金'
    FROM loan_replan a
    INNER JOIN loan b ON a.loan_id=b.id
    INNER JOIN mer_info c ON b.pub_mer_no=c.mer_no
    WHERE   a.is_valid='Y'
      AND b.channel ='ljbc'
  --  AND DATE(a.cul_repay_date)>='2019-12-04'
    GROUP BY c.mer_no, b.channel, DATE(a.cul_repay_date)
    ORDER BY DATE(a.cul_repay_date) DESC,b.channel ASC;",计划还款-龙江银行,xls,2020-06-17 16:04:57,2020-06-17 16:04:57,1,1,nfsp
9,"-- 计划还款数据
SELECT
   DATE(a.cul_repay_date) repayDate,b.channel channel,c.mer_name merName,
		-- SUM(a.cul_total_amt)'计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_total_amt ELSE a.cul_total_amt END) '计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_principal ELSE a.cul_principal END) '计划本金',
-- 提前还款
SUM( CASE WHEN a.opt_status='T' THEN  a.act_principal ELSE 0 END) '提前还款本金',
-- 提前结清
SUM( CASE WHEN a.opt_status IN('J','Q','K') THEN  a.act_principal ELSE 0 END) '提前结清本金',
-- 正常已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status ='N'  THEN  a.act_principal ELSE 0 END) '正常还款本金',
-- 逾期已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status IN ('O','D')  THEN  a.act_principal ELSE 0 END) '逾期还本金'
    FROM loan_replan a
    INNER JOIN loan b ON a.loan_id=b.id
    INNER JOIN mer_info c ON b.pub_mer_no=c.mer_no
    WHERE   a.is_valid='Y'
      AND b.channel ='lx2b'
  --  AND DATE(a.cul_repay_date)>='2019-12-04'
    GROUP BY c.mer_no, b.channel, DATE(a.cul_repay_date)
    ORDER BY DATE(a.cul_repay_date) DESC,b.channel ASC;",计划还款-龙信2b,xls,2020-06-17 16:04:57,2020-06-17 16:04:57,1,1,nfsp
10,"-- 计划还款数据
SELECT
   DATE(a.cul_repay_date) repayDate,b.channel channel,c.mer_name merName,
		-- SUM(a.cul_total_amt)'计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_total_amt ELSE a.cul_total_amt END) '计划总额',
SUM(CASE  WHEN a.opt_status IN('J','Q','K') THEN a.act_principal ELSE a.cul_principal END) '计划本金',
-- 提前还款
SUM( CASE WHEN a.opt_status='T' THEN  a.act_principal ELSE 0 END) '提前还款本金',
-- 提前结清
SUM( CASE WHEN a.opt_status IN('J','Q','K') THEN  a.act_principal ELSE 0 END) '提前结清本金',
-- 正常已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status ='N'  THEN  a.act_principal ELSE 0 END) '正常还款本金',
-- 逾期已还
SUM( CASE WHEN a.opt_status IN('G','R','D') AND a.plan_status IN ('O','D')  THEN  a.act_principal ELSE 0 END) '逾期还本金'
    FROM loan_replan a
    INNER JOIN loan b ON a.loan_id=b.id
    INNER JOIN mer_info c ON b.pub_mer_no=c.mer_no
    WHERE   a.is_valid='Y'
      AND b.channel ='lx2c'
  --  AND DATE(a.cul_repay_date)>='2019-12-04'
    GROUP BY c.mer_no, b.channel, DATE(a.cul_repay_date)
    ORDER BY DATE(a.cul_repay_date) DESC,b.channel ASC;",计划还款-龙信2c,xls,2020-06-17 16:09:44,2020-06-17 16:09:44,1,1,nfsp
