package plugin.xiguananshan.mCDungeon.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import plugin.xiguananshan.mCDungeon.ToolClass;

public class TestGunCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if(sender instanceof Player p){
            p.getInventory().addItem(ToolClass.fromItem(ToolClass.DefaultGun));
            p.getInventory().addItem(ToolClass.fromItem(ToolClass.WindSword));
            p.getInventory().addItem(ToolClass.fromItem(ToolClass.StoneBook));
            p.getInventory().addItem(ToolClass.fromItem(ToolClass.LightningGun));
            p.getInventory().addItem(ToolClass.fromItem(ToolClass.GrassSword));
            p.getInventory().addItem(ToolClass.fromItem(ToolClass.WaterBook));
        }
        return false;
    }
}
