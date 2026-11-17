package plugin.xiguananshan.mCDungeon.ticks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.data.CornerData;

import java.time.Duration;
import java.util.*;

public class EntityRoomCheckService extends BukkitRunnable {
    private int spawnCount;
    private final CornerData.Corner room;
    public int ticks;
    public int next_spawn;
    public final World world;
    public final List<Class<? extends Mob>> spawn_types;
    public final HashSet<UUID> uuids;
    public EntityRoomCheckService(CornerData.Corner corner, int spawnCount, World world, List<Class<? extends Mob>> spawn_types){
        room = corner;
        this.spawnCount = spawnCount;
        this.ticks = 0;
        Random random = new Random();
        int max,min;
        max = (int)Math.ceil(100*1f/(1+ToolClass.round) + 5);
        min = (int)Math.ceil(40*1f/(1+ToolClass.round) + 5);
        this.next_spawn = random.nextInt(max-min+1)+min;
        this.world = world;
        this.spawn_types = spawn_types;
        this.uuids = new HashSet<>();
        setAllDoors(Material.OAK_FENCE,world);
    }

    @Override
    public void run() {
        if(spawnCount > 0){
            ticks++;
            if(ticks>=next_spawn){
                ticks = 0;
                Random random = new Random();
                int max,min;
                max = (int)Math.ceil(200*1f/(1+ToolClass.round) + 5);
                min = (int)Math.ceil(80*1f/(1+ToolClass.round) + 5);
                next_spawn = random.nextInt(max-min+1)+min;
                spawnCount--;
                Class<? extends  Mob> type = spawn_types.get(random.nextInt(spawn_types.size()));
                Location loc = findSafeLocation(world, room, random);
                if(loc == null){
                    return;
                }
                if(!type.equals(Zombie.class) && !type.equals(Skeleton.class)){
                    UUID uuid;
                    uuid = ToolClass.SummonEntity(world,loc,true,(int)Math.ceil((random.nextInt(601)+200)* 0.5 *Math.sqrt(ToolClass.round+1)),type).getUniqueId();
                    uuids.add(uuid);
                    world.playSound(loc, Sound.BLOCK_ROOTED_DIRT_BREAK,3,1);
                    world.spawnParticle(Particle.BLOCK,loc,20,Bukkit.createBlockData(Material.ROOTED_DIRT));
                }else{
                    UUID uuid;
                    HashMap<Material,Integer> helment_map = new HashMap<>(){{
                        put(Material.LEATHER_HELMET,50);
                        put(Material.GOLDEN_HELMET,80);
                        put(Material.CHAINMAIL_HELMET,100);
                        put(Material.IRON_HELMET,200);
                        put(Material.DIAMOND_HELMET,300);
                        put(Material.NETHERITE_HELMET,500);
                    }};
                    HashMap<Material,Integer> chest_map = new HashMap<>(){{
                        put(Material.LEATHER_CHESTPLATE,50);
                        put(Material.GOLDEN_CHESTPLATE,80);
                        put(Material.CHAINMAIL_CHESTPLATE,100);
                        put(Material.IRON_CHESTPLATE,200);
                        put(Material.DIAMOND_CHESTPLATE,300);
                        put(Material.NETHERITE_CHESTPLATE,500);
                    }};
                    HashMap<Material,Integer> legging_map = new HashMap<>(){{
                        put(Material.LEATHER_LEGGINGS,50);
                        put(Material.GOLDEN_LEGGINGS,80);
                        put(Material.CHAINMAIL_LEGGINGS,100);
                        put(Material.IRON_LEGGINGS,200);
                        put(Material.DIAMOND_LEGGINGS,300);
                        put(Material.NETHERITE_LEGGINGS,500);
                    }};
                    HashMap<Material,Integer>boots_map = new HashMap<>(){{
                        put(Material.LEATHER_BOOTS,50);
                        put(Material.GOLDEN_BOOTS,80);
                        put(Material.CHAINMAIL_BOOTS,100);
                        put(Material.IRON_BOOTS,200);
                        put(Material.DIAMOND_BOOTS,300);
                        put(Material.NETHERITE_BOOTS,500);
                    }};
                    Material a = helment_map.keySet().stream().toList().get(random.nextInt(6))
                            ,b = chest_map.keySet().stream().toList().get(random.nextInt(6))
                            ,c = legging_map.keySet().stream().toList().get(random.nextInt(6))
                            ,d = boots_map.keySet().stream().toList().get(random.nextInt(6));
                    int health = (int)Math.ceil((helment_map.get(a) + chest_map.get(b) + legging_map.get(c) + boots_map.get(d) + 100) * 0.5 *Math.sqrt(ToolClass.round+1));
                    if(type.equals(Zombie.class)){
                        uuid =ToolClass.SummonEntity(
                                world,
                                loc,
                                ToolClass.fromEquipment(a,b,c,d,Material.AIR),
                                true,
                                health,
                                Zombie.class
                        ).getUniqueId();
                    }else{
                        uuid = ToolClass.SummonEntity(
                                world,
                                loc,
                                ToolClass.fromEquipment(a,b,c,d,Material.BOW),
                                true,
                                health,
                                Skeleton.class
                        ).getUniqueId();
                    }
                    uuids.add(uuid);
                }
            }
            return;
        }
        boolean clear = true;
        for(UUID uuid : uuids){
            if(Bukkit.getEntity(uuid) != null){
                clear = false;
                break;
            }
        }
        if(clear){
            for(UUID uuid : ToolClass.game_players){
                Player p = Bukkit.getPlayer(uuid);
                if(p != null){
                    Title title = Title.title(Component.text("Clear", NamedTextColor.GOLD, TextDecoration.BOLD),
                            Component.text().build(),
                            Title.Times.times(
                                    Duration.ofMillis(500),
                                    Duration.ofMillis(2000),
                                    Duration.ofMillis(500)
                            ));
                    p.showTitle(title);
                    p.playSound(p,Sound.BLOCK_NOTE_BLOCK_HARP,3,3);
                }
            }
            setAllDoors(Material.AIR,world);
            for(CornerData.Corner corner : ToolClass.LiveCrossRoom.corners){
                for(int x = corner.minx;x<=corner.maxx;x++){
                    for(int y = corner.miny;y<=corner.maxy;y++){
                        for(int z = corner.minz;z<=corner.maxz;z++){
                            world.playSound(new Location(world,x,y,z),Sound.BLOCK_WOOD_BREAK,3,1);
                            world.spawnParticle(Particle.BLOCK,new Location(world,x,y,z),15,Bukkit.createBlockData(Material.OAK_FENCE));
                        }
                    }
                }
            }
            cancel();
        }
    }


