package asatsuki256.germplasm.core.util;

import java.util.Collection;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class GmpmItemUtil {
	
	/**
	 * sourceをdestinationにマージする
	 * どちらも実際には変更されない、変更後のItemStackはMergeResultに格納されている
	 * 
	 * @param destination マージ先ItemStack
	 * @param source マージするItemStack
	 * @return マージ後のdestination
	 */
	public static MergeResult tryMergeItemStack(ItemStack destination, ItemStack source) {
		if(destination.isEmpty()) {
			return new MergeResult(source.copy(), ItemStack.EMPTY.copy(), true);
		}
		if(source.isEmpty()) {
			return new MergeResult(destination.copy(), ItemStack.EMPTY.copy(), true);
		}

		if(!ItemStack.areItemsEqual(destination, source)) return new MergeResult(destination, source, false);
		if(!ItemStack.areItemStackTagsEqual(destination, source)) return new MergeResult(destination, source, false);
		
		if(destination.getCount() + source.getCount() > destination.getMaxStackSize()) {
			int decrCount = destination.getMaxStackSize() - destination.getCount();
			ItemStack destinationRtn = destination.copy();
			destinationRtn.setCount(destination.getMaxStackSize());
			ItemStack sourceRtn = source.copy();
			sourceRtn.setCount(source.getCount() - decrCount);
			return new MergeResult(destinationRtn, sourceRtn, true);
		} else {
			ItemStack destinationRtn = destination.copy();
			destinationRtn.setCount(destination.getCount() + source.getCount());
			return new MergeResult(destinationRtn, ItemStack.EMPTY.copy(), true);
		}
	}
	
	/**
	 * sourceをdestinationにマージ可能かを調べる
	 * destination, sourceともに変更されない
	 * 
	 * @param destination マージ先ItemStack
	 * @param source マージするItemStack
	 * @return マージ可能ならtrue、不可能ならfalse
	 */
	public static boolean canMergeItemStack(ItemStack destination, ItemStack source) {
		return tryMergeItemStack(destination, source).success;
	}
	
	public static class MergeResult {
		
		ItemStack destinationResult;
		ItemStack sourceResult;
		boolean success;
		
		public MergeResult(ItemStack destinationResult, ItemStack sourceResult, boolean success){
			this.destinationResult = destinationResult;
			this.sourceResult = sourceResult;
			this.success = success;
		}
		
		public ItemStack getDestination() {
			return destinationResult;
		}
		
		public ItemStack getSource() {
			return sourceResult;
		}
		
		public boolean getSuccess() {
			return success;
		}
	}
	
	/**
	 * targetがitemsに含まれてるかを調べる
	 * targetの数が多ければ、その数だけ含まれているか調べる
	 * 
	 * @param target あるかどうかを調べるアイテム
	 * @param items 調べる対象
	 * @return itemsの中にtargetが含まれているならtrue
	 * @deprecated Use {@link #takeItemStack(Collection, int, boolean, ItemStack...)}
	 */
	@Deprecated
	public static boolean containItemStack(ItemStack target, Collection<ItemStack> items) {
		if (target.isEmpty()) return true;
		for (ItemStack itemstack : items) {
			if (ItemStack.areItemsEqual(target, itemstack) && target.getCount() <= itemstack.getCount()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * targetをitemsに含まれてるかを調べるから取り去る
	 * 足りなかったらある分だけ取る
	 * targetはItem、数、Damageのみ判定
	 * 
	 * @param target 取り去るアイテム
	 * @param items 取り去る対象
	 * @return 取り去ったアイテム
	 */
	@Deprecated
	public static ItemStack takeItemStack(ItemStack target, Collection<ItemStack> items) {
		int takenCount = 0;
		for (ItemStack itemstack : items) {
			if (ItemStack.areItemsEqual(target, itemstack)) {
				ItemStack split = itemstack.splitStack(target.getCount() - takenCount);
				takenCount += split.getCount();
			}
		}
		ItemStack taken = target.copy();
		taken.setCount(takenCount);
		return taken;
	}
	
	/**
	 * targetsをitemsから取り去る
	 * 足りなかったらある分だけ取る
	 * targetはItem、数、Damageのみ判定
	 * 
	 * @param items 取り去る対象
	 * @param count 取り去る数
	 * @param doTake 実際に取り去るかどうか
	 * @param targets 取り去るアイテム、個数とNBTは無関係
	 * @return 取り去ったアイテム
	 */
	public static int takeItemStack(Collection<ItemStack> items, int count, boolean doTake, ItemStack... targets) {
		int takenCount = 0;
		for (ItemStack itemstack : items) {
			for (ItemStack target : targets) {
				if (ItemStack.areItemsEqual(target, itemstack)) {
					int takeCount = Math.min(itemstack.getCount(), count - takenCount);
					takenCount += takeCount;
					if (doTake) {
						itemstack.setCount(itemstack.getCount() - takeCount);
					}
				}
			}
		}
		return takenCount;
	}
	
	public static int takeItemStack(Collection<ItemStack> items, boolean doTake, ItemStack... targets) {
		if (targets.length > 0 && targets[0] != null) {
			return takeItemStack(items, targets[0].getCount(), doTake, targets);
		}
		return takeItemStack(items, 0, doTake, targets);
	}
	
	public static void transferSingleItem(IItemHandler source, IItemHandler destination) {
		ItemStack stack = ItemStack.EMPTY;
		int inputSlot = -1;
		for (int i = 0; i < source.getSlots(); i++) {
			ItemStack extract = source.extractItem(i, 1, true);
			if (!extract.isEmpty()) {
				stack = extract;
				inputSlot = i;
			}
		}
		if (inputSlot >= 0 && ItemHandlerHelper.insertItem(destination, stack, true).isEmpty()) {
			source.extractItem(inputSlot, 1, false);
			ItemHandlerHelper.insertItem(destination, stack, false);
		}
	}

}
