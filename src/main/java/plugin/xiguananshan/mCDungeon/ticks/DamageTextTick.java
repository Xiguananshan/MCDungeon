package plugin.xiguananshan.mCDungeon.ticks;


import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class DamageTextTick extends BukkitRunnable {
    public Entity e;
    public float timer;
    public DamageTextTick(Entity e,float timer){
        this.e = e;
        this.timer = timer;
    }
    @Override
    public void run() {
        timer -= 0.05f;
        if(timer<0){
            e.remove();
            cancel();
            return;
        }
        e.teleport(e.getLocation().add(0,0.05f,0));
    }
}
