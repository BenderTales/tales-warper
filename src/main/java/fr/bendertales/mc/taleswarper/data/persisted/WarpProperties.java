package fr.bendertales.mc.taleswarper.data.persisted;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;


public class WarpProperties {

	private String      name;
	private String      creator;
	private Identifier  world;
	private Vec3d       position;

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

	public Identifier getWorld() {
		return world;
	}

	public void setWorld(Identifier world) {
		this.world = world;
	}

	public Vec3d getPosition() {
		return position;
	}

	public void setPosition(Vec3d position) {
		this.position = position;
	}
}
