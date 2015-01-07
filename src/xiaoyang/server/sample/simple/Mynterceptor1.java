package xiaoyang.server.sample.simple;

import xiaoyang.server.interceptor.Interceptor;
import xiaoyang.server.interceptor.InvocationHandle;

public class Mynterceptor1 implements Interceptor{

	@Override
	public void intercept(InvocationHandle handler) {
		System.out.println("Mynterceptor1 before invoke()");
		handler.invoke();
		System.out.println("Mynterceptor1 after invoke()");
	}
	
}
