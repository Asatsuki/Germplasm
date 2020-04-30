package asatsuki256.germplasm.core.client;

import asatsuki256.germplasm.core.GermplasmConfig;
import asatsuki256.germplasm.core.network.GermplasmPacketHandler;
import asatsuki256.germplasm.core.network.MessageConfig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class GermplasmClientEventHooks {
	
	@SubscribeEvent
	public void onPlayerLoggedInEvent(PlayerLoggedInEvent event) {
		if(event.player instanceof EntityPlayerMP) {
			MessageConfig packet = new MessageConfig(GermplasmConfig.CONFIG_CORE.requireEnergy);
			GermplasmPacketHandler.INSTANCE.sendTo(packet , (EntityPlayerMP) event.player);
		}
	}
	
//	@SubscribeEvent
//	public void onModelBakeEvent(ModelBakeEvent event)
//	{
//		GermplasmCore.LOGGER.log(Level.DEBUG, "onModelBakeEvent");
//		IBakedModel modelMain;
//		Map<String, IBakedModel> modelSeed = new HashMap<String, IBakedModel>();
//		
//		for(TraitType traitType : GeneAPI.traitTypeRegistory.getTraitTypeList()) {
//			if(traitType instanceof TraitTypeHarvest) {
//				modelSeed.put(traitType.traitId, event.getModelRegistry().getObject(((TraitTypeHarvest) traitType).getItemModel()));
//			}
//		}
//		
//		ModelResourceLocation mrlMain = new ModelResourceLocation(MODID + ":seed_sample", "inventory");
//		ModelResourceLocation mrlInject = new ModelResourceLocation(MODID + ":seed_sample", "inventory");
//		
//		modelMain = event.getModelRegistry().getObject(mrlMain);
//		
//		event.getModelRegistry().putObject(mrlInject, new SeedBakedModel(modelMain, modelSeed));
//		
//	}
}
