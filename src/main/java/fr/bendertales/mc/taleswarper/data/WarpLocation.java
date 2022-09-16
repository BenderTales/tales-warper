package fr.bendertales.mc.taleswarper.data;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;


public record WarpLocation (
	ServerWorld world,
	Vec3d position
)
{ }
