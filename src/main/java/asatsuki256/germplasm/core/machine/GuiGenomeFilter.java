package asatsuki256.germplasm.core.machine;

import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.gene.filter.FilterGenome;
import asatsuki256.germplasm.core.item.ItemFilterGenome;
import asatsuki256.germplasm.core.network.GermplasmPacketHandler;
import asatsuki256.germplasm.core.network.MessageGenomeFilter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class GuiGenomeFilter extends GuiContainer {
	
	private IItemHandler inventory;
	private int pairNum, chromosomeNum, geneNum;
	
	GuiButton pairInc, pairDec, chromosomeInc, chromosomeDec, geneInc, geneDec;
	
	private static final ResourceLocation TEXTURE0 = new ResourceLocation(GermplasmCore.MODID, "textures/gui/container/genome_filter.png");
	
    public GuiGenomeFilter(int x, int y, int z, InventoryPlayer inventoryPlayer, ItemStack itemstack) {
        super(new ContainerGenomeFilter(x, y, z, inventoryPlayer, itemstack));
        xSize = 178;
        ySize = 220;
        inventory = itemstack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        FilterGenome filter = ItemFilterGenome.getFilterGenome(itemstack);
        this.pairNum = filter.pairNum;
        this.chromosomeNum = filter.chromosomeNum;
        this.geneNum = filter.geneNum;
    }
    
    @Override
    public void initGui() {
    	super.initGui();
    	int id = 0;
    	this.pairInc = new GuiButton(id++, this.guiLeft + 60, this.guiTop + 40, 20, 20, "+");
    	this.pairDec = new GuiButton(id++, this.guiLeft + 60, this.guiTop + 80, 20, 20, "-");
    	this.chromosomeInc = new GuiButton(id++, this.guiLeft + 100, this.guiTop + 40, 20, 20, "+");
    	this.chromosomeDec = new GuiButton(id++, this.guiLeft + 100, this.guiTop + 80, 20, 20, "-");
    	this.geneInc = new GuiButton(id++, this.guiLeft + 140, this.guiTop + 40, 20, 20, "+");
    	this.geneDec = new GuiButton(id++, this.guiLeft + 140, this.guiTop + 80, 20, 20, "-");
    	this.buttonList.add(pairInc);
    	this.buttonList.add(pairDec);
    	this.buttonList.add(chromosomeInc);
    	this.buttonList.add(chromosomeDec);
    	this.buttonList.add(geneInc);
    	this.buttonList.add(geneDec);
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
        this.fontRenderer.drawString(I18n.format(UNLOC_PREFIX + "gui.genome_filter.name"), 8, 8, 0x404040);
        this.drawCenteredString(fontRenderer, "" + pairNum, 70, 65, 0xffffff);
        this.drawCenteredString(fontRenderer, "" + chromosomeNum, 110, 65, 0xffffff);
        this.drawCenteredString(fontRenderer, "" + geneNum, 150, 65, 0xffffff);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
        this.mc.renderEngine.bindTexture(TEXTURE0);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 175, 219);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    protected void actionPerformed(GuiButton btn) {
    	if (btn.id == pairInc.id) {
    		if (pairNum < 4) pairNum += 1;
    	} else if (btn.id == pairDec.id) {
    		if (pairNum > 0) pairNum -= 1;
    	} else if (btn.id == chromosomeInc.id) {
    		if (chromosomeNum < 2) chromosomeNum += 1;
    	} else if (btn.id == chromosomeDec.id) {
    		if (chromosomeNum > 0) chromosomeNum -= 1;
    	} else if (btn.id == geneInc.id) {
    		if (geneNum < 16) geneNum += 1;
    	} else if (btn.id == geneDec.id) {
    		if (geneNum > 0) geneNum -= 1;
    	}
    	updateTag();
    }
    
    private void updateTag() {
    	GermplasmPacketHandler.INSTANCE.sendToServer(new MessageGenomeFilter(pairNum, chromosomeNum, geneNum));
    }

}
