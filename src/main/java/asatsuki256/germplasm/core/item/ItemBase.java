package asatsuki256.germplasm.core.item;

import java.util.List;

import javax.annotation.Nullable;

import asatsuki256.germplasm.core.GermplasmCore;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBase extends Item {
	
	private String description = "";
	
	public ItemBase() {
		super();
		this.setCreativeTab(GermplasmCore.tabAgrigenetics);
	}
	
	public ItemBase(boolean inTab) {
		super();
		if (inTab) this.setCreativeTab(GermplasmCore.tabAgrigenetics);
	}
	
	public ItemBase(CreativeTabs creativeTab) {
		super();
		this.setCreativeTab(creativeTab);
	}
	
	public ItemBase(String description) {
		this();
		this.description = description;
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(!description.isEmpty()) {
			tooltip.add(I18n.format(description));
		}
	}

}
