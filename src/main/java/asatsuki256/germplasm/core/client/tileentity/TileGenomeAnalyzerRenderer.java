package asatsuki256.germplasm.core.client.tileentity;

import org.lwjgl.opengl.GL11;

import asatsuki256.germplasm.core.block.BlockGenomeAnalyzer;
import asatsuki256.germplasm.core.tileentity.TileGenomeAnalyzer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TileGenomeAnalyzerRenderer extends TileEntitySpecialRenderer<TileGenomeAnalyzer> {
	
	public static TileGenomeAnalyzerRenderer renderer;
	private long lastTick;
	
	@Override
	public void render(TileGenomeAnalyzer analyzer, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
		if (analyzer != null) {
			World worldObj = analyzer.getWorld();
			if (worldObj.isBlockLoaded(analyzer.getPos())) {
				IBlockState blockState = worldObj.getBlockState(analyzer.getPos());
				if (blockState.getBlock() instanceof BlockGenomeAnalyzer) {
					EnumFacing facing = blockState.getValue(BlockGenomeAnalyzer.FACING);
					renderTile(analyzer.getStackInSlot(0), worldObj, facing, x, y, z);
					return;
				}
			}
		}
    }
	
	private void renderTile(ItemStack stack, World world, EnumFacing facing, double x, double y, double z) {
		if (stack.isEmpty() || world == null) {
			return;
		}
		
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		
		GlStateManager.translate(0.5f, 0.5f, 0.5f);
		switch(facing) {
		case NORTH:
			GlStateManager.rotate(0.0f, 0.0f, 1.0f, 0.0f);
			break;
		case SOUTH:
			GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
			break;
		case WEST:
			GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
			break;
		case EAST:
			GlStateManager.rotate(270.0f, 0.0f, 1.0f, 0.0f);
			break;
		default:
			break;
		}
		
		
		GlStateManager.translate(-0.125f, -0.25f, -0.25f);
		GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
		RenderHelper.enableStandardItemLighting();
		
		
		
		float renderScale = 0.8f;
		GlStateManager.scale(renderScale, renderScale, renderScale);
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.renderItem(stack, TransformType.GROUND);
		
		RenderHelper.disableStandardItemLighting();
		GlStateManager.popMatrix();
		
	}
	
	public void setRendererDispatcher(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super.setRendererDispatcher(rendererDispatcherIn);
        renderer = this;
    }
}
