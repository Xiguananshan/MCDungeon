package plugin.xiguananshan.mCDungeon.command;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import plugin.xiguananshan.mCDungeon.ToolClass;

public class TestChestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(sender instanceof Player p){
            Block block = p.getTargetBlockExact(10);
            if(block!= null){
                if(block.getState() instanceof Chest chest){
                    chest.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("box"), PersistentDataType.BOOLEAN,true);
                    chest.update();
                }
            }
        }
        return false;
    }
}
