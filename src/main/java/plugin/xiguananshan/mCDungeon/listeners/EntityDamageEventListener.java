package plugin.xiguananshan.mCDungeon.listeners;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.enums.DamageType;
import plugin.xiguananshan.mCDungeon.events.GunShotEvent;
import plugin.xiguananshan.mCDungeon.ticks.RockLifeTickTimer;
import plugin.xiguananshan.mCDungeon.ticks.SeedLightningTickTimer;

import javax.swing.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class EntityDamageEventListener implements Listener {
    @EventHandler
    public void run(EntityDamageEvent e){
        if(e.getEntity() instanceof LivingEntity l){
            PersistentDataContainer pdc = l.getPersistentDataContainer();
            if(e.getEntity() instanceof Player p){
                Integer defence = ToolClass.defence.get(p.getUniqueId());
                e.setDamage(1d-0.6d*2/Math.PI*Math.atan(1/200f*defence));
                return;
            }
            if(e.getDamageSource().getCausingEntity() != null){
                if(e.getDamageSource().getCausingEntity() instanceof Player p){
                    ItemStack stack = p.getInventory().getItemInMainHand();
                    ItemMeta meta = stack.getItemMeta();
                    if(meta != null){
                        PersistentDataContainer pdc1 = meta.getPersistentDataContainer();
                        if(pdc1.has(ToolClass.fromNamespaceKey("sword"),PersistentDataType.INTEGER)){
                            Integer k = pdc1.get(ToolClass.fromNamespaceKey("sword"),PersistentDataType.INTEGER);
                            assert k!=null;
                            if(pdc1.has(ToolClass.fromNamespaceKey("element"),PersistentDataType.STRING)){
                                String ele = pdc1.get(ToolClass.fromNamespaceKey("element"),PersistentDataType.STRING);
                                assert ele!=null;
                                try{
                                    DamageType type = DamageType.valueOf(ele);
                                    ToolClass.typeMap.put(l.getUniqueId(),type);
                                    e.setDamage(k);
                                }catch (Exception ignore){}
                            }
                        }
                    }
                }
            }
            if(pdc.has(ToolClass.fromNamespaceKey("entity"))){

                if(ToolClass.typeMap.containsKey(l.getUniqueId())){
                    DamageType t = ToolClass.typeMap.get(l.getUniqueId());
                    ToolClass.typeMap.remove(l.getUniqueId());
                    if(t!=DamageType.PHYSIC){
                        if(ToolClass.entity_type.containsKey(l.getUniqueId())){
                            HashSet<DamageType> types = ToolClass.entity_type.get(l.getUniqueId());
                            if(t != DamageType.EXPLODE)
                                types.add(t);
                            ToolClass.entity_type.replace(l.getUniqueId(),types);
                            Integer tt = ToolClass.element_type.get(l.getUniqueId()).get(t);
                            if(t == DamageType.AIR || t == DamageType.ROCK){
                                ToolClass.element_type.get(l.getUniqueId()).put(t,1);
                            }else if(t != DamageType.EXPLODE){
                                if(tt == null){
                                    ToolClass.element_type.get(l.getUniqueId()).put(t,200);
                                }else {
                                    ToolClass.element_type.get(l.getUniqueId()).put(t,Math.max(tt,200));
                                }

                            }

                            if(t == DamageType.WATER){
                                if(types.contains(DamageType.FIRE)){
                                    e.setDamage(e.getDamage()*2);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.SpawnTextEntity(ChatColor.GOLD + "蒸发",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                }else if(types.contains(DamageType.LIGHTNING)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.element_type.get(l.getUniqueId()).put(DamageType.LIGHTNING,400);
                                    ToolClass.SpawnTextEntity(ChatColor.LIGHT_PURPLE + "感电",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    e.setDamage(e.getDamage()*1.2f);
                                }else if(types.contains(DamageType.ICE)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.SpawnTextEntity(ChatColor.AQUA + "冻结",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    ToolClass.freeze_tick.put(l.getUniqueId(),40);
                                }else if(types.contains(DamageType.GRASS)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.GRASS);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.GRASS);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.SpawnTextEntity(ChatColor.GREEN + "绽放",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    ItemStack stack = new ItemStack(Material.LIME_WOOL);
                                    Item item = l.getWorld().dropItemNaturally(l.getLocation(),stack);
                                    PersistentDataContainer pdc1 = item.getPersistentDataContainer();
                                    item.setPickupDelay(Integer.MAX_VALUE);
                                    pdc1.set(ToolClass.fromNamespaceKey("grass"),PersistentDataType.INTEGER,(int)(Math.ceil(e.getDamage())*0.8));
                                    ToolClass.grass_tick.put(item.getUniqueId(),60);
                                }
                            }else if(t == DamageType.FIRE) {
                                if (types.contains(DamageType.WATER)) {
                                    e.setDamage(e.getDamage() * 1.5f);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.SpawnTextEntity(ChatColor.GOLD + "蒸发", l.getWorld(), l.getLocation().add(0, l.getHeight() + 0.2f, 0));
                                }else if(types.contains(DamageType.LIGHTNING)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.SpawnTextEntity(ChatColor.LIGHT_PURPLE + "超载",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    List<Entity> entities = l.getNearbyEntities(1.5,1.5,1.5);
                                    for(Entity entity : entities){
                                        if(entity.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                                            if(entity instanceof  LivingEntity ll){
                                                ToolClass.typeMap.put(ll.getUniqueId(),DamageType.EXPLODE);
                                                ll.damage(e.getDamage()*0.8f);
                                            }
                                        }
                                    }
                                    ToolClass.typeMap.put(l.getUniqueId(),DamageType.EXPLODE);
                                    l.damage(e.getDamage()*0.8f);
                                    l.getWorld().spawnParticle(Particle.EXPLOSION,l.getLocation(),20);
                                    l.getWorld().playSound(l.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,1,1);
                                }
                                    else if(types.contains(DamageType.GRASS)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.GRASS);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.GRASS);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.SpawnTextEntity(ChatColor.GOLD + "燃烧",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    ToolClass.grass_burn.put(l.getUniqueId(),5);
                                }else if(types.contains(DamageType.ICE)){
                                    e.setDamage(e.getDamage()*2);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    if(ToolClass.freeze_tick.containsKey(l.getUniqueId())){
                                        ToolClass.freeze_tick.replace(l.getUniqueId(),0);
                                    }
                                    ToolClass.SpawnTextEntity(ChatColor.GOLD + "融化",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                }
                            }else if(t == DamageType.ICE){
                                if(types.contains(DamageType.WATER)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.SpawnTextEntity(ChatColor.AQUA + "冻结",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    ToolClass.freeze_tick.put(l.getUniqueId(),40);
                                }else if(types.contains(DamageType.FIRE)){
                                    e.setDamage(e.getDamage()*1.5);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    if(ToolClass.freeze_tick.containsKey(l.getUniqueId())){
                                        ToolClass.freeze_tick.replace(l.getUniqueId(),0);
                                    }
                                    ToolClass.SpawnTextEntity(ChatColor.GOLD + "融化",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                }else if(types.contains(DamageType.LIGHTNING)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    ToolClass.SpawnTextEntity(ChatColor.AQUA + "超导",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    ToolClass.physic_attack.put(l.getUniqueId(),60);
                                }

                            }else if(t == DamageType.AIR){
                                if(types.contains(DamageType.WATER)){

                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.AIR);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.AIR);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.SpawnTextEntity(ChatColor.DARK_AQUA + "扩散",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));

                                    List<Entity> entities = l.getNearbyEntities(2,2,2);
                                    for(Entity entity : entities){
                                        PersistentDataContainer pdc1 = entity.getPersistentDataContainer();
                                        if(pdc1.has(ToolClass.fromNamespaceKey("entity"))){
                                            if(entity instanceof  LivingEntity el){
                                                ToolClass.typeMap.put(entity.getUniqueId(),DamageType.WATER);
                                                el.damage(e.getDamage()/2f);
                                            }
                                        }
                                    }
                                    ToolClass.fillCubeWithParticles(l.getWorld(),l.getLocation(),2,50,new Particle.DustOptions(ToolClass.typeColorMap.get(DamageType.WATER),3));
                                    l.getWorld().playSound(l.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST,1,1);
                                }
                                if(types.contains(DamageType.FIRE)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.AIR);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.AIR);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.SpawnTextEntity(ChatColor.DARK_AQUA + "扩散",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));

                                    List<Entity> entities = l.getNearbyEntities(2,2,2);
                                    for(Entity entity : entities){
                                        PersistentDataContainer pdc1 = entity.getPersistentDataContainer();
                                        if(pdc1.has(ToolClass.fromNamespaceKey("entity"))){
                                            if(entity instanceof  LivingEntity el){
                                                ToolClass.typeMap.put(entity.getUniqueId(),DamageType.FIRE);
                                                el.damage(e.getDamage()/2f);
                                            }
                                        }else if(pdc1.has(ToolClass.fromNamespaceKey("grass")) && entity instanceof Item){
                                            Collection<Entity> entities1 = entity.getNearbyEntities(1,1,1);
                                            for(Entity entity2 : entities1){
                                                if(entity2 instanceof LivingEntity ll){
                                                    if(ll.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                                                        ToolClass.typeMap.put(ll.getUniqueId(),DamageType.FIRE);
                                                        ll.damage(30);
                                                    }
                                                }
                                            }
                                            ToolClass.fillCubeWithParticles(entity.getWorld(),entity.getLocation(),0.8,10,new Particle.DustOptions(ToolClass.typeColorMap.get(DamageType.FIRE),3));
                                            entity.getWorld().playSound(entity.getLocation(),Sound.BLOCK_GRASS_BREAK,1,1);
                                            entity.remove();
                                        }
                                    }
                                    ToolClass.fillCubeWithParticles(l.getWorld(),l.getLocation(),2,50,new Particle.DustOptions(ToolClass.typeColorMap.get(DamageType.FIRE),3));
                                    l.getWorld().playSound(l.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST,1,1);
                                }
                                if(types.contains(DamageType.ICE)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.AIR);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.AIR);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    ToolClass.SpawnTextEntity(ChatColor.DARK_AQUA + "扩散",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));

                                    List<Entity> entities = l.getNearbyEntities(2,2,2);
                                    for(Entity entity : entities){
                                        PersistentDataContainer pdc1 = entity.getPersistentDataContainer();
                                        if(pdc1.has(ToolClass.fromNamespaceKey("entity"))){
                                            if(entity instanceof  LivingEntity el){
                                                ToolClass.typeMap.put(entity.getUniqueId(),DamageType.ICE);
                                                el.damage(e.getDamage()/2f);
                                            }
                                        }
                                    }
                                    ToolClass.fillCubeWithParticles(l.getWorld(),l.getLocation(),2,50,new Particle.DustOptions(ToolClass.typeColorMap.get(DamageType.ICE),3));
                                    l.getWorld().playSound(l.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST,1,1);
                                }
                                if(types.contains(DamageType.LIGHTNING)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.AIR);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.AIR);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    ToolClass.SpawnTextEntity(ChatColor.DARK_AQUA + "扩散",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));

                                    List<Entity> entities = l.getNearbyEntities(2,2,2);
                                    for(Entity entity : entities){
                                        PersistentDataContainer pdc1 = entity.getPersistentDataContainer();
                                        if(pdc1.has(ToolClass.fromNamespaceKey("entity"))){
                                            if(entity instanceof  LivingEntity el){
                                                ToolClass.typeMap.put(entity.getUniqueId(),DamageType.LIGHTNING);
                                                el.damage(e.getDamage()/2f);
                                            }
                                        }else if(pdc1.has(ToolClass.fromNamespaceKey("grass")) && entity instanceof Item){
                                            Collection<Entity> entities1 = entity.getNearbyEntities(8,8,8);
                                            LivingEntity goal = null;
                                            double min = Double.MAX_VALUE;
                                            for(Entity entity2 : entities1){
                                                if(entity2 instanceof LivingEntity ll){
                                                    if(ll.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                                                        if(ll.getLocation().distanceSquared(entity.getLocation())<=min){
                                                            min = ll.getLocation().distanceSquared(entity.getLocation());
                                                            goal = ll;

                                                        }
                                                    }
                                                }
                                            }
                                            if(goal != null){
                                                Integer d = entity.getPersistentDataContainer().get(ToolClass.fromNamespaceKey("grass"),PersistentDataType.INTEGER);
                                                if(d != null)
                                                    new SeedLightningTickTimer(entity.getLocation(),goal,goal.getWorld(),d).runTaskTimer(ToolClass.PLUGIN,0,1);
                                            }
                                            ToolClass.fillCubeWithParticles(entity.getWorld(),entity.getLocation(),0.8,10,new Particle.DustOptions(Color.GREEN,3));
                                            entity.getWorld().playSound(entity.getLocation(),Sound.BLOCK_GRASS_BREAK,1,1);
                                            entity.remove();
                                        }
                                    }
                                    ToolClass.fillCubeWithParticles(l.getWorld(),l.getLocation(),2,50,new Particle.DustOptions(ToolClass.typeColorMap.get(DamageType.LIGHTNING),3));
                                    l.getWorld().playSound(l.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST,1,1);
                                }
                            }else if(t == DamageType.GRASS){
                                if(types.contains(DamageType.WATER)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.GRASS);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.GRASS);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.SpawnTextEntity(ChatColor.GREEN + "绽放",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    ItemStack stack = new ItemStack(Material.LIME_WOOL);
                                    Item item = l.getWorld().dropItemNaturally(l.getLocation(),stack);
                                    PersistentDataContainer pdc1 = item.getPersistentDataContainer();
                                    item.setPickupDelay(Integer.MAX_VALUE);
                                    pdc1.set(ToolClass.fromNamespaceKey("grass"),PersistentDataType.INTEGER,(int)(Math.ceil(e.getDamage())*0.8));
                                    ToolClass.grass_tick.put(item.getUniqueId(),60);
                                } else if (types.contains(DamageType.FIRE)) {
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.GRASS);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.GRASS);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.SpawnTextEntity(ChatColor.GOLD + "燃烧",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    ToolClass.grass_burn.put(l.getUniqueId(),5);
                                } else if(types.contains(DamageType.LIGHTNING)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    ToolClass.element_type.get(l.getUniqueId()).put(DamageType.GRASS,400);
                                    ToolClass.SpawnTextEntity(ChatColor.GREEN + "原激化",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    ToolClass.typeMap.put(l.getUniqueId(),DamageType.GRASS);
                                    l.damage(e.getDamage()*1.2f);
                                }
                            }else if(t == DamageType.LIGHTNING){
                                if(types.contains(DamageType.WATER)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.element_type.get(l.getUniqueId()).put(DamageType.LIGHTNING,400);
                                    ToolClass.SpawnTextEntity(ChatColor.LIGHT_PURPLE + "感电",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    e.setDamage(e.getDamage()*1.2f);
                                }else if(types.contains(DamageType.FIRE)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.SpawnTextEntity(ChatColor.LIGHT_PURPLE + "超载",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    List<Entity> entities = l.getNearbyEntities(1.5,1.5,1.5);
                                    for(Entity entity : entities){
                                        if(entity.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                                            if(entity instanceof  LivingEntity ll){
                                                ToolClass.typeMap.put(ll.getUniqueId(),DamageType.EXPLODE);
                                                ll.damage(e.getDamage()*0.8f);
                                            }
                                        }
                                    }
                                    ToolClass.typeMap.put(l.getUniqueId(),DamageType.EXPLODE);
                                    l.damage(e.getDamage()*0.8f);
                                    l.getWorld().spawnParticle(Particle.EXPLOSION,l.getLocation(),20);
                                    l.getWorld().playSound(l.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,1,1);
                                }else if(types.contains(DamageType.ICE)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    ToolClass.SpawnTextEntity(ChatColor.AQUA + "超导",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    ToolClass.physic_attack.put(l.getUniqueId(),60);
                                }else if(types.contains(DamageType.GRASS)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    ToolClass.element_type.get(l.getUniqueId()).put(DamageType.GRASS,400);
                                    ToolClass.SpawnTextEntity(ChatColor.GREEN + "原激化",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    ToolClass.typeMap.put(l.getUniqueId(),DamageType.GRASS);
                                    l.damage(e.getDamage()*1.2f);
                                }
                            }else if(t == DamageType.ROCK){
                                if(types.contains(DamageType.WATER)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.ROCK);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.ROCK);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.WATER);
                                    Item i = l.getWorld().dropItemNaturally(l.getLocation(),new ItemStack(Material.YELLOW_WOOL));
                                    i.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("rock"),PersistentDataType.INTEGER,(int)Math.ceil(e.getDamage()));
                                    ToolClass.SpawnTextEntity(ChatColor.YELLOW + "结晶",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    new RockLifeTickTimer(100,i).runTaskTimer(ToolClass.PLUGIN,0,1);
                                }
                                if(types.contains(DamageType.FIRE)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.ROCK);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.ROCK);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.FIRE);
                                    Item i = l.getWorld().dropItemNaturally(l.getLocation(),new ItemStack(Material.YELLOW_WOOL));
                                    i.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("rock"),PersistentDataType.INTEGER,(int)Math.ceil(e.getDamage()));
                                    ToolClass.SpawnTextEntity(ChatColor.YELLOW + "结晶",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    new RockLifeTickTimer(100,i).runTaskTimer(ToolClass.PLUGIN,0,1);
                                }
                                if(types.contains(DamageType.ICE)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.ROCK);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.ROCK);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.ICE);
                                    Item i = l.getWorld().dropItemNaturally(l.getLocation(),new ItemStack(Material.YELLOW_WOOL));
                                    i.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("rock"),PersistentDataType.INTEGER,(int)Math.ceil(e.getDamage()));
                                    ToolClass.SpawnTextEntity(ChatColor.YELLOW + "结晶",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    new RockLifeTickTimer(100,i).runTaskTimer(ToolClass.PLUGIN,0,1);
                                }
                                if(types.contains(DamageType.LIGHTNING)){
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.ROCK);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.ROCK);
                                    ToolClass.entity_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    ToolClass.element_type.get(l.getUniqueId()).remove(DamageType.LIGHTNING);
                                    Item i = l.getWorld().dropItemNaturally(l.getLocation(),new ItemStack(Material.YELLOW_WOOL));
                                    i.getPersistentDataContainer().set(ToolClass.fromNamespaceKey("rock"),PersistentDataType.INTEGER,(int)Math.ceil(e.getDamage()));
                                    ToolClass.SpawnTextEntity(ChatColor.YELLOW + "结晶",l.getWorld(),l.getLocation().add(0,l.getHeight()+0.2f,0));
                                    new RockLifeTickTimer(100,i).runTaskTimer(ToolClass.PLUGIN,0,1);
                                }
                            }

                        }else{
                            HashSet<DamageType> types = new HashSet<>();
                            if(t != DamageType.EXPLODE)
                                types.add(t);
                            ToolClass.entity_type.put(l.getUniqueId(),types);
                            ToolClass.element_type.put(l.getUniqueId(),new HashMap<>(){{
                                if(t == DamageType.AIR || t == DamageType.ROCK){
                                    put(t,1);
                                }else if( t != DamageType.EXPLODE){
                                    put(t,200);
                                }
                            }});
                        }
                    }else{
                        if(ToolClass.physic_attack.containsKey(l.getUniqueId())){
                            e.setDamage(e.getDamage()*2);
                        }
                    }
                    Bukkit.getPluginManager().callEvent(new GunShotEvent((int)Math.ceil(e.getDamage()), t,l,true));

                }else{
                    if(ToolClass.physic_attack.containsKey(l.getUniqueId())){
                        e.setDamage(e.getDamage()*2);
                    }
                    Bukkit.getPluginManager().callEvent(new GunShotEvent((int)Math.ceil(e.getDamage()), DamageType.PHYSIC,l,true));
                }
                Integer health = pdc.get(ToolClass.fromNamespaceKey("health"), PersistentDataType.INTEGER);
                if(health != null){
                    health -= (int)Math.ceil(e.getDamage());
                    pdc.set(ToolClass.fromNamespaceKey("health"),PersistentDataType.INTEGER,health);
                    if(health <= 0){

                        if(l.getWorld().getBlockAt(l.getLocation()).getType() == Material.BLUE_ICE){
                            l.getWorld().setBlockData(l.getLocation(),Bukkit.createBlockData(Material.AIR));

                        }
                        ToolClass.freeze_tick.remove(l.getUniqueId());
                        l.setHealth(0f);
                    }
                }
                e.setDamage(0);
            }

        }
    }
}
