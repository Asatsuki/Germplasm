package asatsuki256.germplasm.core.compat.industrialforegoing;

import java.util.List;

import com.buuz135.industrial.api.plant.PlantRecollectable;

import asatsuki256.germplasm.core.block.BlockGeneticCrop;
import asatsuki256.germplasm.core.tileentity.TileGeneticCrop;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GermplasmPlantRecollectable extends PlantRecollectable {

	public GermplasmPlantRecollectable() {
		super("germplasm_genetic_crop");
	}

	@Override
	public boolean canBeHarvested(World world, BlockPos pos, IBlockState blockState) {
		if(blockState.getBlock() instanceof BlockGeneticCrop) {
			TileEntity te = world.getTileEntity(pos);
			if(te != null && te instanceof TileGeneticCrop) {
				TileGeneticCrop crop = (TileGeneticCrop) te;
				return crop.isRipe();
			}
		}
		return false;
	}

	@Override
	public List<ItemStack> doHarvestOperation(World world, BlockPos pos, IBlockState blockState) {
		if(blockState.getBlock() instanceof BlockGeneticCrop) {
			BlockGeneticCrop block = (BlockGeneticCrop) blockState.getBlock();
			TileEntity te = world.getTileEntity(pos);
			if(te != null && te instanceof TileGeneticCrop) {
				TileGeneticCrop crop = (TileGeneticCrop) te;
				crop.harvestGrow();
				return block.getYield(crop);
			}
		}
		return null;
	}

	@Override
	public boolean shouldCheckNextPlant(World arg0, BlockPos arg1, IBlockState arg2) {
		return true;
	}
}
