package fr.bendertales.mc.taleswarper;

import fr.bendertales.mc.taleswarper.data.Warp;
import fr.bendertales.mc.taleswarper.data.WarperException;
import fr.bendertales.mc.taleswarper.data.persisted.WarpsRepositories;
import net.minecraft.server.network.ServerPlayerEntity;


public class WarpManager {

	private final WarpsRepositories warpsRepositories = new WarpsRepositories();

	public void createWarp(String name, ServerPlayerEntity player, boolean force) throws WarperException {
		Warp warp;
		if (!force) {
			warp = warpsRepositories.get(name);
			if (warp != null) {
				throw new WarperException("This warp already exist. Delete it before or use --force");
			}
		}

		warp = new Warp();
		warp.setName(name);
		warp.setCreator(player.getEntityName());
		warp.setPosition(player.getPos());
		warp.setWorld(player.getWorld());

		warpsRepositories.create(warp);
	}

	public void deleteWarp(String name) throws WarperException {
		var warp = warpsRepositories.get(name);
		if (warp == null) {
			throw new WarperException("This warp does not exist");
		}
		warpsRepositories.delete(name);
	}
}
