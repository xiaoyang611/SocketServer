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
		
		if(logger.getHandlers().length==0){//˵���Ѿ����ù�
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
	 * �׳�����ʱ�쳣���Ҽ�¼��־
	 * @param logger ��־��¼��
	 * @param msg    ��Ҫ�׳��쳣�Լ���־��Ҫ��¼����Ϣ
	 * @param e      �쳣����
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
