package xiaoyang.server.core;


/**
 * @author 肖扬
 * 服务器端响应并处理请求的抽象类，由客户端程序员实现process（）方法实现具体的处理逻辑。
 */
abstract public class ServerHandler{
	
	/**
	 * 相应的处理逻辑由客户端程序员完成。
	 */
	public abstract void process(Request request);

}
