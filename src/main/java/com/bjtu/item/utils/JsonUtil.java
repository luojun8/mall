package com.bjtu.item.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtil {

	private static final Gson GSON = new Gson();
	private static final JsonParser PARSER = new JsonParser();

	private JsonUtil() {
	}

	public static String toJson(Object obj) {
		return GSON.toJson(obj);
	}
	
	public static <T> T fromJson(String json, Class<T> classOfT) {
		return GSON.fromJson(json, classOfT);
	}
	
	public static <T> T fromJson(String json, Type typeOfT) {
		return GSON.fromJson(json, typeOfT);
	}
	
	public static <T> List<T> fromJsonArray(String json,Class<T> classOfT){
		if(json==null) return null;
		JsonElement jsonEle = PARSER.parse(json);
		if(jsonEle.isJsonArray()){
			JsonArray arr = (JsonArray)jsonEle;
			int size = arr.size();
			if(size>0){
				List<T> data = null;
				for(int i=0;i<size;i++){
					T obj = GSON.fromJson(arr.get(i), classOfT);
					if(obj!=null){
						if(data==null)data = new ArrayList<T>(size);
						data.add(obj);
					}
				}
				return data;
			}else{
				return null;
			}
		}else{
			T obj = GSON.fromJson(jsonEle, classOfT);
			if(obj==null)return null;
			List<T> data = new ArrayList<T>();
			data.add(obj);
			return data;
		}
	}

	public static <T> List<T> fromJsonArray(String json, Type typeOfT){
		if(json==null) return null;
		JsonElement jsonEle = PARSER.parse(json);
		if(jsonEle.isJsonArray()){
			JsonArray arr = (JsonArray)jsonEle;
			int size = arr.size();
			if(size>0){
				List<T> data = null;
				for(int i=0;i<size;i++){
					T obj = GSON.fromJson(arr.get(i), typeOfT);
					if(obj!=null){
						if(data==null)data = new ArrayList<T>(size);
						data.add(obj);
					}
				}
				return data;
			}else{
				return null;
			}
		}else{
			T obj = GSON.fromJson(jsonEle, typeOfT);
			if(obj==null)return null;
			List<T> data = new ArrayList<T>();
			data.add(obj);
			return data;
		}
	}
	
	private static interface Convert<F,T>{
		public T cvt(F from);
	}
	
	final private static Convert<String,JsonElement> str2json= new Convert<String,JsonElement>(){
		public JsonElement cvt(String from) {
			return PARSER.parse(from);
		}
	};
	final private static Convert<JsonElement,JsonElement> json2json= new Convert<JsonElement,JsonElement>(){
		public JsonElement cvt(JsonElement from) {
			return from;
		}
	};
	final private static Convert<JsonElement,Object> json2obj= new Convert<JsonElement,Object>(){
		public Object cvt(JsonElement from) {
			if(from.isJsonPrimitive()){
				JsonPrimitive value = (JsonPrimitive)from;
				if(value.isBoolean())return value.getAsBoolean();
				if(value.isNumber())return value.getAsDouble();
				return value.getAsString();
			}else if(from.isJsonObject()){
				return GSON.fromJson(from, Map.class);
			}else if(from.isJsonArray()){
				return GSON.fromJson(from, List.class);
			}else
				return null;
		}
	};
	
	private static class ChainConvert<I,O> implements Convert<I,O>{
		private Convert<I,O> converter;
		public ChainConvert(Convert<I,O> converter){
			this.converter=converter;
		}
		public O cvt(I from) {
			return this.converter.cvt(from);
		}
		public <N> ChainConvert<I,N> then(final Convert<O,N> next){
			final Convert<I,O> first = this.converter;
			return new ChainConvert<I,N>(new Convert<I,N>(){
				public N cvt(I from) {
					return next.cvt(first.cvt(from));
				}});
		}
	}
	
	private static <I,O> ChainConvert<I,O> Wrap(Convert<I,O> converter){
		return new ChainConvert<I, O>(converter);
	}

	public static Object fetchObject(String jsonStr,String path){
		if(jsonStr==null) return null;
		if(path==null)return json2obj.cvt(str2json.cvt(jsonStr));//Wrap(str2json).then(json2obj).cvt(jsonStr);
		return fetchByArray(jsonStr,str2json,json2obj,path.split("/"));
	}
	
	public static JsonElement fetchJsonElement(String jsonStr,String path){
		if(jsonStr==null) return null;
		if(path==null)return str2json.cvt(jsonStr);
		return fetchByArray(jsonStr,str2json,json2json,path.split("/"));
	}
	
	public static Object fetchObject(JsonElement json,String path){
		if(json==null) return null;
		if(path==null)return json2obj.cvt(json);
		return fetchByArray(json,json2json,json2obj,path.split("/"));
	}
	
	public static JsonElement fetchJsonElement(JsonElement json,String path){
		if(json==null) return null;
		if(path==null)return json2json.cvt(json);
		return fetchByArray(json,json2json,json2json,path.split("/"));
	}

	private static<IF,OT> OT fetchByArray(IF json,Convert<IF,JsonElement> inputConv,Convert<JsonElement,OT> outputConv,String... paths){
		JsonElement jsonEle = inputConv.cvt(json);//PARSER.parse(json);
		if(paths==null)return outputConv.cvt(jsonEle);
		int idx = 0;
		JsonElement tmp = jsonEle;
		while(tmp!=null){
			if(idx>paths.length-1)break;
			String current = paths[idx];
			if(current!=null&&!current.isEmpty()){
				if(tmp.isJsonObject())
					tmp = ((JsonObject)tmp).get(current);
				else if(tmp.isJsonArray()){
					int subIdx = -1;
					try{
						subIdx = Integer.parseInt(current);
						tmp = subIdx==-1?null:((JsonArray)tmp).get(subIdx);
					}catch(Exception e){
						tmp = null;
					}
				}else
					tmp = null;
			}
			idx++;
		}
		if(tmp!=null){
			return outputConv.cvt(tmp);
		}else
			return null;
	}

	public static void main(String[] args){
		String a = "{\"ActInfo\":{\"value\": \"54\",\"type\": \"Integer\"},\"Bundle\": {\"value\": \"{\\\"itemId\\\":{\\\"type\\\":\\\"Long\\\",\\\"value\\\":\\\"13\\\"}\",\"type\": \"Map\"}}";

		/*Map<String, Object> map = new HashMap<>();
		Map<String, String> innerMap = new HashMap<>();
		innerMap.put("id", "10");
		innerMap.put("type", "Integer");
		map.put("params", innerMap);
		Map<String, Object> outMap = new HashMap<>();
		outMap.put("info", innerMap);No
		map.put("bundle",outMap);
		String jsonMap = toJson(map);
		System.out.println(jsonMap);
		Map<String, Object> map1 = fromJson(jsonMap,new TypeToken<Map<String, Object>>() {
		}.getType());
		System.out.println(map1);
		System.out.println(toJson(map1));*/

		Map<String, Object> map1 = fromJson(a,new TypeToken<Map<String, Object>>() {
		}.getType());
		System.out.println(map1);
		System.out.println(map1.getClass());
	}
}
