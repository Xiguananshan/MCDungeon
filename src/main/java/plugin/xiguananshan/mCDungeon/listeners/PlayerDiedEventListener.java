package plugin.xiguananshan.mCDungeon.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.ticks.PlayerDiedService;

import java.time.Duration;
import java.util.UUID;

public class PlayerDiedEventListener implements Listener {
    @EventHandler
    public void run(EntityDeathEvent e){
        if(e.getEntity() instanceof Player p){
            if(ToolClass.gaming){
                if(ToolClass.game_players.contains(p.getUniqueId())){
                    e.setCancelled(true);
                    AttributeInstance health = p.getAttribute(Attribute.MAX_HEALTH);
                    assert health != null;
                    p.setHealth(health.getBaseValue());
                    p.setGameMode(GameMode.SPECTATOR);
                    ToolClass.died_players.add(p.getUniqueId());
                    Title title = Title.title(Component.text("Noob", NamedTextColor.DARK_RED, TextDecoration.BOLD),
                            Component.text().build(),
                            Title.Times.times(
                                    Duration.ofMillis(500),
                                    Duration.ofMillis(2000),
                                    Duration.ofMillis(500)
                            ));
                   p.showTitle(title);
                   for(UUID uuid : ToolClass.game_players){
                       Player player = Bukkit.getPlayer(uuid);
                       if(player != null){
                           player.sendMessage(Component.text("玩家", NamedTextColor.RED).append(Component.text(p.getName(),NamedTextColor.YELLOW)).append(Component.text("倒地，请前去救援",NamedTextColor.RED)));
                           player.playSound(player, Sound.ENTITY_ENDERMAN_DEATH,3,1);
                       }
                   }
                    ArmorStand stand = p.getWorld().spawn(p.getLocation(), ArmorStand.class);
                    EntityEquipment equipment = stand.getEquipment();
                    stand.setArms(true);
                    stand.setInvulnerable(true);
                    ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta meta = (SkullMeta) stack.getItemMeta();
                    assert meta != null;
                    meta.setOwningPlayer(p);
                    stack.setItemMeta(meta);
                    equipment.setHelmet(stack);
                    equipment.setItemInMainHand(p.getInventory().getItemInMainHand());
                    equipment.setChestplate(p.getEquipment().getChestplate());
                    equipment.setLeggings(p.getEquipment().getLeggings());
                    equipment.setBoots(p.getEquipment().getBoots());
                    stand.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("player"), PersistentDataType.BOOLEAN,true);
                    Component text = Component.text(p.getName(),NamedTextColor.YELLOW).appendNewline().append(Component.text("救援进度：",NamedTextColor.YELLOW)).append(Component.text("0%",NamedTextColor.YELLOW));
                    TextDisplay textDisplay = p.getWorld().spawn(p.getLocation().clone().add(0,2.5,0).add(0.0F, 0.8, 0.0F), TextDisplay.class, (E) -> {
                        E.text(text);
                        E.setBillboard(Display.Billboard.CENTER);
                        E.setLineWidth(200);
                        E.setShadowed(true);
                        E.setSeeThrough(false);
                        E.setDefaultBackground(false);
                        E.setGravity(false);
                        E.setInvulnerable(true);
                        E.setPersistent(true);
                    });
                    new PlayerDiedService(textDisplay,p.getUniqueId(),p.getName(),stand).runTaskTimer(ToolClass.PLUGIN,0,1);
                }
            }
        }
    }
}
