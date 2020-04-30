package asatsuki256.germplasm.core.block;

import asatsuki256.germplasm.core.GermplasmCore;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class BlockDecorative extends Block {
	
	public BlockDecorative(Material material) {
		super(material);
		this.setCreativeTab(GermplasmCore.tabAgrigenetics);
		this.setLightOpacity(0);
		this.setHardness(5.0f);
		this.setSoundType(SoundType.METAL);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

}
