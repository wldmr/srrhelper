package net.wldmr.srraide.cpack;

import net.wldmr.srraide.cpack.Parsing.Node;
import net.wldmr.srraide.cpack.Scene.Trigger;
import net.wldmr.srraide.cpack.Scene.Trigger.*;

public class SceneBuilder implements CPackPartBuilder<Scene> {
	public Scene build(Node root) {
		Scene scene = new Scene(root.getKey());
		for (Node t : root.getChildren("triggers"))
			scene.accessTriggers().add(makeTrigger(t));
		return scene;
	}

	protected Trigger makeTrigger(Node root) {
		String id = root.getChild("idRef").getValue("id");
		String name = root.getValue("name");
		boolean isOneshot = Boolean.valueOf(root.getValue("is_oneshot"));
		boolean isActive = Boolean.valueOf(root.getValue("isActive"));
		Trigger trigger = new Trigger(id, name, isActive, isOneshot);
		
		for (Node n : root.getChildren("events"))
			trigger.accessEvents().add((Event) (new OpBuilder()).build(n));
		return trigger;
	}
}