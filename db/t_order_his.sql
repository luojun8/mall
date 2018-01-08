DROP TABLE IF EXISTS `t_order_his`;
CREATE TABLE `t_order_his` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` bigint(20) NOT NULL,
  `order_id` bigint(20) NOT NULL,
  `json_item_id_qty` varchar(1024) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

delimiter //

drop trigger if exists trigger_order_aft_insert //
create trigger trigger_order_aft_insert
after insert on t_order
for each row
begin
    insert into t_order_his (order_id, create_time, json_item_id_qty) values
    (new.id, unix_timestamp()*1000, new.json_item_id_qty);
end //

delimiter ;