package plugin.xiguananshan.mCDungeon.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.List;

public class GenerateTestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if(sender instanceof Player p){
            ToolClass.GenerateMap(List.of(p),1);
        }
        return false;
    }
}
