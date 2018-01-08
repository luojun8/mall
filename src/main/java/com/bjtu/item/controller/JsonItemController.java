package com.bjtu.item.controller;

import com.bjtu.item.db.SessionContext;
import com.bjtu.item.db.SessionFactory;
import com.bjtu.item.db.mapper.ItemMapper;
import com.bjtu.item.entity.Item;
import com.bjtu.item.mendb.ItemMemDB;
import com.bjtu.item.mendb.UserMemDB;
import com.bjtu.item.utils.ApiResult;
import com.bjtu.item.utils.LoggerUtil;
import com.bjtu.item.utils.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class JsonItemController {

        private Logger logger = LoggerFactory.getLogger(JsonItemController.class);

        @Autowired
        private SessionFactory sessionFactory;

        @RequestMapping(value = "/api/json/item/add", produces = "application/json;charset=utf-8")
        @ResponseBody
        public String add(HttpServletRequest request,
                               HttpServletResponse res,
                               @RequestParam(value = "name") String name,
                               @RequestParam(value = "status",defaultValue = "1") Byte status,
                               @RequestParam(value = "price") Double price,
                               @RequestParam(value = "desc", required = false) String desc,
                               @RequestParam(value = "images", required = false) String images,
                               @RequestParam(value = "stock") Integer stock) {


            HttpSession session = UserMemDB.sessionMap.get(request.getSession().getId());
            if (session == null) {
                return ApiResult.errorStr(MessageUtil.CODE_DB_ERROR, MessageUtil.get(MessageUtil.CODE_DB_ERROR));
            }

            Item item = new Item();
            SessionContext context = sessionFactory.getSessionContext();
            try {
                item.setPrice(price);
                item.setName(name);
                item.setCreateTime(System.currentTimeMillis());
                item.setDesc(desc);
                item.setStock(stock);
                item.setStatus(status);
                item.setImages(images);
                item.setStatus(Item.STATUS_ON);
                int row = context.getMapper(ItemMapper.class).addItem(item);
                context.commit();
                if (row > 0) {
                    ItemMemDB.addItem(item);
                }

                return ApiResult.succStr();
            } catch (Exception e) {
                LoggerUtil.error(logger, e);
                return ApiResult.errorStr(MessageUtil.CODE_UNKNOWN_ERROR,MessageUtil.get(MessageUtil.CODE_UNKNOWN_ERROR));
            } finally {
                SessionContext.closeSilently(context);
            }
        }


    @RequestMapping(value = "/api/json/item/update", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String update(HttpServletRequest request,
                            @RequestParam(value = "id") Long id,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "status",required = false) Byte status,
                           @RequestParam(value = "price",required = false) Double price,
                           @RequestParam(value = "desc", required = false) String desc,
                           @RequestParam(value = "image", required = false) String image,
                           @RequestParam(value = "stock", required = false) Integer stock) {



        HttpSession session = UserMemDB.sessionMap.get(request.getSession().getId());
        if (session == null) {
            return ApiResult.errorStr(MessageUtil.CODE_DB_ERROR, MessageUtil.get(MessageUtil.CODE_DB_ERROR));
        }

        Item old = ItemMemDB.itemMap.get("id");
        Item item = new Item();
        SessionContext context = sessionFactory.getSessionContext();
        try {
            item.setPrice(price);
            item.setName(name);
            item.setUpdateTime(System.currentTimeMillis());
            item.setDesc(desc);
            item.setStatus(Item.STATUS_ON);
            Map<String, Item> params = new HashMap<>();
            params.put("fresh", item);
            params.put("old", old);
            int row = context.getMapper(ItemMapper.class).update(params);
            context.commit();
            if (row > 0) {
                ItemMemDB.addItem(item);
            }
            return ApiResult.succStr();
        } catch (Exception e) {
            LoggerUtil.error(logger, e);
            return ApiResult.errorStr(MessageUtil.CODE_UNKNOWN_ERROR,MessageUtil.get(MessageUtil.CODE_UNKNOWN_ERROR));
        } finally {
            SessionContext.closeSilently(context);
        }
    }


    @RequestMapping(value = "/api/json/item/query", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String register(HttpServletRequest request,
                           @RequestParam(value = "id", required = false) Long id) {


        SessionContext context = sessionFactory.getSessionContext();
        try {
            if (id == null){
                List<Item> items= context.getMapper(ItemMapper.class).selectAll();
                return ApiResult.succStr(items);
            } else {
                Item item = ItemMemDB.itemMap.get(id);
                return ApiResult.succStr(item);
            }
        } catch (Exception e) {
            LoggerUtil.error(logger, e);
            return ApiResult.errorStr(MessageUtil.CODE_UNKNOWN_ERROR,MessageUtil.get(MessageUtil.CODE_UNKNOWN_ERROR));
        } finally {
            SessionContext.closeSilently(context);
        }
    }

}