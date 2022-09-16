package fr.bendertales.mc.taleswarper.commands.suggestions;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import fr.bendertales.mc.talesservercommon.helpers.Perms;
import fr.bendertales.mc.taleswarper.WarpManager;
import fr.bendertales.mc.taleswarper.data.Warp;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;


public class AccessibleWarpSuggestionProvider implements SuggestionProvider<ServerCommandSource> {

	private final WarpManager warpManager;

	public AccessibleWarpSuggestionProvider(WarpManager warpManager) {
		this.warpManager = warpManager;
	}

	@Override
	public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context,
	                                                     SuggestionsBuilder builder) throws CommandSyntaxException {

		var source = context.getSource();

		if (source.hasPermissionLevel(1)
		    || Perms.has(source, "warper.warps.*")) {
			return CommandSource.suggestMatching(warpManager.getAllWarps().map(Warp::getName), builder);
		}

		if (!source.isExecutedByPlayer()) {
			return Suggestions.empty();
		}

		var player = source.getPlayerOrThrow();
		return CommandSource.suggestMatching(
				warpManager.getAllWarps()
					.filter(warp -> Perms.has(player, "warper.warps." + warp.getKey()))
		                .map(Warp::getName),
				builder);
	}
}
