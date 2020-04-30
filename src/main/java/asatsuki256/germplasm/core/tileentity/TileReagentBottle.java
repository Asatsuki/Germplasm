package asatsuki256.germplasm.core.tileentity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import asatsuki256.germplasm.core.block.BlockReagentBottle;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileReagentBottle extends TileEntity implements IFluidHandler, ITickable {
	
	public static final int bottleCount = 4;
	
	private FluidStack[] fluidStack = new FluidStack[bottleCount];
	public static final int CAPACITY = 10000;
	
	public static final List<PropertyBool> blockStates = new ArrayList<PropertyBool>(Arrays.asList(
			BlockReagentBottle.NORTH_WEST, BlockReagentBottle.NORTH_EAST, BlockReagentBottle.SOUTH_WEST, BlockReagentBottle.SOUTH_EAST));

	public TileReagentBottle() {
		for (int i = 0; i < bottleCount; i++) {
			fluidStack[i] = null;
		}
	}
	
	public TileReagentBottle(PropertyBool property, FluidStack fluid) {
		this();
		fluidStack[blockStates.indexOf(property)] = fluid;
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
	public void readFromNBT(NBTTagCompound nbt)
    {
        for (int i = 0; i < bottleCount; i++) {
        	NBTTagCompound fluidNBT = nbt.getCompoundTag("Bottle" + i);
        	if (fluidNBT.hasKey("Empty")) {
        		fluidStack[i] = null;
        	} else {
        		fluidStack[i] = FluidStack.loadFluidStackFromNBT(fluidNBT);
        	}
        }
        super.readFromNBT(nbt);
    }

	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        for(int i = 0; i < bottleCount; i++) {
        	NBTTagCompound fluidNBT = new NBTTagCompound();
    		if (fluidStack[i] == null) {
    			fluidNBT.setString("Empty", "");
        	} else {
        		fluidStack[i].writeToNBT(fluidNBT);
        	}
    		nbt.setTag("Bottle" + i, fluidNBT);
        }
        return super.writeToNBT(nbt);
    }
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
	  if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
	    return true;
	  }
	  return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
	  if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
	    return (T) this;
	  }
	  return super.getCapability(capability, facing);
	}
	
	@Override
	public IFluidTankProperties[] getTankProperties() {
		IFluidTankProperties[] propaties = new IFluidTankProperties[bottleCount];
		for (int i = 0; i < bottleCount; i++) {
			propaties[i] = new FluidTankProperties(fluidStack[i], CAPACITY);
		}
		return propaties;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (getFirstAcceptableBottle(resource) < 0) {
			return 0;
		}
		return fillInternal(resource, doFill, getFirstAcceptableBottle(resource));
	}
	
	public FluidStack getFluid(int bottle) {
		return fluidStack[bottle];
	}
	
	public void setFluid(FluidStack fluid, int bottle) {
		fluidStack[bottle] = fluid;
	}
	
	public int fillInternal(FluidStack resource, boolean doFill, int bottle)
    {
        if (resource == null || resource.amount <= 0)
        {
            return 0;
        }

        if (!doFill)
        {
            if (fluidStack[bottle] == null)
            {
                return Math.min(CAPACITY, resource.amount);
            }

            if (!fluidStack[bottle].isFluidEqual(resource))
            {
                return 0;
            }

            return Math.min(CAPACITY - fluidStack[bottle].amount, resource.amount);
        }

        if (fluidStack[bottle] == null)
        {
        	fluidStack[bottle] = new FluidStack(resource, Math.min(CAPACITY, resource.amount));

            onContentsChanged();
            return fluidStack[bottle].amount;
        }

        if (!fluidStack[bottle].isFluidEqual(resource))
        {
            return 0;
        }
        int filled = CAPACITY - fluidStack[bottle].amount;

        if (resource.amount < filled)
        {
        	fluidStack[bottle].amount += resource.amount;
            filled = resource.amount;
        }
        else
        {
        	fluidStack[bottle].amount = CAPACITY;
        }

        onContentsChanged();

        return filled;
    }

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		int bottle = getLastDrainableBottle(resource.getFluid());
		if (bottle < 0)
        {
            return null;
        }
        return drainInternal(resource, doDrain, bottle);
	}
	
	public FluidStack drainInternal(FluidStack resource, boolean doDrain, int bottle)
    {
        if (resource == null || !resource.isFluidEqual(fluidStack[bottle]))
        {
            return null;
        }
        return drainInternal(resource.amount, doDrain, bottle);
    }

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		int bottle = getLastDrainableBottle(null);
		if (bottle < 0)
        {
            return null;
        }
        return drainInternal(maxDrain, doDrain, bottle);
	}
	
	@Override
	public void onLoad()
    {
        super.onLoad();
        onContentsChanged();
    }
	
	public FluidStack drainInternal(int maxDrain, boolean doDrain, int bottle)
    {
        if (fluidStack[bottle] == null || maxDrain <= 0)
        {
            return null;
        }

        int drained = maxDrain;
        if (fluidStack[bottle].amount < drained)
        {
            drained = fluidStack[bottle].amount;
        }

        FluidStack stack = new FluidStack(fluidStack[bottle], drained);
        if (doDrain)
        {
        	fluidStack[bottle].amount -= drained;
            if (fluidStack[bottle].amount <= 0)
            {
            	fluidStack[bottle] = null;
            }

            onContentsChanged();
        }
        return stack;
    }
	
	private int getFirstAcceptableBottle(FluidStack resource) {
		int bottle = -1;
		for (int i = 0; i < bottleCount; i++) {
			if (isBottleExists(i) && ((resource.isFluidEqual(fluidStack[i]) && fluidStack[i].amount < CAPACITY) || isFluidStackEmpty(fluidStack[i]))) {
				bottle = i;
				if(fluidStack[i] != null) {
					return i;
				}
			}
		}
		return bottle;
	}
	
	private int getLastDrainableBottle(Fluid resource) {
		int bottle = -1;
		for (int i = 0; i < bottleCount; i++) {
			int j = bottleCount -1 - i;
			if (isBottleExists(j) && !isFluidStackEmpty(fluidStack[j]) && (resource == null || fluidStack[j].getFluid().equals(resource))) {
				return j;
			}
		}
		return bottle;
	}
	
	private boolean isFluidStackEmpty(FluidStack fluid) {
		return fluid == null || fluid.getFluid() == null || fluid.amount <= 0;
	}
	
	private boolean isBottleExists(int bottle) {
		return this.world.getBlockState(this.pos).getValue(blockStates.get(bottle));
	}
	
	private void onContentsChanged() {
		this.markDirty();
		this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);

	}

	@Override
	public void update() {
		// NOOP
	}
	
	
	
}
