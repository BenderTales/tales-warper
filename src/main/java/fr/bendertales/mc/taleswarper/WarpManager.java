package fr.bendertales.mc.taleswarper;

import java.util.Optional;
import java.util.stream.Stream;

import fr.bendertales.mc.taleswarper.data.PlayerWarpData;
import fr.bendertales.mc.taleswarper.data.Warp;
import fr.bendertales.mc.taleswarper.data.WarpLocation;
import fr.bendertales.mc.taleswarper.data.WarpPlayerCache;
import fr.bendertales.mc.taleswarper.exceptions.WarpNotFoundException;
import fr.bendertales.mc.taleswarper.exceptions.WarperException;
import fr.bendertales.mc.taleswarper.data.persisted.WarpsRepositories;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
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

	public Stream<Warp> getAllWarps() {
		return warpsRepositories.getAll();
	}

	public void createWarp(String name, ServerPlayerEntity player, boolean force) throws WarperException {
		if ("*".equals(name)) {
			throw new WarperException("* is a forbidden name");
		}
		Warp warp;
		if (!force) {
			warp = warpsRepositories.get(name);
			if (warp != null) {
				throw new WarperException("This warp already exist. Delete it before or use --force");
			}
		}

		var warpLocation = WarpLocation.from(player);

		warp = new Warp();
		warp.setName(name);
		warp.setCreator(player.getEntityName());
		warp.setWarpLocation(warpLocation);

		warpsRepositories.create(warp);
	}

	public void deleteWarp(String name) throws WarpNotFoundException {
		var warp = warpsRepositories.get(name);
		if (warp == null) {
			throw new WarpNotFoundException();
		}
		warpsRepositories.remove(warp);
	}

	public Warp getWarp(String warpName) throws WarpNotFoundException {
		var warp = warpsRepositories.get(warpName);
		if (warp == null) {
			throw new WarpNotFoundException();
		}
		return warp;
	}

	public void teleport(ServerPlayerEntity player, Warp warp) {
		var location = warp.getWarpLocation();

		teleport(player, location);
	}

	public void teleport(ServerPlayerEntity player, WarpLocation location) {
		var previousLocation = WarpLocation.from(player);

		FabricDimensions.teleport(player, location.world(), location.asTeleportTarget());

		var warpData = playerCache.getOrCreate(player);
		warpData.addPreviousLocation(previousLocation);
	}

	public void teleportBack(ServerPlayerEntity player) throws WarperException {
		var warpData = playerCache.getOrCreate(player);
		var optPrevLoc = warpData.getPreviousLocation();
		if (optPrevLoc.isPresent()) {
			var previousLocation = optPrevLoc.get();
			FabricDimensions.teleport(player, previousLocation.world(), previousLocation.asTeleportTarget());
			return;
		}
		throw new WarperException("Player has no previous location");
	}
}
