package fr.bendertales.mc.taleswarper.data;

import java.util.UUID;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.server.network.ServerPlayerEntity;


public class WarpPlayerCache {

	private final Object2ObjectMap<UUID, PlayerWarpData> cache = new Object2ObjectOpenHashMap<>();

	public void clear() {
		cache.clear();
	}

	public PlayerWarpData getOrCreate(ServerPlayerEntity player) {
		return getOrCreate(player.getUuid());
	}

	public PlayerWarpData getOrCreate(UUID uuid) {
		return cache.computeIfAbsent(uuid, (id) -> new PlayerWarpData());
	}

}
