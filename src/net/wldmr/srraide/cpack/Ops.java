package net.wldmr.srraide.cpack;

import java.util.StringJoiner;

public class Ops {

		static class Op {
			private String functionName;
			private Arg[] args;

			public String getFunctionName() { return functionName; }
			public Arg[] getArgs() { return args; }
			
			Op(String functionName) {
				this.functionName = functionName;
			}
			Op(String functionName, Arg[] args) {
				this(functionName);
				this.args = args;
			}
			
			@Override
			public String toString() {
				StringJoiner sj = new StringJoiner(", ", "(", ")");
				for (Arg arg : args)
					sj.add(arg.toString());
				return functionName + " " + sj.toString();
			}
		}
		
		static interface Arg {};
		static abstract class Const<T> implements Arg {
			private T value;
			Const(T value) { this.value = value; }
			T getValue() { return value; }

			@Override public String toString() { return value.toString(); }

			@Override
			public boolean equals(Object other) {
				if (other.getClass() == value.getClass())
					return value.equals(other);
				else
					return false;
			}
		}
		static class StringValue extends Const<String>
			{ StringValue(String value) { super(value); } }
		static class IntValue extends Const<Integer>
			{ IntValue(int value) { super(value); } }
		static class FloatValue extends Const<Float>
			{ FloatValue(float value) { super(value); } }
		/** A reference. A container object can use it to search for the actual object referenced.
		 * 
		 * This is not directly found in the parse tree; it has to be inferred (from the function name),
		 * when processing the tree.
		 * 
		 **/
		// XXX: Might not strictly be necessary. We'll see.
		static class Ref<T extends Identifiable> implements Arg {
			private String id;
			Ref(String id) { this.id = id; }
			String getID() { return id; }
			@Override public String toString() { return "->"+getID(); }
		}
		
		static class Function extends Op implements Arg
			{ Function(String functionName, Arg... args) { super(functionName, args); } }
		
		static Function call_value(String functionName, Arg[] args) { return new Function(functionName, args); }
		static StringValue string_value(String x) { return new StringValue(x); }
		static IntValue int_value(int x) { return new IntValue(x); }
		static FloatValue float_value(float x) { return new FloatValue(x); }
		static <T extends Identifiable> Ref<T> ref(String id) { return new Ref<T>(id); }
}
