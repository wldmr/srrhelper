package net.wldmr.srraide.cpack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Ops {
	private static abstract class Op {}

	public static abstract class Event extends Op {};
	public static abstract class Condition extends Op {};
	public static abstract class Action extends Op {};

	@FunctionName("On Item Interaction")
	public static class OnItemInteraction extends Event {};

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@interface FunctionName { String value(); }

	public static class FunctionNameResolver {
		private static Map<String, Class<?>> name2class = new HashMap<>();
		private static Map<Class<?>, String> class2name = new HashMap<>();
		
		public static void put(String name, Class<?> clazz) {
			name2class.put(name, clazz);
			class2name.put(clazz, name);
		}

		public static Class<?> get(String name) { return name2class.get(name); }
		public static String get(Class<?> clazz) { return class2name.get(clazz); }
		
		public static void process(Class<?> clazz) {
			if (clazz.isAnnotationPresent(FunctionName.class)) {
				System.out.println(clazz);
				FunctionName annotation = clazz.getAnnotation(FunctionName.class);
				put(annotation.value(), clazz);
			}
		}
		
		public static void process(Class<?>[] clazzez) {
			for (Class<?> clazz : clazzez)
				process(clazz);
		}
		
		public static void showAll() {
			for (Entry<String, Class<?>> entry : name2class.entrySet()) {
				System.out.println(entry.getKey() + " -> " + entry.getValue());
			}
		}
	}
}
