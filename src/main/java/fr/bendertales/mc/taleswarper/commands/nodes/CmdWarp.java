package fr.bendertales.mc.taleswarper.commands.nodes;

import java.util.List;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import fr.bendertales.mc.talesservercommon.commands.TalesCommand;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import fr.bendertales.mc.talesservercommon.helpers.Perms;
import fr.bendertales.mc.taleswarper.WarpManager;
import fr.bendertales.mc.taleswarper.data.Warp;
import fr.bendertales.mc.taleswarper.exceptions.WarpNotFoundException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class CmdWarp implements TalesCommandNode, TalesCommand {

	private final List<String>            permissions  = List.of("warper.commands.*", "warper.commands.warp.self");
	private final List<String>            permissionsOther  = List.of("warper.commands.*", "warper.commands.warp.other");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_MEDIOR, permissions);
	private final CommandNodeRequirements requirementsOther = CommandNodeRequirements.of(OP_MEDIOR, permissionsOther);

	private final WarpManager warpManager;

	public CmdWarp(WarpManager warpManager) {
		this.warpManager = warpManager;
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("warp")
		       .requires(requirements.asPredicate())
		       .then(
				    argument("warp-name", StringArgumentType.word())
				    .executes(this::warpSelf)
				    .then(
						literal("as")
						.requires(requirementsOther.asPredicate())
						.then(
							argument("players", EntityArgumentType.players())
							.executes(this::warpOther)
						)
				    )
		       );
	}

	private int warpOther(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();
		var playersSelector = context.getArgument("players", EntitySelector.class);
		var players = playersSelector.getPlayers(source);
		var warpName = context.getArgument("warp-name", String.class);

		try {
			var warp = warpManager.getWarp(warpName);
			if (hasWarpRight(source, warp)) {
				for (ServerPlayerEntity player : players) {
					warpManager.teleport(player, warp);
				}
				source.sendFeedback(Text.of("Teleported " + players.size() + " players"), false);
			}
			else {
				source.sendFeedback(Text.of("You cannot warp to " + warp.getName()), false);
			}
		}
		catch (WarpNotFoundException e) {
			throw asCommandException(e);
		}

		return 1;
	}

	private int warpSelf(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();
		var player = source.getPlayerOrThrow();
		var warpName = context.getArgument("warp-name", String.class);

		try {
			var warp = warpManager.getWarp(warpName);
			if (hasWarpRight(source, warp)) {
				warpManager.teleport(player, warp);
				player.sendMessage(Text.of("Teleported to " + warp.getName()), true);
			}
			else {
				player.sendMessage(Text.of("You cannot warp to " + warp.getName()), true);
			}
		}
		catch (WarpNotFoundException e) {
			throw asCommandException(e);
		}
		return 1;
	}

	private boolean hasWarpRight(CommandSource source, Warp warp) {
		return source.hasPermissionLevel(2)
	        || Perms.has(source, "warper.warps.*")
			|| Perms.has(source, "warper.warps." + warp.getKey());
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}

}
