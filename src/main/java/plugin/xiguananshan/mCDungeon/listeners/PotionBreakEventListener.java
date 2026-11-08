package plugin.xiguananshan.mCDungeon.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.ticks.WaterBookAttackService;

public class PotionBreakEventListener implements Listener {
    @EventHandler
    public void run(PotionSplashEvent e){
        if(e.getPotion().getPersistentDataContainer().has(ToolClass.fromNamespaceKey("water_book"))){
            new WaterBookAttackService(e.getPotion().getLocation()).runTaskTimer(ToolClass.PLUGIN,0,1);
        }
    }
}
