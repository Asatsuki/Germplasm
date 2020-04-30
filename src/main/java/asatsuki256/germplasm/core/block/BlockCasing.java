package asatsuki256.germplasm.core.block;

import asatsuki256.germplasm.core.GermplasmCore;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockCasing extends Block {
	
	public BlockCasing() {
		super(Material.IRON);
		this.setCreativeTab(GermplasmCore.tabAgrigenetics);
		this.setLightOpacity(0);
		this.setHardness(5.0f);
		this.setSoundType(SoundType.METAL);
	}

}
