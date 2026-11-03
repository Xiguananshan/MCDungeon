package plugin.xiguananshan.mCDungeon.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.ticks.PlayerRockDefenceTickTimer;

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
    }
}
