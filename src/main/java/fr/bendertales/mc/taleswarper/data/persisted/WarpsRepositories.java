package fr.bendertales.mc.taleswarper.data.persisted;

import java.util.List;

import fr.bendertales.mc.talesservercommon.repository.ModPaths;
import fr.bendertales.mc.talesservercommon.repository.data.AAllFileCachedRepository;
import fr.bendertales.mc.talesservercommon.repository.serialization.CommonSerializers;
import fr.bendertales.mc.talesservercommon.repository.serialization.JsonSerializerRegistration;
import fr.bendertales.mc.taleswarper.ModConstants;
import fr.bendertales.mc.taleswarper.data.Warp;
import fr.bendertales.mc.taleswarper.data.WarpLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;


public class WarpsRepositories extends AAllFileCachedRepository<String, Warp, WarpProperties> {

	private MinecraftServer minecraftServer;

	public WarpsRepositories() {
		super(WarpProperties.class,
		      ModPaths.createDataFolder(ModConstants.MODID, "data/warps"));
	}

	public void setMinecraftServer(MinecraftServer minecraftServer) {
		this.minecraftServer = minecraftServer;
	}

	@Override
	protected List<JsonSerializerRegistration<?>> getSerializers() {
		return List.of(
			CommonSerializers.identifier(),
			CommonSerializers.vec3d()
		);
	}

	@Override
	protected WarpProperties getDefaultConfiguration() {
		WarpProperties properties = new WarpProperties();
		// leave everything null since it should actually not be used
		return properties;
	}

	@Override
	protected Warp transformAfterRead(WarpProperties props) {
		var worldId = props.getWorld();
		var worldKey = RegistryKey.of(Registry.WORLD_KEY, worldId);
		var world = minecraftServer.getWorld(worldKey);

		var location = new WarpLocation(world, props.getPosition());

		Warp warp = new Warp();

		warp.setName(props.getName());
		warp.setCreator(props.getCreator());
		warp.setWarpLocation(location);

		return warp;
	}

	@Override
	protected WarpProperties transformBeforeSave(Warp warp) {

		var warpLocation = warp.getWarpLocation();

		WarpProperties properties = new WarpProperties();

		properties.setName(warp.getName());
		properties.setCreator(warp.getCreator());
		properties.setPosition(warpLocation.position());
		properties.setWorld(warpLocation.world().getRegistryKey().getValue());

		return properties;
	}

	@Override
	@Deprecated
	public Warp load(String key) {
		var file = dataFolder.resolve(key + ".json");
		var fileContent = tryLoadContent(file);
		return transformAfterRead(fileContent);
	}

	@Override
	@Deprecated
	public void save(String s, Warp warp) {
		var file = dataFolder.resolve(warp.getName() + ".json");
		var fileContent = transformBeforeSave(warp);
		trySaveContent(file, fileContent);
	}
}
