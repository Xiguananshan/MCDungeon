package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;

import java.util.List;

public class StoneSkillService extends BukkitRunnable {

    private final FallingBlock block;
    private int ticks;
    private Location core;
    private Location block_pos;
    private final World world;
    private List<Entity> entities;

    public StoneSkillService(FallingBlock block){
        this.block = block;
        this.ticks = 160;
        core = block.getLocation();
        world = block.getWorld();
        block_pos = new Location(block.getWorld(),block.getLocation().getBlockX(),block.getLocation().getBlockY(),block.getLocation().getBlockZ());
    }

    @Override
    public void run() {
        if(!block.isValid()){
            if(ticks == 160){
                for(Entity entity : entities){
                    if(entity instanceof LivingEntity l){
                        if(l.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                            ToolClass.typeMap.put(l.getUniqueId(), DamageType.ROCK);
                            l.damage(700);
                        }
                    }
                }
                world.spawnParticle(Particle.EXPLOSION,core,15);
                world.playSound(core, Sound.ENTITY_GENERIC_EXPLODE,3,1);
            }else if(ticks<=120&&ticks%30==0){
                new RockExportService(world,core).runTaskTimer(ToolClass.PLUGIN,0,1);
            }

            ticks--;
            if(ticks<=0){
                world.getBlockAt(block_pos).setType(Material.AIR);
                world.spawnParticle(Particle.BLOCK,core,20,Bukkit.createBlockData(Material.GOLD_BLOCK));
                world.playSound(core,Sound.BLOCK_STONE_BREAK,1,1);
                cancel();

            }
        }else{
            core = block.getLocation();
            block_pos = new Location(block.getWorld(),block.getLocation().getBlockX(),block.getLocation().getBlockY(),block.getLocation().getBlockZ());
            entities = block.getNearbyEntities(3,3,3);
        }
    }
}
