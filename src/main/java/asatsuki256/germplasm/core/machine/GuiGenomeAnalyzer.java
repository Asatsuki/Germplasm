package asatsuki256.germplasm.core.machine;

import static asatsuki256.germplasm.core.GermplasmCore.UNLOC_PREFIX;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.TraitType;
import asatsuki256.germplasm.api.gene.unit.IChromosome;
import asatsuki256.germplasm.api.gene.unit.IChromosomePair;
import asatsuki256.germplasm.api.gene.unit.IGene;
import asatsuki256.germplasm.api.gene.unit.IGenome;
import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.tileentity.TileGenomeAnalyzer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class GuiGenomeAnalyzer extends GuiContainer{
	
	private IInventory inventory;
	private IEnergyStorage energy;
	private TileGenomeAnalyzer tile;
	
	private static final ResourceLocation TEXTURE0 = new ResourceLocation(GermplasmCore.MODID, "textures/gui/container/genome_sequencer_0.png");
	private static final ResourceLocation TEXTURE1 = new ResourceLocation(GermplasmCore.MODID, "textures/gui/container/genome_sequencer_1.png");
	
	private int textLeft = 0;
	private int textTop = 0;
	
	private int line = 0;
	private int offset = 0;
	
	private IGenome genome;
	private int page = 0;
	
	public static final int BTN_LEFT = 1;
	public static final int BTN_RIGHT = 1;
	
	GuiButton buttonLeft;
	GuiButton buttonRight;
	
    public GuiGenomeAnalyzer(int x, int y, int z, InventoryPlayer inventoryPlayer, TileGenomeAnalyzer tile) {
        super(new ContainerGenomeAnalyzer(x, y, z, inventoryPlayer, tile));
        xSize = 352;
        ySize = 219;
        inventory = tile;
        energy = tile.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
        this.tile = tile;
    }
    
    @Override
    public void initGui() {
    	super.initGui();
    	buttonLeft = new GuiButton(BTN_LEFT, this.guiLeft + 40, this.guiTop + 195, 20, 20, "<");
    	buttonRight = new GuiButton(BTN_RIGHT, this.guiLeft + 115, this.guiTop + 195, 20, 20, ">");
    	this.buttonList.add(buttonLeft);
    	this.buttonList.add(buttonRight);
    	genome = null;
    	//genome = AgriGenetics.sampleGenome;
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
        
        NBTTagCompound nbt = inventory.getStackInSlot(1).getTagCompound();
        genome = GeneAPI.nbtHelper.getGenomeFromIndividualNBT(nbt);
        if(genome != null) {
        	if(genome.isAnalyzed()) {
        		resetLine(10, 8);
                drawString(TextFormatting.GOLD + genome.getDisplayName(), 0xffffff);
                IChromosomePair pair = genome.getPairs().get(page);
                drawString(TextFormatting.LIGHT_PURPLE + pair.getDisplayName(page+1), 0xffffff);
                
                for(int i = 0; i < 2; i++) {
                	IChromosome chromosome = pair.getChromosomes().get(i);
                	//resetLine(10 + 80 * i, 30);
                	resetLine(10, 30 + 83 * i);
                	drawString(TextFormatting.AQUA + chromosome.getDisplayName(), 0xffffff);
                	for(IGene gene : chromosome.getGenes()) {
                		drawString(TextFormatting.YELLOW + (gene.isDominant() ? "*" : "") + gene.getDisplayName(), 0xffffff);
                	}
                }
                resetLine(186, 8);
                drawString(TextFormatting.YELLOW + "Pair information", 0xffffff);
                for(TraitType traitType : pair.getTraitTypes()) {
            		drawString(traitType.getDisplayName() + ": " + pair.getTotalStrength(traitType), 0xffffff);
            	}
        	} else {
        		resetLine(10, 8);
                drawString(TextFormatting.WHITE + I18n.format(UNLOC_PREFIX + "gui.genome_analyzer.analyzing"), 0xffffff);
        	}
        }
        
        resetLine(255, 110);
        drawString("EN:" + energy.getEnergyStored() + "/" + energy.getMaxEnergyStored(), 0x333333); //エネルギー量
        
        resetLine(75, 201);
        drawString((page+1) + " / " + 4, 0x333333); //ページ
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

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseZ) {
        this.mc.renderEngine.bindTexture(TEXTURE0);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 175, 219);
        this.mc.renderEngine.bindTexture(TEXTURE1);
        this.drawTexturedModalRect(this.guiLeft + 175, this.guiTop, 0, 0, 177, 219);
        this.drawTexturedModalRect(this.guiLeft + 203, this.guiTop + 108, 192, 0, MathHelper.ceil(22 * tile.getProgressBar()), 11);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    protected void actionPerformed(GuiButton btn) {
    	if(btn == buttonLeft) {
    		if(--page < 0) page = 0;
    	}else if(btn == buttonRight) {
    		if(++page > 3) page = 3;
    	}
    }

}
