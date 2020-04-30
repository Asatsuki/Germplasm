package asatsuki256.germplasm.core.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import asatsuki256.germplasm.api.gene.TraitType;
import asatsuki256.germplasm.api.gene.trait.TraitTypeHarvest;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.core.gene.trait.TraitTypeRegistry;
import asatsuki256.germplasm.core.item.GermplasmItems;
import asatsuki256.germplasm.core.tileentity.TileGeneticCrop;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGeneticCrop extends BlockContainer implements IPlantable, IGrowable {

	private static final float GAIN_BASE = 32f;
	
	private static Random rand = new Random();
	
	protected static final AxisAlignedBB CROP_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);

	public BlockGeneticCrop() {
		super(Material.PLANTS);
		this.setTickRandomly(true);
		this.setCreativeTab((CreativeTabs)null);
		this.setLightOpacity(0);
		this.setHardness(0.0F);
		this.setSoundType(SoundType.PLANT);
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return CROP_AABB;
	}

	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return getSeed(worldIn, pos, state);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		IBlockState soil = worldIn.getBlockState(pos.down());
		return super.canPlaceBlockAt(worldIn, pos) && soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos){
		if (!this.canBlockStay(worldIn, pos, state))
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);

		if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
		{
			TileEntity tile = worldIn.getTileEntity(pos);
			if(tile != null && tile instanceof TileGeneticCrop) {
				((TileGeneticCrop)tile).grow();
			}
		}
	}

	private boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getBlock() == this)
		{
			IBlockState soil = worldIn.getBlockState(pos.down());
			return soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), EnumFacing.UP, this);
		}
		return false;
	}

	private ItemStack getSeed(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile != null && tile instanceof TileGeneticCrop) {
			return ((TileGeneticCrop) tile).getSeed();
		}
		return ItemStack.EMPTY;
	}
	
	public List<ItemStack> getYield(TileGeneticCrop tile) {
		List<ItemStack> yields = new ArrayList<ItemStack>();
		IGenome genome = tile.getGenome();
		if(genome == null) return yields;
		List<TraitType> traitTypes = genome.getTraitTypes();
		List<TraitTypeHarvest> traitTypeHarvests = new ArrayList<TraitTypeHarvest>();
		
		int totalStrength = 0;
		for (TraitType traitType : traitTypes){
			if(traitType instanceof TraitTypeHarvest) {
				traitTypeHarvests.add((TraitTypeHarvest) traitType);
				totalStrength += genome.getTotalStrength(traitType);
			}
		}
		
		for(TraitTypeHarvest traitTypeHarvest : traitTypeHarvests) {
			//収穫数を計算
			if (totalStrength <= 0) continue;
			int harvestStrength = genome.getTotalStrength(traitTypeHarvest);
			float countBase = traitTypeHarvest.getHarvestMultiplier() * (harvestStrength / totalStrength);
			int gain = genome.getTotalStrength(TraitTypeRegistry.gain) + harvestStrength;
			countBase *= gain >= 0 ? (gain + GAIN_BASE) / GAIN_BASE : GAIN_BASE / (gain + GAIN_BASE);
			float countRandom = countBase + (rand.nextFloat() - 0.5f);
			int count = countRandom > 0 ? MathHelper.floor(countRandom + 0.5f) : 0;
			
			for(int i = 0; i < count; i++) {
				yields.add(traitTypeHarvest.getHarvest());
			}
		}
		return yields;
	}

	private ItemStack getSeedOffspring(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		TileEntity tile = worldIn.getTileEntity(pos);
		return getSeedOffspring(worldIn, pos, state, tile);
	}

	private ItemStack getSeedOffspring(IBlockAccess worldIn, BlockPos pos, IBlockState state, TileEntity tile) {
		if(tile != null && tile instanceof TileGeneticCrop) {
			return ((TileGeneticCrop) tile).getSeedOffspring();
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity te = worldIn.getTileEntity(pos);
		if(playerIn.getHeldItem(hand).getItem() == GermplasmItems.seed_collector) {
			if(te != null && te instanceof TileGeneticCrop) {
				TileGeneticCrop tileCrop = (TileGeneticCrop) te;
				if(tileCrop.isRipe()) {
					spawnAsEntity(worldIn, pos, getSeedOffspring(worldIn, pos, state, te));
					tileCrop.harvestGrow();
					return true;
				}
			}
        } else {
        	if(te != null && te instanceof TileGeneticCrop) {
    			TileGeneticCrop tileCrop = (TileGeneticCrop) te;
    			if(tileCrop.isRipe()) {
    				List<ItemStack> yields = getYield(tileCrop);
    				for(ItemStack yield : yields) {
    					spawnAsEntity(worldIn, pos, yield);
    				}
    				tileCrop.harvestGrow();
    				return true;
    			}
    		}
        }
		return false;
    }

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
		super.harvestBlock(worldIn, player, pos, state, te, stack);
		
		if(te != null && te instanceof TileGeneticCrop) {
			TileGeneticCrop tileCrop = (TileGeneticCrop) te;
			if(tileCrop.isRipe()) {
				List<ItemStack> yields = getYield(tileCrop);
				for(ItemStack yield : yields) {
					spawnAsEntity(worldIn, pos, yield);
				}
			}
		}
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		spawnAsEntity(worldIn, pos, getSeed(worldIn, pos, state));
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		super.getDrops(drops, world, pos, state, fortune);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.AIR;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileGeneticCrop();
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		return this.getDefaultState();
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile != null && tile instanceof TileGeneticCrop) {
			return !((TileGeneticCrop)tile).isRipe();
		}
		return false;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile != null && tile instanceof TileGeneticCrop) {
			return !((TileGeneticCrop)tile).isRipe();
		}
		return false;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile != null && tile instanceof TileGeneticCrop) {
			((TileGeneticCrop)tile).useBonemeal();;
		}
	}
}
