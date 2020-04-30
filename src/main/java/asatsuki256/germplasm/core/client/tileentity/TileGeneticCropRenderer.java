package asatsuki256.germplasm.core.client.tileentity;

import static asatsuki256.germplasm.core.GermplasmCore.MODID;

import org.lwjgl.opengl.GL11;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.trait.TraitTypeHarvest;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.core.tileentity.TileGeneticCrop;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TileGeneticCropRenderer extends TileEntitySpecialRenderer<TileGeneticCrop> {
	
	public static TileGeneticCropRenderer renderer;
	
	private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
			new ResourceLocation(MODID, "textures/crops/wheat_stage_0.png"),
			new ResourceLocation(MODID, "textures/crops/wheat_stage_1.png"),
			new ResourceLocation(MODID, "textures/crops/wheat_stage_2.png"),
			new ResourceLocation(MODID, "textures/crops/wheat_stage_3.png"),
			new ResourceLocation(MODID, "textures/crops/wheat_stage_4.png"),
			new ResourceLocation(MODID, "textures/crops/wheat_stage_5.png"),
			new ResourceLocation(MODID, "textures/crops/wheat_stage_6.png"),
			new ResourceLocation(MODID, "textures/crops/wheat_stage_7.png"),
	};
	
	private static final ResourceLocation OVERLAY = new ResourceLocation(MODID, "textures/crops/overlay.png");
	
	public void setRendererDispatcher(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super.setRendererDispatcher(rendererDispatcherIn);
        renderer = this;
    }
	
	@Override
	public void render(TileGeneticCrop tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if (tile != null) {
			World world = tile.getWorld();
			if (world.isBlockLoaded(tile.getPos())) {
				
				IGenome genome = tile.getGenome();
				if(genome == null) return;
				TraitTypeHarvest harvest = GeneAPI.geneHelper.getPrimaryHarvest(genome);
				
				GlStateManager.pushMatrix();
				
				GlStateManager.translate((float) x, (float) y, (float) z);
				GlStateManager.translate(0.5f, 0.5f, 0.5f);
				GlStateManager.color(2.5F, 2.5F, 2.5F, 1.0F);
				
				GlStateManager.scale(0.0625F, 0.0625F, 0.0625F);
				
				Tessellator tessellator = Tessellator.getInstance();
				
//				if(Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() == GermplasmItems.loupe) {
//					//TODO: ルーペ持ってると表示がいろいろ変わるようにする
//				}
				
				this.bindTexture(harvest != null ? harvest.getTextures()[tile.getGrowthStage()] : harvest.DEFAULT_TEXTURES[tile.getGrowthStage()]);
				GlStateManager.disableCull();
				if(harvest == null || harvest.getRenderType() == TraitTypeHarvest.RenderType.CROP) {
					drawCrop(tessellator);
					GlStateManager.rotate(90f, 0f, 1f, 0f);
					drawCrop(tessellator);
					GlStateManager.rotate(90f, 0f, 1f, 0f);
					drawCrop(tessellator);
					GlStateManager.rotate(90f, 0f, 1f, 0f);
					drawCrop(tessellator);
				} else if (harvest.getRenderType() == TraitTypeHarvest.RenderType.CROSS) {
					drawCross(tessellator);
					GlStateManager.rotate(90f, 0f, 1f, 0f);
					drawCross(tessellator);
				}
				

				GlStateManager.enableCull();
				
				GlStateManager.popMatrix();
			}
		}
	}
	
	private void drawOverlay(Tessellator tessellator) {
		BufferBuilder vertexBuffer = tessellator.getBuffer();
		vertexBuffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX);
		vertexBuffer.pos(8f, 7f, 0f).tex(0, 0).endVertex();
		vertexBuffer.pos(-8f, 7f, 0f).tex(1, 0).endVertex();
		vertexBuffer.pos(8f, -9f, 0f).tex(0, 1).endVertex();
		vertexBuffer.pos(-8f, -9f, 0f).tex(1, 1).endVertex();
		tessellator.draw();
	}
	
	private void drawCrop(Tessellator tessellator) {
		BufferBuilder vertexBuffer = tessellator.getBuffer();
		vertexBuffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX);
		vertexBuffer.pos(8f, 7f, -4f).tex(0, 0).endVertex();
		vertexBuffer.pos(-8f, 7f, -4f).tex(1, 0).endVertex();
		vertexBuffer.pos(8f, -9f, -4f).tex(0, 1).endVertex();
		vertexBuffer.pos(-8f, -9f, -4f).tex(1, 1).endVertex();
		tessellator.draw();
	}
	
	private void drawCross(Tessellator tessellator) {
		BufferBuilder vertexBuffer = tessellator.getBuffer();
		vertexBuffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX);
		vertexBuffer.pos(8f, 7f, 0f).tex(0, 0).endVertex();
		vertexBuffer.pos(-8f, 7f, 0f).tex(1, 0).endVertex();
		vertexBuffer.pos(8f, -9f, 0f).tex(0, 1).endVertex();
		vertexBuffer.pos(-8f, -9f, 0f).tex(1, 1).endVertex();
		tessellator.draw();
	}

}
