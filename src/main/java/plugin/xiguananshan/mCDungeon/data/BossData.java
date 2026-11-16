package plugin.xiguananshan.mCDungeon.data;

import org.bukkit.entity.LivingEntity;
import plugin.xiguananshan.mCDungeon.ticks.BossService;

public class BossData {
    public LivingEntity entity;
    public BossService service;
    public BossData(LivingEntity entity,BossService service){
        this.entity = entity;
        this.service = service;
    }
}
