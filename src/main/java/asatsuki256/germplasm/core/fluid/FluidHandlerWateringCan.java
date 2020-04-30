package asatsuki256.germplasm.core.fluid;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class FluidHandlerWateringCan extends FluidHandlerItemStack{

	protected static final FluidStack EMPTY = new FluidStack(FluidRegistry.WATER, 0);

	public FluidHandlerWateringCan(ItemStack parContainerStack, int parCapacity) 
	{
		super(parContainerStack, parCapacity);

		if (getFluidStack() == null)
		{
			setContainerToEmpty();
		}
	}

	@Override
	protected void setContainerToEmpty()
	{
		setFluidStack(EMPTY.copy());
		container.getTagCompound().removeTag(FLUID_NBT_KEY);
	}

	@Override
	public boolean canFillFluidType(FluidStack fluid)
	{
		if(fluid.getFluid() == FluidRegistry.WATER) return true;
		if(fluid.getFluid() == GermplasmFluids.mutagen) return true;
		return false;
	}

	public FluidStack getFluidStack()
	{
		return getFluid();
	}

	public void setFluidStack(FluidStack parFluidStack)
	{
		setFluid(parFluidStack);
	}

}
