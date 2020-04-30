package asatsuki256.germplasm.core.item;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import asatsuki256.germplasm.core.fluid.FluidHandlerWateringCan;
import asatsuki256.germplasm.core.fluid.GermplasmFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWateringCan extends ItemBase {

	private Random rand = new Random();
	
	private final int CAPACITY = Fluid.BUCKET_VOLUME;
	private final ItemStack EMPTY_STACK = new ItemStack(this);
	private final FluidStack drainWater = new FluidStack(FluidRegistry.WATER, 10);
	
	public ItemWateringCan() 
	{
		setMaxStackSize(1);
	}

	@Override
	public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt)
	{
		return new FluidHandlerWateringCan(stack, CAPACITY);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if(stack != null) {
			IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack);
			if(fluidHandler != null) {
				FluidStack fluidStack = getFluidStack(stack);
				if(fluidStack != null) {
					if(fluidStack.getFluid() == FluidRegistry.WATER) {
						if(canMoisten(worldIn, pos)) {
							if(moistenFarmland(worldIn, pos, fluidHandler, player)) {
								return EnumActionResult.SUCCESS;
							}
						}
						else if(canMoisten(worldIn, pos.down())) {
							if(moistenFarmland(worldIn, pos.down(), fluidHandler, player)) {
								return EnumActionResult.SUCCESS;
							}
						}
					}
					else if(fluidStack.getFluid() == GermplasmFluids.mutagen) {
						
					}
				}
			}
		}
		
		return EnumActionResult.PASS;
	}
	
	protected boolean moistenFarmland(World worldIn, BlockPos pos, IFluidHandlerItem fluidHandler, EntityPlayer player) {
		if(canMoisten(worldIn, pos)) {
			FluidStack drained = fluidHandler.drain(drainWater, true);
			if(drained != null && drained.amount >= drainWater.amount) {
				IBlockState state = worldIn.getBlockState(pos);
				worldIn.setBlockState(pos, state.withProperty(BlockFarmland.MOISTURE, 7));
				worldIn.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 0.5F, 2.0F);
				for(int i = 0; i < 10; i++) {
					worldIn.spawnParticle(EnumParticleTypes.WATER_SPLASH, 
							pos.getX() + rand.nextDouble(), pos.getY() + 1.0D, pos.getZ() + rand.nextDouble(), 
							0.0F, 0.0F, 0.0F);
				}
				
				return true;
			}
		}
		return false;
	}
	
	protected boolean canMoisten(World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos);
		Block block = state.getBlock();
		if(block == Blocks.FARMLAND) {
			if(state.getValue(BlockFarmland.MOISTURE) < 7) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void getSubItems(@Nullable final CreativeTabs tab, final NonNullList subItems) 
	{
		if (!this.isInCreativeTab(tab)) return;

		subItems.add(EMPTY_STACK);
		
		final FluidStack[] fluidStacks = new FluidStack[] {
				new FluidStack(FluidRegistry.WATER, CAPACITY),
				new FluidStack(GermplasmFluids.mutagen, CAPACITY)
		};

		for(FluidStack fluidStack : fluidStacks) {
			final ItemStack stack = new ItemStack(this);
			final IFluidHandlerItem fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if (fluidHandler != null)
			{
				final int fluidFillAmount = fluidHandler.fill(fluidStack, true);
				if (fluidFillAmount == fluidStack.amount) 
				{
					final ItemStack filledStack = fluidHandler.getContainer();
					subItems.add(filledStack);   
				}
			}
		}
	}

	@Override
	public String getItemStackDisplayName(final ItemStack stack) 
	{
		String unlocalizedName = this.getUnlocalizedName(stack) + ".name";
		FluidStack fluidStack = getFluidStack(stack);

		String displayName = I18n.format(unlocalizedName).trim();
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
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
    {
        return !this.isEmpty(stack);
    }
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
    {
		FluidStack fluidStack = getFluidStack(stack);
		if(this.isEmpty(stack)) {
			return 1d;
		}
		else {
			return 1d - (double)fluidStack.amount / (double)CAPACITY;
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
