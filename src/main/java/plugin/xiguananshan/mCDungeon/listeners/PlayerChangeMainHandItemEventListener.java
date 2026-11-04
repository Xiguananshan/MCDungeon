package plugin.xiguananshan.mCDungeon.listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plugin.xiguananshan.mCDungeon.ToolClass;

public class PlayerChangeMainHandItemEventListener implements Listener {

    private boolean isChanging(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer()
                .has(ToolClass.fromNamespaceKey("changing"));
    }


    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (isChanging(e.getItemDrop().getItemStack())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (isChanging(e.getCurrentItem())) {
            e.setCancelled(true);
        }
        if (e.getClick() == ClickType.NUMBER_KEY && isChanging(e.getCurrentItem())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        for (ItemStack item : e.getNewItems().values()) {
            if (isChanging(item)) {
                e.setCancelled(true);
                return;
            }
        }
    }


    @EventHandler
    public void onHeld(PlayerItemHeldEvent e) {
        if (isChanging(e.getPlayer().getInventory().getItemInMainHand())) {
            e.setCancelled(true);
        }
    }

    // 5. F 键主副手交换
    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        if (isChanging(e.getMainHandItem()) || isChanging(e.getOffHandItem())) {
            e.setCancelled(true);
        }
    }
}