package asatsuki256.germplasm.core.util;

import java.util.Collection;

import net.minecraftforge.fluids.FluidStack;

public class GmpmFluidUtil {
	
	/**
	 * targetがfluidsに含まれてるかを調べる
	 * targetの数が多ければ、その数だけ含まれているか調べる
	 * 
	 * @param target あるかどうかを調べるアイテム
	 * @param fluids 調べる対象
	 * @return fluidsの中にtargetが含まれているならtrue
	 */
	public static boolean containFluid(FluidStack target, Collection<FluidStack> fluids) {
		if(target == null) return true;
		if(fluids == null) return false;
		for (FluidStack fluidstack : fluids) {
			if (fluidstack != null && target.getFluid() == fluidstack.getFluid() && target.amount <= fluidstack.amount) {
				return true;
			}
		}
		return false;
	}

}
