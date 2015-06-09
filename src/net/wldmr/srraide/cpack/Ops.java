package net.wldmr.srraide.cpack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Ops {
	//public static abstract class Op {
	//	private List<Values.Value> args = new ArrayList<>();
	//	public List<Values.Value> accessArgs() { return args; }
	//	
	//	public String getFunctionName() {
	//		return FunctionNameResolver.get(this.getClass());
	//	}

	//	/** Default implementation just joins name and arguments. */
	//	public String toString() {
	//		StringJoiner sj = new StringJoiner(", ", "(", ")");
	//		for (Values.Value arg : accessArgs())
	//			sj.add(arg.toString());
	//		return getFunctionName() + " " + sj.toString();
	//	}
	//}
	
	public static interface Arg<T> {}
	
	public static abstract class Fun0<R> implements Arg<R> {
		private R value;
		public R value() { return this.value; }
		public void value(R value) { this.value = value; }
	}
	public static abstract class Fun1<R, T1> extends Fun0<R> {
		private Arg<T1> arg1;
		public Arg<T1> arg1() { return arg1; }
		public void arg1(Arg<T1> arg1) { this.arg1 = arg1; }
	}
	public static abstract class Fun2<R, T1, T2> extends Fun1<R, T1> {
		private T2 arg2;
		public T2 arg2() { return arg2; }
		public void arg2(T2 arg2) { this.arg2 = arg2; }
	}
	public static abstract class Fun3<R, T1, T2, T3> extends Fun2<R, T1, T2> {
		private T3 arg3;
		public T3 arg3() { return arg3; }
		public void arg3(T3 arg3) { this.arg3 = arg3; }
	}

	//public static abstract class Function<T extends Returnable> extends Op {}
	//public static abstract class Event extends Function<Event> implements Returnable {}
	//public static abstract class Condition extends Fun<Values.BoolValue> {}
	//public static abstract class Action extends Op {}

	@FunctionName("Get Map Item (Event)")
	public static class GetMapItemEvent extends Fun1<Scene.MapEvent, Values.Value<String>> {}
	
	@FunctionName("On Conversation Complete")
	public static class OnConversationComplete extends Fun0<Values.NoValue> {}
	
	@FunctionName("Send Event")
	public static class SendEvent extends Fun1<Values.NoValue, Values.Value<String>> {}
	
	public static class MakeString extends Fun0<Values.Value<String>> {}
	public static class TakeString extends Fun1<Values.NoValue, Values.Value<String>> {}

	/** <h1>FunctionName Annotation</h1>
	 *
	 * More information about this in the blog post
	 * <a href="http://isagoksu.com/2009/creating-custom-annotations-and-making-use-of-them/">Creating Custom Annotations and Using Them</a>.
	 **/
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
		
		public static void processNested(Class<?> clazz) {
			for (Class<?> nested : clazz.getClasses())
				process(nested);
		}
	}
}
