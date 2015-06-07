package net.wldmr.srraide.cpack;

import java.util.ArrayList;
import java.util.List;

public class Scene extends CPackFile {
	private List<Trigger> triggers = new ArrayList<>();
	//private List<String> mapEvents;
	//private List<Goals> goals;
	//private List<Variable> variables;
	
	public class Trigger {
		private String name;
		private List<Ops.Event> events = new ArrayList<>();
		private List<Ops.Condition> conditions = new ArrayList<>();
		private List<Ops.Action> actions = new ArrayList<>();
		private List<Ops.Action> elseActions = new ArrayList<>();
		private String id;  // is actually nested in an IDRef, but I don't see the point in modeling that here.
		private boolean isActive;
		private boolean isOneshot;
	}
}
