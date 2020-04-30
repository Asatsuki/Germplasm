package asatsuki256.germplasm.core.block;

import javax.annotation.Nullable;

import asatsuki256.germplasm.core.GermplasmCore;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWallLight extends Block {

	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(2/16D, 14/16D, 2/16D, 14/16D, 16/16D, 14/16D);
	protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(2/16D, 0/16D, 2/16D, 14/16D, 2/16D, 14/16D);
	protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(2/16D, 2/16D, 14/16D, 14/16D, 14/16D, 16/16D);
	protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(2/16D, 2/16D, 0/16D, 14/16D, 14/16D, 2/16D);
	protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0/16D, 2/16D, 2/16D, 2/16D, 14/16D, 14/16D);
	protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(14/16D, 2/16D, 2/16D, 16/16D, 14/16D, 14/16D);
	
	public BlockWallLight() {
		super(Material.GLASS);
		this.setCreativeTab(GermplasmCore.tabAgrigenetics);
		this.setHardness(1.0f);
		this.setSoundType(SoundType.STONE);
		this.setLightOpacity(0);
		this.setLightLevel(1f);
	}
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch ((EnumFacing)state.getValue(FACING))
        {
        case DOWN:
        default:
            return DOWN_AABB;
        case UP:
        	return UP_AABB;
        case EAST:
        	return EAST_AABB;
        case WEST:
        	return WEST_AABB;
        case SOUTH:
        	return SOUTH_AABB;
        case NORTH:
        	return NORTH_AABB;
        }
    }
	
	@Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
	
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
    	return this.getDefaultState().withProperty(FACING, facing);
    }
    
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();
        iblockstate = iblockstate.withProperty(FACING, EnumFacing.getFront(meta));
        return iblockstate;
    }
    
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }
    
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

}
