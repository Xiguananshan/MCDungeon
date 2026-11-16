package plugin.xiguananshan.mCDungeon.ticks;

import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class BossService extends BukkitRunnable {
    private final LivingEntity entity;
    public BossService(LivingEntity entity){
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    @Override
    public void run() {
        if(!entity.isValid()) cancel();
    }
}
