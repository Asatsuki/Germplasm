package asatsuki256.germplasm.core.network;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class GermplasmPacketHandler {
	
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
	
	private static int id = 0;
	
	public static void init() {
		INSTANCE.registerMessage(MHandlerConfig.class, MessageConfig.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MHandlerEnergyStorage.class, MessageEnergyStorage.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MHandlerGenomeAnalyzer.class, MessageGenomeAnalyzer.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MHandlerGeneticCrop.class, MessageGeneticCrop.class, id++, Side.CLIENT);
		
		INSTANCE.registerMessage(MHandlerResearchDeskServer.class, MessageResearchDeskServer.class, id++, Side.SERVER);
		INSTANCE.registerMessage(MessageGenomeFilter.Handler.class, MessageGenomeFilter.class, id++, Side.SERVER);
		INSTANCE.registerMessage(MessageSampleSorter.Handler.class, MessageSampleSorter.class, id++, Side.SERVER);
		
	}

}
