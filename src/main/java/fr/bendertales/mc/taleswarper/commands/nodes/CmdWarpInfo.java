package fr.bendertales.mc.taleswarper.commands.nodes;

import java.util.List;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import fr.bendertales.mc.talesservercommon.commands.TalesCommand;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import fr.bendertales.mc.taleswarper.WarpManager;
import fr.bendertales.mc.taleswarper.commands.suggestions.AccessibleWarpSuggestionProvider;
import fr.bendertales.mc.taleswarper.data.Warp;
import fr.bendertales.mc.taleswarper.exceptions.WarpNotFoundException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class CmdWarpInfo implements TalesCommandNode, TalesCommand {

	private final List<String>            permissions  = List.of("warper.commands.*", "warper.commands.warpinfo");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_MEDIOR, permissions);

	private final WarpManager warpManager;

	public CmdWarpInfo(WarpManager warpManager) {
		this.warpManager = warpManager;
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("warpinfo")
		       .requires(requirements.asPredicate())
		       .then(
			        argument("warp-name", StringArgumentType.word())
			        .suggests(new AccessibleWarpSuggestionProvider(warpManager))
			        .executes(this::warpInfo)
		       );
	}

	private int warpInfo(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var warpName = context.getArgument("warp-name", String.class);

		try {
			var warp = warpManager.getWarp(warpName);
			context.getSource().sendFeedback(formatWarp(warp), false);
		}
		catch (WarpNotFoundException e) {
			throw asCommandException(e);
		}

		return 1;
	}

	private Text formatWarp(Warp warp) {
		var location = warp.getWarpLocation();
		return Text.literal("Name: ").append(warp.getName())
		           .append("\nCreator: ").append(warp.getCreator())
		           .append("\nWorld: ").append(location.world().toString())
		           .append("\nPosition: ").append(location.position().toString());
	}


	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}

}
