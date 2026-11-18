package plugin.xiguananshan.mCDungeon;

import com.jeff_media.morepersistentdatatypes.DataType;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.structure.Structure;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import plugin.xiguananshan.mCDungeon.activity.LiveShop;
import plugin.xiguananshan.mCDungeon.boss.ZombieKingService;
import plugin.xiguananshan.mCDungeon.data.*;
import plugin.xiguananshan.mCDungeon.enums.CrossStructureType;
import plugin.xiguananshan.mCDungeon.enums.DamageType;
import plugin.xiguananshan.mCDungeon.events.GunShotEvent;
import plugin.xiguananshan.mCDungeon.ticks.BossRoomLabelService;
import plugin.xiguananshan.mCDungeon.ticks.DamageTextTick;
import plugin.xiguananshan.mCDungeon.ticks.PlayerGunService;
import plugin.xiguananshan.mCDungeon.ticks.PlayerLightningGunService;
import plugin.xiguananshan.mCDungeon.ticks.PlayerRockDefenceTickTimer;
import plugin.xiguananshan.mCDungeon.ticks.PlayerSkillService;
import plugin.xiguananshan.mCDungeon.ticks.SeedLightningTickTimer;

public class ToolClass {
   public static MCDungeon PLUGIN = MCDungeon.getPlugin(MCDungeon.class);
   public static ItemStack DefaultGun;
   public static ItemStack WindSword;
   public static ItemStack StoneBook;
   public static ItemStack LightningGun;
   public static ItemStack GrassSword;
   public static ItemStack WaterBook;
   public static ItemStack FireBook;
   public static ItemStack Null;
   public static ItemStack IceBow;
   public static ItemStack IceBowArrow;
   public static ItemStack PhysicSword;
   public static ItemStack GodSpawner;
   public static ItemStack ElementStone;
   public static ItemStack Compass;
   public static ItemStack Iron;
   public static ItemStack Gold;
   public static Map<ItemStack, Float> itemPool;
   public static Map<UUID, Long> Shoot_timer;
   public static Map<UUID, DamageType> typeMap;
   public static Map<UUID, Map<DamageType, Integer>> entity_defence;
   public static Map<UUID, HashSet<DamageType>> entity_type;
   public static Map<UUID, Map<DamageType, Integer>> element_type;
   public static Map<UUID, Integer> freeze_tick;
   public static Map<UUID, Integer> grass_tick;
   public static Map<UUID, Integer> grass_burn;
   public static Map<UUID, Integer> physic_attack;
   public static Map<UUID, Integer> defence;
   public static Map<UUID, Integer> grass_sword_skill_tick;
   public static Map<UUID, PlayerGunService> gunServiceMap;
   public static Map<UUID, PlayerRockDefenceTickTimer> rockDefenceTickTimerMap;
   public static Map<UUID, PlayerSkillService> skillServiceMap;
   public static Map<UUID, PlayerLightningGunService> lightningGunServiceMap;
   public static Map<UUID, Integer> fireBookSkillTick;
   public static Map<UUID, Long> book_tick;
   public static Map<UUID, Integer> IceBowSkillTick;
   public static Integer max_item_id;
   public static List<UUID> game_players;
   public static boolean gaming;
   public static Structure CROSS_ROOM_1_1;
   public static Structure CROSS_ROOM_1_2;
   public static Structure ACTIVITY_ROOM_1_1;
   public static Structure BOSS_ROOM_1_1;
   public static Structure START_ROOM_1_1;
   public static Structure CENTER_ROOM_1_1;
   public static Structure CENTER_ROOM_1_2;
   public static Structure SHOP_ROOM_1_1;
   public static Structure Box_ROOM_1_1;
   public static StorageManager storageManager;
   public static CornerData EntityRoomData;
   public static Map<UUID, Long> sword_tick;
   public static CornerData LiveCrossRoom;
   public static Map<UUID, ActivityRequest> requestMap;
   public static Map<DamageType, Color> typeColorMap;
   public static Map<DamageType, String> typeNameMap;
   public static Map<ItemStack, CostData> ShopData;
   public static Integer round = 1;
   public static CornerData.Corner BossRoom;
   public static Map<UUID,Scoreboard> scoreboardMap;
   public static Map<UUID, PDCModify> modifyMap;
   public static HashSet<UUID> modified;
   public static HashSet<UUID> died_players;
   public static HashMap<UUID,Location> un_relief_players;
   public static CornerData.Corner BossDoor;
   public static boolean isIceBow(ItemStack stack) {
      if (stack != null && stack.getType() == Material.BOW) {
         ItemMeta meta = stack.getItemMeta();
         return meta != null && meta.getPersistentDataContainer().has(fromNamespaceKey("ice_bow"), PersistentDataType.BOOLEAN);
      } else {
         return false;
      }
   }

   public static boolean isIceBowArrow(ItemStack stack) {
      if (stack != null && stack.getType() == Material.ARROW) {
         ItemMeta meta = stack.getItemMeta();
         return meta != null && meta.getPersistentDataContainer().has(fromNamespaceKey("ice_bow_arrow"), PersistentDataType.BOOLEAN);
      } else {
         return false;
      }
   }

   public static boolean giveIceBowArrow(Player p) {
      NamespacedKey granted = fromNamespaceKey("ice_bow_arrow_granted");
      PersistentDataContainer pdc = p.getPersistentDataContainer();
      if (pdc.has(granted, PersistentDataType.BOOLEAN)) {
         return false;
      } else {
         for(ItemStack stack : p.getInventory().getContents()) {
            if (isIceBowArrow(stack)) {
               pdc.set(granted, PersistentDataType.BOOLEAN, true);
               return false;
            }
         }

         int slot = p.getInventory().firstEmpty();
         if (slot < 0) {
            return false;
         } else {
            p.getInventory().setItem(slot, IceBowArrow.clone());
            pdc.set(granted, PersistentDataType.BOOLEAN, true);
            return true;
         }
      }
   }

   public static ItemStack fromItem(ItemStack stack) {
      ItemStack s = stack.clone();
      ItemMeta meta = s.getItemMeta();

      assert meta != null;
      if(stack.getType()  != Material.GOLD_INGOT && stack.getType() != Material.IRON_INGOT)
         meta.getPersistentDataContainer().set(fromNamespaceKey("id"), PersistentDataType.INTEGER, max_item_id);
      if (meta.getPersistentDataContainer().has(fromNamespaceKey("random_element"))) {
         Random random = new Random();

         DamageType t;
         for(t = DamageType.PHYSIC; t == DamageType.PHYSIC || t == DamageType.EXPLODE; t = DamageType.values()[random.nextInt(DamageType.values().length)]) {
         }

         meta.getPersistentDataContainer().set(fromNamespaceKey("element"), PersistentDataType.STRING, t.toString());
         List<Component> lore = meta.lore();
         if (lore != null) {
            lore.add(Component.text("元素：", NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(t), NamedTextColor.nearestTo(TextColor.color((typeColorMap.get(t)).asRGB())))));
         } else {
            lore = List.of(Component.text("元素：", NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(t), NamedTextColor.nearestTo(TextColor.color((typeColorMap.get(t)).asRGB())))));
         }

