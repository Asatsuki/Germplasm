package asatsuki256.germplasm.core.tileentity;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import java.util.Random;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.core.gene.Genome;
import asatsuki256.germplasm.core.gene.trait.TraitTypeRegistry;
import asatsuki256.germplasm.core.item.GermplasmItems;
import asatsuki256.germplasm.core.network.GermplasmPacketHandler;
import asatsuki256.germplasm.core.network.MessageGeneticCrop;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.Biome;

public class TileGeneticCrop extends TileEntity {
	
	private Random rand = new Random();
	
	private IGenome genome;
	private int growthStage;
	private final int maxGrow = 7;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		NBTTagCompound nbtGenome = nbt.getCompoundTag(NBT_PREFIX + "genome");
		IGenome genome = new Genome();
		genome.deserializeNBT(nbtGenome);
		this.genome = genome;
		this.growthStage = nbt.getInteger(NBT_PREFIX + "growth_stage");
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if(genome != null) {
			NBTTagCompound nbtGenome = this.genome.serializeNBT();
			nbt.setTag(NBT_PREFIX + "genome", nbtGenome);
		}
		nbt.setInteger(NBT_PREFIX + "growth_stage", growthStage);
		return nbt;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag()
    {
		NBTTagCompound nbt = new NBTTagCompound();
		return this.writeToNBT(nbt);
    }
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new SPacketUpdateTileEntity(pos, 1, nbt);
	}
	
	public void setGenome(IGenome genome) {
		this.genome = genome;
	}
	
	public IGenome getGenome() {
		return this.genome;
	}
	
	public int getGrowthStage() {
		return growthStage;
	}
	
	public void setGrowthStage(int growthStage) {
		this.growthStage = growthStage;
	}
	
	public boolean isRipe() {
		return getGrowthStage() >= maxGrow;
	}
	
	public void grow() {
		int growthStrength = this.genome.getTotalStrength(TraitTypeRegistry.growth);
		if(rand.nextFloat() < 0.32f + growthStrength * 0.01f) {
			doGrow(getGrowPoint());
		}
	}
	
	private int getGrowPoint() {
		int growthStrength = this.genome.getTotalStrength(TraitTypeRegistry.growth);
		float chance = 0.10f + growthStrength * 0.01f;
		
		if (this.hasWorld()) {
			Biome biome = this.getWorld().getBiome(getPos());
			int coldStrength = this.genome.getTotalStrength(TraitTypeRegistry.cold_tolerance);
			int heatStrength = this.genome.getTotalStrength(TraitTypeRegistry.heat_tolerance);
			if (biome.getTemperature(pos) < 0.6f) {
				if (coldStrength > 0) {
					chance += 0.15f * coldStrength;
				} else {
					chance *= 0.8f;
				}
				if (heatStrength > 0) {
					chance -= 0.05f * heatStrength;
				}
			} else {
				if (biome.getTemperature(pos) > 1.0f) {
					if (heatStrength > 0) {
						chance += 0.15f * heatStrength;
					} else {
						chance *= 0.8f;
					}
					if (coldStrength > 0) {
						chance -= 0.05f * coldStrength;
					}
				}
			}
		}
		
		int point = 0;
		while (chance > 1f) {
			chance -= 1f;
			++point;
		}
		if(rand.nextFloat() < 0.10f + growthStrength * 0.01f) {
			return point + 1;
		}
		return point;
	}
	
	public void doGrow(int grow) {
		growthStage += grow;
		if(growthStage > maxGrow) {
			growthStage = maxGrow;
		}
		this.markDirty();
		GermplasmPacketHandler.INSTANCE.sendToDimension(new MessageGeneticCrop(this), this.world.provider.getDimension());
	}
	
	//右クリックで収穫後、成長段階をリセット
	public void harvestGrow() {
		growthStage = 2;
		this.markDirty();
		GermplasmPacketHandler.INSTANCE.sendToDimension(new MessageGeneticCrop(this), this.world.provider.getDimension());
	}
	
	public ItemStack getSeed() {
		ItemStack seedItem = new ItemStack(GermplasmItems.seed_sample, 1);
        NBTTagCompound nbt = new NBTTagCompound();
        if(genome != null) {
    		GeneAPI.nbtHelper.setGenomeToIndividualNBT(nbt, genome);
    	}
        seedItem.setTagCompound(nbt);
        return seedItem;
	}
	
	public ItemStack getSeedOffspring() {
		ItemStack seedItem = new ItemStack(GermplasmItems.seed_sample, this.getSeedAmount());
        NBTTagCompound nbt = new NBTTagCompound();
        if(genome != null) {
        	IGenome offspring = GeneAPI.geneHelper.getOffspring(genome, getRandomSpouse()); //自家受精
        	if(offspring != null) {
        		GeneAPI.nbtHelper.setGenomeToIndividualNBT(nbt, offspring);
        	}
    	}
        seedItem.setTagCompound(nbt);
        return seedItem;
	}
	
	//TODO: Remove
	@Deprecated
	public ItemStack getHarvest() {
		ItemStack harvestItem = new ItemStack(Items.WHEAT);
        NBTTagCompound nbt = new NBTTagCompound();
        if(genome != null) {
    		GeneAPI.nbtHelper.setGenomeToIndividualNBT(nbt, genome);
    	}
        harvestItem.setTagCompound(nbt);
        return harvestItem;
	}
	
	public void useBonemeal() {
		this.doGrow(rand.nextInt(4));
	}
	
	/*
	 * 壊したときに得られる種の数
	 */
	private int getSeedAmount() {
		if(this.isRipe()) {
			return rand.nextInt(3) + 1; //TODO: 種の取れやすさの遺伝子を追加して反映する
			
		}
		return 1;
	}
	
	/*
	 * 交配後の種の父親を返す
	 * 50%の確率で自身、50%の確率で隣接する作物のいずれか(存在しなければ自身)になる
	 */
	private IGenome getRandomSpouse() {
		TileEntity tile;
		IGenome spouse;
		if(rand.nextBoolean()) {
			EnumFacing facing = EnumFacing.getHorizontal(rand.nextInt(4));
			tile = world.getTileEntity(pos.offset(facing));
			if(tile != null && tile instanceof TileGeneticCrop) {
				spouse = ((TileGeneticCrop) tile).getGenome();
				if(spouse != null && ((TileGeneticCrop) tile).isRipe()) return spouse;
			}
		}
		return this.genome;
	}

}
