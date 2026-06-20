-- 动物医院动物管理系统 数据库脚本
CREATE DATABASE IF NOT EXISTS animal_hospital DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE animal_hospital;

-- 用户表(登录注册)
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
  `role` VARCHAR(20) DEFAULT 'admin' COMMENT '角色(admin/doctor)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入默认管理员账号: admin / 123456
INSERT INTO `user` (`username`, `password`, `real_name`, `role`) VALUES
('admin', '123456', '系统管理员', 'admin'),
('doctor1', '123456', '赵医生', 'doctor');

-- 主人表
DROP TABLE IF EXISTS `owner`;
CREATE TABLE `owner` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '电话',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '地址',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物主人表';

-- 动物表
DROP TABLE IF EXISTS `animal`;
CREATE TABLE `animal` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) NOT NULL COMMENT '动物名称',
  `species` VARCHAR(50) NOT NULL COMMENT '种类(猫/狗/兔等)',
  `breed` VARCHAR(50) DEFAULT NULL COMMENT '品种',
  `gender` VARCHAR(10) DEFAULT NULL COMMENT '性别',
  `age` INT DEFAULT NULL COMMENT '年龄(月)',
  `weight` DECIMAL(5,2) DEFAULT NULL COMMENT '体重(kg)',
  `owner_id` INT DEFAULT NULL COMMENT '主人ID',
  `symptom` VARCHAR(500) DEFAULT NULL COMMENT '症状',
  `photo` VARCHAR(500) DEFAULT NULL COMMENT '照片路径',
  `vaccine_date` DATE DEFAULT NULL COMMENT '最近疫苗日期',
  `next_vaccine_date` DATE DEFAULT NULL COMMENT '下次疫苗日期',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动物表';

-- 医生表
DROP TABLE IF EXISTS `doctor`;
CREATE TABLE `doctor` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `title` VARCHAR(50) DEFAULT NULL COMMENT '职称',
  `specialty` VARCHAR(100) DEFAULT NULL COMMENT '专长',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '电话',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生表';

-- 就诊记录表
DROP TABLE IF EXISTS `medical_record`;
CREATE TABLE `medical_record` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `animal_id` INT NOT NULL COMMENT '动物ID',
  `doctor_id` INT NOT NULL COMMENT '医生ID',
  `visit_date` DATE NOT NULL COMMENT '就诊日期',
  `symptom` VARCHAR(500) DEFAULT NULL COMMENT '症状',
  `diagnosis` VARCHAR(500) DEFAULT NULL COMMENT '诊断',
  `treatment` VARCHAR(500) DEFAULT NULL COMMENT '治疗方案',
  `cost` DECIMAL(10,2) DEFAULT NULL COMMENT '费用',
  `payment_status` VARCHAR(20) DEFAULT '未缴费' COMMENT '缴费状态(未缴费/已缴费)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='就诊记录表';

-- 预约表
DROP TABLE IF EXISTS `appointment`;
CREATE TABLE `appointment` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `animal_id` INT NOT NULL COMMENT '动物ID',
  `doctor_id` INT NOT NULL COMMENT '医生ID',
  `appointment_date` DATE NOT NULL COMMENT '预约日期',
  `appointment_time` VARCHAR(20) NOT NULL COMMENT '预约时间',
  `reason` VARCHAR(500) DEFAULT NULL COMMENT '预约原因',
  `status` VARCHAR(20) DEFAULT '待确认' COMMENT '状态(待确认/已确认/已取消/已完成)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约表';

-- 操作日志表
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(50) DEFAULT NULL COMMENT '操作用户',
  `operation` VARCHAR(200) DEFAULT NULL COMMENT '操作结果',
  `module` VARCHAR(50) DEFAULT NULL COMMENT '模块',
  `method` VARCHAR(50) DEFAULT NULL COMMENT '方法',
  `params` TEXT DEFAULT NULL COMMENT '参数',
  `ip` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 插入示例数据
INSERT INTO `owner` (`name`, `phone`, `address`) VALUES
('张三', '13800138001', '北京市朝阳区'),
('李四', '13800138002', '北京市海淀区'),
('王五', '13800138003', '北京市西城区');

INSERT INTO `doctor` (`name`, `title`, `specialty`, `phone`) VALUES
('赵医生', '主治医师', '犬科疾病', '13900139001'),
('钱医生', '副主任医师', '猫科疾病', '13900139002'),
('孙医生', '主任医师', '异宠诊疗', '13900139003');

INSERT INTO `animal` (`name`, `species`, `breed`, `gender`, `age`, `weight`, `owner_id`) VALUES
('大黄', '狗', '金毛寻回犬', '公', 36, 28.50, 1),
('咪咪', '猫', '英国短毛猫', '母', 24, 4.20, 2),
('小白', '兔', '荷兰垂耳兔', '母', 12, 1.80, 3),
('旺财', '狗', '柯基犬', '公', 18, 12.00, 1),
('橘子', '猫', '中华田园猫', '公', 6, 3.50, 2);

INSERT INTO `medical_record` (`animal_id`, `doctor_id`, `visit_date`, `symptom`, `diagnosis`, `treatment`, `cost`, `payment_status`) VALUES
(1, 1, '2024-03-15', '食欲不振，精神萎靡', '肠胃炎', '输液+口服药物', 350.00, '已缴费'),
(2, 2, '2024-03-20', '打喷嚏，流鼻涕', '上呼吸道感染', '抗生素治疗', 200.00, '已缴费'),
(3, 3, '2024-04-01', '皮肤红肿', '真菌感染', '外用药膏+药浴', 180.00, '未缴费'),
(4, 1, '2024-04-10', '跛行', '扭伤', '休息+消炎止痛', 260.00, '已缴费'),
(5, 2, '2024-04-15', '呕吐', '毛球症', '化毛膏+调整饮食', 150.00, '未缴费');