    public static Location findSafeLocation(World world, CornerData.Corner room, Random random){
        int minX = room.minx + 2;
        int maxX = room.maxx - 2;
        int minZ = room.minz + 2;
        int maxZ = room.maxz - 2;
        int y = room.miny + 1;
        if(maxX < minX || maxZ < minZ) return null;

        for(int attempt = 0; attempt < 80; attempt++){
            int x = random.nextInt(maxX - minX + 1) + minX;
            int z = random.nextInt(maxZ - minZ + 1) + minZ;
            if(isSafeSpawn(world, x, y, z)){
                return new Location(world, x + 0.5, y, z + 0.5);
            }
        }
        return null;
    }

    private static boolean isSafeSpawn(World world, int x, int y, int z){
        for(int dx = -1; dx <= 1; dx++){
            for(int dz = -1; dz <= 1; dz++){
                if(!world.getBlockAt(x + dx, y, z + dz).isPassable()) return false;
                if(!world.getBlockAt(x + dx, y + 1, z + dz).isPassable()) return false;
            }
        }
        return world.getBlockAt(x, y - 1, z).getType().isSolid();
    }

    public static void setAllDoors(Material material,World world){
        for(CornerData.Corner c : ToolClass.LiveCrossRoom.corners){
            for(int x = c.minx; x <= c.maxx; x++){
                for(int y = c.miny; y <= c.maxy; y++){
                    for(int z = c.minz; z <= c.maxz; z++){
                        world.getBlockAt(x, y, z).setType(material, true);
                    }
                }
            }
        }
    }
}
