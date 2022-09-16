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
import fr.bendertales.mc.taleswarper.exceptions.WarperException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class CmdWarpSet implements TalesCommandNode, TalesCommand {

	private final List<String>            permissions  = List.of("warper.commands.*", "warper.commands.warpset");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_MEDIOR, permissions);

	private final WarpManager warpManager;

	public CmdWarpSet(WarpManager warpManager) {
		this.warpManager = warpManager;
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("warpset")
		       .requires(getRequirements().asPredicate())
		       .then(
			        argument("name", StringArgumentType.word())
			        .executes(this::set)
			        .then(
						literal("--force")
						.executes(this::forceSet)
			        )
		       );
	}

	private int forceSet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return set(context, true);
	}

	private int set(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return set(context, false);
	}

	private int set(CommandContext<ServerCommandSource> context, boolean force) throws CommandSyntaxException {
		var source = context.getSource();
		var player = source.getPlayerOrThrow();

		var name = context.getArgument("name", String.class);
		try {
			warpManager.createWarp(name, player, force);
			player.sendMessage(Text.literal("Warp [" + name + "] created"), true);
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
