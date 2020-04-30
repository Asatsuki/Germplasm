package asatsuki256.germplasm.core.item;

import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import java.util.List;

import javax.annotation.Nullable;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemGenomeSample extends ItemBase {
	
	public ItemGenomeSample() {
		super(false);
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
