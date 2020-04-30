package asatsuki256.germplasm.core.machine;

import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.TraitType;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.tileentity.TileMicroscope;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class GuiMicroscope extends GuiContainer {
	
	private IInventory inventory;
	private TileMicroscope tile;
	private IGermplasmUnitBase unit;
	
	private int textLeft = 0;
	private int textTop = 0;
	private int line = 0;
	private int offset = 0;
	
	
	private static final ResourceLocation TEXTURE0 = new ResourceLocation(GermplasmCore.MODID, "textures/gui/container/microscope_0.png");
	private static final ResourceLocation TEXTURE1 = new ResourceLocation(GermplasmCore.MODID, "textures/gui/container/microscope_1.png");
	
	
	public GuiMicroscope(int x, int y, int z, InventoryPlayer inventoryPlayer, TileMicroscope tile) {
        super(new ContainerMicroscope(x, y, z, inventoryPlayer, tile));
        xSize = 352;
        ySize = 219;
        inventory = tile;
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
        this.renderHoveredToolTip(mouseX, mouseY);
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.mc.renderEngine.bindTexture(TEXTURE0);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 175, 219);
        this.mc.renderEngine.bindTexture(TEXTURE1);
        this.drawTexturedModalRect(this.guiLeft + 175, this.guiTop, 0, 0, 177, 219);
        
	}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseZ);
		resetLine(183, 6);
		drawString(I18n.format(UNLOC_PREFIX + "gui.microscope.name"), 0x404040);
		
		//Trait情報
		resetLine(12, 10);
		NBTTagCompound nbt = inventory.getStackInSlot(0).getTagCompound();
        unit = GeneAPI.nbtHelper.getUnitFromIndividualNBT(nbt);
        if(unit != null) {
        	for(TraitType traitType : unit.getTraitTypes()) {
        		drawString(traitType.getDisplayName() + ": " + unit.getTotalStrength(traitType), 0x000000);
        	}
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
