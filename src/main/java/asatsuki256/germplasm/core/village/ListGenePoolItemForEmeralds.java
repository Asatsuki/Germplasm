package asatsuki256.germplasm.core.village;

import java.util.Random;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.GenePool;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class ListGenePoolItemForEmeralds implements ITradeList {
	
	public ItemStack itemStack;
	public PriceInfo priceInfo;
	public GenePool genePool;
	
	public ListGenePoolItemForEmeralds(ItemStack item, PriceInfo price, String genePool)
    {
        this.itemStack = item;
        this.priceInfo = price;
        this.genePool = GeneAPI.genePoolRegistry.pool(genePool);
    }
	
	@Override
	public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
    {
        int i = 1;

        if (this.priceInfo != null)
        {
            i = this.priceInfo.getPrice(random);
        }

        ItemStack buy = new ItemStack(Items.EMERALD, i, 0);
        ItemStack sell = itemStack.copy();
        IGenome genome = GeneAPI.geneHelper.getGenomeFromPool(random, genePool, 4);
        NBTTagCompound nbt = new NBTTagCompound();
        GeneAPI.nbtHelper.setGenomeToIndividualNBT(nbt, genome);
        sell.setTagCompound(nbt);
        recipeList.add(new MerchantRecipe(buy, sell));
    }

}
