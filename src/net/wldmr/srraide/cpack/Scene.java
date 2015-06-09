package net.wldmr.srraide.cpack;

import java.util.ArrayList;
import java.util.List;

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
		//private List<Ops.Event> events = new ArrayList<>();
		//public List<Ops.Event> accessEvents() { return events; }
		//private List<Ops.Condition> conditions = new ArrayList<>();
		//private List<Ops.Action> actions = new ArrayList<>();
		//private List<Ops.Action> elseActions = new ArrayList<>();
		//private boolean isActive;
		//private boolean isOneshot;
		
		Trigger(String id) {
			super(id);
		}
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
