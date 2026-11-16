package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.List;

public class SpiderTargetService extends BukkitRunnable {

    @Override
    public void run() {
        List<Entity> entities = Bukkit.selectEntities(Bukkit.getConsoleSender(),"@e[type=minecraft:spider]");
        for(Entity entity : entities){
            Spider spider = (Spider) entity;
            if(spider.getTarget() == null){
                LivingEntity nearest = null;
                double length_min = Double.MAX_VALUE;
                List<Entity> entityList = spider.getNearbyEntities(12,12,12);
                for(Entity e : entityList){
                    if(e instanceof LivingEntity l){
                        if(l instanceof Player || l.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("friend"))){
                            double dis = l.getLocation().distanceSquared(spider.getLocation());
                            if(dis<length_min){
                                nearest = l;
                                length_min = dis;
                            }
                        }
                    }
                }
                if(nearest != null)
                    spider.setTarget(nearest);
            }
        }
    }
}
