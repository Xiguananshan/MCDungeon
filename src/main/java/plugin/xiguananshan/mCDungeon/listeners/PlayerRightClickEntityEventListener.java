package plugin.xiguananshan.mCDungeon.listeners;

import com.jeff_media.morepersistentdatatypes.DataType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.data.ActivityRequest;

import java.util.UUID;


public class PlayerRightClickEntityEventListener implements Listener {
    @EventHandler
    public void run(PlayerInteractEntityEvent e){
        if(e.getHand() != EquipmentSlot.HAND) return;

        Entity entity = e.getRightClicked();
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        if(pdc.has(ToolClass.fromNamespaceKey("uuid"),PersistentDataType.STRING)){
            String uuid = pdc.get(ToolClass.fromNamespaceKey("uuid"),PersistentDataType.STRING);
            assert  uuid != null;
            UUID Uuid = UUID.fromString(uuid);
            if(ToolClass.requestMap.containsKey(Uuid)){
                ActivityRequest request = ToolClass.requestMap.get(Uuid);
                request.onRightClick(entity,e.getPlayer());
                e.setCancelled(true);
                return;
            }
        }
        if(!pdc.has(ToolClass.fromNamespaceKey("shop"))) return;

        Integer iron = pdc.get(ToolClass.fromNamespaceKey("iron"), PersistentDataType.INTEGER);
        Integer gold = pdc.get(ToolClass.fromNamespaceKey("gold"),PersistentDataType.INTEGER);
        ItemStack out = pdc.get(ToolClass.fromNamespaceKey("out"), DataType.ITEM_STACK);
        Boolean limited = pdc.get(ToolClass.fromNamespaceKey("limited"),PersistentDataType.BOOLEAN);
        if(iron == null || gold == null || out == null || limited == null) return;

        Player player = e.getPlayer();
        if(!ToolClass.CheckItem(player, Material.IRON_INGOT,iron) || !ToolClass.CheckItem(player,Material.GOLD_INGOT,gold)){
            player.sendMessage(Component.text("资源不足", NamedTextColor.RED));
            player.playSound(player,Sound.BLOCK_NOTE_BLOCK_HARP,1,1);
            return;
        }

        e.setCancelled(true);
        ToolClass.ClearItem(player,Material.IRON_INGOT,iron);
        ToolClass.ClearItem(player,Material.GOLD_INGOT,gold);
        player.getInventory().addItem(ToolClass.fromItem(out));
        player.sendMessage(Component.text("购买成功", NamedTextColor.GREEN));
        player.playSound(player,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);

        if(limited){
            removeLinked(pdc,"shop_item");
            removeLinked(pdc,"shop_text");
            entity.getWorld().spawnParticle(Particle.POOF,entity.getLocation(),25);
            entity.remove();
        }
    }

    private static void removeLinked(PersistentDataContainer pdc, String key){
        String uuid = pdc.get(ToolClass.fromNamespaceKey(key),PersistentDataType.STRING);
        if(uuid == null) return;
        try{
            Entity linked = Bukkit.getEntity(UUID.fromString(uuid));
            if(linked != null) linked.remove();
        }catch(IllegalArgumentException ignored){
        }
    }
}
