package asatsuki256.germplasm.core.capability.handler;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidTankWithType extends FluidTank {
	
	private Fluid fluidType;
	
	public FluidTankWithType(Fluid fluidType, int capacity)
    {
        super(capacity);
        this.fluidType = fluidType;
    }
	
	@Override
	public boolean canFillFluidType(FluidStack fluid)
    {
        return fluid.getFluid() == fluidType && canFill();
    }
	
	@Override
	public FluidStack getFluid()
    {
        return fluid != null ? fluid : new FluidStack(fluidType, 0);
    }

}
