package plugin.xiguananshan.mCDungeon.data;

import org.bukkit.NamespacedKey;
import java.util.UUID;

public record UndoPdcModify(UUID owner, NamespacedKey key,Float value) {

}
