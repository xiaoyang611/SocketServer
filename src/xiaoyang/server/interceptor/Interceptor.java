package xiaoyang.server.interceptor;

/**
 * @author Ф��
 * �������ӿ�
 */
public interface Interceptor {
	
	/**
	 * ���ط��������ڴ˱�д�����߼����Ƿ����InvocationHandle.invoke()�������Ƿ񽫶���������һ��������
	 */
	public void intercept(InvocationHandle handler);
	
}
