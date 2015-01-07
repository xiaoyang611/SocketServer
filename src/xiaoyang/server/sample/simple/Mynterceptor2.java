package xiaoyang.server.sample.simple;

import xiaoyang.server.interceptor.Interceptor;
import xiaoyang.server.interceptor.InvocationHandle;

public class Mynterceptor2 implements Interceptor{

	@Override
	public void intercept(InvocationHandle handler) {
		System.out.println("Mynterceptor2 before invoke()");
		handler.invoke();
		System.out.println("Mynterceptor2 after invoke()");
	}
	
}
