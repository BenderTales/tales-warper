package fr.bendertales.mc.taleswarper;

import fr.bendertales.mc.taleswarper.commands.CommandRegistrar;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;


public class TalesWarper implements ModInitializer {
	@Override
	public void onInitialize() {
		var warpManager = new WarpManager();

		ServerLifecycleEvents.SERVER_STARTING.register(warpManager::init);
		CommandRegistrar.registerCommands(warpManager);
	}
}
