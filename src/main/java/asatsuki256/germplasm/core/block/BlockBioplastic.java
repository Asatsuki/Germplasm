package asatsuki256.germplasm.core.block;

import asatsuki256.germplasm.core.GermplasmCore;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBioplastic extends Block {
	
	private BlockRenderLayer layer = BlockRenderLayer.SOLID;
	
	public BlockBioplastic() {
		super(Material.ROCK);
		this.setCreativeTab(GermplasmCore.tabAgrigenetics);
		this.setHardness(2.0f);
		this.setSoundType(SoundType.STONE);
	}
	
	public BlockBioplastic setRenderLayer(BlockRenderLayer layer) {
		this.layer = layer;
		return this;
	}
	
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return layer;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return !(layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT);
    }
	
	@Override
	public boolean isFullCube(IBlockState state)
    {
        return !(layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT);
    }
	
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
		if (!(layer == BlockRenderLayer.CUTOUT || layer == BlockRenderLayer.TRANSLUCENT)) {
			return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
		}
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();
        if (block == this) {
        	return false;
        } else {
        	return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
    }

}
