package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import plugin.xiguananshan.mCDungeon.ToolClass;
import java.util.List;

public class CommandBlockSpawnEntityService extends BukkitRunnable {


    @Override
    public void run() {
        World world = Bukkit.getWorld("world");
        assert world != null;
        List<Entity> entities = world.getEntities();
        for(Entity entity : entities){
            if(entity.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("CommandBlockSpawn"), PersistentDataType.STRING) && entity instanceof LivingEntity l){
                String type = entity.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("CommandBlockSpawn"),PersistentDataType.STRING);
                assert type != null;
                switch (type){
                    case "Command" ->{
                        boolean flag = true;
                        for(Player p : Bukkit.getOnlinePlayers()){
                            if(ToolClass.game_players.contains(p.getUniqueId()) && !ToolClass.died_players.contains(p.getUniqueId())){
                                RayTraceResult result;
                                Vector dir = p.getEyeLocation().getDirection().normalize();
                                result = world.rayTraceEntities(p.getEyeLocation(), dir, 33, 0.0F, (entity1x) -> (entity1x instanceof LivingEntity && !entity1x.getUniqueId().equals(p.getUniqueId())));
                                if(result != null && result.getHitEntity() != null){
                                    if(result.getHitEntity().getUniqueId().equals(l.getUniqueId())){
                                        flag = false;
                                        break;
                                    }
                                }
                            }
                        }
                        l.setAI(flag);
                    }
                    case "Chain" ->{
                        List<Entity> entityList = l.getNearbyEntities(4,4,4);
                        boolean flag = false;
                        for(Entity entity1 : entityList){
                            if(entity1 instanceof LivingEntity){
                                if(entity1.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("CommandBlockSpawn"),PersistentDataType.STRING)){
                                    String t = entity1.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("CommandBlockSpawn"),PersistentDataType.STRING);
                                    if("Repeat".equals(t)){
                                        flag =true;
                                        break;
                                    }
                                }
                            }
                        }
                        l.setAI(flag);
                    }
                }
            }
        }
    }
}
