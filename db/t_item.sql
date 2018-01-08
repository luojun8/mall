DROP TABLE IF EXISTS `t_item`;
CREATE TABLE `t_item` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `name` varchar(64) NOT NULL,
  `price` decimal(20,2) DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  `images` longtext DEFAULT NULL,
  `stock` smallint(5) unsigned DEFAULT '0',
  `desc` longtext DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


delimiter //

drop trigger if exists trigger_item_aft_insert//
create trigger trigger_item_aft_insert
after insert on t_every_order
for each row
begin
    update t_item set stock = stock - new.qty where id = new.item_id and stock >= new.qty;
end //

delimiter ;