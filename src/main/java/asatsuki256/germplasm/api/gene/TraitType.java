package asatsuki256.germplasm.api.gene;

import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import net.minecraft.client.resources.I18n;

public class TraitType {
	
	public static final String UNLOC = UNLOC_PREFIX + "trait.";
	
	public final String traitId;
	public final String unlocalizedName;
	
	public TraitType(String traitId, String unlocalizedName){
		this.traitId = traitId;
		this.unlocalizedName = unlocalizedName;
	}

	public String getTraitId() {
		return traitId;
	}
	
	public String getUnlocalizedName() {
		return UNLOC + unlocalizedName;
	}

	public String getDisplayName() {
		return I18n.format(getUnlocalizedName()).trim();
	}
	
}
