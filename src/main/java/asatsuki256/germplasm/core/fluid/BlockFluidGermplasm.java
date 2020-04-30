package asatsuki256.germplasm.core.fluid;

import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFluidGermplasm extends BlockFluidClassic {
	
	BlockFluidGermplasm(FluidGermplasm fluid) {
		super(fluid, fluid.getMaterial());
		setRegistryName(fluid.getName());
		setUnlocalizedName(fluid.getUnlocalizedName());
		ForgeRegistries.BLOCKS.register(this);
	}
	
	@SideOnly(Side.CLIENT)
	void render() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LEVEL).build());
	}

}
