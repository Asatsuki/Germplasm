package asatsuki256.germplasm.core.village;

import java.util.List;
import java.util.Random;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.core.block.BlockReagentBottle;
import asatsuki256.germplasm.core.block.BlockWallLight;
import asatsuki256.germplasm.core.block.GermplasmBlocks;
import asatsuki256.germplasm.core.tileentity.TileGeneticCrop;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class VillageLabAgriculture extends StructureVillagePieces.Village {

	public VillageLabAgriculture()
    {
    }
	
	public VillageLabAgriculture(StructureVillagePieces.Start parStart, int parType, Random parRand, StructureBoundingBox parStructBB, EnumFacing parFacing)
    {
        super(parStart, parType);
        setCoordBaseMode(parFacing);
        boundingBox = parStructBB;
    }
	
	public static VillageLabAgriculture createPiece(StructureVillagePieces.Start start, List<StructureComponent> p_175852_1_, Random rand, int p_175852_3_, int p_175852_4_, int p_175852_5_, EnumFacing facing, int p_175852_7_)
    {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175852_3_, p_175852_4_, p_175852_5_, 0, 0, 0, 12, 5, 7, facing);
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175852_1_, structureboundingbox) == null ? new VillageLabAgriculture(start, p_175852_7_, rand, structureboundingbox, facing) : null;
    }
	
	@Override
	public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox boundingboxIn) {
		if (this.averageGroundLvl < 0)
        {
            this.averageGroundLvl = this.getAverageGroundLevel(worldIn, boundingboxIn);

            if (this.averageGroundLvl < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 5 - 1, 0);
        }
		
		IBlockState airState = Blocks.AIR.getDefaultState();
		IBlockState baseState = Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
		IBlockState wallState = Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);
		IBlockState ceilingState = GermplasmBlocks.bioplastic_tile.getDefaultState();
		IBlockState floorState = GermplasmBlocks.bioplastic_tile_small.getDefaultState();
		IBlockState farmState = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7);
		IBlockState windowState = Blocks.GLASS.getDefaultState();
		IBlockState greenhouseState = GermplasmBlocks.organic_glass.getDefaultState();
		IBlockState floorLightState = GermplasmBlocks.plastic_wall_light.getDefaultState().withProperty(BlockWallLight.FACING, EnumFacing.UP);
		IBlockState ceilingLightState = GermplasmBlocks.plastic_wall_light.getDefaultState().withProperty(BlockWallLight.FACING, EnumFacing.DOWN);
		IBlockState researchTableState = GermplasmBlocks.research_desk.getDefaultState().withProperty(BlockHorizontal.FACING, this.getCoordBaseMode().getOpposite());
		IBlockState microscopeState = GermplasmBlocks.microscope.getDefaultState().withProperty(BlockHorizontal.FACING, this.getCoordBaseMode().getOpposite());
		IBlockState genomeAnalyzerState = GermplasmBlocks.genome_analyzer.getDefaultState().withProperty(BlockHorizontal.FACING, this.getCoordBaseMode());
		IBlockState deskState = GermplasmBlocks.desk.getDefaultState();
		IBlockState reagentBottleState = GermplasmBlocks.reagent_bottle.getDefaultState().withProperty(BlockReagentBottle.SOUTH_EAST, true);
		IBlockState stairState = this.getBiomeSpecificBlockState(Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
		IBlockState cropState = this.getBiomeSpecificBlockState(GermplasmBlocks.genetic_crop.getDefaultState());
        
		//空洞
		this.fillWithBlocks(worldIn, boundingboxIn, 0, 0, 0, 11, 4, 6, airState, airState, false);
		//外形
		this.fillWithBlocks(worldIn, boundingboxIn, 7, 0, 0, 11, 4, 6, greenhouseState, airState, false);
		this.fillWithBlocks(worldIn, boundingboxIn, 0, 0, 0, 7, 4, 6, wallState, airState, false);
		//基礎
		this.fillWithBlocks(worldIn, boundingboxIn, 0, 0, 0, 11, 0, 6, baseState, baseState, false);
		this.fillWithBlocks(worldIn, boundingboxIn, 1, 0, 1, 6, 0, 5, floorState, floorState, false);
		this.fillWithBlocks(worldIn, boundingboxIn, 8, 0, 1, 10, 0, 5, farmState, farmState, false);
		this.setBlockState(worldIn, Blocks.WATER.getDefaultState(), 9, 0, 3, boundingboxIn);
		//天井
		this.fillWithBlocks(worldIn, boundingboxIn, 0, 4, 0, 7, 4, 6, baseState, baseState, false);
		this.fillWithBlocks(worldIn, boundingboxIn, 1, 4, 1, 6, 4, 5, ceilingState, ceilingState, false);
		//ドア
		this.createVillageDoor(worldIn, boundingboxIn, randomIn, 2, 1, 0, EnumFacing.SOUTH);
		this.setBlockState(worldIn, airState, 7, 1, 3, boundingboxIn);
		this.setBlockState(worldIn, airState, 7, 2, 3, boundingboxIn);
		//窓
		this.fillWithBlocks(worldIn, boundingboxIn, 4, 2, 0, 5, 2, 0, windowState, windowState, false);
		this.fillWithBlocks(worldIn, boundingboxIn, 0, 2, 2, 0, 2, 4, windowState, windowState, false);
		this.fillWithBlocks(worldIn, boundingboxIn, 3, 2, 6, 4, 2, 6, windowState, windowState, false);
		//照明
		this.setBlockState(worldIn, ceilingLightState, 3, 3, 3, boundingboxIn);
		this.setBlockState(worldIn, floorLightState, 0, 5, 0, boundingboxIn);
		this.setBlockState(worldIn, floorLightState, 7, 5, 0, boundingboxIn);
		this.setBlockState(worldIn, floorLightState, 0, 5, 6, boundingboxIn);
		this.setBlockState(worldIn, floorLightState, 7, 5, 6, boundingboxIn);
		//内装
		this.setBlockState(worldIn, researchTableState, 1, 1, 5, boundingboxIn);
		this.setBlockState(worldIn, microscopeState, 2, 1, 5, boundingboxIn);
		this.setBlockState(worldIn, deskState, 6, 1, 5, boundingboxIn);
		this.setBlockState(worldIn, reagentBottleState, 6, 2, 5, boundingboxIn);
		this.setBlockState(worldIn, genomeAnalyzerState, 6, 1, 1, boundingboxIn);
		//階段
		if (this.getBlockStateFromPos(worldIn, 2, 0, -1, boundingboxIn).getMaterial() == Material.AIR && this.getBlockStateFromPos(worldIn, 2, -1, -1, boundingboxIn).getMaterial() != Material.AIR)
        {
			this.setBlockState(worldIn, stairState, 2, 0, -1, boundingboxIn);
            if (this.getBlockStateFromPos(worldIn, 2, -1, -1, boundingboxIn).getBlock() == Blocks.GRASS_PATH)
            {
                this.setBlockState(worldIn, Blocks.GRASS.getDefaultState(), 2, -1, -1, boundingboxIn);
            }
        }
		//作物
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 5; j++) {
				if (randomIn.nextBoolean() && this.getBlockStateFromPos(worldIn, 8+i, 0, 1+j, boundingboxIn).getBlock() == Blocks.FARMLAND) {
					this.setBlockState(worldIn, cropState, 8+i, 1, 1+j, boundingboxIn);
					BlockPos blockpos = new BlockPos(this.getXWithOffset(8+i, 1+j), this.getYWithOffset(1), this.getZWithOffset(8+i, 1+j));
			        TileEntity tile = worldIn.getTileEntity(blockpos);
			        if (tile instanceof TileGeneticCrop) {
			        	TileGeneticCrop tileCrop = (TileGeneticCrop) tile;
			        	tileCrop.setGenome(GeneAPI.geneHelper.getGenomeFromPool(randomIn, GeneAPI.genePoolRegistry.pool("carrot"), 4));
			        	tileCrop.doGrow(randomIn.nextInt(8));
			        }
				}
			}
		}
		//村人
		this.spawnVillagers(worldIn, boundingboxIn, 3, 1, 2, 1);
		
		return true;
	}
	
	@Override
    protected BlockDoor biomeDoor()
    {
        return Blocks.DARK_OAK_DOOR;
    }
	
	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound)
    {
        super.writeStructureToNBT(tagCompound);
    }

	@Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_)
    {
        super.readStructureFromNBT(tagCompound, p_143011_2_);
    }
	
	@Override
    protected VillagerRegistry.VillagerProfession chooseForgeProfession(int count, VillagerRegistry.VillagerProfession prof)
    {
        return GermplasmVillage.geneticsProfession;
    }

}
