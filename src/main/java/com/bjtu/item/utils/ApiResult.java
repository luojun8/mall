package com.bjtu.item.utils;


public class ApiResult<T> {
	private boolean success = true;
	private String errCode;
	private String errMsg;
	private String callbackSequence;
	private Long sysTime = Long.valueOf(System.currentTimeMillis());
	private T data;

	public ApiResult() {
	}

	public static String succStr() {
		return succStr((Object)null);
	}

	public static <T> String succStr(T data) {
		ApiResult r = new ApiResult();
		r.setSuccess(true);
		r.setData(data);
		return JsonUtil.toJson(r);
	}


	public static String errorStr(String errCode) {
		return errorStr(errCode, (String)null, (Object)null);
	}

	public static String errorStr(String errCode, String errMsg) {
		return errorStr(errCode, errMsg, (Object)null);
	}

	public static String errorStr(String errCode, String errMsg, Object data) {
		ApiResult r = new ApiResult();
		r.setSuccess(false);
		r.setErrCode(errCode);
		r.setErrMsg(errMsg);
		r.setData(data);
		return JsonUtil.toJson(r);
	}

	public boolean isSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrCode() {
		return this.errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return this.errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getCallbackSequence() {
		return this.callbackSequence;
	}

	public void setCallbackSequence(String callbackSequence) {
		this.callbackSequence = callbackSequence;
	}

	public Long getSysTime() {
		return this.sysTime;
	}

	public void setSysTime(Long sysTime) {
		this.sysTime = sysTime;
	}

	public T getData() {
		return this.data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
