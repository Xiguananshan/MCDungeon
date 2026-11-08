package plugin.xiguananshan.mCDungeon.ticks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.ArrayList;
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
                        List<Component> typeNames = new ArrayList<>();
                        for (DamageType t : DamageType.values()) {
                            if (!set.contains(t)) continue;
                            Color c = ToolClass.typeColorMap.get(t);
                            TextColor textColor = TextColor.color(c.asRGB());
                            typeNames.add(Component.text(ToolClass.typeNameMap.get(t), textColor));
                        }


                        Component next = Component.text("(", NamedTextColor.GRAY)
                                .append(Component.join(JoinConfiguration.separator(Component.text(",", NamedTextColor.GRAY)), typeNames))
                                .append(Component.text(")", NamedTextColor.GRAY));

                        Integer health = pdc.get(ToolClass.fromNamespaceKey("health"), PersistentDataType.INTEGER);
                        if (health != null) {
                            entity.customName(
                                    Component.text("剩余血量：", NamedTextColor.YELLOW)
                                            .append(Component.text(health, NamedTextColor.RED))
                                            .append(next)
                            );
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
                            entity.customName(Component.text("剩余血量",NamedTextColor.YELLOW).append(Component.text(String.valueOf(health),NamedTextColor.RED)));
                        }
                    }
                }else{
                    Integer health = pdc.get(ToolClass.fromNamespaceKey("health"), PersistentDataType.INTEGER);
                    if(health != null){
                        entity.customName(Component.text("剩余血量",NamedTextColor.YELLOW).append(Component.text(String.valueOf(health),NamedTextColor.RED)));
                    }
                }
            }
        }
    }
}
