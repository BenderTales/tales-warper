package fr.bendertales.mc.taleswarper.data;

import fr.bendertales.mc.talesservercommon.repository.data.HasKey;


public class Warp implements HasKey<String> {

	private String name;
	private String creator;
	private WarpLocation warpLocation;

	@Override
	public String getKey() {
		return name;
	}

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

	public WarpLocation getWarpLocation() {
		return warpLocation;
	}

	public void setWarpLocation(WarpLocation warpLocation) {
		this.warpLocation = warpLocation;
	}
}
