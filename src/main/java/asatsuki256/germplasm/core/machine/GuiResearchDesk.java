package asatsuki256.germplasm.core.machine;

import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.network.GermplasmPacketHandler;
import asatsuki256.germplasm.core.network.MessageResearchDeskServer;
import asatsuki256.germplasm.core.tileentity.TileResearchDesk;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class GuiResearchDesk extends GuiContainer {
	
	private BlockPos pos;
	private IInventory inventory;
	private TileResearchDesk tile;
	private IGermplasmUnitBase unit;
	
	private int textLeft = 0;
	private int textTop = 0;
	private int line = 0;
	private int offset = 0;
	
	GuiTextField textbox;
	
	GuiButton changeName;
	GuiButton resetGeneration;
	
	private static final ResourceLocation TEXTURE0 = new ResourceLocation(GermplasmCore.MODID, "textures/gui/container/research_desk.png");
	
	
	public GuiResearchDesk(int x, int y, int z, InventoryPlayer inventoryPlayer, TileResearchDesk tile) {
        super(new ContainerResearchDesk(x, y, z, inventoryPlayer, tile));
        pos = new BlockPos(x, y, z);
        xSize = 176;
        ySize = 219;
        inventory = tile;
        this.tile = tile;
    }

	@Override
    public void initGui() {
    	super.initGui();
    	int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.textbox = new GuiTextField(0, this.fontRenderer, this.guiLeft + 12, this.guiTop + 17, 100, 12);
    	this.changeName = new GuiButton(1, this.guiLeft + 120, this.guiTop + 13, 45, 20, I18n.format(UNLOC_PREFIX + "gui.research_desk.button.change_name"));
    	this.resetGeneration = new GuiButton(2, this.guiLeft + 40, this.guiTop + 40, 100, 20, I18n.format(UNLOC_PREFIX + "gui.research_desk.button.reset_generation"));
    	this.buttonList.add(changeName);
    	this.buttonList.add(resetGeneration);
    }
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.textbox.drawTextBox();
        this.renderHoveredToolTip(mouseX, mouseY);
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(TEXTURE0);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 219);
        
	}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
		resetLine(6, 6);
		drawString(I18n.format(UNLOC_PREFIX + "gui.research_desk.name"), 0x404040);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int z) throws IOException
    {
        super.mouseClicked(x, y, z);
        this.textbox.mouseClicked(x, y, z);
    }
	
	@Override
    protected void actionPerformed(GuiButton btn) {
       if(btn.id == this.changeName.id){
    	   GermplasmPacketHandler.INSTANCE.sendToServer(new MessageResearchDeskServer(pos, MessageResearchDeskServer.TYPE_NAME, textbox.getText()));
       }else if(btn.id == this.resetGeneration.id){
    	   GermplasmPacketHandler.INSTANCE.sendToServer(new MessageResearchDeskServer(pos, MessageResearchDeskServer.TYPE_GENERATION_RESET, ""));
       }
    }
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (this.textbox.textboxKeyTyped(typedChar, keyCode))
        {
            
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }
		
	private void resetLine(int textLeft, int textTop) {
    	this.textLeft = textLeft;
    	this.textTop = textTop;
    	line = 0;
    }
	
	private void drawString(String string, int color) {
    	this.fontRenderer.drawString(string, textLeft + offset, textTop + line*10, color);
    	line++;
    }

}
