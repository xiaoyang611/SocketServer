package xiaoyang.server.interceptor;

public class DefaultInterceptor implements Interceptor{

	@Override
	public void intercept(InvocationHandle handler) {
		System.out.println("DefaultInterceptor before invoke()");
		handler.invoke();
		System.out.println("DefaultInterceptor after invoke()");
	}
	
}
