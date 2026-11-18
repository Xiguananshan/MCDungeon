package plugin.xiguananshan.mCDungeon.ticks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.data.CornerData;

import java.time.Duration;
import java.util.UUID;

public class BossRoomService extends BukkitRunnable {
    private final LivingEntity entity;
    public BossRoomService(LivingEntity entity){
        this.entity = entity;
    }
    @Override
    public void run() {
        if(!ToolClass.gaming){
            cancel();
            return;
        }
        if(!entity.isValid()){
            for(UUID uuid : ToolClass.game_players){
                Player p = Bukkit.getPlayer(uuid);
                if(p != null){
                    Title title = Title.title(Component.text("Victory", NamedTextColor.GOLD, TextDecoration.BOLD),
                            Component.text().build(),
                            Title.Times.times(
                                    Duration.ofMillis(500),
                                    Duration.ofMillis(2000),
                                    Duration.ofMillis(500)
                            ));
                    p.showTitle(title);
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_HARP,3,3);

                    EntityRoomCheckService.setAllDoors(Material.AIR,p.getWorld());
                    for(CornerData.Corner corner : ToolClass.LiveCrossRoom.corners){
                        for(int x = corner.minx;x<=corner.maxx;x++){
                            for(int y = corner.miny;y<=corner.maxy;y++){
                                for(int z = corner.minz;z<=corner.maxz;z++){
                                    p.getWorld().playSound(new Location(p.getWorld(),x,y,z),Sound.BLOCK_WOOD_BREAK,3,1);
                                    p.getWorld().spawnParticle(Particle.BLOCK,new Location(p.getWorld(),x,y,z),15,Bukkit.createBlockData(Material.OAK_FENCE));
                                }
                            }
                        }
                    }
                    for(int X = ToolClass.BossDoor.minx;X<=ToolClass.BossDoor.maxx;X++){
                        for(int Y = ToolClass.BossDoor.miny;Y<=ToolClass.BossDoor.maxy;Y++){
                            for(int Z = ToolClass.BossDoor.minz;Z <= ToolClass.BossDoor.maxz;Z++){
                                p.getWorld().getBlockAt(X,Y,Z).setType(Material.DARK_OAK_FENCE,true);
                            }
                        }
                    }
                    cancel();
                }
            }
        }
    }
}
