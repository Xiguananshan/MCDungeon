package plugin.xiguananshan.mCDungeon.ticks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;


import java.util.List;
import java.util.UUID;

public class PlayerDiedService extends BukkitRunnable {
    private final TextDisplay display;
    private final UUID uuid;
    private final String name;
    private final ArmorStand stand;
    private int f;
    private int ticks;
    public PlayerDiedService(TextDisplay display,UUID uuid,String name,ArmorStand stand){
        this.display = display;
        this.uuid = uuid;
        this.name = name;
        this.f = 0;
        this.ticks = 0;
        this.stand = stand;
    }
    @Override
    public void run() {
        if(!ToolClass.gaming){
            cancel();
            return;
        }
        display.text(Component.text(name, NamedTextColor.YELLOW).appendNewline().append(Component.text("救援进度：",NamedTextColor.YELLOW)).append(Component.text(f,NamedTextColor.GREEN)).append(Component.text("%",NamedTextColor.GREEN)));
        List<Entity> entities = stand.getNearbyEntities(1,1,1);
        boolean flag = false;
        for(Entity entity : entities){
            if(ToolClass.game_players.contains(entity.getUniqueId())){
                if(entity instanceof Player p){
                    if(p.getGameMode() == GameMode.ADVENTURE){

                        f++;
                        flag = true;
                        break;
                    }
                }
            }
        }
        if(f>100){
            Player p = Bukkit.getPlayer(uuid);
            if(p!=null){
                p.teleport(stand.getLocation());
                stand.remove();
                display.remove();
                p.setGameMode(GameMode.ADVENTURE);
                ToolClass.died_players.remove(p.getUniqueId());
                p.getWorld().spawnParticle(Particle.EXPLOSION,p.getLocation(),15);
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE,3,1);
                p.clearActivePotionEffects();
            }else{
                stand.getWorld().spawnParticle(Particle.EXPLOSION,stand.getLocation(),15);
                stand.getWorld().playSound(stand.getLocation(), Sound.ENTITY_GENERIC_EXPLODE,3,1);
                ToolClass.un_relief_players.put(uuid,stand.getLocation());
                ToolClass.died_players.remove(uuid);
                stand.remove();
                display.remove();
            }
            cancel();
            return;
        }
        if(!flag){
            ticks++;
            if(ticks>=5 && f>0){
                f--;
                ticks = 0;
            }
        }
    }
}
