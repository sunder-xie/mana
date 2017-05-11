create table mana_customer(
`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除,Y删除,N未删除',
  `gmt_create` datetime NOT NULL DEFAULT '1970-01-01 12:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '1970-01-01 12:00:00' COMMENT '更新时间',
  `creator` varchar(20) NOT NULL DEFAULT '' COMMENT '创建人',
  `modifier` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
customer_mobile varchar(11) not null comment '客户手机号',
customer_name varchar(10) not null default '' comment '客户名称',
customer_source tinyint unsigned not null default 0 comment '客户来源 0:淘汽 1:门店',
customer_province int(11) unsigned not null default 0 comment '客户地址省份',
customer_city int(11) unsigned not null default 0 comment '客户地址城市',
customer_address varchar(100) not null default '' comment '客户详细地址',

certificate_type tinyint unsigned not null default 0 comment '证件类型 0:身份证 1:护照 2:军人证',
certificate_no varchar(30) not null default '' comment '证件号码',

relation_id int(11) unsigned NOT NULL default 0 comment '关联id(人工订正数据时使用):当一个客户有多个手机号时,选取一个为主id,其它记录关联这个主id',
remark_info varchar(500) not null default '' comment '备注信息(人工订正数据时,可以写一些必要的备注)',

primary key(id),
UNIQUE KEY uk_customer_mobile(customer_mobile)
)DEFAULT charset=utf8 comment '客户表';


create table mana_customer_vehicle(
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `is_deleted` char(1) NOT NULL DEFAULT 'N' COMMENT '是否删除,Y删除,N未删除',
  `gmt_create` datetime NOT NULL DEFAULT '1970-01-01 12:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '1970-01-01 12:00:00' COMMENT '更新时间',
  `creator` varchar(20) NOT NULL DEFAULT '' COMMENT '创建人',
  `modifier` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
customer_id int(11) unsigned NOT NULL comment 'mana_customer主键id',
licence_plate varchar(10) not null default '' comment '车牌号',
has_licence_plate tinyint unsigned not null default 1 comment '是否有车牌 0:否 1:是',
vehicle_type varchar(50) not null default '' comment '品牌车型(淘汽车型数据,有可能是文本框输入)',
insure_status tinyint unsigned not null default 0 comment '车险状态 0:未投保 1:在保 2:脱保',
insure_start_date date default null comment '起保日期(通过终保日期算出来)',
insure_end_date date default null comment '终保日期',
insure_province int(11) unsigned not null default 0 comment '投保省份',
insure_city int(11) unsigned not null default 0 comment '投保城市',
insure_vehicle_type varchar(100) not null default '' comment '品牌型号(文本框输入)',
engine_no varchar(20) not null default '' comment '发动机号',
vin_no varchar(17) not null default '' comment '车架号(vin码)',
vehicle_reg_date date default null comment '车辆登记日期',

remark_info varchar(500) not null default '' comment '备注信息(预留字段)',

primary key(id),
UNIQUE key uk_customer_licence_plate(customer_id,licence_plate)
)DEFAULT charset=utf8 comment '客户车辆表';


create table mana_customer_communicate_record(
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `gmt_create` datetime NOT NULL DEFAULT '1970-01-01 12:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '1970-01-01 12:00:00' COMMENT '更新时间',
  `creator` varchar(20) NOT NULL DEFAULT '' COMMENT '创建人',
  `modifier` varchar(20) NOT NULL DEFAULT '' COMMENT '修改人',
customer_vehicle_id int(11) unsigned NOT NULL comment 'mana_customer_vehicle主键id',
customer_mobile varchar(11) not null default '' comment '客户手机号(冗余字段)',
communicate_channel tinyint unsigned not null default 0 comment '沟通渠道 0:车主致电 1:电话回访 2:拜访',
communicate_date date not null comment '沟通日期',
communicate_content varchar(1000) not null default '' comment '沟通内容',

recommend_shop_id int(11) unsigned not null default 0 comment '推荐门店',
recommend_shop_info varchar(300) not null default '' comment '推荐门店信息(记录当时的门店信息,用于前端展示)',

remark_info varchar(500) not null default '' comment '备注信息(预留字段)',

primary key(id),
key idx_customer_vehicle_id(customer_vehicle_id)
)DEFAULT charset=utf8 comment '客户沟通记录表';
