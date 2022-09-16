package fr.bendertales.mc.taleswarper.data.persisted;


public class WarpProperties {

	private String      name;
	private String      creator;
	private WarpLocationProperties location;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public WarpLocationProperties getLocation() {
		return location;
	}

	public void setLocation(WarpLocationProperties location) {
		this.location = location;
	}
}
