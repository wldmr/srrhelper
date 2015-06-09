package net.wldmr.srraide.cpack;

public class Values {
	public static abstract class BaseValue {}

	public static class NoValue extends BaseValue implements Ops.Arg<Object> {
		@Override public String toString() { return null; }
	}

	public static class Value<T> extends BaseValue implements Ops.Arg<T> {
		private Object value;
		public Value(Object value) {
			super();
			this.value = value;
		}
		@Override public String toString() { return value.toString(); }
	}
		
	//public static class StringValue extends Value implements Ops.Arg<T> 
	//	{ public StringValue(Object value) { super(value); } }
	//public static class IntValue extends Value implements Ops.Arg<T> 
	//	{ public IntValue(Object value) { super(value); } }
	//public static class FloatValue extends Value implements Ops.Arg<T> 
	//	{ public FloatValue(Object value) { super(value); } }
	//public static class BoolValue extends Value implements Ops.Arg<T> 
	//	{ public BoolValue(Object value) { super(value); } }
	//public static class CallValue extends Value implements Ops.Arg<T> 
	//	{ public CallValue(Object value) { super(value); } }
}
