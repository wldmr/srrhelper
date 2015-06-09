package net.wldmr.srraide.cpack;

/** Identifiable items are things that are addressable objects in the content pack.
 * 
 * This is an interface that all toplevel items (triggers, ...) must implement
 * in order to be addressed/found inside the content pack.
 */
public interface Identifiable {
	String getID();
	
	public static abstract class ByID implements Identifiable {
		private String id;
		ByID(String id) { this.id = id; }
		@Override public String getID() { return id; }
	}
}