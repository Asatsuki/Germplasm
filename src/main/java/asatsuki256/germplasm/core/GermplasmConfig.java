package asatsuki256.germplasm.core;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Type;

public class GermplasmConfig {
	
	@Config(modid = MODID, type = Type.INSTANCE, name = MODID, category="general")
	public static class CONFIG_CORE {
		
		@Comment({"If set false, machines don't require Forge Energy to work."})
		public static boolean	requireEnergy = true;
		
	}

}
