package asatsuki256.germplasm.core.village;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;

import asatsuki256.germplasm.core.item.GermplasmItems;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class GermplasmVillage {
	
	public static VillagerProfession geneticsProfession;
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<VillagerProfession> event) {
		register();
		event.getRegistry().registerAll(geneticsProfession);
		
		VillagerRegistry.instance().registerVillageCreationHandler(new VillageLabAgricultureCreationHandler());
		MapGenStructureIO.registerStructureComponent(VillageLabAgriculture.class, MODID+":lab_agriculture");
	}
	
	public static void register() {
		geneticsProfession = new ProfessionGenetics();
		((ProfessionGenetics)geneticsProfession).init(
				new VillagerCareer(geneticsProfession, MODID + ".agronomist")
					.addTrade(1,
						new ListGenePoolItemForEmeralds(new ItemStack(GermplasmItems.seed_sample), new PriceInfo(3, 5), "villager_default")
					).addTrade(2,
						new ListGenePoolItemForEmeralds(new ItemStack(GermplasmItems.seed_sample), new PriceInfo(3, 5), "villager_default")
					)
				);
	}

}
