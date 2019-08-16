package protocol;

import java.util.Set;

import org.reflections.Reflections;

public class testpmap {

	public static void main(String[] args) {
		Reflections reflections = new Reflections("protocol");
		Set<Class<?>> msgscls = reflections.getTypesAnnotatedWith(MyMessage.class);
		for (Class<?> c : msgscls) {
			MyMessage a = c.getAnnotation(MyMessage.class);
			String value = a.value();
			System.out.println(value);
		}
	}

}
