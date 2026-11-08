package plugin.xiguananshan.mCDungeon.ticks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PlayerGunService extends BukkitRunnable {
    private final Player p;
    public PlayerGunService(Player p){
        this.p = p;
    }
    @Override
    public void run() {
        ItemStack stack = p.getInventory().getItemInMainHand();
        if(!p.isOnline()){
            return;
        }
        if(p.getGameMode() == GameMode.SPECTATOR){
            return;
        }
        if(stack.getItemMeta() != null){
            ItemMeta meta = stack.getItemMeta();
            if(meta.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("changing"), PersistentDataType.INTEGER)){
                Integer k = meta.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("changing"),PersistentDataType.INTEGER);
                Integer max =meta.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("max_count"),PersistentDataType.INTEGER);
                assert k!= null && max != null;
                k--;
                if(k<=0){
                    meta.getPersistentDataContainer().remove(ToolClass.fromNamespaceKey("changing"));

                    meta.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("count"),PersistentDataType.INTEGER,max);
                    Component ss = Component.text("剩余弹药：", NamedTextColor.YELLOW)
                            .append(Component.text(max + "/" + max, NamedTextColor.GREEN));


                    List<Component> lore = meta.lore();
                    if (lore == null) lore = new ArrayList<>();
                    while (lore.size() <= 2) lore.add(Component.empty());
                    lore.set(2, ss);
                    meta.lore(lore);
                    stack.setItemMeta(meta);
                    p.getInventory().setItemInMainHand(stack);
                    p.playSound(p, Sound.ENTITY_ITEM_FRAME_ROTATE_ITEM,1,1);
                }else{
                    meta.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("changing"),PersistentDataType.INTEGER,k);
                    DecimalFormat format = new DecimalFormat("0.00");
                    Component ss = Component.text("换弹时间：", NamedTextColor.YELLOW)
                            .append(Component.text(format.format(k / 20f) + "秒", NamedTextColor.GREEN));

                    List<Component> lore = meta.lore();
                    if (lore == null) lore = new ArrayList<>();
                    while (lore.size() <= 2) lore.add(Component.empty());
                    lore.set(2, ss);
                    meta.lore(lore);
                    stack.setItemMeta(meta);
                    p.getInventory().setItemInMainHand(stack);
                    p.playSound(p, Sound.ENTITY_ITEM_FRAME_ROTATE_ITEM,1,1);
                }
            }
        }
    }
}
