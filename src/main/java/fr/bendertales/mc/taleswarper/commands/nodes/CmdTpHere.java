package fr.bendertales.mc.taleswarper.commands.nodes;

import java.util.List;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import fr.bendertales.mc.talesservercommon.commands.TalesCommand;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import fr.bendertales.mc.taleswarper.WarpManager;
import fr.bendertales.mc.taleswarper.data.WarpLocation;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class CmdTpHere implements TalesCommandNode, TalesCommand {

	private final List<String>            permissions  = List.of("warper.commands.*", "warper.commands.tphere");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_MEDIOR, permissions);

	private final WarpManager warpManager;

	public CmdTpHere(WarpManager warpManager) {
		this.warpManager = warpManager;
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("tphere")
		       .requires(requirements.asPredicate())
		       .then(
				    argument("players", EntityArgumentType.players())
				    .executes(this::tpHere)
		       );
	}

	private int tpHere(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();
		var player = source.getPlayerOrThrow();
		var playersSelector = context.getArgument("players", EntitySelector.class);
		var players = playersSelector.getPlayers(source);

		var location = WarpLocation.from(player);
		for (ServerPlayerEntity target: players) {
			warpManager.teleport(target, location);
			target.sendMessage(Text.of("You were teleported"), true);
		}

		return 1;
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}
}
