package plugin.xiguananshan.mCDungeon.command;

import io.papermc.paper.command.brigadier.argument.resolvers.PlayerProfileListResolver;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import plugin.xiguananshan.mCDungeon.ToolClass;

import java.util.List;

public class SettingsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(sender instanceof Player p){
            int round = ToolClass.PLUGIN.getConfig().getInt("total_rounds");
            DialogInput rounds = DialogInput.numberRange(
                    "rounds",
                    200,
                    Component.text("通关游戏所需轮数", NamedTextColor.YELLOW),
                    "options.generic_value",
                    5,
                    20,
                    (float)round,
                    (float)1
            );
            ActionButton confirm = ActionButton.create(
                    Component.text("保存",NamedTextColor.GREEN),
                    Component.text("点击保存游戏配置",NamedTextColor.GREEN),
                    150,
                    DialogAction.customClick(
                            (view,entity)->{
                                if(entity instanceof Player player){
                                    Float input_round = view.getFloat("rounds");
                                    if(input_round != null){
                                        ToolClass.PLUGIN.getConfig().set("total_rounds",input_round);
                                        ToolClass.PLUGIN.saveConfig();
                                        ToolClass.PLUGIN.reloadConfig();
                                        player.sendMessage(Component.text("保存成功",NamedTextColor.GREEN));
                                        player.closeDialog();
                                    }
                                }
                            },
                            ClickCallback.Options.builder().build()
                    )
            );
            ActionButton cancel = ActionButton.create(
                    Component.text("取消",NamedTextColor.RED),
                    Component.text("取消游戏配置的更改",NamedTextColor.RED),
                    150,
                    DialogAction.customClick(
                            (view,entity)->{
                                if(entity instanceof Player player){
                                    player.closeDialog();
                                }
                            },
                            ClickCallback.Options.builder().build()
                    )
            );
            Dialog dialog = Dialog.create(builder -> builder.empty()
                    .base(DialogBase.builder(Component.text("游戏设置", NamedTextColor.GOLD))
                            .inputs(List.of(rounds))
                            .canCloseWithEscape(true)
                            .build()
                    )
                    .type(DialogType.multiAction(
                            List.of(confirm, cancel)
                    ).build())
            );
            p.showDialog(dialog);
        }
        return false;
    }
}
