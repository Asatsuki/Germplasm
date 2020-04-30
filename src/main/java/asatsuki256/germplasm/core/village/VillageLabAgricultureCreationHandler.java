package asatsuki256.germplasm.core.village;

import java.util.List;
import java.util.Random;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class VillageLabAgricultureCreationHandler implements VillagerRegistry.IVillageCreationHandler {

	@Override
	public PieceWeight getVillagePieceWeight(Random random, int size) {
		return new PieceWeight(getComponentClass(), 15, MathHelper.getInt(random, 0, 1 + size));
	}

	@Override
	public Class<? extends Village> getComponentClass() {
		return VillageLabAgriculture.class;
	}

	@Override
	public Village buildComponent(PieceWeight villagePiece, Start startPiece, List<StructureComponent> pieces,
			Random random, int x, int y, int z, EnumFacing facing, int p5) {
		StructureBoundingBox structBB = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 12, 5, 7, facing);
        return VillageLabAgriculture.createPiece(startPiece, pieces, random, x, y, z, facing, p5);
	}

}
