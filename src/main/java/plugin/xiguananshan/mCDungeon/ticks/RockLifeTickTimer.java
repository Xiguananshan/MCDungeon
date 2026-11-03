package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.List;

public class RockLifeTickTimer extends BukkitRunnable {

    private int ticks;
    private final Item item;
    public RockLifeTickTimer(int ticks,Item item){
        this.ticks = ticks;
        this.item = item;
    }

    @Override
    public void run() {
        if(item.isValid()){
            ticks--;
            if(ticks<=0){
                List<Entity> entities = item.getNearbyEntities(1f,1f,1f);
                Integer k = item.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("rock"), PersistentDataType.INTEGER);
                assert k!=null;
                for(Entity entity : entities){
                    if(entity instanceof LivingEntity l){
                        if(entity.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                            ToolClass.typeMap.put(l.getUniqueId(), DamageType.ROCK);
                            l.damage(k);
                        }
                    }
                }
                ToolClass.fillCubeWithParticles(item.getWorld(),item.getLocation(),1,10,new Particle.DustOptions(Color.YELLOW,3));
                item.getWorld().playSound(item.getLocation(), Sound.BLOCK_GLASS_BREAK,1,1);
                item.remove();
                cancel();
            }
        }else{
            cancel();
        }
    }
}
