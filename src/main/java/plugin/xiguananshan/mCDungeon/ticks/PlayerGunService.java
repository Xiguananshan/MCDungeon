package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.List;

public class PlayerGunService extends BukkitRunnable {
    private Player p;
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
                    String ss = ChatColor.YELLOW + "剩余弹药：" + ChatColor.GREEN + max + "/" + max;
                    List<String> lore = meta.getLore();
                    if(lore!=null)
                        lore.set(2,ss);
                    meta.setLore(lore);
                    stack.setItemMeta(meta);
                    p.getInventory().setItemInMainHand(stack);
                    p.playSound(p, Sound.ENTITY_ITEM_FRAME_ROTATE_ITEM,1,1);
                }else{
                    meta.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("changing"),PersistentDataType.INTEGER,k);
                    DecimalFormat format = new DecimalFormat("0.00");
                    String ss = ChatColor.YELLOW + "换弹时间：" + ChatColor.GREEN + format.format(k/20f) + "秒";
                    List<String> lore = meta.getLore();
                    if(lore!=null)
                        lore.set(2,ss);
                    meta.setLore(lore);
                    stack.setItemMeta(meta);
                    p.getInventory().setItemInMainHand(stack);
                    p.playSound(p, Sound.ENTITY_ITEM_FRAME_ROTATE_ITEM,1,1);
                }
            }
        }
    }
}
