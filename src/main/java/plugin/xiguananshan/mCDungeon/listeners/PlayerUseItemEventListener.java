package plugin.xiguananshan.mCDungeon.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;
import plugin.xiguananshan.mCDungeon.ticks.PlayerSkillService;
import plugin.xiguananshan.mCDungeon.ticks.WindHoleService;

import java.util.List;

public class PlayerUseItemEventListener implements Listener {
    @EventHandler
    public void run(PlayerInteractEvent e){
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK){
            if(e.getItem()!=null){
                ItemMeta meta = e.getItem().getItemMeta();
                assert meta != null;
                PersistentDataContainer pdc = meta.getPersistentDataContainer();
                if(!pdc.has(ToolClass.fromNamespaceKey("changing")) && pdc.has(ToolClass.fromNamespaceKey("count"))){
                    pdc.set(ToolClass.fromNamespaceKey("changing"),PersistentDataType.INTEGER,100);
                    e.getItem().setItemMeta(meta);
                }
            }
            return;
        }
        long now_time = System.currentTimeMillis();
        if(e.getItem() != null){
            ItemMeta meta = e.getItem().getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            DamageType t;
            String ts = pdc.get(ToolClass.fromNamespaceKey("element"), PersistentDataType.STRING);
            try{
                t = DamageType.valueOf(ts);
            }catch (Exception E){
                t = DamageType.PHYSIC;
            }
            Integer id = pdc.get(ToolClass.fromNamespaceKey("id"),PersistentDataType.INTEGER);
            Integer skill = pdc.get(ToolClass.fromNamespaceKey("skill"),PersistentDataType.INTEGER);
            if(id != null && skill != null){
                if(e.getPlayer().isSneaking()){
                    PlayerSkillService service = ToolClass.skillServiceMap.get(e.getPlayer().getUniqueId());

                    if(service!=null){
                        if(!service.isCold(id)){
                            if(pdc.has(ToolClass.fromNamespaceKey("wind_sword"))){
                                ArmorStand armorStand = e.getPlayer().getWorld().spawn(e.getPlayer().getLocation().clone().add(0,3,0), ArmorStand.class);
                                armorStand.setVisible(false);
                                armorStand.setInvulnerable(true);
                                assert armorStand.getEquipment()!=null;
                                armorStand.getEquipment().setHelmet(new ItemStack(Material.BLACK_CONCRETE));
                                armorStand.setGravity(false);
                                new WindHoleService(armorStand).runTaskTimer(ToolClass.PLUGIN,0,1);
                                e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.ENTITY_GENERIC_EXPLODE,3,1);
                            }
                            service.addCold(id,skill);
                        }
                    }
                }
            }
            if(pdc.has(ToolClass.fromNamespaceKey("normal_gun")) && pdc.has(ToolClass.fromNamespaceKey("count"))){
                Integer k = pdc.get(ToolClass.fromNamespaceKey("count"),PersistentDataType.INTEGER);
                if(k!=null){
                    if(k>0 && !pdc.has(ToolClass.fromNamespaceKey("changing"))){
                        if(ToolClass.Shoot_timer.containsKey(e.getPlayer().getUniqueId())){
                            if(now_time - ToolClass.Shoot_timer.get(e.getPlayer().getUniqueId()) >= 300){

                                ToolClass.Shoot(e.getPlayer().getEyeLocation(),e.getPlayer().getWorld(),10f,35, t);
                                ToolClass.Shoot_timer.put(e.getPlayer().getUniqueId(),now_time);

                                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST,1,1);
                                k--;
                                pdc.set(ToolClass.fromNamespaceKey("count"),PersistentDataType.INTEGER,k);
                                List<String> lore = meta.getLore();
                                String ss = ChatColor.YELLOW + "剩余弹药：" + ChatColor.GREEN + k + "/10";
                                if(lore!=null)
                                    lore.set(2,ss);
                                meta.setLore(lore);
                                e.getItem().setItemMeta(meta);
                            }
                        }else{
                            ToolClass.Shoot_timer.put(e.getPlayer().getUniqueId(),now_time);
                            ToolClass.Shoot(e.getPlayer().getEyeLocation(),e.getPlayer().getWorld(),10f,35, t);
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST,1,1);
                            k--;
                            pdc.set(ToolClass.fromNamespaceKey("count"),PersistentDataType.INTEGER,k);
                            List<String> lore = meta.getLore();
                            String ss = ChatColor.YELLOW + "剩余弹药：" + ChatColor.GREEN + k + "/10";
                            if(lore!=null)
                                lore.set(2,ss);
                            meta.setLore(lore);
                            e.getItem().setItemMeta(meta);
                        }

                    }
                    if(k<=0){
                        if(!pdc.has(ToolClass.fromNamespaceKey("changing"))){
                            pdc.set(ToolClass.fromNamespaceKey("changing"),PersistentDataType.INTEGER,100);
                            e.getItem().setItemMeta(meta);
                        }

                    }

                }

            }

        }

    }
}
