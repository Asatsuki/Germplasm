package asatsuki256.germplasm.core.item;

import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockDesc extends ItemBlock {

	public ItemBlockDesc(Block block) {
		super(block);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format(getUnlocalizedName() + ".desc"));
	}

}
