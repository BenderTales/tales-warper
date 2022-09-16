package fr.bendertales.mc.taleswarper.data.persisted;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;


public class WarpLocationProperties {

	private Identifier world;
	private Vec3d      position;
	private float yaw;
	private float pitch;

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

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
}
