package asatsuki256.germplasm.core.energy;

public class EnergyWrapperReceiver extends EnergyWrapper {
	
	/*
	 * 外部からextractできないEnergyWrapper
	 * extractInternalでTileEntityなどからはextract可
	 */

	public EnergyWrapperReceiver(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}
	
	public int extractInternal(int maxExtract, boolean simulate) {
		return super.extractEnergy(maxExtract, simulate);
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return 0;
	}
	
}