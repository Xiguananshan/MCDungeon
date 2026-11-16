package plugin.xiguananshan.mCDungeon.data;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface ActivityRequest {
    void onRightClick(Entity entity, Player player);
}
