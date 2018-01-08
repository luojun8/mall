package com.bjtu.item.utils;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class MessageUtil {

    // 系统相关错误码
    public static final String CODE_SYS_UNKNOWN = "SYSTEM.0001";
    public static final String CODE_SYS_ILLEGAL_PARAM = "SYSTEM.0002";
    public static final String CODE_SYS_UNEXPECTED_ERROR = "SYSTEM.0003";
    public static final String CODE_SYS_ILLEGAL_OPERATION = "SYSTEM.0004";
    public static final String CODE_SYS_INTERNAL_ERROR = "SYSTEM.0005";
    public static final String CODE_SYS_OUT_OF_SERVICE = "SYSTEM.0006";
    public static final String CODE_DB_ERROR = "SYSTEM.0007";
    public static final String CODE_UNKNOWN_ERROR = "SYSTEM.0008";
    public static final String CODE_DB_ERROR_IDEMPOTENT = "SYSTEM.0009";
    public static final String CODE_BANNERS_NUMBER_NOT_RIGHT = "SYSTEM.0010";


    private static final Map<String, String> MESSAGES;

    static {
        Map<String, String> workingMessage = new HashMap<>();

        workingMessage.put(CODE_SYS_UNKNOWN, "账号已被注册");
        workingMessage.put(CODE_SYS_ILLEGAL_PARAM, "Illegal parameter(s).");
        workingMessage.put(CODE_SYS_UNEXPECTED_ERROR, "账号不存在");
        workingMessage.put(CODE_SYS_ILLEGAL_OPERATION, "密码不对");
        workingMessage.put(CODE_SYS_INTERNAL_ERROR, "Internal exception: {0}");
        workingMessage.put(CODE_DB_ERROR, "您需要先登录");
        workingMessage.put(CODE_UNKNOWN_ERROR, "网络异常");
        workingMessage.put(CODE_DB_ERROR_IDEMPOTENT, "Please do not retry, already successfully.");
        workingMessage.put(CODE_BANNERS_NUMBER_NOT_RIGHT, "Banners number is not right");

        MESSAGES = Collections.unmodifiableMap(workingMessage);
    }

    private MessageUtil() {
    }

    public static String get(String code) {
        String message = MESSAGES.get(code);
        if (message == null) {
            return code;
        } else {
            return message;
        }
    }


    public static String get(String code, String... tokens) {
        String message = MESSAGES.get(code);
        if (message == null) {
            return code;
        } else {
            return MessageFormat.format(message, (Object[]) tokens);
        }
    }

    public static String[] getAsArray(String code) {
        String message = MESSAGES.get(code);
        if (message == null) {
            return new String[]{code, null};
        }

        return new String[]{code, message};
    }

}
