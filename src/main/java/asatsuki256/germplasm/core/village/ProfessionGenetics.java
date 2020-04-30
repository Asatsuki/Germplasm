package asatsuki256.germplasm.core.village;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;

import java.util.Random;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class ProfessionGenetics extends VillagerProfession {
	
	private VillagerCareer[] careers;

    public ProfessionGenetics(){
        super(new ResourceLocation(MODID, "genetics").toString(),
        		new ResourceLocation(MODID, "textures/entity/villager/genetics.png").toString(),
        		new ResourceLocation(MODID, "textures/entity/zombie_villager/zombie_genetics.png").toString());
    }
    
    public void init(VillagerCareer... careers){
        this.careers = careers;
    }

    @Override
    public VillagerCareer getCareer(int id){
        return careers[id];
    }

    @Override
    public int getRandomCareer(Random rand){
        return rand.nextInt(careers.length);
    }

}
