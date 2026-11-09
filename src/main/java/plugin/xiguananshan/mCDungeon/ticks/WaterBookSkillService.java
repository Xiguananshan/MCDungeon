package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.List;
import java.util.Random;

public class WaterBookSkillService extends BukkitRunnable {
    private final Player player;
    private int ticks;
    public WaterBookSkillService(Player player){
        this.player =player;
        this.ticks = 200;
    }

    @Override
    public void run() {
        World world = player.getWorld();
        if(ticks%10 == 0){
            for(double i = 0;i<=Math.PI*2;i+=0.2){
                world.spawnParticle(Particle.DUST,player.getLocation().clone().add(3*Math.cos(i),1,3*Math.sin(i)),1,new Particle.DustOptions(ToolClass.typeColorMap.get(DamageType.WATER),3));

            }
            List<Entity> entities = player.getNearbyEntities(3,3,3);
            for(Entity entity : entities){
                if(entity.getLocation().distanceSquared(player.getLocation()) <= 9){
                    if(entity instanceof LivingEntity l){
                        if(l.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                            ToolClass.typeMap.put(l.getUniqueId(),DamageType.WATER);
                            l.damage(120);
                            Random random = new Random();
                            if(random.nextInt(100)+1<=40){
                                player.heal(1.0);
                            }
                            Vector dir = l.getLocation().clone().subtract(player.getLocation()).toVector().normalize();
                            l.setVelocity(dir.multiply(0.5f).setY(0.3f));
                        }
                    }
                }
            }
        }
        ticks--;
        if(ticks<=0){
            cancel();
        }

    }
}
