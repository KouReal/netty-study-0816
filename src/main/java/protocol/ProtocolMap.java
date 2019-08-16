package protocol;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolMap {
	private static Logger logger = LoggerFactory.getLogger(ProtocolMap.class);
	private static Map<Integer, String> pmap_name = new HashMap<>();
	private static Map<Integer,Class<?>> pmap = new HashMap<>();

	private static void setpmap() throws ProtocolException{
		Header header = new Header();
		Class<?> header_cls = header.getClass();
		Field[] header_fs = header_cls.getDeclaredFields();
		Map<String, Integer> namemap = new HashMap<>();
		for (Field f : header_fs) {
			f.setAccessible(true);
			try {
				namemap.put(f.getName(),(Integer)f.get(header));
				pmap_name.put((Integer)f.get(header), f.getName());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		logger.info("setpmap:{}",namemap);
		
		Reflections reflections = new Reflections("protocol");
		Set<Class<?>> msg_cls_set = reflections.getTypesAnnotatedWith(MyMessage.class);
		logger.info("class sets:{}",msg_cls_set);
		for (Class<?> msg_cls : msg_cls_set) {
			MyMessage anno = msg_cls.getAnnotation(MyMessage.class);
			String name = anno.value();
			if(!namemap.containsKey(name)){
				throw new ProtocolException("协议class:"+msg_cls+"的MyMessage注解值:【"+name+"】对应的协议未定义");
			}
			Integer protocol_id = namemap.get(name);
			pmap.put(protocol_id, msg_cls);
		}
		for (Field f : header_fs) {
			f.setAccessible(true);
			try {
				Integer id = (Integer)f.get(header);
				if(!pmap.containsKey(id)){
					pmap.put(id, null);
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static boolean checkprotocol(Integer id) throws ProtocolException{
		if(pmap_name.isEmpty()){
			setpmap();
		}
		return pmap_name.containsKey(id);
	}
	public static Class<?> getclass(Integer id) throws ProtocolException{
		if(pmap_name.isEmpty()){
			setpmap();
		}
		return pmap.get(id);
	}
}
