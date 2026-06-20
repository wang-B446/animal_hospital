-- 修复数据库编码并重新插入示例数据
ALTER DATABASE `animal_hospital` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password` VARCHAR(100) NOT NULL,
  `real_name` VARCHAR(50) DEFAULT NULL,
  `role` VARCHAR(20) DEFAULT 'admin',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;

DELETE FROM `user`;
INSERT INTO `user` (`username`, `password`, `real_name`, `role`) VALUES
('admin', '123456', '系统管理员', 'admin'),
('doctor1', '123456', '赵医生', 'doctor');

-- 主人表
CREATE TABLE IF NOT EXISTS `owner` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `phone` VARCHAR(20) DEFAULT NULL,
  `address` VARCHAR(200) DEFAULT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;

DELETE FROM `owner`;
INSERT INTO `owner` (`name`, `phone`, `address`) VALUES
('张三', '13800138001', '北京市朝阳区幸福路1号'),
('李四', '13800138002', '上海市浦东新区科技大道88号'),
('王五', '13800138003', '广州市天河区珠江新城');

-- 医生表
CREATE TABLE IF NOT EXISTS `doctor` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `specialty` VARCHAR(100) DEFAULT NULL,
  `phone` VARCHAR(20) DEFAULT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;

DELETE FROM `doctor`;
INSERT INTO `doctor` (`name`, `specialty`, `phone`) VALUES
('赵医生', '犬猫内科', '13900139001'),
('钱医生', '外科手术', '13900139002'),
('孙医生', '皮肤科', '13900139003');

-- 动物表
CREATE TABLE IF NOT EXISTS `animal` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `species` VARCHAR(50) DEFAULT NULL,
  `breed` VARCHAR(50) DEFAULT NULL,
  `gender` VARCHAR(10) DEFAULT NULL,
  `age_months` INT DEFAULT NULL,
  `weight_kg` DECIMAL(5,2) DEFAULT NULL,
  `owner_id` INT DEFAULT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;

DELETE FROM `animal`;
INSERT INTO `animal` (`name`, `species`, `breed`, `gender`, `age_months`, `weight_kg`, `owner_id`) VALUES
('旺财', '狗', '金毛', '公', 36, 28.5, 1),
('咪咪', '猫', '英短', '母', 24, 4.2, 2),
('豆豆', '狗', '泰迪', '公', 12, 1.8, 3),
('橘子', '猫', '橘猫', '母', 18, 12.0, 1),
('小白', '狗', '萨摩耶', '公', 6, 3.5, 2);

-- 就诊记录表
CREATE TABLE IF NOT EXISTS `medical_record` (
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `animal_id` INT NOT NULL,
  `doctor_id` INT NOT NULL,
  `diagnosis_date` DATE NOT NULL,
  `symptoms` TEXT DEFAULT NULL,
  `diagnosis` TEXT DEFAULT NULL,
  `treatment` TEXT DEFAULT NULL,
  `cost` DECIMAL(10,2) DEFAULT NULL,
  `notes` TEXT DEFAULT NULL,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;

DELETE FROM `medical_record`;
INSERT INTO `medical_record` (`animal_id`, `doctor_id`, `diagnosis_date`, `symptoms`, `diagnosis`, `treatment`, `cost`, `notes`) VALUES
(1, 1, '2025-12-01', '食欲不振、精神萎靡', '肠胃炎', '禁食、输液、开胃药', 350.00, '一周后复查'),
(2, 1, '2025-12-05', '频繁打喷嚏、流鼻涕', '上呼吸道感染', '抗病毒药物、雾化治疗', 280.00, '注意保暖'),
(3, 2, '2025-12-10', '后腿跛行、疼痛', '关节扭伤', '止痛药、限制活动、理疗', 450.00, '两周内避免剧烈运动'),
(4, 3, '2025-12-15', '皮肤红肿、脱毛', '真菌性皮炎', '抗真菌洗液、口服药', 320.00, '每周洗澡一次');
