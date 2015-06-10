package net.wldmr.srraide.cpack;

import java.util.ArrayList;
import java.util.List;

public class ContentPack extends CPackFile {
	//private String project_id;
	//private String project_name;
	//private String project_version;
	//private String author_name;
	//private String synopsis;
	//private boolean has_story;
	//private String preview_image;
	
	//private List<Conversation> convos;
	//private List<Item> items;
	//private List<Map> maps;

	private List<Scene> scenes = new ArrayList<>();
	public List<Scene> accessScenes() { return scenes; }

	//private List<Scratchpad> scratchpad;
	//private List<Story> stories;
}