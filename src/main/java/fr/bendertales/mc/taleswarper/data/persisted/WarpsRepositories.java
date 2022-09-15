package fr.bendertales.mc.taleswarper.data.persisted;

import java.util.Collection;
import java.util.List;

import fr.bendertales.mc.talesservercommon.repository.ModPaths;
import fr.bendertales.mc.talesservercommon.repository.data.ASingleFileCachedRepository;
import fr.bendertales.mc.talesservercommon.repository.serialization.JsonSerializerRegistration;
import fr.bendertales.mc.taleswarper.ModConstants;
import fr.bendertales.mc.taleswarper.data.Warp;


public class WarpsRepositories extends ASingleFileCachedRepository<String, Warp, WarpsProperties> {
	public WarpsRepositories() {
		super(WarpsProperties.class,
		      ModPaths.createDataFolder(ModConstants.MODID).resolve("warps.json"));
	}

	@Override
	protected Collection<Warp> transformAfterRead(WarpsProperties warpsProperties) {
		return null;
	}

	@Override
	protected WarpsProperties transformBeforeSave(Collection<Warp> warps) {
		return null;
	}

	@Override
	protected List<JsonSerializerRegistration<?>> getSerializers() {
		return List.of();
	}

	@Override
	protected WarpsProperties getDefaultConfiguration() {
		return WarpsProperties.defaultWarps();
	}
}
