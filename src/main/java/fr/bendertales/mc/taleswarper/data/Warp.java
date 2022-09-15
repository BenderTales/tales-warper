package fr.bendertales.mc.taleswarper.data;

import fr.bendertales.mc.talesservercommon.repository.data.HasKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;


public class Warp implements HasKey<String> {

	private String name;
	private String creator;
	private ServerWorld world;
	private Vec3d position;

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

	public ServerWorld getWorld() {
		return world;
	}

	public void setWorld(ServerWorld world) {
		this.world = world;
	}

	public Vec3d getPosition() {
		return position;
	}

	public void setPosition(Vec3d position) {
		this.position = position;
	}
}
