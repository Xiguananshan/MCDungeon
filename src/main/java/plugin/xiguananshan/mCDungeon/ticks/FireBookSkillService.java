package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.UUID;

public class FireBookSkillService extends BukkitRunnable {

    @Override
    public void run() {
        for(UUID uuid : ToolClass.fireBookSkillTick.keySet()){
            Integer k = ToolClass.fireBookSkillTick.get(uuid);
            k--;
            if(k<=0){
               Integer d =  ToolClass.defence.get(uuid);
               d-=1000;
               ToolClass.defence.put(uuid,d);
               ToolClass.fireBookSkillTick.remove(uuid);
               return;
            }
            Player p = Bukkit.getPlayer(uuid);
            ToolClass.fireBookSkillTick.put(uuid,k);
            if(p!=null){
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,1,2,false));
            }
        }
    }
}
