package asatsuki256.germplasm.core.util;

import net.minecraft.util.EnumFacing;

public class GmpmUtil {
	
	public static EnumFacing toggleFacing(EnumFacing facing, int toggle) {
		return EnumFacing.VALUES[(facing.getIndex() + toggle) % EnumFacing.VALUES.length];
	}

}
