package xiaoyang.server.interceptor;

/**
 * @author 肖扬
 * 拦截器接口
 */
public interface Interceptor {
	
	/**
	 * 拦截方法，可在此编写拦截逻辑，是否调用InvocationHandle.invoke()决定了是否将动作传给下一个拦截器
	 */
	public void intercept(InvocationHandle handler);
	
}
