package plugin.xiguananshan.mCDungeon.ticks;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.UUID;

public class BossBarService extends BukkitRunnable {
    private final LivingEntity boss;
    private final int max_health;
    private final BossBar bossBar;
    public BossBarService(LivingEntity entity, Component title, BossBar.Color color){
        this.boss = entity;
        Integer k = entity.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("health"), PersistentDataType.INTEGER);
        assert k!=null;
        this.max_health = k;
        this.bossBar = BossBar.bossBar(title,1,color,BossBar.Overlay.PROGRESS);
        for(UUID uuid : ToolClass.game_players){
            Player p = Bukkit.getPlayer(uuid);
            if(p != null){
                p.showBossBar(bossBar);
            }
        }

    }
    @Override
    public void run() {
        if(!boss.isValid()){
            for(UUID uuid : ToolClass.game_players){
                Player p = Bukkit.getPlayer(uuid);
                if(p != null){
                    p.hideBossBar(bossBar);
                }
                cancel();
                return;
            }
        }
        Integer k = boss.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("health"),PersistentDataType.INTEGER);
        if(k!= null)
            bossBar.progress(1.0f*k/max_health);
    }
}
