package xiaoyang.server.interceptor;

import java.util.List;

import xiaoyang.server.core.Request;
import xiaoyang.server.core.ServerHandler;

/**
 * ���ô����������ж����������������ã�ά�ָ����������ĵ���˳��
 * @author Ф��
 */
public class InvocationHandle {
	
	//�������б�
	private List<Interceptor> interceptors;
	
	//��ǰ�õ��õ��������б�����
	private int interceptorIndex=0;
	
	//����˴����߼������Լ���Ӧ���������
	private ServerHandler action;
	private Request request;
	
	public InvocationHandle(Request request,ServerHandler action,List<Interceptor> interceptors){
		this.request=request;
		this.interceptors=interceptors;
		this.action=action;
	}
	
	public void invoke(){
		
		if(interceptorIndex==this.interceptors.size()){
			action.process(request);
			return ;
		}
		
		this.interceptors.get(interceptorIndex++).intercept(this);
	}
	
}
