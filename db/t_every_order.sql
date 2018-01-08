DROP TABLE IF EXISTS `t_every_order`;
CREATE TABLE `t_every_order` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `qty` tinyint(4) NOT NULL DEFAULT '1',
  `item_id` varchar(64) NOT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;