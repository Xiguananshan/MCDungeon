package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ChatColorUtil;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.HashSet;
import java.util.List;

public class ElementTypeTimer extends BukkitRunnable {

    @Override
    public void run() {
        List<Entity> entities = Bukkit.getWorld("world").getEntities();
        for(Entity entity : entities){
            PersistentDataContainer pdc = entity.getPersistentDataContainer();
            if(pdc.has(ToolClass.fromNamespaceKey("entity"))){
                if(ToolClass.entity_type.containsKey(entity.getUniqueId())){
                    HashSet<DamageType> set = ToolClass.entity_type.get(entity.getUniqueId());
                    if(!set.isEmpty()){
                        String next = ChatColor.GRAY + "(";
                        for(int i = 0;i< DamageType.values().length;i++){
                            DamageType t = DamageType.values()[i];
                            if(set.contains(t)) {

                                next += (ChatColorUtil.toChatColor(ToolClass.typeColorMap.get(t)) + ToolClass.typeNameMap.get(t) + ChatColor.GRAY);
                                next += ",";

                            }
                        }
                        next = next.substring(0,next.length()-1);
                        next += ")";
                        Integer health = pdc.get(ToolClass.fromNamespaceKey("health"), PersistentDataType.INTEGER);
                        if(health != null){
                            entity.setCustomName(ChatColor.YELLOW + "剩余血量：" + ChatColor.RED + health + next);
                        }
                        for(DamageType t : DamageType.values()){
                            if(ToolClass.element_type.containsKey(entity.getUniqueId())){
                                if(ToolClass.element_type.get(entity.getUniqueId()).containsKey(t)){
                                    Integer ticks = ToolClass.element_type.get(entity.getUniqueId()).get(t);
                                    ticks--;
                                    ToolClass.element_type.get(entity.getUniqueId()).replace(t,ticks);
                                    if(ticks<=0){
                                        ToolClass.element_type.get(entity.getUniqueId()).remove(t);
                                        ToolClass.entity_type.get(entity.getUniqueId()).remove(t);
                                    }
                                }
                            }

                        }
                    }else{
                        Integer health = pdc.get(ToolClass.fromNamespaceKey("health"), PersistentDataType.INTEGER);
                        if(health != null){
                            entity.setCustomName(ChatColor.YELLOW + "剩余血量：" + ChatColor.RED + health);
                        }
                    }
                }else{
                    Integer health = pdc.get(ToolClass.fromNamespaceKey("health"), PersistentDataType.INTEGER);
                    if(health != null){
                        entity.setCustomName(ChatColor.YELLOW + "剩余血量：" + ChatColor.RED + health);
                    }
                }
            }
        }
    }
}
