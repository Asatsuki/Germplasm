package asatsuki256.germplasm.core.tileentity;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import java.util.List;

import asatsuki256.germplasm.api.gene.GeneAPI;
import asatsuki256.germplasm.api.gene.filter.IGeneticFilter;
import asatsuki256.germplasm.api.gene.unit.IGermplasmUnitBase;
import asatsuki256.germplasm.core.block.BlockSampleSorter;
import asatsuki256.germplasm.core.capability.handler.ItemHandlerExtractOnly;
import asatsuki256.germplasm.core.capability.handler.ItemHandlerInsertOnly;
import asatsuki256.germplasm.core.item.GermplasmItems;
import asatsuki256.germplasm.core.item.ItemGeneticFilter;
import asatsuki256.germplasm.core.util.GmpmItemUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ITickable;
import net.minecraft.util.Rotation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import scala.actors.threadpool.Arrays;

public class TileSampleSorter extends TileEntity implements ITickable {
	
	public static final int transferTick = 10;
	public static final List<Item> acceptables = Arrays.asList(new Item[] {
			GermplasmItems.seed_sample,
			GermplasmItems.genome_sample,
			GermplasmItems.gene_sample,
			GermplasmItems.callus
	});
	
	public ItemHandlerInsertOnly sourceInv;
	public ItemHandlerExtractOnly matchedInv;
	public ItemHandlerExtractOnly remainingInv;
	public ItemStackHandler filterInv;
	public int tick = 0;
	public EnumFacing sourceFacing;
	public EnumFacing matchedFacing;
	public EnumFacing remainingFacing;
	public boolean autoInput;
	public boolean autoOutput;
	
