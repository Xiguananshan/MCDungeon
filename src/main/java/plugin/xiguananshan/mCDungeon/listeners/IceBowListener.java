package plugin.xiguananshan.mCDungeon.listeners;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;
import plugin.xiguananshan.mCDungeon.ToolClass;

public class IceBowListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onShoot(EntityShootBowEvent e){
        if(!(e.getEntity() instanceof Player)) return;
        if(!ToolClass.isIceBow(e.getBow())) return;
        if(ToolClass.IceBowSkillTick.containsKey(e.getEntity().getUniqueId())){
            e.setCancelled(true);
        }
        if(e.getProjectile() instanceof AbstractArrow arrow){
            arrow.getPersistentDataContainer().set(
                    ToolClass.fromNamespaceKey("ice_bow_arrow"),
                    PersistentDataType.BOOLEAN,true);
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getAction().isRightClick() && e.getHand() == EquipmentSlot.HAND){
            if(ToolClass.IceBowSkillTick.containsKey(e.getPlayer().getUniqueId())){
                Arrow arrow = e.getPlayer().launchProjectile(Arrow.class);
                arrow.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("ice_skill_arrow"),PersistentDataType.BOOLEAN,true);
                arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if(ToolClass.isIceBowArrow(e.getItemDrop().getItemStack())){
            e.setCancelled(true);
            e.getPlayer().updateInventory();
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e){
        if(ToolClass.isIceBowArrow(e.getCursor())){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void OnClick(InventoryClickEvent e){
        if(ToolClass.isIceBowArrow(e.getCurrentItem())){
            e.setCancelled(true);
        }
    }


}
