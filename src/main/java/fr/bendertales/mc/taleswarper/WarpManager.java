package fr.bendertales.mc.taleswarper;

import fr.bendertales.mc.taleswarper.data.Warp;
import fr.bendertales.mc.taleswarper.data.WarpLocation;
import fr.bendertales.mc.taleswarper.data.WarpPlayerCache;
import fr.bendertales.mc.taleswarper.data.WarperException;
import fr.bendertales.mc.taleswarper.data.persisted.WarpsRepositories;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;


public class WarpManager {

	private final WarpsRepositories warpsRepositories = new WarpsRepositories();
	private final WarpPlayerCache playerCache = new WarpPlayerCache();

	public void init(MinecraftServer server) {
		warpsRepositories.setMinecraftServer(server);
		load();
	}

	public void reload() {
		playerCache.clear();
		load();
	}

	private void load() {
		warpsRepositories.loadAll();
	}

	public void createWarp(String name, ServerPlayerEntity player, boolean force) throws WarperException {
		Warp warp;
		if (!force) {
			warp = warpsRepositories.get(name);
			if (warp != null) {
				throw new WarperException("This warp already exist. Delete it before or use --force");
			}
		}

		var warpLocation = new WarpLocation(player.getWorld(), player.getPos());

		warp = new Warp();
		warp.setName(name);
		warp.setCreator(player.getEntityName());
		warp.setWarpLocation(warpLocation);

		warpsRepositories.create(warp);
	}

	public void deleteWarp(String name) throws WarperException {
		var warp = warpsRepositories.get(name);
		if (warp == null) {
			throw new WarperException("This warp does not exist");
		}
		warpsRepositories.remove(warp);
	}
}
