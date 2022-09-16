package fr.bendertales.mc.taleswarper.commands.nodes;

import java.util.List;
import java.util.stream.Collectors;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import fr.bendertales.mc.talesservercommon.commands.CommandNodeRequirements;
import fr.bendertales.mc.talesservercommon.commands.TalesCommand;
import fr.bendertales.mc.talesservercommon.commands.TalesCommandNode;
import fr.bendertales.mc.taleswarper.WarpManager;
import fr.bendertales.mc.taleswarper.data.Warp;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class CmdWarps implements TalesCommandNode, TalesCommand {

	private static final int pageSize = 20;
	private final List<String>            permissions  = List.of("warper.commands.*", "warper.commands.warps");
	private final CommandNodeRequirements requirements = CommandNodeRequirements.of(OP_MEDIOR, permissions);

	private final WarpManager warpManager;

	public CmdWarps(WarpManager warpManager) {
		this.warpManager = warpManager;
	}

	@Override
	public LiteralArgumentBuilder<ServerCommandSource> asBrigadierNode() {
		return literal("warps")
		       .requires(getRequirements().asPredicate())
		       .executes(this::warps)
		       .then(
			        argument("page", IntegerArgumentType.integer(1))
			        .executes(this::warpsWithPage)
		       );
	}

	private int warpsWithPage(CommandContext<ServerCommandSource> context) {
		var page = context.getArgument("page", Integer.class);
		return warps(context, page);
	}

	private int warps(CommandContext<ServerCommandSource> context) {
		return warps(context, 1);
	}

	private int warps(CommandContext<ServerCommandSource> context, int page) {
		var source = context.getSource();

		var result = warpManager.getAllWarps()
             .sorted((w1, w2) -> w1.getName().compareToIgnoreCase(w2.getName()))
             .skip((long) (page - 1) * pageSize)
             .limit(pageSize)
             .map(Warp::getName)
             .collect(Collectors.joining(", "));

		if (StringUtils.isNotBlank(result)) {
			source.sendFeedback(Text.of(page + ": " + result), false);
		}
		else {
			source.sendFeedback(Text.of("No warps were found"), false);
		}
		return 1;
	}

	@Override
	public CommandNodeRequirements getRequirements() {
		return requirements;
	}
}