	public TileSampleSorter() {
		sourceInv = new ItemHandlerInsertOnly(3);
		matchedInv = new ItemHandlerExtractOnly(3);
		remainingInv = new ItemHandlerExtractOnly(3);
		filterInv = new ItemStackHandler(1);
		sourceFacing = EnumFacing.UP;
		matchedFacing = EnumFacing.DOWN;
		remainingFacing = EnumFacing.NORTH;
		autoInput = true;
		autoOutput = true;
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			this.markDirty();
			this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
			if (++tick < transferTick) return;
			tick = 0;
			process();
			transfer();
		}
	}
	
	private void process() {
		ItemStack processingItem = ItemStack.EMPTY;
		int slot = -1;
		for (int i = 0; i < sourceInv.getSlots(); i++) {
			ItemStack extract = sourceInv.extractInternal(i, 1, true);
			int matchedSlot = getInsertableSlot(extract, matchedInv);
			int remainingSlot = getInsertableSlot(extract, remainingInv);
			if (!extract.isEmpty() && matchedSlot >= 0 && remainingSlot >= 0) {
				processingItem = extract;
				slot = i;
				break;
			}
		}
		if (processingItem.isEmpty() || slot < 0) return;
		
		sourceInv.extractInternal(slot, 1, false);
		if (isItemAcceptable(processingItem)) {
			matchedInv.insertInternal(getInsertableSlot(processingItem, matchedInv), processingItem, false);
		} else {
			remainingInv.insertInternal(getInsertableSlot(processingItem, remainingInv), processingItem, false);
		}
	}
	
	private void transfer() {
		EnumFacing facing = EnumFacing.NORTH;
		if (world.getBlockState(pos).getBlock() instanceof BlockSampleSorter) {
			facing = world.getBlockState(pos).getValue(BlockSampleSorter.FACING);
		}
		if (autoInput) {
			inputItem(sourceInv, rotated(sourceFacing, facing));
		}
		if (autoOutput) {
			outputItem(matchedInv, rotated(matchedFacing, facing));
			outputItem(remainingInv, rotated(remainingFacing, facing));
		}
	}
	
	private void inputItem(IItemHandler destination, EnumFacing facing) {
		TileEntity tile = world.getTileEntity(pos.offset(facing));
		if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
			IItemHandler inputInv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
			GmpmItemUtil.transferSingleItem(inputInv, destination);
		}
	}
	
	private void outputItem(IItemHandler source, EnumFacing facing) {
		TileEntity matchedTile = world.getTileEntity(pos.offset(facing));
		if (matchedTile != null && matchedTile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
			IItemHandler outputInv = matchedTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
			GmpmItemUtil.transferSingleItem(source, outputInv);
		}
	}
	
	/*
	 * itemstackをinvにすべて挿入可能ならスロット、不可能なら-1
	 */
	private int getInsertableSlot(ItemStack itemstack, ItemHandlerExtractOnly inv) {
		int matchedInsSlot = -1;
		for (int i = 0; i < inv.getSlots(); i++) {
			ItemStack insert = inv.insertInternal(i, itemstack, true);
			if (insert.isEmpty()) {
				matchedInsSlot = i;
				break;
			}
		}
		return matchedInsSlot;
	}
	
	private boolean isItemAcceptable(ItemStack itemstack) {
		if (!acceptables.contains(itemstack.getItem())) return false;
		if (filterInv.getStackInSlot(0).getItem() instanceof ItemGeneticFilter) {
			IGermplasmUnitBase unit = GeneAPI.nbtHelper.getUnitFromIndividualNBT(itemstack.getTagCompound());
			if (unit == null) return false;
			ItemGeneticFilter itemGeneticFilter = (ItemGeneticFilter) filterInv.getStackInSlot(0).getItem();
			IGeneticFilter filter = itemGeneticFilter.getFilter(filterInv.getStackInSlot(0));
			return filter.isMatched(unit);
		}
		return true;
	}
	
	private EnumFacing rotated(EnumFacing configured, EnumFacing block) {
		if (configured.getAxis() == Axis.Y) {
			return configured;
		}
		switch (block) {
		case NORTH:
			return Rotation.NONE.rotate(configured);
		case SOUTH:
			return Rotation.CLOCKWISE_180.rotate(configured);
		case WEST:
			return Rotation.COUNTERCLOCKWISE_90.rotate(configured);
		case EAST:
			return Rotation.CLOCKWISE_90.rotate(configured);
		default:
			return Rotation.NONE.rotate(configured);
		}
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		EnumFacing blockFacing = EnumFacing.NORTH;
		if (world.getBlockState(pos).getBlock() instanceof BlockSampleSorter) {
			blockFacing = world.getBlockState(pos).getValue(BlockSampleSorter.FACING);
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == rotated(sourceFacing, blockFacing)) return true;
			if (facing == rotated(matchedFacing, blockFacing)) return true;
			if (facing == rotated(remainingFacing, blockFacing)) return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		EnumFacing blockFacing = EnumFacing.NORTH;
		if (world.getBlockState(pos).getBlock() instanceof BlockSampleSorter) {
			blockFacing = world.getBlockState(pos).getValue(BlockSampleSorter.FACING);
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == rotated(sourceFacing, blockFacing)) return (T) sourceInv;
			if (facing == rotated(matchedFacing, blockFacing)) return (T) matchedInv;
			if (facing == rotated(remainingFacing, blockFacing)) return (T) remainingInv;
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		sourceInv.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "sourceInv"));
		matchedInv.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "matchedInv"));
		remainingInv.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "remainingInv"));
		filterInv.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "filterInv"));
		tick = nbt.getInteger(NBT_PREFIX + "tick");
		sourceFacing = EnumFacing.byName(nbt.getString(NBT_PREFIX + "sourceFacing"));
		matchedFacing = EnumFacing.byName(nbt.getString(NBT_PREFIX + "matchedFacing"));
		remainingFacing = EnumFacing.byName(nbt.getString(NBT_PREFIX + "remainingFacing"));
		autoInput = nbt.getBoolean(NBT_PREFIX + "autoInput");
		autoOutput = nbt.getBoolean(NBT_PREFIX + "autoOutput");
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setTag(NBT_PREFIX + "sourceInv", sourceInv.serializeNBT());
		nbt.setTag(NBT_PREFIX + "matchedInv", matchedInv.serializeNBT());
		nbt.setTag(NBT_PREFIX + "remainingInv", remainingInv.serializeNBT());
		nbt.setTag(NBT_PREFIX + "filterInv", filterInv.serializeNBT());
		nbt.setInteger(NBT_PREFIX + "tick", tick);
		nbt.setString(NBT_PREFIX + "sourceFacing", sourceFacing.getName());
		nbt.setString(NBT_PREFIX + "matchedFacing", matchedFacing.getName());
		nbt.setString(NBT_PREFIX + "remainingFacing", remainingFacing.getName());
		nbt.setBoolean(NBT_PREFIX + "autoInput", autoInput);
		nbt.setBoolean(NBT_PREFIX + "autoOutput", autoOutput);
		return nbt;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbt = new NBTTagCompound();
		return this.writeToNBT(nbt);
    }
	
	@Override
	public void handleUpdateTag(NBTTagCompound nbt) {
		this.readFromNBT(nbt);
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
	    NBTTagCompound nbtTag = new NBTTagCompound();
	    writeToNBT(nbtTag);
	    return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
	    readFromNBT(pkt.getNbtCompound());
	}
	
	public ItemStackHandler getSource() {
		return sourceInv;
	}
	
	public ItemStackHandler getMatched() {
		return matchedInv;
	}
	
	public ItemStackHandler getRemaining() {
		return remainingInv;
	}
	
	public ItemStackHandler getFilter() {
		return filterInv;
	}

}
