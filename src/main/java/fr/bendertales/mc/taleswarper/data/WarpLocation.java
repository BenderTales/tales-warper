package fr.bendertales.mc.taleswarper.data;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;


public record WarpLocation (
	ServerWorld world,
	Vec3d position,
	float yaw,
	float pitch
)
{
	public static WarpLocation from(ServerPlayerEntity player) {
		return new WarpLocation(
			player.getWorld(),
			player.getPos(),
			player.getYaw(),
			player.getPitch()
		);
	}

	public TeleportTarget asTeleportTarget() {
		return new TeleportTarget(position, Vec3d.ZERO, yaw, pitch);
	}
}
