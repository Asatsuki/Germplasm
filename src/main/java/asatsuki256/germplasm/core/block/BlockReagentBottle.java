package asatsuki256.germplasm.core.block;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.item.ItemBlockReagentBottle;
import asatsuki256.germplasm.core.tileentity.TileReagentBottle;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockReagentBottle extends BlockContainer {

	public static final PropertyBool NORTH_WEST = PropertyBool.create("north_west"); //0
	public static final PropertyBool NORTH_EAST = PropertyBool.create("north_east"); //90
	public static final PropertyBool SOUTH_WEST = PropertyBool.create("south_west"); //270
	public static final PropertyBool SOUTH_EAST = PropertyBool.create("south_east"); //180

	public static final AxisAlignedBB NORTH_WEST_AABB = new AxisAlignedBB(1/16D, 0/16D, 1/16D, 7/16D, 12/16D, 7/16D);
	public static final AxisAlignedBB NORTH_EAST_AABB = new AxisAlignedBB(9/16D, 0/16D, 1/16D, 15/16D, 13/16D, 7/16D);
	public static final AxisAlignedBB SOUTH_WEST_AABB = new AxisAlignedBB(1/16D, 0/16D, 9/16D, 7/16D, 13/16D, 15/16D);
	public static final AxisAlignedBB SOUTH_EAST_AABB = new AxisAlignedBB(9/16D, 0/16D, 9/16D, 15/16D, 13/16D, 15/16D);

	public BlockReagentBottle() {
		super(Material.GLASS);
		this.setCreativeTab(GermplasmCore.tabAgrigenetics);
		this.setLightOpacity(0);
		this.setHardness(2.0f);
		this.setSoundType(SoundType.METAL);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		if(facing == EnumFacing.DOWN || facing == EnumFacing.UP) {
			if (hitX < 0.5f) {
				if (hitZ < 0.5f) {
					return getStateSingleBottle(NORTH_WEST);
				} else {
					return getStateSingleBottle(SOUTH_WEST);
				}
			} else {
				if (hitZ < 0.5f) {
					return getStateSingleBottle(NORTH_EAST);
				} else {
					return getStateSingleBottle(SOUTH_EAST);
				}
			}
		} else if (facing == EnumFacing.SOUTH) {
			if (hitX < 0.5f) {
				return getStateSingleBottle(NORTH_WEST);
			} else {
				return getStateSingleBottle(NORTH_EAST);
			}
		} else if (facing == EnumFacing.NORTH) {
			if (hitX < 0.5f) {
				return getStateSingleBottle(SOUTH_WEST);
			} else {
				return getStateSingleBottle(SOUTH_EAST);
			}
		} else if (facing == EnumFacing.WEST) {
			if (hitZ < 0.5f) {
				return getStateSingleBottle(NORTH_EAST);
			} else {
				return getStateSingleBottle(SOUTH_EAST);
			}
		} else if (facing == EnumFacing.EAST) {
			if (hitZ < 0.5f) {
				return getStateSingleBottle(NORTH_WEST);
			} else {
				return getStateSingleBottle(SOUTH_WEST);
			}
		}
		return getStateSingleBottle(NORTH_WEST);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {NORTH_WEST, NORTH_EAST, SOUTH_WEST, SOUTH_EAST});
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		FluidStack fluid = ItemBlockReagentBottle.getFluidStack(stack);
		if(state.getValue(NORTH_WEST)) {
			setFluid(worldIn, pos, fluid, 0);
		}else if(state.getValue(NORTH_EAST)) {
			setFluid(worldIn, pos, fluid, 1);
		}else if(state.getValue(SOUTH_WEST)) {
			setFluid(worldIn, pos, fluid, 2);
		}else if(state.getValue(SOUTH_EAST)) {
			setFluid(worldIn, pos, fluid, 3);
		}
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		int meta = 0;
		if(state.getValue(NORTH_WEST)) meta |= 1;
		if(state.getValue(NORTH_EAST)) meta |= 2;
		if(state.getValue(SOUTH_WEST)) meta |= 4;
		if(state.getValue(SOUTH_EAST)) meta |= 8;
		return meta;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState()
				.withProperty(NORTH_WEST, (meta & 1) > 0)
				.withProperty(NORTH_EAST, (meta & 2) > 0)
				.withProperty(SOUTH_WEST, (meta & 4) > 0)
				.withProperty(SOUTH_EAST, (meta & 8) > 0);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		if (!isActualState) {
			state = state.getActualState(worldIn, pos);
		}

		for (AxisAlignedBB aabb : getCollisionBoxList(state)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, aabb);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		worldIn.getTileEntity(pos).markDirty();
		ItemStack heldItem = playerIn.getHeldItem(hand);
		if(heldItem.getItem() == Item.getItemFromBlock(this) && playerIn.isSneaking()) { //新たな瓶を追加
			boolean flagPlaced = false;
			if (facing == EnumFacing.SOUTH && hitZ < 0.5f) {
				if(hitX < 0.5f) {
					flagPlaced = ItemBlockReagentBottle.setBottle(this, worldIn, pos, state, heldItem, 2);
				} else {
					flagPlaced = ItemBlockReagentBottle.setBottle(this, worldIn, pos, state, heldItem, 3);
				}
			} else if (facing == EnumFacing.NORTH && hitZ >= 0.5f) {
				if(hitX < 0.5f) {
					flagPlaced = ItemBlockReagentBottle.setBottle(this, worldIn, pos, state, heldItem, 0);
				} else {
					flagPlaced = ItemBlockReagentBottle.setBottle(this, worldIn, pos, state, heldItem, 1);
				}
			} else if (facing == EnumFacing.WEST && hitX >= 0.5f) {
				if(hitZ < 0.5f) {
					flagPlaced = ItemBlockReagentBottle.setBottle(this, worldIn, pos, state, heldItem, 0);
				} else {
					flagPlaced = ItemBlockReagentBottle.setBottle(this, worldIn, pos, state, heldItem, 2);
				}
			} else if (facing == EnumFacing.EAST && hitX < 0.5f) {
				if(hitZ < 0.5f) {
					flagPlaced = ItemBlockReagentBottle.setBottle(this, worldIn, pos, state, heldItem, 1);
				} else {
					flagPlaced = ItemBlockReagentBottle.setBottle(this, worldIn, pos, state, heldItem, 3);
				}
			}

			if(flagPlaced) {
				SoundType soundtype = this.getSoundType(state, worldIn, pos, playerIn);
				worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				heldItem.shrink(1);
				return true;
			}
		}else if (heldItem.isEmpty() && playerIn.isSneaking()){ //瓶を回収
			IBlockState newState = state;
			ItemStack newItem = new ItemStack(this);
			IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(newItem);
			if (hitX < 0.5f) {
				if (hitZ < 0.5f) {
					newState = state.withProperty(NORTH_WEST, false);
					fluidHandler.fill(getFluid(worldIn, pos, 0), true);
					setFluid(worldIn, pos, null, 0);
				} else {
					newState = state.withProperty(SOUTH_WEST, false);
					fluidHandler.fill(getFluid(worldIn, pos, 2), true);
					setFluid(worldIn, pos, null, 2);
				}
			} else {
				if (hitZ < 0.5f) {
					newState = state.withProperty(NORTH_EAST, false);
					fluidHandler.fill(getFluid(worldIn, pos, 1), true);
					setFluid(worldIn, pos, null, 1);
				} else {
					newState = state.withProperty(SOUTH_EAST, false);
					fluidHandler.fill(getFluid(worldIn, pos, 3), true);
					setFluid(worldIn, pos, null, 3);
				}
			}
			playerIn.addItemStackToInventory(newItem);
			if (isEmpty(newState)) {
				worldIn.setBlockToAir(pos);
			} else {
				worldIn.setBlockState(pos, newState);
			}
			return true;
		} else if (heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {

			TileEntity tile = worldIn.getTileEntity(pos);
			if(worldIn.getBlockState(pos).getBlock() != this || tile == null || !(tile instanceof TileReagentBottle)) {
				return false;
			}
			TileReagentBottle tileReagentBottle = (TileReagentBottle) tile;
			IFluidHandlerItem fluidHandlerItem = heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if (fluidHandlerItem != null) {
				int bottle = -1;
				if (hitX < 0.5f) {
					if (hitZ < 0.5f) {
						bottle = 0;
					} else {
						bottle = 2;
					}
				} else {
					if (hitZ < 0.5f) {
						bottle = 1;
					} else {
						bottle = 3;
					}
				}
				if(bottle >= 0) {
					FluidStack d = fluidHandlerItem.drain(tileReagentBottle.CAPACITY, false);
					FluidStack bottleFluid = getFluid(worldIn, pos, bottle);
					IItemHandler playerInventory = playerIn.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
					
					if(FluidUtil.tryEmptyContainerAndStow(heldItem, tileReagentBottle, playerInventory, tileReagentBottle.CAPACITY, playerIn, true).isSuccess()) {
						return true;
					} else if (FluidUtil.tryFillContainerAndStow(heldItem, tileReagentBottle, playerInventory, tileReagentBottle.CAPACITY, playerIn, true).isSuccess()){
						return true;
					}
				}


			}
		} else { //それ以外
			if(worldIn.isRemote) {
				FluidStack fluid = null;
				if (hitX < 0.5f) {
					if (hitZ < 0.5f) {
						fluid = getFluid(worldIn, pos, 0);
					} else {
						fluid = getFluid(worldIn, pos, 2);
					}
				} else {
					if (hitZ < 0.5f) {
						fluid = getFluid(worldIn, pos, 1);
					} else {
						fluid = getFluid(worldIn, pos, 3);
					}
				}
				if(fluid != null) {
					playerIn.sendMessage(new TextComponentString(fluid.getLocalizedName() + " " + fluid.amount + "mB"));
				} else {
					playerIn.sendMessage(new TextComponentString("0mB"));
				}
			}
			return true;
		} 

		return false;
	}

	public boolean addBool(World worldIn, BlockPos pos, IBlockState state, PropertyBool property) {
		if(!state.getValue(property)) {
			worldIn.setBlockState(pos, state.withProperty(property, true));
			return true;
		}
		return false;
	}

	public boolean setFluid(World worldIn, BlockPos pos, FluidStack fluid, int bottle) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(worldIn.getBlockState(pos).getBlock() == this && tile != null && tile instanceof TileReagentBottle) {
			TileReagentBottle tileReagentBottle = (TileReagentBottle) tile;
			tileReagentBottle.setFluid(fluid, bottle);
			return true;
		}
		return false;
	}

	public FluidStack getFluid(World worldIn, BlockPos pos, int bottle) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(worldIn.getBlockState(pos).getBlock() == this && tile != null && tile instanceof TileReagentBottle) {
			TileReagentBottle tileReagentBottle = (TileReagentBottle) tile;
			return tileReagentBottle.getFluid(bottle);
		}
		return null;
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos){
		if (!this.canBlockStay(worldIn, pos, state))
		{
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
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

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Nullable
	public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
		List<RayTraceResult> list = new ArrayList<RayTraceResult>();
		for (AxisAlignedBB axisalignedbb : getCollisionBoxList(this.getActualState(blockState, worldIn, pos)))
		{
			list.add(this.rayTrace(pos, start, end, axisalignedbb));
		}

		RayTraceResult raytraceresult1 = null;
		double d1 = 0.0D;

		for (RayTraceResult raytraceresult : list)
		{
			if (raytraceresult != null)
			{
				double d0 = raytraceresult.hitVec.squareDistanceTo(end);

				if (d0 > d1)
				{
					raytraceresult1 = raytraceresult;
					d1 = d0;
				}
			}
		}

		return raytraceresult1;
	}

	private IBlockState getStateSingleBottle(PropertyBool property) {
		return this.getDefaultState()
				.withProperty(NORTH_WEST, NORTH_WEST == property)
				.withProperty(NORTH_EAST, NORTH_EAST == property)
				.withProperty(SOUTH_WEST, SOUTH_WEST == property)
				.withProperty(SOUTH_EAST, SOUTH_EAST == property);
	}

	private static List<AxisAlignedBB> getCollisionBoxList(IBlockState state){
		List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();
		if (state.getValue(NORTH_WEST)) {
			list.add(NORTH_WEST_AABB);
		}
		if (state.getValue(NORTH_EAST)) {
			list.add(NORTH_EAST_AABB);
		}
		if (state.getValue(SOUTH_WEST)) {
			list.add(SOUTH_WEST_AABB);
		}
		if (state.getValue(SOUTH_EAST)) {
			list.add(SOUTH_EAST_AABB);
		}
		return list;
	}

	private static boolean isEmpty(IBlockState state) {
		return !(state.getValue(NORTH_WEST) || state.getValue(NORTH_EAST) || state.getValue(SOUTH_WEST) || state.getValue(SOUTH_EAST));
	}

	private boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getBlock() == this)
		{
			return !isEmpty(state);
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileReagentBottle();
	}


}
