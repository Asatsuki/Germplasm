package asatsuki256.germplasm.core.network;

import asatsuki256.germplasm.core.GermplasmConfig;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MHandlerConfig implements IMessageHandler<MessageConfig, IMessage>{
	
	@Override
	public IMessage onMessage(MessageConfig message, MessageContext ctx) {
		
		GermplasmConfig.CONFIG_CORE.requireEnergy = message.requireEnergy;
		
		return null;
	}

}
