DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
   user_id bigint(20) NOT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `price` decimal(20,2) DEFAULT NULL,
  `json_item_id_qty` varchar(1024) NOT NULL,
  KEY `userid` (`user_id`),
  CONSTRAINT `user_id_order` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
  ON DELETE CASCADE ON UPDATE CASCADE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



DROP PROCEDURE IF EXISTS PROC_QUERY_ORDER_BY_ID;
DELIMITER //
CREATE PROCEDURE PROC_QUERY_ORDER_BY_ID(IN uid bigint)
BEGIN
  SELECT id, user_id userId, create_time createTime, price, json_item_id_qty jsonItemIdQty FROM t_order where user_id = uid;
END//
DELIMITER ;
