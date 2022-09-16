package fr.bendertales.mc.taleswarper.data;


import java.util.Optional;


public class PlayerWarpData {

	private SizedStack<WarpLocation> previousLocations = new SizedStack<>(WarpLocation.class);

	public void addPreviousLocation(WarpLocation location) {
		previousLocations.add(location);
	}

	public Optional<WarpLocation> getPreviousLocation() {
		return previousLocations.pop();
	}
}
