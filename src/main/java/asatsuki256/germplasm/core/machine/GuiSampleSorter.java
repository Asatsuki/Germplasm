package asatsuki256.germplasm.core.machine;

import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.network.GermplasmPacketHandler;
import asatsuki256.germplasm.core.network.MessageResearchDeskServer;
import asatsuki256.germplasm.core.network.MessageSampleSorter;
import asatsuki256.germplasm.core.network.MessageSampleSorter.MessageType;
import asatsuki256.germplasm.core.tileentity.TileSampleSorter;
import asatsuki256.germplasm.core.util.GmpmUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class GuiSampleSorter extends GuiContainer {
	
	TileSampleSorter tile;
	
	private static final ResourceLocation TEXTURE0 = new ResourceLocation(GermplasmCore.MODID, "textures/gui/container/sample_sorter.png");

	GuiButton autoInput;
	GuiButton autoOutput;
	GuiButton sourceFacing;
	GuiButton matchedFacing;
	GuiButton remainingFacing;
	
	public GuiSampleSorter(int x, int y, int z, InventoryPlayer inventoryPlayer, TileSampleSorter tile) {
        super(new ContainerSampleSorter(x, y, z, inventoryPlayer, tile));
        xSize = 178;
        ySize = 220;
        this.tile = tile;
    }
	
	@Override
    public void initGui() {
    	super.initGui();
    	this.autoInput = new GuiButton(0, this.guiLeft + 10, this.guiTop + 93, 65, 20, switchButtonName(UNLOC_PREFIX + "gui.sample_sorter.auto_input", tile.autoInput));
    	this.autoOutput = new GuiButton(1, this.guiLeft + 100, this.guiTop + 93, 65, 20, switchButtonName(UNLOC_PREFIX + "gui.sample_sorter.auto_output", tile.autoOutput));
    	this.sourceFacing = new GuiButton(2, this.guiLeft + 3, this.guiTop + 40, 20, 20, facingButtonName(tile.sourceFacing));
    	this.matchedFacing = new GuiButton(3, this.guiLeft + 152, this.guiTop + 23, 20, 20, facingButtonName(tile.matchedFacing));
    	this.remainingFacing = new GuiButton(4, this.guiLeft + 152, this.guiTop + 59, 20, 20, facingButtonName(tile.remainingFacing));
    	this.buttonList.add(autoInput);
    	this.buttonList.add(autoOutput);
    	this.buttonList.add(sourceFacing);
    	this.buttonList.add(matchedFacing);
    	this.buttonList.add(remainingFacing);
    }
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
	
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
        this.fontRenderer.drawString(I18n.format(UNLOC_PREFIX + "gui.sample_sorter.name"), 8, 8, 0x404040);
    }
	
	@Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
        this.mc.renderEngine.bindTexture(TEXTURE0);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 175, 219);
    }
	
	private String switchButtonName(String key, boolean bool) {
		String str = "";
		str += I18n.format(key);
		str += " : ";
		str += I18n.format(bool ? UNLOC_PREFIX + "gui.sample_sorter.on" : UNLOC_PREFIX + "gui.sample_sorter.off");
		return str;
	}
	
	@Override
    protected void actionPerformed(GuiButton btn) {
       if (btn.id == this.sourceFacing.id){
    	   GermplasmPacketHandler.INSTANCE.sendToServer(new MessageSampleSorter(tile.getPos(), MessageType.SOURCE_FACING));
    	   btn.displayString = facingButtonName(GmpmUtil.toggleFacing(tile.sourceFacing, 1));
       } else if (btn.id == this.matchedFacing.id){
    	   GermplasmPacketHandler.INSTANCE.sendToServer(new MessageSampleSorter(tile.getPos(), MessageType.MATCHED_FACING));
    	   btn.displayString = facingButtonName(GmpmUtil.toggleFacing(tile.matchedFacing, 1));
       } else if (btn.id == this.remainingFacing.id){
    	   GermplasmPacketHandler.INSTANCE.sendToServer(new MessageSampleSorter(tile.getPos(), MessageType.REMAINING_FACING));
    	   btn.displayString = facingButtonName(GmpmUtil.toggleFacing(tile.remainingFacing, 1));
       } else if (btn.id == this.autoInput.id){
    	   GermplasmPacketHandler.INSTANCE.sendToServer(new MessageSampleSorter(tile.getPos(), MessageType.AUTO_INPUT));
    	   btn.displayString = switchButtonName(UNLOC_PREFIX + "gui.sample_sorter.auto_input", !tile.autoInput);
       } else if (btn.id == this.autoOutput.id){
    	   GermplasmPacketHandler.INSTANCE.sendToServer(new MessageSampleSorter(tile.getPos(), MessageType.AUTO_OUTPUT));
    	   btn.displayString = switchButtonName(UNLOC_PREFIX + "gui.sample_sorter.auto_output", !tile.autoOutput);
       }
    }
	
	private String facingButtonName(EnumFacing facing) {
		switch (facing) {
		case UP:
			return I18n.format(UNLOC_PREFIX + "gui.sample_sorter.up");
		case DOWN:
			return I18n.format(UNLOC_PREFIX + "gui.sample_sorter.down");
		case EAST:
			return I18n.format(UNLOC_PREFIX + "gui.sample_sorter.right");
		case NORTH:
			return I18n.format(UNLOC_PREFIX + "gui.sample_sorter.front");
		case SOUTH:
			return I18n.format(UNLOC_PREFIX + "gui.sample_sorter.back");
		case WEST:
			return I18n.format(UNLOC_PREFIX + "gui.sample_sorter.left");
		default:
			return "";
		}
	}

}
