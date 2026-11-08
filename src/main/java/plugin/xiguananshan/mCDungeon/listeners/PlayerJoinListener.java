package plugin.xiguananshan.mCDungeon.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.ticks.PlayerGunService;
import plugin.xiguananshan.mCDungeon.ticks.PlayerLightningGunService;
import plugin.xiguananshan.mCDungeon.ticks.PlayerRockDefenceTickTimer;
import plugin.xiguananshan.mCDungeon.ticks.PlayerSkillService;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void run(PlayerJoinEvent e){
        if(!ToolClass.defence.containsKey(e.getPlayer().getUniqueId())){
            ToolClass.defence.put(e.getPlayer().getUniqueId(),0);
        }
        if(!ToolClass.rockDefenceTickTimerMap.containsKey(e.getPlayer().getUniqueId())){
            PlayerRockDefenceTickTimer  timer = new PlayerRockDefenceTickTimer(e.getPlayer());
            timer.runTaskTimer(ToolClass.PLUGIN,0,1);
            ToolClass.rockDefenceTickTimerMap.put(e.getPlayer().getUniqueId(),timer);
        }
        if(!ToolClass.gunServiceMap.containsKey(e.getPlayer().getUniqueId())){
            PlayerGunService service = new PlayerGunService(e.getPlayer());
            service.runTaskTimer(ToolClass.PLUGIN,0,1);
            ToolClass.gunServiceMap.put(e.getPlayer().getUniqueId(),service);
        }
        if(!ToolClass.skillServiceMap.containsKey(e.getPlayer().getUniqueId())){
            PlayerSkillService service = new PlayerSkillService(e.getPlayer());
            service.runTaskTimer(ToolClass.PLUGIN,0,1);
            ToolClass.skillServiceMap.put(e.getPlayer().getUniqueId(),service);
        }
        if(!ToolClass.lightningGunServiceMap.containsKey(e.getPlayer().getUniqueId())){
            PlayerLightningGunService service = new PlayerLightningGunService(e.getPlayer());
            service.runTaskTimer(ToolClass.PLUGIN,0,1);
            ToolClass.lightningGunServiceMap.put(e.getPlayer().getUniqueId(),service);
        }
    }
}
