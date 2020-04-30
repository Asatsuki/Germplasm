package asatsuki256.germplasm.core.item;

import java.util.List;

import javax.annotation.Nullable;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.unit.IGene;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import asatsuki256.germplasm.api.gene.unit.ITrait;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemGeneSample extends ItemBase {
	
	public ItemGeneSample() {
		super(false);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt != null) {
			IGermplasmUnitBase unit = GeneAPI.nbtHelper.getUnitFromIndividualNBT(nbt);
			if(unit != null && unit instanceof IGene) {
				IGene gene = (IGene) unit;
				for (ITrait trait : gene.getTraits()) {
					tooltip.add(trait.getDisplayName());
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
			IGermplasmUnitBase gene = GeneAPI.nbtHelper.getUnitFromIndividualNBT(nbt);
			if(gene != null && gene instanceof IGene) {
				String genomeName = gene.getDisplayName();
				if(genomeName != null) {
					name += " (" + genomeName + ")";
				}
			}
		}
		return name;
    }

}
