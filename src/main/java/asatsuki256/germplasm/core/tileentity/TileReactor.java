package asatsuki256.germplasm.core.tileentity;

import static asatsuki256.germplasm.core.GermplasmCore.NBT_PREFIX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import asatsuki256.germplasm.api.recipe.IReactorRecipe;
import asatsuki256.germplasm.api.recipe.RecipeAPI;
import asatsuki256.germplasm.core.GermplasmConfig;
import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.capability.handler.ItemHandlerProcessing;
import asatsuki256.germplasm.core.energy.EnergyWrapperReceiver;
import asatsuki256.germplasm.core.util.GmpmItemUtil;
import asatsuki256.germplasm.core.util.GmpmItemUtil.MergeResult;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileReactor extends TileEntity implements ITickable{

	public static final int energyPerUpdate = 100;
	
	static Random rand = new Random();
	
	ItemStackHandler inventory;
	FluidHandlerReactor fluidTank;
	EnergyWrapperReceiver energy;
	int progress;
	int maxProgress;
	
	int timer;
	IReactorRecipe recipe;
	
	public TileReactor(){
		inventory = new ItemHandlerProcessing(5, 5);
		fluidTank = new FluidHandlerReactor(this);
		energy = new EnergyWrapperReceiver(100000, 500, 200);
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			if(++timer >= 5) {
				timer = 0;
				onContentsChanged();
			}
			transferFluidItem(3, 8, fluidTank.getReagentTank());
			transferFluidItem(4, 9, fluidTank.getResultTank());
			processRecipe();
		}
	}
	
	private void transferFluidItem(int slotContainer, int slotEmpty, FluidTank tank) {
		if(inventory.getStackInSlot(slotContainer).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			ItemStack container = inventory.getStackInSlot(slotContainer).copy();
			container.setCount(1);
			
			FluidActionResult testFluidResult = FluidUtil.tryEmptyContainer(container, tank, tank.getCapacity(), null, false);
			MergeResult testMergeResult = GmpmItemUtil.tryMergeItemStack(inventory.getStackInSlot(slotEmpty), testFluidResult.result);
			if(testFluidResult.success && testMergeResult.getSource().isEmpty()) {
				FluidActionResult fluidResult = FluidUtil.tryEmptyContainer(container, tank, tank.getCapacity(), null, true);
				MergeResult mergeResult = GmpmItemUtil.tryMergeItemStack(inventory.getStackInSlot(slotEmpty), fluidResult.result);
				inventory.setStackInSlot(slotContainer, mergeResult.getSource());
				inventory.setStackInSlot(slotEmpty, mergeResult.getDestination());
				return;
			}
			FluidActionResult testFluidExport = FluidUtil.tryFillContainer(container, tank, tank.getCapacity(), null, false);
			MergeResult testMergeExport = GmpmItemUtil.tryMergeItemStack(inventory.getStackInSlot(slotEmpty), testFluidExport.result);
			if (testFluidExport.success && testMergeExport.getSource().isEmpty()) {
				FluidActionResult fluidResult = FluidUtil.tryFillContainer(container, tank, tank.getCapacity(), null, true);
				MergeResult mergeResult = GmpmItemUtil.tryMergeItemStack(inventory.getStackInSlot(slotEmpty), fluidResult.result);
				inventory.setStackInSlot(slotContainer, mergeResult.getSource());
				inventory.setStackInSlot(slotEmpty, mergeResult.getDestination());
				return;
			}
			
		}
	}
	
	private void processRecipe() {
		NonNullList<ItemStack> items = NonNullList.create();
		items.add(inventory.getStackInSlot(0).copy());
		items.add(inventory.getStackInSlot(1).copy());
		items.add(inventory.getStackInSlot(2).copy());
		List<FluidStack> fluids = new ArrayList<FluidStack>();
		FluidStack reagentFluidStack = fluidTank.getReagentTank().getFluid();
		if(reagentFluidStack != null) fluids.add(reagentFluidStack.copy());
		recipe = RecipeAPI.recipeRegistry.getReactorRecipe(items, fluids);
		
		
		if(recipe == null) {
			progress = 0;
			return;
		}
		
		NonNullList<ItemStack> outputAll = recipe.getResultItemAll(items, fluids);
		List<FluidStack> outputFluid = recipe.getResultFluidFromInput(items, fluids);
		if(outputAll.size() > 3 || outputFluid.size() > 1) {
			progress = 0;
			return;
		}
		
		maxProgress = recipe.getEnergy();
		//progressがレシピのエネルギーに達していなければ、progressに加算
		if (progress < maxProgress) {
			if (GermplasmConfig.CONFIG_CORE.requireEnergy) {
				int maxExtract = Math.min(recipe.getEnergy() - progress, energyPerUpdate);
				int extract =  energy.extractInternal(maxExtract, false);
				progress += extract;
				return;
			} else {
				progress += Math.min(recipe.getEnergy() - progress, energyPerUpdate);
				return;
			}
		}
		
		//成果物欄にちゃんと成果物が全部入るか判断
		for (int i = 0; i < outputAll.size(); i++) {
			// TODO: 場所が違ってもスタック可能な場所があれば作れるようにする
			if (!GmpmItemUtil.tryMergeItemStack(inventory.getStackInSlot(i+5), outputAll.get(i)).getSource().isEmpty()) {
				return;
			}
		}
		for (int i = 0; i < outputFluid.size(); i++) {
			if (outputFluid.get(i) != null && fluidTank.resultTank.fill(outputFluid.get(i), false) < outputFluid.get(i).amount) {
				return;
			}
		}
		//クラフト遂行
		NonNullList<List<ItemStack>> inputItem = recipe.getIngredientItem();
		List<ItemStack> inputs = Arrays.asList(inventory.getStackInSlot(0), inventory.getStackInSlot(1), inventory.getStackInSlot(2));
		for (List<ItemStack> itemStack : inputItem) {
			GmpmItemUtil.takeItemStack(inputs, true, itemStack.toArray(new ItemStack[itemStack.size()]));
		}
		NonNullList<List<FluidStack>> inputFluid = recipe.getIngredientFluid();
		for (int i = 0; i < inputFluid.size(); i++) {
			for (int j = 0; j < inputFluid.get(i).size(); j++) {
				fluidTank.reagentTank.drain(inputFluid.get(i).get(j), true);
			}
		}
		//成果物を成果物スロットに格納
		NonNullList<ItemStack> output = recipe.getResultItemFromInput(items, fluids, rand);
		for (int i = 0; i < output.size(); i++) {
			MergeResult result = GmpmItemUtil.tryMergeItemStack(inventory.getStackInSlot(i+5), output.get(i));
			inventory.setStackInSlot(i+5, result.getDestination());
		}
		for (int i = 0; i < outputFluid.size(); i++) {
			GermplasmCore.LOGGER.info(outputFluid.get(i).copy().getLocalizedName());
			fluidTank.resultTank.fill(outputFluid.get(i).copy(), true);
		}
		progress = 0;
		onContentsChanged();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		inventory.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "inventory"));
		fluidTank.readFromNBT(nbt.getCompoundTag(NBT_PREFIX + "fluidTank"));
		energy.deserializeNBT(nbt.getCompoundTag(NBT_PREFIX + "energy"));
		progress = nbt.getInteger(NBT_PREFIX + "progress");
		maxProgress = nbt.getInteger(NBT_PREFIX + "maxProgress");
	}
	
	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setTag(NBT_PREFIX + "inventory", inventory.serializeNBT());
		nbt.setTag(NBT_PREFIX + "fluidTank", fluidTank.writeToNBT(new NBTTagCompound()));
		nbt.setTag(NBT_PREFIX + "energy", energy.serializeNBT());
		nbt.setInteger(NBT_PREFIX + "progress", progress);
		nbt.setInteger(NBT_PREFIX + "maxProgress", maxProgress);
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

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return true;
		} else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		} else if (capability == CapabilityEnergy.ENERGY) {
		    return true;
		  }
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T)inventory;
		} else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (T)fluidTank;
		} else if (capability == CapabilityEnergy.ENERGY) {
			return (T) energy;
		}
		return super.getCapability(capability, facing);
	}
	
	public FluidHandlerReactor getFluidHandlerReactor() {
		return fluidTank;
	}
	
	public void onContentsChanged() {
		this.markDirty();
		this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);

	}
	
	public IEnergyStorage getEnergy() {
		return energy;
	}
	
	public float getProgress() {
		if (maxProgress <= 0) {
			return 0f;
		}
		return (float)progress / (float)maxProgress;
	}

	public class FluidHandlerReactor implements IFluidHandler {
		
		TileReactor tile;
		FluidTank reagentTank;
		FluidTank resultTank;
		
		public FluidHandlerReactor(TileReactor tile){
			this.tile = tile;
			reagentTank = new FluidTankReactor(8000, this);
			resultTank = new FluidTankReactor(8000, this);
		}
		
		@Override
		public IFluidTankProperties[] getTankProperties() {
			List<IFluidTankProperties> properties = new ArrayList<IFluidTankProperties>();
			properties.addAll(Arrays.asList(reagentTank.getTankProperties()));
			properties.addAll(Arrays.asList(resultTank.getTankProperties()));
			return properties.toArray(new IFluidTankProperties[0]);
		}
		
		public FluidHandlerReactor readFromNBT(NBTTagCompound nbt)
	    {
			reagentTank.readFromNBT(nbt.getCompoundTag("reagentTank"));
			resultTank.readFromNBT(nbt.getCompoundTag("resultTank"));
			return this;
	    }
		
		public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	    {
			NBTTagCompound tagReagent = new NBTTagCompound();
			reagentTank.writeToNBT(tagReagent);
			NBTTagCompound tagResult = new NBTTagCompound();
			resultTank.writeToNBT(tagResult);
			nbt.setTag("reagentTank", tagReagent);
			nbt.setTag("resultTank", tagResult);
			return nbt;
	    }
		
		@Override
		public int fill(FluidStack resource, boolean doFill) {
			return reagentTank.fill(resource, doFill);
		}
		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			return resultTank.drain(resource, doDrain);
		}
		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			return resultTank.drain(maxDrain, doDrain);
		}

		public FluidTank getReagentTank() {
			return reagentTank;
		}
		
		public FluidTank getResultTank() {
			return resultTank;
		}
		
		public void onContentsChanged() {
			tile.onContentsChanged();
		}
		
	}
	
	public class FluidTankReactor extends FluidTank {
		
		FluidHandlerReactor parent;

		public FluidTankReactor(int capacity, FluidHandlerReactor parent) {
			super(capacity);
			this.parent = parent;
		}
		
		@Override
		protected void onContentsChanged() {
			parent.onContentsChanged();
		}
		
	}
	
	public enum SlotType {
		INGREDIENT, RESULT, INVALID
	}

}