         meta.lore(lore);
      }

      max_item_id = max_item_id + 1;
      s.setItemMeta(meta);
      return s;
   }

   public static Mob SummonEntity(World world, Location location, List<ItemStack> equipments, boolean ai, int health, Class<? extends Mob> type) {
      Mob entity = world.spawn(location, type);
      entity.setAI(ai);
      EntityEquipment equipment = entity.getEquipment();
      equipment.setHelmetDropChance(0.0F);
      equipment.setChestplateDropChance(0.0F);
      equipment.setLeggingsDropChance(0.0F);
      equipment.setBootsDropChance(0.0F);
      equipment.setItemInMainHandDropChance(0.0F);
      entity.setRemoveWhenFarAway(false);
      equipment.setHelmet(equipments.get(0));
      equipment.setChestplate(equipments.get(1));
      equipment.setLeggings(equipments.get(2));
      equipment.setBoots(equipments.get(3));
      equipment.setItemInMainHand(equipments.get(4));
      PersistentDataContainer pdc = entity.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("health"), PersistentDataType.INTEGER, health);
      pdc.set(fromNamespaceKey("entity"), PersistentDataType.BOOLEAN, true);
      entity.setCustomNameVisible(true);
      entity.customName(Component.text("剩余血量", NamedTextColor.YELLOW).append(Component.text(String.valueOf(health), NamedTextColor.RED)));
      return entity;
   }

   public static Mob SummonEntity(World world, Location location, boolean ai, int health, Class<? extends Mob> type) {
      Mob entity = world.spawn(location, type);
      entity.setAI(ai);
      EntityEquipment equipment = entity.getEquipment();
      equipment.setHelmetDropChance(0.0F);
      equipment.setChestplateDropChance(0.0F);
      equipment.setLeggingsDropChance(0.0F);
      equipment.setBootsDropChance(0.0F);
      equipment.setItemInMainHandDropChance(0.0F);
      entity.setRemoveWhenFarAway(false);
      PersistentDataContainer pdc = entity.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("health"), PersistentDataType.INTEGER, health);
      pdc.set(fromNamespaceKey("entity"), PersistentDataType.BOOLEAN, true);
      entity.setCustomNameVisible(true);
      entity.customName(Component.text("剩余血量", NamedTextColor.YELLOW).append(Component.text(String.valueOf(health), NamedTextColor.RED)));
      return entity;
   }

   public static NamespacedKey fromNamespaceKey(String name) {
      return new NamespacedKey(PLUGIN, name);
   }



   public static void SpawnDamageEntity(int damage, World world, Location location, NamedTextColor color) {
      if (damage > 0) {
         TextDisplay entity = world.spawn(location, TextDisplay.class);
         entity.text(Component.text("-" + damage, color));
         entity.setBillboard(Billboard.CENTER);
         entity.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
         entity.setShadowed(true);
         entity.setPersistent(false);
         BukkitRunnable runnable = new DamageTextTick(entity, 0.5F);
         runnable.runTaskTimer(PLUGIN, 0L, 1L);
      }
   }

   public static void SpawnTextEntity(Component text, World world, Location location) {
      TextDisplay entity = world.spawn(location, TextDisplay.class);
      entity.text(text);
      entity.setBillboard(Billboard.CENTER);
      entity.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
      entity.setShadowed(true);
      entity.setPersistent(false);
      BukkitRunnable runnable = new DamageTextTick(entity, 0.5F);
      runnable.runTaskTimer(PLUGIN, 0L, 1L);
   }

   public static void Shoot(Location location, World world, float max_distance, int damage, DamageType type, Player source) {
      Vector direction = location.getDirection();
      RayTraceResult block = world.rayTraceBlocks(location, direction, max_distance, FluidCollisionMode.NEVER, true);
      RayTraceResult entity = world.rayTraceEntities(location, direction, max_distance, 0.0F, (entity1x) -> {
         PersistentDataContainer pdc = entity1x.getPersistentDataContainer();
         return pdc.has(fromNamespaceKey("entity")) || entity1x instanceof Item && pdc.has(fromNamespaceKey("grass")) && (type == DamageType.LIGHTNING || type == DamageType.FIRE);
      });
      RayTraceResult r = null;
      if (entity != null) {
         double e = entity.getHitPosition().distance(location.toVector());
         double b = block != null ? block.getHitPosition().distance(location.toVector()) : Double.MAX_VALUE;
         if (e < b) {
            Entity entity1 = entity.getHitEntity();

            assert entity1 != null;

            if (entity1 instanceof LivingEntity l) {
                Vector n_direction = direction.normalize();
               n_direction.setY(0);
               l.setVelocity(n_direction.multiply(0.5F).setY(0.3F));
               Bukkit.getPluginManager().callEvent(new GunShotEvent(damage, type, l, false, source));
            } else if (entity1.getPersistentDataContainer().has(fromNamespaceKey("grass"))) {
               if (type == DamageType.LIGHTNING) {
                  Collection<Entity> entities = entity1.getNearbyEntities(8.0F, 8.0F, 8.0F);
                  LivingEntity goal = null;
                  double min = Double.MAX_VALUE;

                  for(Entity entity2 : entities) {
                     if (entity2 instanceof LivingEntity ll) {
                         if (ll.getPersistentDataContainer().has(fromNamespaceKey("entity")) && ll.getLocation().distanceSquared(entity1.getLocation()) <= min) {
                           min = ll.getLocation().distanceSquared(entity1.getLocation());
                           goal = ll;
                        }
                     }
                  }

                  if (goal != null) {
                     Integer d = entity1.getPersistentDataContainer().get(fromNamespaceKey("grass"), PersistentDataType.INTEGER);
                     if (d != null) {
                        (new SeedLightningTickTimer(entity1.getLocation(), goal, goal.getWorld(), d)).runTaskTimer(PLUGIN, 0L, 1L);
                     }
                  }

                  fillCubeWithParticles(entity1.getWorld(), entity1.getLocation(), 0.8, 10, (new Particle.DustOptions(Color.GREEN, 3.0F)));
                  entity1.getWorld().playSound(entity1.getLocation(), Sound.BLOCK_GRASS_BREAK, 1.0F, 1.0F);
                  entity1.remove();
               } else if (type == DamageType.FIRE) {
                  for(Entity entity2 : entity1.getNearbyEntities(1.0F, 1.0F, 1.0F)) {
                     if (entity2 instanceof LivingEntity ll) {
                         if (ll.getPersistentDataContainer().has(fromNamespaceKey("entity"))) {
                           typeMap.put(ll.getUniqueId(), DamageType.FIRE);
                           ll.damage(30.0F);
                        }
                     }
                  }

                  fillCubeWithParticles(entity1.getWorld(), entity1.getLocation(), 0.8, 10, (new Particle.DustOptions(typeColorMap.get(DamageType.FIRE), 3.0F)));
                  entity1.getWorld().playSound(entity1.getLocation(), Sound.BLOCK_GRASS_BREAK, 1.0F, 1.0F);
                  entity1.remove();
               }
            }

            r = entity;
         } else {
            r = block;
         }
      } else if (block != null) {
         r = block;
      }

      DrawRay(r, world, location, max_distance, typeColorMap.get(type));
   }

   public static void DrawRay(RayTraceResult ray, World world, Location start, float max_distance, Color color) {
      Vector end;
      if (ray != null) {
         end = ray.getHitPosition();
      } else {
         end = start.toVector().add(start.getDirection().clone().normalize().multiply(max_distance));
      }

      Particle.DustOptions DustData = new Particle.DustOptions(color, 1.0F);
      Vector startVec = start.toVector();
      Vector step = end.clone().subtract(startVec);
      Vector now = startVec.clone();
      double totalDistance = step.length();
      step.normalize().multiply(0.2F);

      for(double t = 0.0F; t < totalDistance; t += 0.2F) {
         world.spawnParticle(Particle.DUST, now.getX(), now.getY(), now.getZ(), 1, 0.0F, 0.0F, 0.0F, 0.0F, DustData);
         now.add(step);
      }

   }

   public static void fillCubeWithParticles(World world, Location center, double halfSize, int particleCount, Particle.DustOptions dust) {
      Random random = ThreadLocalRandom.current();

      for(int i = 0; i < particleCount; ++i) {
         double x = center.getX() + (random.nextDouble() * (double)2.0F - (double)1.0F) * halfSize;
         double y = center.getY() + (random.nextDouble() * (double)2.0F - (double)1.0F) * halfSize;
         double z = center.getZ() + (random.nextDouble() * (double)2.0F - (double)1.0F) * halfSize;
         world.spawnParticle(Particle.DUST, x, y, z, 1, 0.0F, 0.0F, 0.0F, 0.0F, dust);
      }

   }

   public static void fillCubeWithParticles(World world, Location center, double halfSize, int particleCount, Particle particle) {
      Random random = ThreadLocalRandom.current();

      for(int i = 0; i < particleCount; ++i) {
         double x = center.getX() + (random.nextDouble() * (double)2.0F - (double)1.0F) * halfSize;
         double y = center.getY() + (random.nextDouble() * (double)2.0F - (double)1.0F) * halfSize;
         double z = center.getZ() + (random.nextDouble() * (double)2.0F - (double)1.0F) * halfSize;
         world.spawnParticle(particle, x, y, z, 1, 0.0F, 0.0F, 0.0F, 0.0F);
      }

   }

   private static boolean isMainRoomProtected(EasyLocation loc) {
      if (loc.y() != 100) {
         return false;
      } else {
         int x = loc.x();
         int z = loc.z();
         return x == 0 && z == 0 || x == 30 && z == 0 || x == -30 && z == 0 || x == 0 && (z == -30 || z == 30);
      }
   }

   private static boolean isMainRoomCorridorAdjacent(Location loc) {
      if (loc.getBlockY() != 100) {
         return false;
      } else {
         int x = loc.getBlockX();
         int z = loc.getBlockZ();
         return x == -30 && z == 0 || x == 0 && (z == -30 || z == 30) || x == 30 && z == 0;
      }
   }

   private static void recordCorner(CornerData data, CornerData.Corner corner) {
      if (data.corners.contains(corner)) {
         return;
      }
      data.corners.add(corner);
      try {
         storageManager.save(data);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void GenerateMap(List<Player> players, int type) {
      EntityRoomData = new CornerData();
      LiveCrossRoom = new CornerData();
      if (type == 1) {
         game_players.clear();
         Title title = Title.title(Component.text("正在生成地图", NamedTextColor.YELLOW), Component.text().build(), Times.times(Duration.ofMillis(500L), Duration.ofMillis(2000L), Duration.ofMillis(500L)));

         for(Player player : players) {
            player.showTitle(title);
         }

         List<Structure> spawnable = List.of(CENTER_ROOM_1_2, CENTER_ROOM_1_1, SHOP_ROOM_1_1, ACTIVITY_ROOM_1_1, Box_ROOM_1_1);
         World world = Bukkit.getWorld("world");

         assert world != null;

         try {
            CornerData data = storageManager.load();
            if (data != null) {
               int minX = Integer.MAX_VALUE;
               int maxX = Integer.MIN_VALUE;
               int minY = Integer.MAX_VALUE;
               int maxY = Integer.MIN_VALUE;
               int minZ = Integer.MAX_VALUE;
               int maxZ = Integer.MIN_VALUE;
               for(CornerData.Corner corner : data.corners) {
                  minX = Math.min(minX, corner.minx);
                  maxX = Math.max(maxX, corner.maxx);
                  minY = Math.min(minY, corner.miny);
                  maxY = Math.max(maxY, corner.maxy);
                  minZ = Math.min(minZ, corner.minz);
                  maxZ = Math.max(maxZ, corner.maxz);
                  for(int x = corner.minx; x <= corner.maxx; ++x) {
                     for(int y = corner.miny; y <= corner.maxy; ++y) {
                        for(int z = corner.minz; z <= corner.maxz; ++z) {
                           Block block = world.getBlockAt(x, y, z);
                           BlockState var13 = block.getState();
                           if (var13 instanceof Chest chest) {
                               chest.getInventory().clear();
                           }

                           block.setType(Material.AIR, false);
                        }
                     }
                  }
               }
               // 再清一遍旧地图的外包围盒，处理“有方块但没被 corner 记录”的历史残留
               if (!data.corners.isEmpty()) {
                  int margin = 30;
                  for(int x = minX - margin; x <= maxX + margin; ++x) {
                     for(int y = minY; y <= maxY; ++y) {
                        for(int z = minZ - margin; z <= maxZ + margin; ++z) {
                           Block block = world.getBlockAt(x, y, z);
                           BlockState state = block.getState();
                           if (state instanceof Chest chest) {
                               chest.getInventory().clear();
                           }

                           block.setType(Material.AIR, false);
                        }
                     }
                  }
               }
            }
         } catch (Exception e) {
            e.printStackTrace();
         }

         int center = 0;
         int shop = 0;
         int box = 0;
         Random rand = new Random();
         int count = rand.nextInt(5) + 6;
         int c = 0;
         List<CrossData> spawn = new ArrayList<>();
         HashSet<EasyLocation> rooms = new HashSet<>();
         CornerData data = new CornerData();
         data.corners = new ArrayList<>();
         recordCorner(data, new CornerData.Corner(0, 100, 0, 29, 105, 29));
         START_ROOM_1_1.place(new Location(world, 0.0F, 100.0F, 0.0F), false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
         rooms.add(new EasyLocation(0, 100, 0));
         recordCorner(data, new CornerData.Corner(0, 100, 0, 29, 105, 29));
         spawn.add(new CrossData(new Location(world, 30.0F, 100.0F, 0.0F), CrossStructureType.XA));
         EasyLocation boss_room = null;

         label667:
         while((center < 3 || shop < 1 || box < 1 || c < count) && c < 2 * count) {
            Structure s = spawnable.get(rand.nextInt(spawnable.size()));
            CrossData l = spawn.get(rand.nextInt(spawn.size()));
            spawn.remove(l);
            switch (l.type) {
               case XA: {
                  EasyLocation location = new EasyLocation(l.l.getBlockX() + 30, l.l.getBlockY(), l.l.getBlockZ());
                  if (isMainRoomProtected(location) || isMainRoomCorridorAdjacent(l.l) && (l.l.getBlockX() != 30 || l.l.getBlockZ() != 0 || l.type != CrossStructureType.XA)) {
                     continue;
                  }

                  recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                  CROSS_ROOM_1_2.place(l.l, false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                  recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                  if (rooms.contains(location)) {
                     break;
                  }

                  rooms.add(location);
                  recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                  s.place(new Location(world, location.x(), location.y(), location.z()), false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                  CrossData c1 = new CrossData(new Location(world, location.x() + 30, location.y(), location.z()), CrossStructureType.XA);
                  CrossData c2 = new CrossData(new Location(world, location.x(), location.y(), location.z() - 30), CrossStructureType.ZR);
                  CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() + 30), CrossStructureType.ZA);
                  spawn.add(c1);
                  spawn.add(c2);
                  spawn.add(c3);
                  recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                  if (s == CENTER_ROOM_1_1 || s == CENTER_ROOM_1_2) {
                     ++center;
                     EntityRoomData.corners.add(new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                  }
                  if(s== ACTIVITY_ROOM_1_1){
                     Random random = new Random();
                     SpawnActivity(1,world,new Location(world,location.x()+14,location.y()+1,location.z()+14));
                  }

                  if (s == SHOP_ROOM_1_1) {
                     ++shop;
                     Random random = new Random();

                     for(int z = 0; z <= 10; z += 2) {
                        Location loc = new Location(world, location.x() + 13, location.y() + 3, location.z() + 9 + z);
                        List<ItemStack> stacks = ShopData.keySet().stream().toList();
                        ItemStack stack = stacks.get(random.nextInt(stacks.size()));
                        BuildStore(stack.getType() != Material.GOLD_INGOT, fromItem(stack), (ShopData.get(stack)).iron(), (ShopData.get(stack)).gold(), loc.clone().add(0.5F, 0.0F, 0.5F));
                     }
                  }

                  if (s == Box_ROOM_1_1) {
                     ++box;
                  }
                  break;
               }
               case XR: {
                  EasyLocation location = new EasyLocation(l.l.getBlockX() - 30, l.l.getBlockY(), l.l.getBlockZ());
                  if (isMainRoomProtected(location) || isMainRoomCorridorAdjacent(l.l) && (l.l.getBlockX() != 30 || l.l.getBlockZ() != 0 || l.type != CrossStructureType.XA)) {
                     continue;
                  }

                  recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                  CROSS_ROOM_1_2.place(l.l, false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                  recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                  if (rooms.contains(location)) {
                     break;
                  }

                  rooms.add(location);
                  recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                  s.place(new Location(world, location.x(), location.y(), location.z()), false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                  CrossData c1 = new CrossData(new Location(world, location.x() - 30, location.y(), location.z()), CrossStructureType.XR);
                  CrossData c2 = new CrossData(new Location(world, location.x(), location.y(), location.z() - 30), CrossStructureType.ZR);
                  CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() + 30), CrossStructureType.ZA);
                  spawn.add(c1);
                  spawn.add(c2);
                  spawn.add(c3);
                  recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                  if (s == CENTER_ROOM_1_1 || s == CENTER_ROOM_1_2) {
                     ++center;
                     EntityRoomData.corners.add(new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                  }
                  if(s== ACTIVITY_ROOM_1_1){
                     Random random = new Random();
                     SpawnActivity(1,world,new Location(world,location.x()+14,location.y()+1,location.z()+14));
                  }

                  if (s == SHOP_ROOM_1_1) {
                     ++shop;
                     Random random = new Random();

                     for(int z = 0; z <= 10; z += 2) {
                        Location loc = new Location(world, location.x() + 13, location.y() + 3, location.z() + 9 + z);
                        List<ItemStack> stacks = ShopData.keySet().stream().toList();
                        ItemStack stack = stacks.get(random.nextInt(stacks.size()));
                        BuildStore(stack.getType() != Material.GOLD_INGOT, fromItem(stack), (ShopData.get(stack)).iron(), (ShopData.get(stack)).gold(), loc.clone().add(0.5F, 0.0F, 0.5F));
                     }
                  }

                  if (s == Box_ROOM_1_1) {
                     ++box;
                  }
                  break;
               }
               case ZA: {
                  EasyLocation location = new EasyLocation(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ() + 30);
                  if (isMainRoomProtected(location) || isMainRoomCorridorAdjacent(l.l) && (l.l.getBlockX() != 30 || l.l.getBlockZ() != 0 || l.type != CrossStructureType.XA)) {
                     continue;
                  }

                  recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                  CROSS_ROOM_1_1.place(l.l, false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                  recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                  if (rooms.contains(location)) {
                     break;
                  }

                  rooms.add(location);
                  recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                  s.place(new Location(world, location.x(), location.y(), location.z()), false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                  CrossData c1 = new CrossData(new Location(world, location.x() - 30, location.y(), location.z()), CrossStructureType.XR);
                  CrossData c2 = new CrossData(new Location(world, location.x() + 30, location.y(), location.z()), CrossStructureType.XA);
                  CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() + 30), CrossStructureType.ZA);
                  spawn.add(c1);
                  spawn.add(c2);
                  spawn.add(c3);
                  recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                  if (s == CENTER_ROOM_1_1 || s == CENTER_ROOM_1_2) {
                     ++center;
                     EntityRoomData.corners.add(new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                  }
                  if(s== ACTIVITY_ROOM_1_1){
                     Random random = new Random();
                     SpawnActivity(1,world,new Location(world,location.x()+14,location.y()+1,location.z()+14));
                  }

                  if (s == SHOP_ROOM_1_1) {
                     ++shop;
                     Random random = new Random();

                     for(int z = 0; z <= 10; z += 2) {
                        Location loc = new Location(world, location.x() + 13, location.y() + 3, location.z() + 9 + z);
                        List<ItemStack> stacks = ShopData.keySet().stream().toList();
                        ItemStack stack = stacks.get(random.nextInt(stacks.size()));
                        BuildStore(stack.getType() != Material.GOLD_INGOT, fromItem(stack), (ShopData.get(stack)).iron(), (ShopData.get(stack)).gold(), loc.clone().add(0.5F, 0.0F, 0.5F));
                     }
                  }

                  if (s == Box_ROOM_1_1) {
                     ++box;
                  }
                  break;
               }
               case ZR: {
                  EasyLocation location = new EasyLocation(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ() - 30);
                  if (isMainRoomProtected(location) || isMainRoomCorridorAdjacent(l.l) && (l.l.getBlockX() != 30 || l.l.getBlockZ() != 0 || l.type != CrossStructureType.XA)) {
                     continue;
                  }

                  recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                  CROSS_ROOM_1_1.place(l.l, false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                  recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                  if (!rooms.contains(location)) {
                     rooms.add(location);
                     recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                     s.place(new Location(world, location.x(), location.y(), location.z()), false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                     CrossData c1 = new CrossData(new Location(world, location.x() + 30, location.y(), location.z()), CrossStructureType.XA);
                     CrossData c2 = new CrossData(new Location(world, location.x() - 30, location.y(), location.z()), CrossStructureType.XR);
                     CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() - 30), CrossStructureType.ZR);
                     spawn.add(c1);
                     spawn.add(c2);
                     spawn.add(c3);
                     recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                     if (s == CENTER_ROOM_1_1 || s == CENTER_ROOM_1_2) {
                        ++center;
                        EntityRoomData.corners.add(new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                     }

                     if(s== ACTIVITY_ROOM_1_1){
                        Random random = new Random();
                        SpawnActivity(1,world,new Location(world,location.x()+14,location.y()+1,location.z()+14));
                     }
                     if (s == SHOP_ROOM_1_1) {
                        ++shop;
                        Random random = new Random();

                        for(int z = 0; z <= 10; z += 2) {
                           Location loc = new Location(world, location.x() + 13, location.y() + 3, location.z() + 9 + z);
                           List<ItemStack> stacks = ShopData.keySet().stream().toList();
                           ItemStack stack = stacks.get(random.nextInt(stacks.size()));
                           BuildStore(stack.getType() != Material.GOLD_INGOT, fromItem(stack), (ShopData.get(stack)).iron(), (ShopData.get(stack)).gold(), loc.clone().add(0.5F, 0.0F, 0.5F));
                        }
                     }

                     if (s == Box_ROOM_1_1) {
                        ++box;
                     }
                  }
               }
            }

            ++c;
            boolean lastRound = center >= 3 && shop >= 1 && box >= 1 && c >= count || c >= 2 * count;
            if (lastRound) {
               int boss = 0;

               while(boss < 1 && !spawn.isEmpty()) {
                  l = spawn.get(rand.nextInt(spawn.size()));
                  spawn.remove(l);
                  switch (l.type) {
                     case XA: {
                        EasyLocation location = new EasyLocation(l.l.getBlockX() + 30, l.l.getBlockY(), l.l.getBlockZ());
                        if (isMainRoomProtected(location) || isMainRoomCorridorAdjacent(l.l) && (l.l.getBlockX() != 30 || l.l.getBlockZ() != 0 || l.type != CrossStructureType.XA)) {
                           break;
                        }

                        recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                        CROSS_ROOM_1_2.place(l.l, false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                        recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                        if (!rooms.contains(location)) {
                           rooms.add(location);
                           boss_room = location;
                           recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                           BOSS_ROOM_1_1.place(new Location(world, location.x(), location.y(), location.z()), false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                           CrossData c1 = new CrossData(new Location(world, location.x() + 30, location.y(), location.z()), CrossStructureType.XA);
                           CrossData c2 = new CrossData(new Location(world, location.x(), location.y(), location.z() - 30), CrossStructureType.ZR);
                           CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() + 30), CrossStructureType.ZA);
                           spawn.add(c1);
                           spawn.add(c2);
                           spawn.add(c3);
                           ++boss;
                           recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                        }
                        break;
                     }
                     case XR: {
                        EasyLocation location = new EasyLocation(l.l.getBlockX() - 30, l.l.getBlockY(), l.l.getBlockZ());
                        if (isMainRoomProtected(location) || isMainRoomCorridorAdjacent(l.l) && (l.l.getBlockX() != 30 || l.l.getBlockZ() != 0 || l.type != CrossStructureType.XA)) {
                           break;
                        }

                        recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                        CROSS_ROOM_1_2.place(l.l, false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                        recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                        if (!rooms.contains(location)) {
                           rooms.add(location);
                           boss_room = location;
                           recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                           BOSS_ROOM_1_1.place(new Location(world, location.x(), location.y(), location.z()), false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                           CrossData c1 = new CrossData(new Location(world, location.x() - 30, location.y(), location.z()), CrossStructureType.XR);
                           CrossData c2 = new CrossData(new Location(world, location.x(), location.y(), location.z() - 30), CrossStructureType.ZR);
                           CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() + 30), CrossStructureType.ZA);
                           spawn.add(c1);
                           spawn.add(c2);
                           spawn.add(c3);
                           ++boss;
                           recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                        }
                        break;
                     }
                     case ZA: {
                        EasyLocation location = new EasyLocation(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ() + 30);
                        if (isMainRoomProtected(location) || isMainRoomCorridorAdjacent(l.l) && (l.l.getBlockX() != 30 || l.l.getBlockZ() != 0 || l.type != CrossStructureType.XA)) {
                           break;
                        }

                        recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                        CROSS_ROOM_1_1.place(l.l, false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                        recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                        if (!rooms.contains(location)) {
                           rooms.add(location);
                           boss_room = location;
                           recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                           BOSS_ROOM_1_1.place(new Location(world, location.x(), location.y(), location.z()), false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                           CrossData c1 = new CrossData(new Location(world, location.x() - 30, location.y(), location.z()), CrossStructureType.XR);
                           CrossData c2 = new CrossData(new Location(world, location.x() + 30, location.y(), location.z()), CrossStructureType.XA);
                           CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() + 30), CrossStructureType.ZA);
                           spawn.add(c1);
                           spawn.add(c2);
                           spawn.add(c3);
                           ++boss;
                           recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                        }
                        break;
                     }
                     case ZR: {
                        EasyLocation location = new EasyLocation(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ() - 30);
                        if (!isMainRoomProtected(location) && (!isMainRoomCorridorAdjacent(l.l) || l.l.getBlockX() == 30 && l.l.getBlockZ() == 0 && l.type == CrossStructureType.XA)) {
                           recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                           CROSS_ROOM_1_1.place(l.l, false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                           recordCorner(data, new CornerData.Corner(l.l.getBlockX(), l.l.getBlockY(), l.l.getBlockZ(), l.l.getBlockX() + 29, l.l.getBlockY() + 5, l.l.getBlockZ() + 29));
                           if (!rooms.contains(location)) {
                              rooms.add(location);
                              boss_room = location;
                              recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                              BOSS_ROOM_1_1.place(new Location(world, location.x(), location.y(), location.z()), false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                              CrossData c1 = new CrossData(new Location(world, location.x() + 30, location.y(), location.z()), CrossStructureType.XA);
                              CrossData c2 = new CrossData(new Location(world, location.x() - 30, location.y(), location.z()), CrossStructureType.XR);
                              CrossData c3 = new CrossData(new Location(world, location.x(), location.y(), location.z() - 30), CrossStructureType.ZR);
                              spawn.add(c1);
                              spawn.add(c2);
                              spawn.add(c3);
                              ++boss;
                              recordCorner(data, new CornerData.Corner(location.x(), location.y(), location.z(), location.x() + 29, location.y() + 5, location.z() + 29));
                           }
                        }
                     }
                  }
               }

               if (boss == 0) {
                  for(EasyLocation room : rooms) {
                     for(CrossStructureType crossType : CrossStructureType.values()) {
                        int dx = 0;
                        int dz = 0;
                        switch (crossType) {
                           case XA -> dx = 30;
                           case XR -> dx = -30;
                           case ZA -> dz = 30;
                           case ZR -> dz = -30;
                        }

                        EasyLocation corridor = new EasyLocation(room.x() + dx, room.y(), room.z() + dz);
                        EasyLocation dest = new EasyLocation(room.x() + dx * 2, room.y(), room.z() + dz * 2);
                        if (!isMainRoomProtected(dest) && !rooms.contains(dest)) {
                           Location corridorLoc = new Location(world, corridor.x(), corridor.y(), corridor.z());
                           if (!isMainRoomCorridorAdjacent(corridorLoc)) {
                              CornerData.Corner corridorCorner = new CornerData.Corner(corridor.x(), corridor.y(), corridor.z(), corridor.x() + 29, corridor.y() + 5, corridor.z() + 29);
                              if (!data.corners.contains(corridorCorner)) {
                                 if (crossType != CrossStructureType.XA && crossType != CrossStructureType.XR) {
                                    recordCorner(data, corridorCorner);
                                    CROSS_ROOM_1_1.place(corridorLoc, false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                                 } else {
                                    recordCorner(data, corridorCorner);
                                    CROSS_ROOM_1_2.place(corridorLoc, false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                                 }

                                 recordCorner(data, corridorCorner);
                                 Location destLoc = new Location(world, dest.x(), dest.y(), dest.z());
                                 recordCorner(data, new CornerData.Corner(dest.x(), dest.y(), dest.z(), dest.x() + 29, dest.y() + 5, dest.z() + 29));
                                 BOSS_ROOM_1_1.place(destLoc, false, StructureRotation.NONE, Mirror.NONE, 0, 1.0F, rand);
                                 rooms.add(dest);
                                 boss_room = new EasyLocation(dest.x(), dest.y(), dest.z());
                                 recordCorner(data, new CornerData.Corner(dest.x(), dest.y(), dest.z(), dest.x() + 29, dest.y() + 5, dest.z() + 29));
                                 ++boss;
                                 continue label667;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
         BossRoom = new CornerData.Corner(boss_room.x(),boss_room.y(),boss_room.z(),boss_room.x()+29,boss_room.y()+5,boss_room.z()+29);
         for(EasyLocation location : rooms) {
            EasyLocation xamin = new EasyLocation(location.x() + 29, location.y() + 1, location.z() + 14);
            EasyLocation xamax = new EasyLocation(location.x() + 29, location.y() + 3, location.z() + 15);
            EasyLocation xrmin = new EasyLocation(location.x(), location.y() + 1, location.z() + 14);
            EasyLocation xrmax = new EasyLocation(location.x(), location.y() + 3, location.z() + 15);
            EasyLocation zamin = new EasyLocation(location.x() + 14, location.y() + 1, location.z() + 29);
            EasyLocation zamax = new EasyLocation(location.x() + 15, location.y() + 3, location.z() + 29);
            EasyLocation zrmin = new EasyLocation(location.x() + 14, location.y() + 1, location.z());
            EasyLocation zrmax = new EasyLocation(location.x() + 15, location.y() + 3, location.z());
            if (!data.corners.contains(new CornerData.Corner(location.x() + 30, location.y(), location.z(), location.x() + 59, location.y() + 5, location.z() + 29))) {
               for(int x = xamin.x(); x <= xamax.x(); ++x) {
                  for(int y = xamin.y(); y <= xamax.y(); ++y) {
                     for(int z = xamin.z(); z <= xamax.z(); ++z) {
                        world.getBlockAt(x, y, z).setType(Material.STONE_BRICKS, true);
                     }
                  }
               }
            } else if (!location.equals(boss_room)) {
               LiveCrossRoom.corners.add(new CornerData.Corner(xamin.x(), xamin.y(), xamin.z(), xamax.x(), xamax.y(), xamax.z()));
            } else {
               for(int x = xamin.x(); x <= xamax.x(); ++x) {
                  for(int y = xamin.y(); y <= xamax.y(); ++y) {
                     for(int z = xamin.z(); z <= xamax.z(); ++z) {
                        world.getBlockAt(x, y, z).setType(Material.DARK_OAK_FENCE, true);
                     }
                  }
               }
               BossRoom = new CornerData.Corner(xamin.x(),xamin.z(),xamin.y(),xamax.x(),xamax.y(), xamax.z());
               TextDisplay display = world.spawn(new Location(world, (float)(xamin.x() + xamax.x()) / 2.0F + 0.5F, (float)(xamin.y() + xamax.y()) / 2.0F, (float)(xamin.z() + xamax.z()) / 2.0F), TextDisplay.class);
               (new BossRoomLabelService(display, new CornerData.Corner(xamin.x(), xamin.y(), xamin.z(), xamax.x(), xamax.y(), xamax.z()))).runTaskTimer(PLUGIN, 0L, 1L);
            }

            if (!data.corners.contains(new CornerData.Corner(location.x() - 30, location.y(), location.z(), location.x() - 1, location.y() + 5, location.z() + 29))) {
               for(int x = xrmin.x(); x <= xrmax.x(); ++x) {
                  for(int y = xrmin.y(); y <= xrmax.y(); ++y) {
                     for(int z = xrmin.z(); z <= xrmax.z(); ++z) {
                        world.getBlockAt(x, y, z).setType(Material.STONE_BRICKS, true);
                     }
                  }
               }
            } else if (!location.equals(boss_room)) {
               LiveCrossRoom.corners.add(new CornerData.Corner(xrmin.x(), xrmin.y(), xrmin.z(), xrmax.x(), xrmax.y(), xrmax.z()));
            } else {
               for(int x = xrmin.x(); x <= xrmax.x(); ++x) {
                  for(int y = xrmin.y(); y <= xrmax.y(); ++y) {
                     for(int z = xrmin.z(); z <= xrmax.z(); ++z) {
                        world.getBlockAt(x, y, z).setType(Material.DARK_OAK_FENCE, true);
                     }
                  }
               }
               BossRoom = new CornerData.Corner(xrmin.x(),xrmin.z(),xrmin.y(),xrmax.x(),xrmax.y(), xrmax.z());
               TextDisplay display = world.spawn(new Location(world, (float)(xrmin.x() + xrmax.x()) / 2.0F - 0.5F, (float)(xrmin.y() + xrmax.y()) / 2.0F, (float)(xrmin.z() + xrmax.z()) / 2.0F), TextDisplay.class);
               (new BossRoomLabelService(display, new CornerData.Corner(xrmin.x(), xrmin.y(), xrmin.z(), xrmax.x(), xrmax.y(), xrmax.z()))).runTaskTimer(PLUGIN, 0L, 1L);
            }

            if (!data.corners.contains(new CornerData.Corner(location.x(), location.y(), location.z() + 30, location.x() + 29, location.y() + 5, location.z() + 59))) {
               for(int x = zamin.x(); x <= zamax.x(); ++x) {
                  for(int y = zamin.y(); y <= zamax.y(); ++y) {
                     for(int z = zamin.z(); z <= zamax.z(); ++z) {
                        world.getBlockAt(x, y, z).setType(Material.STONE_BRICKS, true);
                     }
                  }
               }
            } else if (!location.equals(boss_room)) {
               LiveCrossRoom.corners.add(new CornerData.Corner(zamin.x(), zamin.y(), zamin.z(), zamax.x(), zamax.y(), zamax.z()));
            } else {
               for(int x = zamin.x(); x <= zamax.x(); ++x) {
                  for(int y = zamin.y(); y <= zamax.y(); ++y) {
                     for(int z = zamin.z(); z <= zamax.z(); ++z) {
                        world.getBlockAt(x, y, z).setType(Material.DARK_OAK_FENCE, true);
                     }
                  }
               }
               BossRoom = new CornerData.Corner(zamin.x(),zamax.z(),zamin.y(),zamax.x(),zamax.y(), zamax.z());
               TextDisplay display = world.spawn(new Location(world, (float)(zamin.x() + zamax.x()) / 2.0F, (float)(zamin.y() + zamax.y()) / 2.0F, (float)(zamin.z() + zamax.z()) / 2.0F + 0.5F), TextDisplay.class);
               (new BossRoomLabelService(display, new CornerData.Corner(zamin.x(), zamin.y(), zamin.z(), zamax.x(), zamax.y(), zamax.z()))).runTaskTimer(PLUGIN, 0L, 1L);
            }

            if (!data.corners.contains(new CornerData.Corner(location.x(), location.y(), location.z() - 30, location.x() + 29, location.y() + 5, location.z() - 1))) {
               for(int x = zrmin.x(); x <= zrmax.x(); ++x) {
                  for(int y = zrmin.y(); y <= zrmax.y(); ++y) {
                     for(int z = zrmin.z(); z <= zrmax.z(); ++z) {
                        world.getBlockAt(x, y, z).setType(Material.STONE_BRICKS, true);
                     }
                  }
               }
            } else if (!location.equals(boss_room)) {
               LiveCrossRoom.corners.add(new CornerData.Corner(zrmin.x(), zrmin.y(), zrmin.z(), zrmax.x(), zrmax.y(), zrmax.z()));
            } else {
               for(int x = zrmin.x(); x <= zrmax.x(); ++x) {
                  for(int y = zrmin.y(); y <= zrmax.y(); ++y) {
                     for(int z = zrmin.z(); z <= zrmax.z(); ++z) {
                        world.getBlockAt(x, y, z).setType(Material.DARK_OAK_FENCE, true);
                     }
                  }
               }
               BossRoom = new CornerData.Corner(zrmin.x(),zrmax.z(),zrmin.y(),zrmax.x(),zrmax.y(), zrmax.z());
               TextDisplay display = world.spawn(new Location(world, (float)(zrmin.x() + zrmax.x()) / 2.0F, (float)(zrmin.y() + zrmax.y()) / 2.0F, (float)(zrmin.z() + zrmax.z()) / 2.0F - 0.5F), TextDisplay.class);
               (new BossRoomLabelService(display, new CornerData.Corner(zrmin.x(), zrmin.y(), zrmin.z(), zrmax.x(), zrmax.y(), zrmax.z()))).runTaskTimer(PLUGIN, 0L, 1L);
            }
         }

         try {
            storageManager.save(data);
         } catch (Exception e) {
            e.printStackTrace();
         }

         Title title1 = Title.title(Component.text("地图生成完成", NamedTextColor.GREEN), Component.text("第",NamedTextColor.YELLOW).append(Component.text(round,NamedTextColor.YELLOW).append(Component.text("层",NamedTextColor.YELLOW))), Times.times(Duration.ofMillis(500L), Duration.ofMillis(2000L), Duration.ofMillis(500L)));

         for(Player player : players) {
            game_players.add(player.getUniqueId());
            player.showTitle(title1);
         }
      }

   }

   public static void ShootLightningGun(Player p) {
      if (lightningGunServiceMap.containsKey(p.getUniqueId())) {
         PlayerLightningGunService service = lightningGunServiceMap.get(p.getUniqueId());
         int damage = (int)Math.ceil((double)100.0F + (double)400.0F * service.process);
         Vector core = p.getEyeLocation().clone().add(p.getEyeLocation().getDirection().normalize().multiply(0.7)).toVector();
         Vector dir = p.getEyeLocation().getDirection().normalize();
         Vector reference = Math.abs(dir.getY()) > 0.99 ? new Vector(1, 0, 0) : new Vector(0, 1, 0);
         Vector u = dir.clone().crossProduct(reference).normalize();
         Vector v = dir.clone().crossProduct(u).normalize();
         World world = p.getWorld();

         for(double x = 0.0F; x <= (double)6.0F; x += 0.2) {
            Vector offset1 = u.clone().multiply(0.8 * Math.sin(x)).add(v.clone().multiply(0.8 * Math.cos(x)));
            Vector p1 = core.clone().add(offset1).add(dir.clone().multiply(x));
            Vector offset2 = u.clone().multiply(0.8 * Math.sin(x + 2.0943951023931953)).add(v.clone().multiply(0.8 * Math.cos(x + 2.0943951023931953)));
            Vector p2 = core.clone().add(offset2).add(dir.clone().multiply(x));
            Vector offset3 = u.clone().multiply(0.8 * Math.sin(x + 4.1887902047863905)).add(v.clone().multiply(0.8 * Math.cos(x + 4.1887902047863905)));
            Vector p3 = core.clone().add(offset3).add(dir.clone().multiply(x));
            world.spawnParticle(Particle.DUST, p1.toLocation(world), 1, new Particle.DustOptions(typeColorMap.get(DamageType.LIGHTNING), 1.0F));
            world.spawnParticle(Particle.DUST, p2.toLocation(world), 1, new Particle.DustOptions(typeColorMap.get(DamageType.LIGHTNING), 1.0F));
            world.spawnParticle(Particle.DUST, p3.toLocation(world), 1, new Particle.DustOptions(typeColorMap.get(DamageType.LIGHTNING), 1.0F));
            Vector axisPoint = core.clone().add(dir.clone().multiply(x));
            world.spawnParticle(Particle.DUST, axisPoint.toLocation(world), 1, new Particle.DustOptions(typeColorMap.get(DamageType.LIGHTNING), 3.0F));
         }

         for(Entity entity : GetCyEntity(core, world, dir, 6.0F, 1.2)) {
            if (entity instanceof LivingEntity l) {
                if (l.getPersistentDataContainer().has(fromNamespaceKey("entity"))) {
                  Damage(l, p, damage, DamageType.LIGHTNING);
                  l.setVelocity(dir.clone().multiply(0.6).setY(0.3));
               }
            }

            if (entity.getPersistentDataContainer().has(fromNamespaceKey("grass"))) {
               Collection<Entity> entities1 = entity.getNearbyEntities(8.0F, 8.0F, 8.0F);
               LivingEntity goal = null;
               double min = Double.MAX_VALUE;

               for(Entity entity2 : entities1) {
                  if (entity2 instanceof LivingEntity ll) {
                      if (ll.getPersistentDataContainer().has(fromNamespaceKey("entity")) && ll.getLocation().distanceSquared(entity2.getLocation()) <= min) {
                        min = ll.getLocation().distanceSquared(entity2.getLocation());
                        goal = ll;
                     }
                  }
               }

               if (goal != null) {
                  Integer d = entity.getPersistentDataContainer().get(fromNamespaceKey("grass"), PersistentDataType.INTEGER);
                  if (d != null) {
                     (new SeedLightningTickTimer(entity.getLocation(), goal, goal.getWorld(), d)).runTaskTimer(PLUGIN, 0L, 1L);
                  }
               }

               fillCubeWithParticles(entity.getWorld(), entity.getLocation(), 0.8, 10, (new Particle.DustOptions(Color.GREEN, 3.0F)));
               entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_GRASS_BREAK, 1.0F, 1.0F);
               entity.remove();
            }
         }

      }
   }

   public static List<Entity> GetCyEntity(Vector core, World world, Vector dire, double length, double r) {
      Vector axis = dire.clone().normalize();
      double boxR = Math.sqrt(r * r + length / (double)2.0F * (length / (double)2.0F));
      Location boxC = core.clone().add(axis.clone().multiply(length / (double)2.0F)).toLocation(world);
      Collection<Entity> ans = world.getNearbyEntities(boxC, boxR, boxR, boxR);
      List<Entity> result = new ArrayList<>();

      for(Entity entity : ans) {
         Location center = entity.getBoundingBox().getCenter().toLocation(world);
         Vector vel = center.toVector().subtract(core);
         double along = vel.dot(axis);
         if (!(along < (double)0.0F) && !(along > length)) {
            double rq = vel.lengthSquared() - along * along;
            if (rq <= r * r) {
               result.add(entity);
            }
         }
      }

      return result;
   }

   public static List<ItemStack> fromEquipment(Material... m) {
      List<ItemStack> ans = new ArrayList<>();

      for(int i = 0; i < m.length; ++i) {
         ItemStack stack = new ItemStack(m[i]);
         ItemMeta meta = stack.getItemMeta();
         if (meta != null) {
            meta.setUnbreakable(true);
            stack.setItemMeta(meta);
         }

         ans.add(stack);
      }

      return ans;
   }

   public static Mob SummonBoss(int id, Location loc) {
      switch (id) {
         case 1->{
            World world = loc.getWorld();
            Zombie zombie = world.spawn(loc, Zombie.class);
            zombie.getPersistentDataContainer().set(fromNamespaceKey("health"), PersistentDataType.INTEGER, 14000);
            zombie.getPersistentDataContainer().set(fromNamespaceKey("entity"), PersistentDataType.BOOLEAN, true);
            zombie.getPersistentDataContainer().set(fromNamespaceKey("boss"), PersistentDataType.BOOLEAN, true);
            EntityEquipment equipment = zombie.getEquipment();
            List<ItemStack> stacks = fromEquipment(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.DIAMOND_SWORD);
            equipment.setHelmet(stacks.get(0));
            equipment.setChestplate(stacks.get(1));
            equipment.setLeggings(stacks.get(2));
            equipment.setBoots(stacks.get(3));
            equipment.setItemInMainHand(stacks.get(4));
            equipment.setHelmetDropChance(0.0F);
            equipment.setChestplateDropChance(0.0F);
            equipment.setLeggingsDropChance(0.0F);
            equipment.setBootsDropChance(0.0F);
            equipment.setItemInMainHandDropChance(0.0F);
            (Objects.requireNonNull(zombie.getAttribute(Attribute.MOVEMENT_SPEED))).setBaseValue(0.35);
            ZombieKingService service = new ZombieKingService(zombie);
            zombie.setCustomNameVisible(true);
            service.runTaskTimer(PLUGIN, 0L, 1L);
            return zombie;
         }


         default -> {return null;}



      }
   }

   public static void BuildStore(boolean isLimited, ItemStack out, int iron, int gold, Location loc) {
      World world = loc.getWorld();
      Interaction interaction = world.spawn(loc, Interaction.class, (e) -> {
         e.setInteractionWidth(1.2F);
         e.setInteractionHeight(2.0F);
         e.setResponsive(true);
         e.setGravity(false);
         e.setInvulnerable(true);
         e.setPersistent(true);
      });
      ItemDisplay itemDisplay = world.spawn(loc, ItemDisplay.class, (e) -> {
         e.setItemStack(out);
         e.setBillboard(Billboard.FIXED);
         e.setGravity(false);
         e.setInvulnerable(true);
         e.setPersistent(true);
      });
      Transformation transformation = itemDisplay.getTransformation();
      transformation.getScale().set(0.5F, 0.5F, (double)0.5F);
      transformation.getLeftRotation().rotateAxis(((float)Math.PI / 2F), new Vector3f(0.0F, 1.0F, 0.0F));
      itemDisplay.setTransformation(transformation);
      out.displayName();
      Component name = out.displayName();
      Component text = name.appendNewline().append(Component.text(iron + " 铁", NamedTextColor.WHITE)).appendNewline().append(Component.text(gold + " 金", NamedTextColor.GOLD)).appendNewline().append(isLimited ? Component.text("仅限购买一次", NamedTextColor.RED) : Component.text("该商店可无限购买", NamedTextColor.GREEN));
      TextDisplay textDisplay = world.spawn(loc.clone().add(0.0F, 0.8, 0.0F), TextDisplay.class, (e) -> {
         e.text(text);
         e.setBillboard(Billboard.CENTER);
         e.setLineWidth(200);
         e.setShadowed(true);
         e.setSeeThrough(false);
         e.setDefaultBackground(false);
         e.setGravity(false);
         e.setInvulnerable(true);
         e.setPersistent(true);
      });
      PersistentDataContainer pdc = interaction.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("shop"), PersistentDataType.BOOLEAN, true);
      pdc.set(fromNamespaceKey("limited"), PersistentDataType.BOOLEAN, isLimited);
      pdc.set(fromNamespaceKey("out"), DataType.ITEM_STACK, out);
      pdc.set(fromNamespaceKey("iron"), PersistentDataType.INTEGER, iron);
      pdc.set(fromNamespaceKey("gold"), PersistentDataType.INTEGER, gold);
      pdc.set(fromNamespaceKey("shop_item"), PersistentDataType.STRING, itemDisplay.getUniqueId().toString());
      pdc.set(fromNamespaceKey("shop_text"), PersistentDataType.STRING, textDisplay.getUniqueId().toString());
   }

   public static boolean CheckItem(Player p, Material m, int count) {
      PlayerInventory inv = p.getInventory();
      Iterator<ItemStack> it = inv.iterator();

      while(it.hasNext()) {
         ItemStack stack = it.next();
         if (stack != null && stack.getType() == m) {
            count -= stack.getAmount();
         }

         if (count <= 0) {
            return true;
         }
      }

      return false;
   }

   public static void ClearItem(Player p, Material m, int count) {
      PlayerInventory inv = p.getInventory();

      for(int slot = 0; slot < inv.getSize(); ++slot) {
         ItemStack stack = inv.getItem(slot);
         if (stack != null && stack.getType() == m) {
            if (stack.getAmount() <= count) {
               inv.setItem(slot, new ItemStack(Material.AIR));
               count -= stack.getAmount();
            } else if (stack.getAmount() > count) {
               stack.setAmount(stack.getAmount() - count);
               return;
            }
         }
      }

   }

   public static void SpawnActivity(int id, World world, Location loc) {
      switch (id) {
         case 1:
            ArmorStand armorStand = world.spawn(loc, ArmorStand.class);
            EntityEquipment equipment = armorStand.getEquipment();
            equipment.setHelmet(fromEquipment(Material.WITHER_SKELETON_SKULL).getFirst());
            equipment.setChestplate(fromEquipment(Material.LEATHER_CHESTPLATE).getFirst());
            equipment.setLeggings(fromEquipment(Material.LEATHER_LEGGINGS).getFirst());
            equipment.setBoots(fromEquipment(Material.LEATHER_BOOTS).getFirst());
            armorStand.setArms(true);
            armorStand.setInvulnerable(true);
            armorStand.setGravity(false);
            armorStand.setAI(false);
            armorStand.setCustomNameVisible(true);
            armorStand.customName(Component.text("嗜血恶魔", NamedTextColor.GOLD));
            PersistentDataContainer pdc = armorStand.getPersistentDataContainer();
            pdc.set(fromNamespaceKey("limited"), PersistentDataType.BOOLEAN, true);

            UUID uuid;
            do {
               uuid = UUID.randomUUID();
            } while(requestMap.containsKey(uuid));

            pdc.set(fromNamespaceKey("uuid"), PersistentDataType.STRING, uuid.toString());
            requestMap.put(uuid, new LiveShop());
            break;
         default:
            break;
      }
   }

   static {
      DefaultGun = new ItemStack(Material.GOLDEN_HOE);
      WindSword = new ItemStack(Material.DIAMOND_SWORD);
      StoneBook = new ItemStack(Material.BOOK);
      LightningGun = new ItemStack(Material.DIAMOND_HOE);
      GrassSword = new ItemStack(Material.WOODEN_SWORD);
      WaterBook = new ItemStack(Material.BOOK);
      FireBook = new ItemStack(Material.BOOK);
      Null = new ItemStack(Material.BARRIER);
      IceBow = new ItemStack(Material.BOW);
      IceBowArrow = new ItemStack(Material.ARROW);
      PhysicSword = new ItemStack(Material.IRON_SWORD);
      GodSpawner = new ItemStack(Material.COMMAND_BLOCK);
      ElementStone = new ItemStack(Material.AMETHYST_SHARD);
      Iron = new ItemStack(Material.IRON_INGOT);
      Gold = new ItemStack(Material.GOLD_INGOT);
      Compass = new ItemStack(Material.COMPASS);
      itemPool = new HashMap<>();
      Shoot_timer = new HashMap<>();
      typeMap = new HashMap<>();
      entity_defence = new HashMap<>();
      entity_type = new HashMap<>();
      element_type = new HashMap<>();
      freeze_tick = new HashMap<>();
      grass_tick = new HashMap<>();
      grass_burn = new HashMap<>();
      physic_attack = new HashMap<>();
      defence = new HashMap<>();
      grass_sword_skill_tick = new HashMap<>();
      gunServiceMap = new HashMap<>();
      rockDefenceTickTimerMap = new HashMap<>();
      skillServiceMap = new HashMap<>();
      lightningGunServiceMap = new HashMap<>();
      fireBookSkillTick = new HashMap<>();
      book_tick = new HashMap<>();
      IceBowSkillTick = new HashMap<>();
      max_item_id = 0;
      game_players = new ArrayList<>();
      CROSS_ROOM_1_1 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("cross_north"));
      CROSS_ROOM_1_2 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("cross_east"));
      ACTIVITY_ROOM_1_1 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("activite_room"));
      BOSS_ROOM_1_1 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("boss_room"));
      START_ROOM_1_1 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("start_room"));
      CENTER_ROOM_1_1 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("center_room"));
      CENTER_ROOM_1_2 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("center_room1"));
      SHOP_ROOM_1_1 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("shop_room"));
      Box_ROOM_1_1 = Bukkit.getStructureManager().loadStructure(NamespacedKey.minecraft("box_room"));
      storageManager = new StorageManager("map.dat");
      EntityRoomData = new CornerData();
      sword_tick = new HashMap<>();
      LiveCrossRoom = new CornerData();
      requestMap = new HashMap<>();
      scoreboardMap = new HashMap<>();
      modifyMap = new HashMap<>();
      modified = new HashSet<>();
      died_players = new HashSet<>();
      un_relief_players = new HashMap<>();
      typeColorMap = new HashMap<>() {
         {
            this.put(DamageType.PHYSIC, Color.fromARGB(255, 255, 255, 255));
            this.put(DamageType.AIR, Color.fromRGB(43690));
            this.put(DamageType.WATER, Color.fromARGB(255, 74, 74, 255));
            this.put(DamageType.ICE, Color.fromARGB(255, 0, 255, 255));
            this.put(DamageType.GRASS, Color.fromARGB(255, 0, 255, 70));
            this.put(DamageType.FIRE, Color.fromARGB(255, 255, 100, 0));
            this.put(DamageType.ROCK, Color.fromRGB(16777045));
            this.put(DamageType.LIGHTNING, Color.fromARGB(255, 200, 0, 255));
            this.put(DamageType.EXPLODE, Color.fromRGB(16733695));
         }
      };
      typeNameMap = new HashMap<>() {
         {
            this.put(DamageType.PHYSIC, "物理");
            this.put(DamageType.AIR, "空气");
            this.put(DamageType.WATER, "水");
            this.put(DamageType.ICE, "冰");
            this.put(DamageType.GRASS, "草");
            this.put(DamageType.FIRE, "火");
            this.put(DamageType.ROCK, "岩");
            this.put(DamageType.LIGHTNING, "雷电");
         }
      };
      ShopData = new HashMap<>();
      ItemMeta meta = DefaultGun.getItemMeta();

      assert meta != null;

      meta.setUnbreakable(true);
      PersistentDataContainer pdc = meta.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("normal_gun"), PersistentDataType.BOOLEAN, true);
      pdc.set(fromNamespaceKey("element"), PersistentDataType.STRING, "PHYSIC");
      pdc.set(fromNamespaceKey("count"), PersistentDataType.INTEGER, 10);
      pdc.set(fromNamespaceKey("max_count"), PersistentDataType.INTEGER, 10);
      pdc.set(fromNamespaceKey("load_tick"), PersistentDataType.INTEGER, 100);
      Component ss = Component.text("剩余弹药：", NamedTextColor.YELLOW).append(Component.text("10/10", NamedTextColor.GREEN));
      Component name = Component.text("普通枪", NamedTextColor.WHITE);
      meta.displayName(name);
      meta.lore(List.of(Component.text("只是一把再普通不过的枪，射击敌人造成80伤害", NamedTextColor.YELLOW), Component.text("元素：", NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.PHYSIC), NamedTextColor.nearestTo(TextColor.color((typeColorMap.get(DamageType.PHYSIC)).asRGB())))), ss, Component.text("左键换弹", NamedTextColor.YELLOW)));
      DefaultGun.setItemMeta(meta);
      meta = WindSword.getItemMeta();

      assert meta != null;

      meta.setUnbreakable(true);
      meta.addEnchant(Enchantment.SHARPNESS, 1, true);
      pdc = meta.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("wind_sword"), PersistentDataType.BOOLEAN, true);
      pdc.set(fromNamespaceKey("element"), PersistentDataType.STRING, "AIR");
      pdc.set(fromNamespaceKey("sword"), PersistentDataType.INTEGER, 160);
      pdc.set(fromNamespaceKey("skill"), PersistentDataType.INTEGER, 600);
      name = Component.text("狂风怒号之刃", NamedTextColor.GOLD);
      meta.displayName(name);
      meta.lore(List.of(Component.text("一把汲取了浓烈空气元素力量的剑，见证了风在星球上的千年历史，攻击敌人造成160点伤害", NamedTextColor.YELLOW), Component.text("元素：", NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.AIR), NamedTextColor.nearestTo(TextColor.color((typeColorMap.get(DamageType.AIR)).asRGB())))), Component.text("按住Shift+右键可以释放这把剑的技能", NamedTextColor.YELLOW), Component.text("技能：在上方生成一个持续10秒的风洞，风洞会不断吸收周围5格内的敌人，并对周围2格内的敌人每秒造成2次40点空气元素伤害", NamedTextColor.YELLOW)));
      WindSword.setItemMeta(meta);
      meta = StoneBook.getItemMeta();

      assert meta != null;

      meta.setUnbreakable(true);
      meta.addEnchant(Enchantment.SHARPNESS, 1, true);
      pdc = meta.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("stone_book"), PersistentDataType.BOOLEAN, true);
      pdc.set(fromNamespaceKey("element"), PersistentDataType.STRING, "ROCK");
      pdc.set(fromNamespaceKey("skill"), PersistentDataType.INTEGER, 300);
      name = Component.text("粉骨碎身之法", NamedTextColor.GOLD);
      meta.displayName(name);
      meta.lore(List.of(Component.text("一本上面写着《岩国通史》的书，后来因为其拥有者得到了岩的祝福而变成了强力的法器，右键向前投出一块石头，击中敌人造成80点岩元素伤害", NamedTextColor.YELLOW), Component.text("元素：", NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.ROCK), NamedTextColor.nearestTo(TextColor.color((typeColorMap.get(DamageType.ROCK)).asRGB())))), Component.text("按住Shift+右键可以释放这本法器的技能", NamedTextColor.YELLOW), Component.text("技能：从上方砸下一块石头，落地时给周围3*3范围内的敌人造成700岩元素伤害，落下的石头在接下来8秒内进行4次岩共鸣，每次共鸣对周围4格范围内的敌人造成100点岩元素伤害", NamedTextColor.YELLOW)));
      StoneBook.setItemMeta(meta);
      meta = Iron.getItemMeta();

      assert meta != null;

      name = Component.text("铁", NamedTextColor.WHITE);
      meta.displayName(name);
      meta.lore(List.of(Component.text("一种货币，可以在商店中购买物品", NamedTextColor.YELLOW)));
      Iron.setItemMeta(meta);
      meta = Gold.getItemMeta();

      assert meta != null;

      name = Component.text("金", NamedTextColor.BLUE);
      meta.displayName(name);
      meta.lore(List.of(Component.text("一种货币，可以在商店中购买物品", NamedTextColor.YELLOW)));
      Gold.setItemMeta(meta);
      meta = LightningGun.getItemMeta();

      assert meta != null;

      meta.setUnbreakable(true);
      meta.addEnchant(Enchantment.SHARPNESS, 1, true);
      pdc = meta.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("lightning_gun"), PersistentDataType.BOOLEAN, true);
      pdc.set(fromNamespaceKey("element"), PersistentDataType.STRING, "LIGHTNING");
      pdc.set(fromNamespaceKey("count"), PersistentDataType.INTEGER, 1);
      pdc.set(fromNamespaceKey("max_count"), PersistentDataType.INTEGER, 1);
      pdc.set(fromNamespaceKey("load_tick"), PersistentDataType.INTEGER, 60);
      pdc.set(fromNamespaceKey("skill"), PersistentDataType.INTEGER, 1200);
      ss = Component.text("剩余弹药：", NamedTextColor.YELLOW).append(Component.text("1/1", NamedTextColor.GREEN));
      name = Component.text("雷光展露之炮", NamedTextColor.GOLD);
      meta.displayName(name);
      meta.lore(List.of(Component.text("大人，时代变了，当该武器弹药不为0时手持该武器可以蓄力，蓄力越久伤害越高，最高可达500点", NamedTextColor.YELLOW), Component.text("元素：", NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.LIGHTNING), NamedTextColor.nearestTo(TextColor.color((typeColorMap.get(DamageType.LIGHTNING)).asRGB())))), ss, Component.text("左键换弹", NamedTextColor.YELLOW), Component.text("按住Shift+右键可以释放这门炮的技能", NamedTextColor.YELLOW), Component.text("技能：向前挥出5格宽的雷元素剑气，可穿透墙壁和敌人，造成1200点雷元素伤害", NamedTextColor.YELLOW)));
      LightningGun.setItemMeta(meta);
      meta = GrassSword.getItemMeta();

      assert meta != null;

      meta.setUnbreakable(true);
      meta.addEnchant(Enchantment.SHARPNESS, 1, true);
      pdc = meta.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("grass_sword"), PersistentDataType.BOOLEAN, true);
      pdc.set(fromNamespaceKey("element"), PersistentDataType.STRING, "GRASS");
      pdc.set(fromNamespaceKey("sword"), PersistentDataType.INTEGER, 220);
      pdc.set(fromNamespaceKey("skill"), PersistentDataType.INTEGER, 400);
      name = Component.text("蔓草丛生之剑", NamedTextColor.GOLD);
      meta.displayName(name);
      meta.lore(List.of(Component.text("草木哺育生命，也能扼杀生命，攻击造成220点草元素伤害", NamedTextColor.YELLOW), Component.text("元素：", NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.GRASS), NamedTextColor.nearestTo(TextColor.color((typeColorMap.get(DamageType.GRASS)).asRGB())))), Component.text("按住Shift+右键可以释放这把剑的技能", NamedTextColor.YELLOW), Component.text("技能：在附近4*4的敌人的位置生成藤蔓使其无法动弹，并造成400点草元素伤害，降低240点草元素抗性", NamedTextColor.YELLOW)));
      GrassSword.setItemMeta(meta);
      meta = WaterBook.getItemMeta();

      assert meta != null;

      meta.setUnbreakable(true);
      meta.addEnchant(Enchantment.SHARPNESS, 1, true);
      pdc = meta.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("water_book"), PersistentDataType.BOOLEAN, true);
      pdc.set(fromNamespaceKey("element"), PersistentDataType.STRING, "WATER");
      pdc.set(fromNamespaceKey("skill"), PersistentDataType.INTEGER, 400);
      name = Component.text("静水翻涌之典", NamedTextColor.GOLD);
      meta.displayName(name);
      meta.lore(List.of(Component.text("水是生命之源，但愿是。右键投出一瓶水，落地产生一滩能对敌人持续每秒造成65点水元素伤害的水，持续3秒", NamedTextColor.YELLOW), Component.text("元素：", NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.WATER), NamedTextColor.nearestTo(TextColor.color((typeColorMap.get(DamageType.WATER)).asRGB())))), Component.text("按住Shift+右键可以释放这本法器的技能", NamedTextColor.YELLOW), Component.text("技能：在周围形成一个持续10秒的水圈，在水圈内部的敌人会每秒受到2次120点的水元素伤害，并被排开，每有一个敌人受到来自水圈的伤害，你都有40%的概率恢复1HP", NamedTextColor.YELLOW)));
      WaterBook.setItemMeta(meta);
      meta = FireBook.getItemMeta();

      assert meta != null;

      meta.setUnbreakable(true);
      meta.addEnchant(Enchantment.SHARPNESS, 1, true);
      pdc = meta.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("fire_book"), PersistentDataType.BOOLEAN, true);
      pdc.set(fromNamespaceKey("element"), PersistentDataType.STRING, "FIRE");
      pdc.set(fromNamespaceKey("skill"), PersistentDataType.INTEGER, 800);
      name = Component.text("野火焚尽之契", NamedTextColor.GOLD);
      meta.displayName(name);
      meta.lore(List.of(Component.text("如果一切都为时已晚，便让火来重置世间一切。右键发射一枚火球，击中敌人造成70点火元素伤害", NamedTextColor.YELLOW), Component.text("元素：", NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.FIRE), NamedTextColor.nearestTo(TextColor.color((typeColorMap.get(DamageType.FIRE)).asRGB())))), Component.text("按住Shift+右键可以释放这本法器的技能", NamedTextColor.YELLOW), Component.text("技能：接下来的10秒内，所有普通攻击都会向四周发射发射火球，并获得1000抗性和速度II效果", NamedTextColor.YELLOW)));
      FireBook.setItemMeta(meta);
      meta = Null.getItemMeta();

      assert meta != null;

      meta.setUnbreakable(true);
      meta.addEnchant(Enchantment.SHARPNESS, 1, true);
      pdc = meta.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("null"), PersistentDataType.BOOLEAN, true);
      name = Component.text("滚木", NamedTextColor.LIGHT_PURPLE);
      meta.displayName(name);
      meta.lore(List.of(Component.text("很遗憾，这个箱子里没有任何东西，但又话说回来，极致的非酋有何尝不是另一种意义上的欧皇呢", NamedTextColor.YELLOW), Component.text("一次性用品，攻击任意一个非Boss敌人可以将其抹杀", NamedTextColor.YELLOW)));
      Null.setItemMeta(meta);
      meta = GodSpawner.getItemMeta();

      assert meta != null;

      meta.setUnbreakable(true);
      meta.addEnchant(Enchantment.SHARPNESS, 1, true);
      pdc = meta.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("god_spawner"), PersistentDataType.BOOLEAN, true);
      name = Component.text("神临", NamedTextColor.GOLD);
      meta.displayName(name);
      meta.lore(List.of(Component.text("神的降临从来不需要理由", NamedTextColor.YELLOW), Component.text("一次性用品，召唤一个400血的僵尸为你而战，僵尸穿戴下界合金套装并备有锋利V的下界合金剑，每一秒损失5点生命值", NamedTextColor.YELLOW)));
      GodSpawner.setItemMeta(meta);
      meta = IceBow.getItemMeta();

      assert meta != null;

      meta.setUnbreakable(true);
      meta.addEnchant(Enchantment.INFINITY, 1, true);
      pdc = meta.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("ice_bow"), PersistentDataType.BOOLEAN, true);
      pdc.set(fromNamespaceKey("element"), PersistentDataType.STRING, "ICE");
      pdc.set(fromNamespaceKey("skill"), PersistentDataType.INTEGER, 1000);
      name = Component.text("雪国冰霜之弓", NamedTextColor.GOLD);
      meta.displayName(name);
      meta.lore(List.of(Component.text("寒风呼啸而过，雪中打猎的猎人已经拉满的弦。射出箭矢对敌人造成300冰元素伤害", NamedTextColor.YELLOW), Component.text("元素：", NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.ICE), NamedTextColor.nearestTo(TextColor.color((typeColorMap.get(DamageType.ICE)).asRGB())))), Component.text("按住Shift+右键可以释放这把弓的技能", NamedTextColor.YELLOW), Component.text("技能：在接下来的5秒内，无需拉弓，只需要右键就能射出一发箭，每发箭造成150点冰元素伤害（没有击退）", NamedTextColor.YELLOW)));
      IceBow.setItemMeta(meta);
      meta = IceBowArrow.getItemMeta();

      assert meta != null;

      pdc = meta.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("ice_bow_arrow"), PersistentDataType.BOOLEAN, true);
      meta.displayName(Component.text("箭", NamedTextColor.WHITE));
      meta.lore(List.of(Component.text("与弓绑定的箭矢", NamedTextColor.YELLOW), Component.text("射出的箭无法拾取，自身也不会被消耗", NamedTextColor.YELLOW)));
      meta.setEnchantmentGlintOverride(true);
      IceBowArrow.setItemMeta(meta);
      meta = ElementStone.getItemMeta();

      assert meta != null;

      meta.setUnbreakable(true);
      meta.addEnchant(Enchantment.SHARPNESS, 1, true);
      pdc = meta.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("element_stone"), PersistentDataType.BOOLEAN, true);
      pdc.set(fromNamespaceKey("random_element"), PersistentDataType.BOOLEAN, true);
      pdc.set(fromNamespaceKey("no_element"), PersistentDataType.BOOLEAN, true);
      name = Component.text("元素晶石", NamedTextColor.BLUE);
      meta.displayName(name);
      meta.lore(List.of(Component.text("一个汲取了元素力精华的晶石，可以用来强化武器", NamedTextColor.YELLOW), Component.text("消耗品，将要进行元素附魔的武器放置在副手，右键此道具可以对其进行元素附魔", NamedTextColor.YELLOW)));
      ElementStone.setItemMeta(meta);
      meta = PhysicSword.getItemMeta();

      assert meta != null;

      meta.setUnbreakable(true);
      meta.addEnchant(Enchantment.SHARPNESS, 1, true);
      pdc = meta.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("physic_sword"), PersistentDataType.BOOLEAN, true);
      pdc.set(fromNamespaceKey("element"), PersistentDataType.STRING, "PHYSIC");
      pdc.set(fromNamespaceKey("sword"), PersistentDataType.INTEGER, 280);
      pdc.set(fromNamespaceKey("skill"), PersistentDataType.INTEGER, 1000);
      pdc.set(fromNamespaceKey("no_element"), PersistentDataType.BOOLEAN, true);
      name = Component.text("遗愿终成之刀", NamedTextColor.GOLD);
      meta.displayName(name);
      meta.lore(List.of(Component.text("得到元素祝福的愿望终会达成，一定会的。攻击造成280点物理伤害", NamedTextColor.YELLOW), Component.text("元素：", NamedTextColor.YELLOW).append(Component.text(typeNameMap.get(DamageType.PHYSIC), NamedTextColor.nearestTo(TextColor.color((typeColorMap.get(DamageType.PHYSIC)).asRGB())))), Component.text("按住Shift+右键可以释放这把剑的技能", NamedTextColor.YELLOW), Component.text("技能：在上方生成一把铁剑，1秒后落下，落地产生冲击波，造成1500点物理伤害", NamedTextColor.YELLOW), Component.text("该武器无法被元素晶石强化", NamedTextColor.RED)));
      PhysicSword.setItemMeta(meta);
      meta = Compass.getItemMeta();
      assert meta != null;
      pdc = meta.getPersistentDataContainer();
      pdc.set(fromNamespaceKey("compass"),PersistentDataType.BOOLEAN,true);
      name = Component.text("地牢指南针",NamedTextColor.WHITE);
      meta.displayName(name);
      meta.lore(List.of(Component.text("指向最近的刷怪房或Boss房，相信我，没有这个你会迷路的",NamedTextColor.YELLOW)));

      Compass.setItemMeta(meta);

      itemPool.put(DefaultGun, 0.9F);
      itemPool.put(ElementStone, 0.65F);
      itemPool.put(WindSword, 0.015F);
      itemPool.put(StoneBook, 0.015F);
      itemPool.put(GrassSword, 0.015F);
      itemPool.put(LightningGun, 0.015F);
      itemPool.put(WaterBook, 0.015F);
      itemPool.put(FireBook, 0.015F);
      itemPool.put(IceBow, 0.015F);
      itemPool.put(PhysicSword, 0.015F);
      itemPool.put(GodSpawner, 0.015F);
      ShopData.put(DefaultGun, new CostData(25, 0));
      ShopData.put(WindSword, new CostData(36, 20));
      ShopData.put(StoneBook, new CostData(36, 20));
      ShopData.put(GrassSword, new CostData(36, 20));
      ShopData.put(LightningGun, new CostData(36, 20));
      ShopData.put(WaterBook, new CostData(36, 20));
      ShopData.put(FireBook, new CostData(36, 20));
      ShopData.put(IceBow, new CostData(36, 20));
      ShopData.put(PhysicSword, new CostData(36, 20));
      ShopData.put(GodSpawner, new CostData(24, 15));
      ShopData.put(Gold, new CostData(20, 0));
   }
   public static void Start(){
      ToolClass.gaming = true;
      died_players = new HashSet<>();

      un_relief_players = new HashMap<>();
      for(Entity entity : Bukkit.selectEntities(Bukkit.getConsoleSender(),"@e[type=!minecraft:player]")){
         entity.remove();
      }
      List<Player> players = new ArrayList<>();
      for(UUID uuid : game_players) {
         Player player = Bukkit.getPlayer(uuid);
         if(player!=null){
            players.add(player);
         }
      }
      ToolClass.requestMap.clear();
      GenerateMap(players,1);
      for(Player player : players)
      {
         player.teleport(new Location(player.getWorld(),14,101,14));
         player.getInventory().addItem(fromItem(DefaultGun));
         player.getInventory().addItem(fromItem(Compass));
      }
   }
   public static void NextRound(){
      ToolClass.round++;
      for(Entity entity : Bukkit.selectEntities(Bukkit.getConsoleSender(),"@e[type=!minecraft:player]")){
         entity.remove();
      }
      List<Player> players = new ArrayList<>();
      for(UUID uuid : game_players) {
         Player player = Bukkit.getPlayer(uuid);
         if(player!=null){
            players.add(player);
         }
      }
      ToolClass.requestMap.clear();
      GenerateMap(players,1);
      for(Player player : players){
         player.teleport(new Location(player.getWorld(),14,101,14));
      }
   }
   public static void Damage(LivingEntity goal,Player source,int damage,DamageType t){
      PersistentDataContainer pdc = source.getPersistentDataContainer();
      Float lv = pdc.get(fromNamespaceKey("lv"),PersistentDataType.FLOAT);
      Float la = pdc.get(fromNamespaceKey("la"),PersistentDataType.FLOAT);
      if(lv != null && la != null){
         double r = Math.random();
         if(r<lv/100f){
            damage = (int)Math.ceil(damage * (1f+la/100f));
            ToolClass.typeMap.put(goal.getUniqueId(),t);
            goal.damage(damage);
            SpawnTextEntity(Component.text("暴击",NamedTextColor.DARK_RED), goal.getWorld(), goal.getLocation().clone().add(0,2.5,0));
         }else{
            ToolClass.typeMap.put(goal.getUniqueId(),t);
            goal.damage(damage);
         }
      }
   }
   public static void reset(Player player){
      for (Attribute attribute : Registry.ATTRIBUTE) {
         AttributeInstance instance = player.getAttribute(attribute);
         if (instance == null) continue;

         for (AttributeModifier modifier : instance.getModifiers()) {
            instance.removeModifier(modifier);
         }

         instance.setBaseValue(attribute.getDefaultValue());
      }

      AttributeInstance speed = player.getAttribute(Attribute.MOVEMENT_SPEED);
      if (speed != null) {
         speed.setBaseValue(0.1);
      }
      defence.put(player.getUniqueId(),0);
      player.getPersistentDataContainer().set(fromNamespaceKey("lv"),PersistentDataType.FLOAT,5f);
      player.getPersistentDataContainer().set(fromNamespaceKey("la"),PersistentDataType.FLOAT,50f);
   }
}
