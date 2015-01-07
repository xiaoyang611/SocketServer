package xiaoyang.server.log;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import xiaoyang.server.config.Global;

public class ServerLoggerFactory {
	
	@SuppressWarnings("rawtypes")
	public static Logger getLogger(Class clazz) {
		
		Logger logger=Logger.getLogger(clazz.getName());
		FileHandler fileHandler=null;
		
		if(logger.getHandlers().length==0){//说明已经设置过
			String file=(String) Global.get("file");
			String level=(String) Global.get("level");
			
			if(file!=null && !"".equals(file)){
				
				try {
					fileHandler=new FileHandler(file);
					fileHandler.setFormatter(new SimpleFormatter());
					logger.addHandler(fileHandler);
					logger.addHandler(new ConsoleHandler());
					logger.setUseParentHandlers(false);
				}  catch (IOException e) {
					throw new RuntimeException(e);
				}
				
			}
			
			if(level != null && !"".equals(level)){
				logger.setLevel(Level.parse(level.trim().toUpperCase()));
			}
			
		}
		
		return logger;
	}
	
	/**
	 * 抛出运行时异常并且记录日志
	 * @param logger 日志记录器
	 * @param msg    需要抛出异常以及日志需要记录的消息
	 * @param e      异常对象
	 */
	public static void throwRuntimeExceptionAndLog(Logger logger,String msg,Exception e){
		logger.log(Level.SEVERE, msg, e);
		throw new RuntimeException(msg);
	}
	
	/*public static void main(String[] args) {
		Logger logge=ServerLoggerFactory.getLogger(ServerLoggerFactory.class);
		logge.info("test test test");
	}*/
}
