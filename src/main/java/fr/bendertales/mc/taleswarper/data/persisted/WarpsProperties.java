package fr.bendertales.mc.taleswarper.data.persisted;

import java.util.ArrayList;
import java.util.List;


public class WarpsProperties {

	private List<String> warps;

	public List<String> getWarps() {
		return warps;
	}

	public void setWarps(List<String> warps) {
		this.warps = warps;
	}

	public static WarpsProperties defaultWarps() {
		var properties = new WarpsProperties();

		properties.warps = new ArrayList<>(2);

		return properties;
	}
}
