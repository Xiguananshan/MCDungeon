package plugin.xiguananshan.mCDungeon.ticks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class PlayerSkillService extends BukkitRunnable {
    private final Player p;
    private final Map<Integer,Integer> cold;
    public PlayerSkillService(Player p){
        this.p = p;
        cold = new HashMap<>();
    }

    public boolean isCold(Integer k){
        return  cold.containsKey(k);
    }

    public void addCold(Integer id,Integer ticks){
        cold.put(id,ticks);
    }

    @Override
    public void run() {
        for(Integer i : cold.keySet()){
            Integer l = cold.get(i);
            l--;
            cold.replace(i,l);
            if(l<=0){
                cold.remove(i);
                return;
            }
        }
        ItemStack stack = p.getInventory().getItemInMainHand();
        if(stack.getItemMeta() != null){
            ItemMeta meta = stack.getItemMeta();
            Integer k = meta.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("id"), PersistentDataType.INTEGER);
            if(k!=null){
                if(cold.containsKey(k)){
                    Integer ticks = cold.get(k);
                    DecimalFormat format = new DecimalFormat("0.00");
                    if (ticks > 1) {
                        p.sendActionBar(
                                Component.text("此道具技能冷却剩余时间：", NamedTextColor.YELLOW)
                                        .append(Component.text(format.format(ticks / 20f) + "秒", NamedTextColor.GREEN))
                        );
                    } else {
                        p.sendActionBar(Component.text("此道具技能冷却完毕！", NamedTextColor.GREEN));
                    }

                }
            }
        }
    }
}
