package fr.bendertales.mc.taleswarper.commands.nodes;

import java.util.List;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;


public class CmdTop implements TalesCommandNode {

	private final List<String>            permissions  = List.of("warper.commands.*", "warper.commands.top");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_JUNIOR, permissions);

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("top")
				       .requires(getRequirements().asPredicate())
				       .executes(this::moveToTop);
	}

	private int moveToTop(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();
		var player = source.getPlayerOrThrow();
		var playerBlockPos = player.getBlockPos();

		var world = player.getWorld();
		final int maxY = world.getTopY();
		final int minY = Math.max(world.getBottomY(), playerBlockPos.getY());

		boolean isBelowCeiling = false;
		int y = maxY;
		boolean found = false;
		for( ; y > minY; y -= 1) {
			var currentBlockPos = playerBlockPos.withY(y);
			var blockState = world.getBlockState(currentBlockPos);
			if (blockState.isAir()) {
				isBelowCeiling = true;
				continue;
			}

			if (isBelowCeiling && !blockState.isAir()) {
				found = true;
				break;
			}
		}

		if (found) {
			player.teleport(playerBlockPos.getX(), y+1 , playerBlockPos.getZ());
			return 1;
		}

		return -1;
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}
}
