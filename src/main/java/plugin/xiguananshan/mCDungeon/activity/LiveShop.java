package plugin.xiguananshan.mCDungeon.activity;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.data.ActivityRequest;

import java.awt.*;
import java.util.List;

public class LiveShop implements ActivityRequest {

    @Override
    public void onRightClick(Entity entity, Player player) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        if(!pdc.has(ToolClass.fromNamespaceKey("limited")) && !pdc.has(ToolClass.fromNamespaceKey("unlimited"))){
            player.sendMessage(Component.text("NPC互动次数已用完", NamedTextColor.RED));
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP,1,1);
            return;
        }
        DialogBody t1 = DialogBody.plainMessage(Component.text("嗜血的恶魔饥渴地看着你，你愿意用你的生命换取财富吗",NamedTextColor.DARK_RED));
        DialogBody t2 = DialogBody.plainMessage(Component.text("同意交易，你将失去",NamedTextColor.YELLOW).append(Component.text("4HP生命上限",NamedTextColor.RED))
                .append(Component.text("但是你将得到：",NamedTextColor.YELLOW)));
        ItemStack i = ToolClass.Iron.clone();
        i.setAmount(40);
        ItemStack g = ToolClass.Gold.clone();
        g.setAmount(10);
        DialogBody iron = DialogBody.item(i).build();
        DialogBody gold = DialogBody.item(g).build();
        ActionButton commit = ActionButton.create(
                Component.text("确定交易",NamedTextColor.GREEN),
                Component.text("签订恶魔协议换取财富",NamedTextColor.YELLOW),
                150,
                DialogAction.customClick(
                        (_, e)->{
                            if(e instanceof Player p){
                                p.closeDialog();
                                if(!pdc.has(ToolClass.fromNamespaceKey("limited")) && !pdc.has(ToolClass.fromNamespaceKey("unlimited"))){
                                    p.sendMessage(Component.text("NPC互动次数已用完", NamedTextColor.RED));
                                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_HARP,1,1);
                                    return;
                                }
                                AttributeInstance maxHealthAttr = p.getAttribute(Attribute.MAX_HEALTH);
                                assert maxHealthAttr != null;
                                if(maxHealthAttr.getBaseValue() <= 4){
                                    p.sendMessage(Component.text("你的最大生命上限不足",NamedTextColor.RED, TextDecoration.BOLD));
                                    p.playSound(p,Sound.BLOCK_NOTE_BLOCK_HARP,1,1);
                                    return;
                                }
                                p.sendMessage(Component.text("爽快！视财如命的人类！",NamedTextColor.DARK_RED, TextDecoration.BOLD));
                                p.playSound(p,Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                                p.getInventory().addItem(i);
                                p.getInventory().addItem(g);
                                maxHealthAttr.setBaseValue(maxHealthAttr.getBaseValue()-4);
                                if(p.getHealth() > maxHealthAttr.getBaseValue()){
                                    p.setHealth(maxHealthAttr.getBaseValue());
                                }
                                pdc.remove(ToolClass.fromNamespaceKey("limited"));
                            }
                        },
                        ClickCallback.Options.builder().build()
                )
        );
        ActionButton cancel = ActionButton.create(
                Component.text("取消",NamedTextColor.RED),
                Component.text("我再考虑一下下",NamedTextColor.YELLOW),
                150,
                DialogAction.customClick(
                        (_, e)->{
                            if(e instanceof Player p){
                                p.closeDialog();
                            }
                        },
                        ClickCallback.Options.builder().build()
                )
        );
        Dialog dialog = Dialog.create(
                builder -> builder.empty()
                        .base(DialogBase.builder(Component.text("嗜血恶魔", NamedTextColor.GOLD))
                                .canCloseWithEscape(true)
                                .body(List.of(t1,t2,iron,gold))
                                .build()
                        )
                        .type(DialogType.multiAction(
                                List.of(commit, cancel)
                        ).build())
        );
        player.showDialog(dialog);
    }
}
