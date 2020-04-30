package asatsuki256.germplasm.core.item;

import asatsuki256.germplasm.core.GermplasmCore;
import net.minecraft.item.ItemFood;

public class ItemGmpmFood extends ItemFood{

	public ItemGmpmFood(int amount, float saturation, boolean isWolfFood) {
		super(amount, saturation, isWolfFood);
		this.setCreativeTab(GermplasmCore.tabAgrigenetics);
	}

}
