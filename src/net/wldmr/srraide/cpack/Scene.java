package net.wldmr.srraide.cpack;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Scene extends CPackFile implements Identifiable {

	// inferred from filename: <Scene Name>.srt.txt
	// Not the scene title!
	private String sceneName;
	// For interface Identifiable
	@Override
	public String getID() { return sceneName; };
	
	
	private List<Trigger> triggers = new ArrayList<>();
	public List<Trigger> accessTriggers() { return triggers; }

	private List<MapEvent> mapEvents = new ArrayList<>();
	public List<MapEvent> accessMapEvents() { return mapEvents; }

	//private List<Goals> goals;
	//private List<Variable> variables;
	
	public Scene(String sceneName) {
		this.sceneName = sceneName;
	}

	public static class Trigger extends Identifiable.ByID {
		private String name;
		private List<Event> events = new ArrayList<>();
		public List<Event> accessEvents() { return events; }

		private List<Condition> conditions = new ArrayList<>();
		public List<Condition> accessConditions() { return conditions; }

		private List<Action> actions = new ArrayList<>();
		public List<Action> accessActions() { return actions; }

		private List<Action> elseActions = new ArrayList<>();
		public List<Action> accessElseActions() { return elseActions; }

		private boolean isActive = true;
		private boolean isOneshot = true;  // UI calls this "retain after firing", which has the logic reversed. :-/
		
		Trigger(String id, String name, boolean isActive, boolean isOneshot) {
			super(id);
			this.name = name;
			this.isActive = isActive;
			this.isOneshot = isOneshot;
		}
		
		static abstract class Event extends Ops.Op
			{ Event(String functionName) { super(functionName); } };
		static abstract class Condition extends Ops.Op
			{ Condition(String functionName) { super(functionName); } };
		static abstract class Action extends Ops.Op
			{ Action(String functionName) { super(functionName); } };
	}

	public static class MapEvent implements Identifiable {
		private String eventName;
		MapEvent(String eventName) {
			this.eventName = eventName;
		}

		@Override
		public String getID() { return eventName; }
	}
}
