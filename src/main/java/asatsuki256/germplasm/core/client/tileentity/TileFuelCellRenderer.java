package asatsuki256.germplasm.core.client.tileentity;

import org.lwjgl.opengl.GL11;

import asatsuki256.germplasm.core.tileentity.TileFuelCell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidStack;

public class TileFuelCellRenderer  extends TileEntitySpecialRenderer<TileFuelCell> {
	
	@Override
	public void render(TileFuelCell tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
		GlStateManager.pushMatrix();
		
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.translate(0.5f, 0.5f, 0.5f);
		GlStateManager.scale(1/16F, 1/16F, 1/16F);
		
		Tessellator tessellator = Tessellator.getInstance();
		
		if (tile.getTank().getFluid() != null && tile.getTank().getFluid().getFluid() != null) {
			for(int i = 0; i < 4; i++) {
				renderFluid(tessellator, tile.getTank().getFluid(), i);
			}
		}
		
		
		
		GlStateManager.popMatrix();
    }
	
	private void renderFluid(Tessellator tessellator, FluidStack fluid, int bottle) {
		GlStateManager.disableLighting();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
		TextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite(fluid.getFluid().getStill(fluid).toString());
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		float uMin = textureatlassprite.getMinU();
		float vMin = textureatlassprite.getMinV();
		float uMax = textureatlassprite.getMaxU();
		float vMax = textureatlassprite.getMaxV();
		
		float transX = (bottle & 1) > 0 ? 4f : -4f;
		float transY = -2f;
		float transZ = (bottle & 2) > 0 ? 4f : -4f;
		float offset = 1/4f;
		float f = 3f - offset;
		float bottom = offset;
		float top = offset + (10f - offset*2) * (float)fluid.amount / (float)4000;
		
		BufferBuilder vertexBuffer = tessellator.getBuffer();
		//Top
		vertexBuffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX);
		vertexBuffer.pos(transX + -f, transY + top, transZ + +f).tex(uMin, vMin).endVertex();
		vertexBuffer.pos(transX + +f, transY + top, transZ + +f).tex(uMax, vMin).endVertex();
		vertexBuffer.pos(transX + -f, transY + top, transZ + -f).tex(uMin, vMax).endVertex();
		vertexBuffer.pos(transX + +f, transY + top, transZ + -f).tex(uMax, vMax).endVertex();
		tessellator.draw();
		//bottom
		vertexBuffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX);
		vertexBuffer.pos(transX + -f, transY + bottom, transZ + -f).tex(uMin, vMin).endVertex();
		vertexBuffer.pos(transX + +f, transY + bottom, transZ + -f).tex(uMax, vMin).endVertex();
		vertexBuffer.pos(transX + -f, transY + bottom, transZ + +f).tex(uMin, vMax).endVertex();
		vertexBuffer.pos(transX + +f, transY + bottom, transZ + +f).tex(uMax, vMax).endVertex();
		tessellator.draw();
		//north
		vertexBuffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX);
		vertexBuffer.pos(transX + -f, transY + top, transZ + -f).tex(uMin, vMin).endVertex();
		vertexBuffer.pos(transX + +f, transY + top, transZ + -f).tex(uMax, vMin).endVertex();
		vertexBuffer.pos(transX + -f, transY + bottom, transZ + -f).tex(uMin, vMax).endVertex();
		vertexBuffer.pos(transX + +f, transY + bottom, transZ + -f).tex(uMax, vMax).endVertex();
		tessellator.draw();
		//south
		vertexBuffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX);
		vertexBuffer.pos(transX + -f, transY + bottom, transZ + +f).tex(uMin, vMin).endVertex();
		vertexBuffer.pos(transX + +f, transY + bottom, transZ + +f).tex(uMax, vMin).endVertex();
		vertexBuffer.pos(transX + -f, transY + top, transZ + +f).tex(uMin, vMax).endVertex();
		vertexBuffer.pos(transX + +f, transY + top, transZ + +f).tex(uMax, vMax).endVertex();
		tessellator.draw();
		//west
		vertexBuffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX);
		vertexBuffer.pos(transX + -f, transY + bottom, transZ + -f).tex(uMin, vMin).endVertex();
		vertexBuffer.pos(transX + -f, transY + bottom, transZ + +f).tex(uMax, vMin).endVertex();
		vertexBuffer.pos(transX + -f, transY + top, transZ + -f).tex(uMin, vMax).endVertex();
		vertexBuffer.pos(transX + -f, transY + top, transZ + +f).tex(uMax, vMax).endVertex();
		tessellator.draw();
		//east
		vertexBuffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX);
		vertexBuffer.pos(transX + +f, transY + top, transZ + -f).tex(uMin, vMin).endVertex();
		vertexBuffer.pos(transX + +f, transY + top, transZ + +f).tex(uMax, vMin).endVertex();
		vertexBuffer.pos(transX + +f, transY + bottom, transZ + -f).tex(uMin, vMax).endVertex();
		vertexBuffer.pos(transX + +f, transY + bottom, transZ + +f).tex(uMax, vMax).endVertex();
		tessellator.draw();
		
		GL11.glDisable(GL11.GL_BLEND);
	}

}
