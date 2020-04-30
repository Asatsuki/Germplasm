package asatsuki256.germplasm.core.network;

import asatsuki256.germplasm.core.GermplasmCore;
import asatsuki256.germplasm.core.energy.EnergyWrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MHandlerEnergyStorage implements IMessageHandler<MessageEnergyStorage, IMessage>{
	
	@Override
	public IMessage onMessage(MessageEnergyStorage message, MessageContext ctx) {
		
		EntityPlayer player = GermplasmCore.proxy.getClientPlayer();
		int x = message.x;
		int y = message.y;
		int z = message.z;
		int energy = message.energy;
		int capacity = message.capacity;
		if(player != null && player.world != null) {
			TileEntity tile = player.world.getTileEntity(new BlockPos(x, y, z));
			if(tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, null)) {
				IEnergyStorage energyStorage = tile.getCapability(CapabilityEnergy.ENERGY, null);
				if(energyStorage instanceof EnergyWrapper) {
					((EnergyWrapper) energyStorage).setStats(energy, capacity);
				}
			}
		}
		
		return null;
	}

}
