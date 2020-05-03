package asatsuki256.germplasm.core.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import asatsuki256.germplasm.core.block.BlockReagentBottle;
import asatsuki256.germplasm.core.tileentity.TileReagentBottle;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockReagentBottle extends ItemBlock {

	private final int CAPACITY = TileReagentBottle.CAPACITY;
	
	public ItemBlockReagentBottle(Block block) {
		super(block);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
    {
		if(super.canPlaceBlockOnSide(worldIn, pos, side, player, stack)) return true;
		
		BlockPos posA = pos.offset(side);
		if(worldIn.getBlockState(posA).getBlock() instanceof BlockReagentBottle) {
			return true;
		}
		if(worldIn.getBlockState(pos).getBlock() instanceof BlockReagentBottle) {
			return true;
		}

        return false;
    }
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		Block clickedBlock = worldIn.getBlockState(pos).getBlock();
		if(clickedBlock instanceof BlockReagentBottle) {
			if(clickedBlock.onBlockActivated(worldIn, pos, worldIn.getBlockState(pos), player, hand, facing, hitX, hitY, hitZ)) {
				return EnumActionResult.SUCCESS;
			}
		}
		
		BlockPos posA = pos.offset(facing);
		IBlockState iblockstate = worldIn.getBlockState(posA);
        Block block = iblockstate.getBlock();
    	
        if (block instanceof BlockReagentBottle) {
        	BlockReagentBottle blockBottle = (BlockReagentBottle) block;
        	ItemStack itemstack = player.getHeldItem(hand);
        	boolean placed = false;
        	
        	if(facing == EnumFacing.DOWN || facing == EnumFacing.UP) {
    			if (hitX < 0.5f) {
    				if (hitZ < 0.5f) {
    					placed = setBottle(blockBottle, worldIn, posA, iblockstate, itemstack, 0);
    				} else {
    					placed = setBottle(blockBottle, worldIn, posA, iblockstate, itemstack, 2);
    				}
    			} else {
    				if (hitZ < 0.5f) {
    					placed = setBottle(blockBottle, worldIn, posA, iblockstate, itemstack, 1);
    				} else {
    					placed = setBottle(blockBottle, worldIn, posA, iblockstate, itemstack, 3);
    				}
    			}
    		} else if (facing == EnumFacing.SOUTH) {
    			if (hitX < 0.5f) {
    				placed = setBottle(blockBottle, worldIn, posA, iblockstate, itemstack, 0);
    			} else {
    				placed = setBottle(blockBottle, worldIn, posA, iblockstate, itemstack, 1);
    			}
    		} else if (facing == EnumFacing.NORTH) {
    			if (hitX < 0.5f) {
    				placed = setBottle(blockBottle, worldIn, posA, iblockstate, itemstack, 2);
    			} else {
    				placed = setBottle(blockBottle, worldIn, posA, iblockstate, itemstack, 3);
    			}
    		} else if (facing == EnumFacing.WEST) {
    			if (hitZ < 0.5f) {
    				placed = setBottle(blockBottle, worldIn, posA, iblockstate, itemstack, 2);
    			} else {
    				placed = setBottle(blockBottle, worldIn, posA, iblockstate, itemstack, 3);
    			}
    		} else if (facing == EnumFacing.EAST) {
    			if (hitZ < 0.5f) {
    				placed = setBottle(blockBottle, worldIn, posA, iblockstate, itemstack, 0);
    			} else {
    				placed = setBottle(blockBottle, worldIn, posA, iblockstate, itemstack, 2);
    			}
    		}
        	if(placed) {
        		SoundType soundtype = iblockstate.getBlock().getSoundType(iblockstate, worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
        	}
        }
        return super.onItemUse(player, worldIn, posA, hand, facing, hitX, hitY, hitZ);
    }
	
	public static boolean setBottle(BlockReagentBottle blockBottle, World world, BlockPos pos, IBlockState state, ItemStack itemstack, int bottle) {
		if (blockBottle.addBool(world, pos, state, TileReagentBottle.blockStates.get(bottle))) {
			blockBottle.setFluid(world, pos, getFluidStack(itemstack), bottle);
			return true;
		}
		return false;
	}
	
	@Override
	public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new FluidHandlerItemStack(stack, CAPACITY);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String getItemStackDisplayName(final ItemStack stack) 
	{
		String unlocalizedName = this.getUnlocalizedName(stack) + ".name";
		FluidStack fluidStack = getFluidStack(stack);

		String displayName = I18n.translateToLocal(unlocalizedName).trim();
		if (!this.isEmpty(stack)) {
			displayName += " (" + fluidStack.getLocalizedName() + ")";
		}
		return displayName;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		FluidStack fluidStack = getFluidStack(stack);
		
		if(fluidStack != null) {
			tooltip.add("" + fluidStack.amount + " / " + CAPACITY + " mB");
		}
		else
		{
			tooltip.add("0 / 1000 mB");
		}

    }
	
	public static FluidStack getFluidStack(ItemStack stack) {
		IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack);
		if(fluidHandler != null) {
			return fluidHandler.getTankProperties()[0].getContents();
		}
		return null;
	}
	
	private boolean isEmpty(ItemStack stack) {
		FluidStack fluidStack = getFluidStack(stack);
		return fluidStack == null || fluidStack.amount <= 0;
	}

}
