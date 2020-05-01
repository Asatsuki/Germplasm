package asatsuki256.germplasm.core.machine;

import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import java.util.ArrayList;
import java.util.List;

import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.tileentity.TileElectroporator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GuiElectroporator extends GuiContainer {
	
	private final IItemHandler inventory;
	TileElectroporator tile;
	
	private static final ResourceLocation TEXTURE0 = new ResourceLocation(GermplasmCore.MODID, "textures/gui/container/electroporator.png");

	public GuiElectroporator(int x, int y, int z, InventoryPlayer inventoryPlayer, TileElectroporator tile) {
        super(new ContainerElectroporator(x, y, z, inventoryPlayer, tile));
        xSize = 178;
        ySize = 220;
        inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        this.tile = tile;
    }
	
	@Override
    public void initGui() {
    	super.initGui();
    }
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (mouseX >= guiLeft+7-1 && mouseX < guiLeft+7+32+1 && mouseY >= guiTop+25-1 && mouseY < guiTop+25+4+1) {
        	List<String> desc = new ArrayList<String>();
        	desc.add("Energy");
        	desc.add(String.format("%,3d", tile.getEnergy().getEnergyStored()) + " / " + String.format("%,3d", tile.getEnergy().getMaxEnergyStored()) + " FE");
        	drawHoveringText(desc, mouseX, mouseY);
        } else if (mouseX >= guiLeft+73-1 && mouseX < guiLeft+73+32+1 && mouseY >= guiTop+40-1 && mouseY < guiTop+40+4+1) {
        	List<String> desc = new ArrayList<String>();
        	desc.add("Buffered Energy");
        	desc.add(String.format("%,3d", tile.getEnergyBuffer().getEnergyStored()) + " / " + String.format("%,3d", tile.getEnergyBuffer().getMaxEnergyStored()) + " FE");
        	drawHoveringText(desc, mouseX, mouseY);
        } else {
        	this.renderHoveredToolTip(mouseX, mouseY);
        }
    }
	
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
        this.fontRenderer.drawString(I18n.format(UNLOC_PREFIX + "gui.electroporator.name"), 8, 8, 0x404040);
    }
	
	@Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
        this.mc.renderEngine.bindTexture(TEXTURE0);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 177, 219);
        //progress
        this.drawTexturedModalRect(guiLeft + 66, guiTop + 48, 192, 40, MathHelper.ceil(tile.getProgress() * 46), 48);
        //energy
        float energy = (float)tile.getEnergy().getEnergyStored() / (float)tile.getEnergy().getMaxEnergyStored();
        this.drawTexturedModalRect(guiLeft + 7, guiTop + 25, 192, 32, MathHelper.ceil(energy * 32), 4);
        //energy buffer
        float energyBuffer = (float)tile.getEnergyBuffer().getEnergyStored() / (float)tile.getEnergyBuffer().getMaxEnergyStored();
        this.drawTexturedModalRect(guiLeft + 73, guiTop + 40, 192, 0, MathHelper.ceil(energyBuffer * 32), 4);
    }

}
