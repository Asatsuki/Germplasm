package asatsuki256.germplasm.core.block;

import java.util.ArrayList;
import java.util.List;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.GenePool;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.gene.Chromosome;
import asatsuki256.germplasm.core.gene.ChromosomePair;
import asatsuki256.germplasm.core.gene.Gene;
import asatsuki256.germplasm.core.gene.Genome;
import asatsuki256.germplasm.core.item.GermplasmItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockThresher extends Block {
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	public BlockThresher() {
		super(Material.WOOD);
		this.setCreativeTab(GermplasmCore.tabAgrigenetics);
		this.setLightOpacity(0);
		this.setHardness(5.0f);
		this.setSoundType(SoundType.WOOD);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		ItemStack ingredientStack = playerIn.getHeldItem(hand);
		GenePool genePool = GeneAPI.genePoolRegistry.getPoolFromItem(ingredientStack);
		if (genePool == null) return false;
		
		ItemStack seedStack = new ItemStack(GermplasmItems.seed_sample);
		
		NBTTagCompound nbt = new NBTTagCompound();
		
		IGenome genome;
		List<ChromosomePair> pairs = new ArrayList<ChromosomePair>();
    	for(int i = 0; i < 4; i++) {
    		List<Chromosome> chromosomes = new ArrayList<Chromosome>();
        	for(int j = 0; j < 2; j++) {
        		List<Gene> genes = new ArrayList<Gene>();
        		for(int k = 0; k < 4; k++) {
        			genes.add((Gene) genePool.getWeightedGene());
        		}
        		chromosomes.add(new Chromosome(genes));
        	}
        	pairs.add(new ChromosomePair(chromosomes));
    	}
    	genome = new Genome(pairs);
		
		GeneAPI.nbtHelper.setGenomeToIndividualNBT(nbt, genome);
		
		seedStack.setTagCompound(nbt);
		
		playerIn.addItemStackToInventory(seedStack);
		ingredientStack.shrink(1);
		
		return true;
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
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }
	
	@Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }
    
    @Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
		return this.getDefaultState().withProperty(FACING, placer.getAdjustedHorizontalFacing().getOpposite());
    }

}
