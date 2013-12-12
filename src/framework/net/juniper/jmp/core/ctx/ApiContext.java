package net.juniper.jmp.core.ctx;

import java.util.Map;

import net.juniper.jmp.core.cache.CacheManager;
import net.juniper.jmp.core.cache.ICache;

public class ApiContext {
	private static ThreadLocal<ApiContextInfo> threadLocal = new ThreadLocal<ApiContextInfo>();
	public static void reset() {
		threadLocal.remove();
	}
	public static void init(ApiContextInfo apiContext) {
		threadLocal.set(apiContext);
	}
	
	public static PagingContext getPagingContext() {
		return threadLocal.get().getPageContext();
	}
	
	public static String getParameter(String key) {
		return threadLocal.get().getRequest().getParameter(key);
	}
	
	public static String[] getParameters(String key){
		return threadLocal.get().getRequest().getParameters(key);
	}
	
	public static Map<String, String[]> getParameterMap(){
		return threadLocal.get().getRequest().getParameterMap();
	}
	
	public static ICache getGlobalSessionCache() {
		String sessionId = threadLocal.get().getRequest().getSessionId();
		return CacheManager.getInstance().getStrongCache(sessionId);
	}
}
