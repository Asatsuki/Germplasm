package asatsuki256.germplasm.core.item;

import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import java.util.List;

import javax.annotation.Nullable;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.core.block.GermplasmBlocks;
import asatsuki256.germplasm.core.tileentity.TileGeneticCrop;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemSeedSample extends ItemBase implements IPlantable {
	
	public ItemSeedSample() {
		super(false);
	}
	
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        
		if(facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, itemstack) && worldIn.isAirBlock(pos.up())) {
			IBlockState soil = worldIn.getBlockState(pos);
			if(soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), EnumFacing.UP, this)) {
				//ブロックを設置
				worldIn.setBlockState(pos.up(), getPlant(worldIn, pos));
				
				//タイルエンティティに遺伝情報をセット
				TileEntity tile = worldIn.getTileEntity(pos.up());
				IGenome genome = GeneAPI.nbtHelper.getGenomeFromIndividualNBT(itemstack.getTagCompound());
				if(genome != null && tile != null && tile instanceof TileGeneticCrop) {
					((TileGeneticCrop) tile).setGenome(genome);
				}
				
				if (player instanceof EntityPlayerMP)
	            {
	                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos.up(), itemstack);
	                itemstack.shrink(1);
	            }
				return EnumActionResult.SUCCESS;
			}
        }
		return EnumActionResult.FAIL;
    }

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		IBlockState state = GermplasmBlocks.genetic_crop.getDefaultState();
		return GermplasmBlocks.genetic_crop.getDefaultState();
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt != null) {
			IGenome genome = GeneAPI.nbtHelper.getGenomeFromIndividualNBT(nbt);
			if(genome != null) {
				tooltip.add(I18n.format(UNLOC_PREFIX + "tooltip.has_genome"));
				tooltip.add(I18n.format(UNLOC_PREFIX + "tooltip.generation", genome.getGeneration()));
				if(genome.isAnalyzed()) {
					tooltip.add(I18n.format(UNLOC_PREFIX + "tooltip.analyzed"));
				}
				if(genome.getName() != null) {
					
				}
			}
		}
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
    {
		String name = super.getItemStackDisplayName(stack);
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt != null) {
			IGenome genome = GeneAPI.nbtHelper.getGenomeFromIndividualNBT(nbt);
			if(genome != null) {
				String genomeName = genome.getName();
				if(genomeName != null) {
					name += " (" + genomeName + ")";
				}
			}
		}
		return name;
    }

}
