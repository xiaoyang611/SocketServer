package xiaoyang.server.config;

import java.util.LinkedHashMap;

/**
 * @author Ð¤Ñï
 * È«¾Ö»º´æ
 */
public class Global {
	
	private static LinkedHashMap<String, Object> cache;
	
	static{
		cache=new LinkedHashMap<String, Object>();
	}
	
	private Global(){}
	
	public static synchronized void put(String key,Object value){
		cache.put(key, value);
	}
	 
	public static  Object  get(String key){
		return cache.get(key);
	}
	
	public static synchronized  Object remove(String key){
		return cache.remove(key);
	}
	
	public static boolean isContainedKey(String key){
		return cache.containsKey(key);
	}
	
	
}
