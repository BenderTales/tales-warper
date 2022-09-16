package fr.bendertales.mc.taleswarper.commands;

import java.util.stream.Stream;

import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import fr.bendertales.mc.taleswarper.WarpManager;
import fr.bendertales.mc.taleswarper.commands.nodes.CmdTop;
import fr.bendertales.mc.taleswarper.commands.nodes.CmdWarpInfo;
import fr.bendertales.mc.taleswarper.commands.nodes.CmdWarpSet;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;


public class CommandRegistrar {

	public static void registerCommands(WarpManager warpManager) {
		var commandRegistry = CommandRegistrationCallback.EVENT;

		commandRegistry.register((dispatcher, ra, env) ->
            buildCommands(warpManager).forEach(cmd -> dispatcher.register(cmd.asBrigadierNode()))
		);
	}

	private static Stream<TalesCommandNode> buildCommands(WarpManager warpManager) {
		return Stream.of(
			new CmdTop(),
			new CmdWarpSet(warpManager),
			new CmdWarpInfo(warpManager)
		);
	}
}
