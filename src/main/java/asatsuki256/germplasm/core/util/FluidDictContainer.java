package asatsuki256.germplasm.core.util;

import java.util.Map;

import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidDictContainer {

	private final String fluidName;
	private final int amount;

	public FluidDictContainer (String fluidName) {
		this(fluidName, 1000);
	}

	public FluidDictContainer (String fluidName, int amount) {
		this.fluidName = fluidName;
		this.amount = amount;
	}

	public NonNullList<FluidStack> getFluids() {
		NonNullList<FluidStack> items = NonNullList.create();
		Map<String, Fluid> fluidMap = FluidRegistry.getRegisteredFluids();
		for (String fluidId : fluidMap.keySet()) {
			//System.out.println("fluidKey:" + fluidId + " accepts:" + fluidName + " contains:" + fluidId.contains(fluidName));
			if (fluidId.contains(fluidName)) {
				//System.out.println("containFluid");
				FluidStack stack = new FluidStack(fluidMap.get(fluidId), amount);
				items.add(stack);
			}
		}
		return items;
	}
}