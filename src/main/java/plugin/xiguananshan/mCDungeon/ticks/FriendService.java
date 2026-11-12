package plugin.xiguananshan.mCDungeon.ticks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import org.bukkit.entity.Zombie;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;



public class FriendService extends BukkitRunnable {

    private int targetTick;

    public FriendService(){
        targetTick = 0;
    }

    @Override
    public void run() {
        targetTick++;
        for(Entity entity : Bukkit.getServer().selectEntities(Bukkit.getConsoleSender(),"@e")){
            if(entity instanceof LivingEntity l){
                if(l.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("friend"))){
                    Integer k = l.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("health"), PersistentDataType.INTEGER);
                    if(k!=null){
                        l.setCustomNameVisible(true);
                        l.customName(Component.text("剩余血量：", NamedTextColor.YELLOW).append(Component.text(k,NamedTextColor.GREEN)));
                    }
                }
            }
        }
        if(targetTick >= 20){
            for(Entity entity : Bukkit.getServer().selectEntities(Bukkit.getConsoleSender(),"@e[type=minecraft:zombie]")){
                if(entity.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("limit_live"))){
                    LivingEntity l =(LivingEntity) entity;
                    Integer k = l.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("hurt"),PersistentDataType.INTEGER);
                    if(k!=null)
                        l.damage(k);
                }
            }
            targetTick = 0;
        }


    }



}
