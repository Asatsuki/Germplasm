package asatsuki256.germplasm.core.client.tileentity;

import org.lwjgl.opengl.GL11;

import asatsuki256.germplasm.core.block.BlockGenomeAnalyzer;
import asatsuki256.germplasm.core.block.BlockMicroscope;
import asatsuki256.germplasm.core.tileentity.TileMicroscope;
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

public class TileMicroscopeRenderer extends TileEntitySpecialRenderer<TileMicroscope> {
	
	public static TileMicroscopeRenderer renderer;
	
	@Override
	public void render(TileMicroscope tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
		if (tile != null) {
			World worldObj = tile.getWorld();
			if (worldObj.isBlockLoaded(tile.getPos())) {
				IBlockState blockState = worldObj.getBlockState(tile.getPos());
				if (blockState.getBlock() instanceof BlockMicroscope) {
					EnumFacing facing = blockState.getValue(BlockGenomeAnalyzer.FACING);
					renderTile(tile.getStackInSlot(0), worldObj, facing, x, y, z);
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
		
		
		GlStateManager.translate(0f, -0.28f, -0.1f);
		GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
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
