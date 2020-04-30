package asatsuki256.germplasm.core.item;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.filter.IGeneticFilter;
import asatsuki256.germplasm.api.gene.unit.IGene;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.GuiHandler;
import asatsuki256.germplasm.core.capability.handler.ItemHandlerGenomeFilter;
import asatsuki256.germplasm.core.gene.filter.FilterGenome;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemFilterGenome extends ItemGeneticFilter {
	
	public static final String GENE_NBT_KEY = NBT_PREFIX + "gene";
	public static final String PAIR_NUM_NBT_KEY = NBT_PREFIX + "pairNum";
	public static final String CHROMOSOME_NUM_NBT_KEY = NBT_PREFIX + "chromosomeNum";
	public static final String GENE_NUM_NBT_KEY = NBT_PREFIX + "geneNum";
	
	@Override
	public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new ItemHandlerGenomeFilter(stack, 1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.openGui(GermplasmCore.instance, GuiHandler.GUI_ID_GENOME_FILTER, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public IGeneticFilter getFilter(ItemStack item) {
		return getFilterGenome(item);
	}
	
	public static FilterGenome getFilterGenome(ItemStack item) {
		NBTTagCompound nbt = item.getTagCompound();
		if (nbt == null) return new FilterGenome(null, 0, 0, 0);
		IItemHandler inv = item.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		ItemStack geneItem = inv.getStackInSlot(0);
		IGermplasmUnitBase unit = GeneAPI.nbtHelper.getUnitFromIndividualNBT(geneItem.getTagCompound());
		IGene gene = unit != null && unit instanceof IGene ? (IGene) unit : null;
		int pairNum = nbt.getInteger(PAIR_NUM_NBT_KEY);
		int chromosomeNum = nbt.getInteger(CHROMOSOME_NUM_NBT_KEY);
		int geneNum = nbt.getInteger(GENE_NUM_NBT_KEY);
		return new FilterGenome(gene, pairNum, chromosomeNum, geneNum);
	}
	
	public static ItemStack setStats(ItemStack stack, int pairNum, int chromosomeNum, int geneNum) {
		NBTTagCompound nbt = stack.getTagCompound();
		nbt.setInteger(PAIR_NUM_NBT_KEY, pairNum);
		nbt.setInteger(CHROMOSOME_NUM_NBT_KEY, chromosomeNum);
		nbt.setInteger(GENE_NUM_NBT_KEY, geneNum);
		ItemStack newStack = stack.copy();
		newStack.setTagCompound(nbt);
		return newStack;
	}

}
