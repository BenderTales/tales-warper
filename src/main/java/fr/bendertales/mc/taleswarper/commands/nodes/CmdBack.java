package fr.bendertales.mc.taleswarper.commands.nodes;

import java.util.List;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import fr.bendertales.mc.talesservercommon.commands.TalesCommand;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import fr.bendertales.mc.taleswarper.WarpManager;
import fr.bendertales.mc.taleswarper.exceptions.WarperException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class CmdBack implements TalesCommandNode, TalesCommand {

	//back = self
	//back as [player]

	private final List<String> permissions      = List.of("warper.commands.*", "warper.commands.warp.self");
	private final List<String> permissionsOther = List.of("warper.commands.*", "warper.commands.warp.other");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_MEDIOR, permissions);
	private final CommandNodeRequirements requirementsOther = CommandNodeRequirements.of(OP_MEDIOR, permissionsOther);

	private final WarpManager warpManager;

	public CmdBack(WarpManager warpManager) {
		this.warpManager = warpManager;
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("back")
		       .requires(requirements.asPredicate())
		       .executes(this::warpSelf)
		       .then(
			        literal("as")
			        .requires(requirementsOther.asPredicate())
			        .then(
						argument("players", EntityArgumentType.players())
						.executes(this::warpOthers)
			        )
		       );
	}

	private int warpOthers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();
		var entitySelector = context.getArgument("players", EntitySelector.class);
		var players = entitySelector.getPlayers(source);

		int errorCount = 0;

		for (ServerPlayerEntity player : players) {
			try {
				warpManager.teleportBack(player);
			}
			catch (WarperException e) {
				errorCount += 1;
			}
		}

		if (errorCount > 0) {
			source.sendFeedback(Text.of(errorCount + " had no previous location"), false);
		}

		return players.size() - errorCount;
	}

	private int warpSelf(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();
		var player = source.getPlayerOrThrow();

		try {
			warpManager.teleportBack(player);
		}
		catch (WarperException e) {
			throw asCommandException(e);
		}

		return 1;
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}

}
