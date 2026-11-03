package plugin.xiguananshan.mCDungeon.listeners;


import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import plugin.xiguananshan.mCDungeon.ChatColorUtil;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.events.GunShotEvent;

public class GunShootEventListener implements Listener {
    @EventHandler
    public void run(GunShotEvent e){
        LivingEntity l = e.getEntity();
        if(e.getExecuted()){
            ToolClass.SpawnDamageEntity(e.GetHealth(),l.getWorld(),l.getLocation().add(0,l.getHeight(),0), ChatColorUtil.toChatColor(ToolClass.typeColorMap.get(e.getDamageType())));
            return;
        }
        l.damage(e.GetHealth());
    }
}
