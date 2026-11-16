package plugin.xiguananshan.mCDungeon.boss;

import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import plugin.xiguananshan.mCDungeon.ToolClass;
import plugin.xiguananshan.mCDungeon.ticks.BossService;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ZombieKingService extends BossService {
    private int next_skill_ticks;
    private SkillType next_skill;
    public boolean in_skill;
    public ZombieKingService(LivingEntity entity) {
        next_skill_ticks = -1;
        next_skill = null;
        in_skill = false;
        super(entity);
    }

    @Override
    public void run() {
        if(next_skill_ticks <=0 && !in_skill && next_skill == null){
            Random random = new Random();
            next_skill_ticks = random.nextInt(320)+100;
            next_skill = SkillType.values()[random.nextInt(SkillType.values().length)];
            in_skill = false;
        }
        if(next_skill != null && next_skill_ticks > 0){
            next_skill_ticks--;
        }
        if(next_skill_ticks<=0 && next_skill != null && !in_skill){
            in_skill = true;
            switch (next_skill){
                case SPAWN -> new SpawnService(this).runTaskTimer(ToolClass.PLUGIN,0,1);
                case RUN -> new RunService(this).runTaskTimer(ToolClass.PLUGIN,0,1);
                case JUMP -> new JumpService(this).runTaskTimer(ToolClass.PLUGIN,0,1);
            }
            next_skill = null;
        }
        super.run();
    }
}
class SpawnService extends BukkitRunnable{
    private int spawn_tick;

    private final ZombieKingService service;
    public SpawnService(ZombieKingService service){
        this.spawn_tick = 60;
        this.service = service;
    }
    @Override
    public void run() {
        LivingEntity entity = service.getEntity();
        if(!entity.isValid()){
            cancel();
            return;
        }
        entity.setAI(false);
        if(spawn_tick%20 == 0){
            ToolClass.fillCubeWithParticles(entity.getWorld(), entity.getLocation(),2,25,Particle.ANGRY_VILLAGER);
        }
        spawn_tick --;
        if(spawn_tick<=0){
            List<Class<? extends Mob>> spawn_type = List.of(Zombie.class, Skeleton.class, Spider.class,Creeper.class);
            entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ZOMBIE_DEATH,3,1);
            for(int i = 0;i<4;i++){
                Random random = new Random();
                Class<? extends Mob> spawn = spawn_type.get(random.nextInt(spawn_type.size()));
                if(spawn.equals(Zombie.class)){
                    Zombie m = (Zombie) ToolClass.SummonEntity(entity.getWorld(),entity.getLocation(),ToolClass.fromEquipment(
                            Material.IRON_HELMET,
                            Material.IRON_CHESTPLATE,
                            Material.IRON_LEGGINGS,
                            Material.IRON_BOOTS,
                            Material.IRON_SWORD
                    ),true,1000,spawn);
                    Objects.requireNonNull(m.getAttribute(Attribute.MOVEMENT_SPEED)).setBaseValue(0.4);
                }else{
                    ToolClass.SummonEntity(entity.getWorld(),entity.getLocation(),true,600,spawn);
                }
            }
            entity.setAI(true);
            service.in_skill = false;
            cancel();
        }
    }
}
class JumpService extends BukkitRunnable{
    private final ZombieKingService service;
    private int ticks;
    private Location down;
    public JumpService(ZombieKingService service){
        this.service = service;
        ticks = 10;
    }

    @Override
    public void run() {
        LivingEntity entity = service.getEntity();
        if(!entity.isValid()){
            cancel();
            return;
        }
        if(ticks == 10){
            down = entity.getLocation();
            entity.setAI(false);
            Location loc = entity.getLocation().clone().add(0,2,0);
            entity.teleport(loc);
            entity.getWorld().playSound(entity.getLocation(),Sound.ENTITY_ENDERMAN_TELEPORT,3,1);
        }
        ticks--;
        if(ticks<=0){
            entity.teleport(down);
            entity.setAI(true);
            entity.getWorld().spawnParticle(Particle.EXPLOSION,down,25);
            entity.getWorld().playSound(entity.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,3,1);
            List<Entity> entities = entity.getNearbyEntities(4,4,4);
            for(Entity entity1 : entities){
                if(entity1 instanceof LivingEntity l){
                    if(!entity1.getPersistentDataContainer().has(ToolClass.fromNamespaceKey("entity"))){
                        double distance = entity.getLocation().distance(l.getLocation());
                        l.damage(4+(4-distance));
                    }
                }
            }
            service.in_skill = false;
            cancel();
        }
    }
}
class RunService extends BukkitRunnable{
    private static final int WINDUP_TICKS = 60;
    private static final double TARGET_RANGE = 16.0;
    private static final double MAX_DISTANCE = 12.0;
    private static final double STEP = 0.6;

    private final ZombieKingService service;
    private int ticks;
    private boolean charging;
    private boolean finished;
    private Player target;
    private Vector direction;
    private double traveled;

    public RunService(ZombieKingService service){
        this.service = service;
        this.ticks = WINDUP_TICKS;
        this.charging = false;
        this.finished = false;
        this.traveled = 0;
    }

    @Override
    public void run() {
        LivingEntity entity = service.getEntity();
        if(finished) return;
        if(!entity.isValid()){
            finish();
            return;
        }

        if(!charging){
            if(ticks == WINDUP_TICKS){
                target = findTarget(entity);
                if(target == null){
                    finish();
                    return;
                }
                entity.setAI(false);
                face(entity, target.getLocation());
                entity.getWorld().playSound(entity.getLocation(),Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR,3,0.8f);
            }
            ticks--;
            entity.getWorld().spawnParticle(Particle.CRIT,entity.getLocation().add(0,1,0),15);
            if(ticks<=0){
                startCharge(entity);
            }
            return;
        }

        charge(entity);
    }

    private Player findTarget(LivingEntity entity){
        Location loc = entity.getLocation();
        return entity.getWorld().getNearbyEntities(loc,TARGET_RANGE,8,TARGET_RANGE).stream()
                .filter(e -> e instanceof Player)
                .map(e -> (Player) e)
                .filter(this::isValidTarget)
                .filter(entity::hasLineOfSight)
                .min(Comparator.comparingDouble(p -> p.getLocation().distanceSquared(loc)))
                .orElse(null);
    }

    private boolean isValidTarget(Player player){
        if(player.isDead() || !player.isValid()) return false;
        GameMode mode = player.getGameMode();
        return mode == GameMode.SURVIVAL || mode == GameMode.ADVENTURE;
    }

    private void startCharge(LivingEntity entity){
        if(target == null || !isValidTarget(target) || target.getWorld() != entity.getWorld()){
            finish();
            return;
        }

        Location from = entity.getLocation();
        Vector dir = target.getLocation().toVector().subtract(from.toVector());
        dir.setY(0);
        if(dir.lengthSquared() < 0.01){
            hitPlayer(entity,target);
            finish();
            return;
        }

        direction = dir.normalize();
        face(entity,target.getLocation());
        charging = true;
        entity.getWorld().playSound(entity.getLocation(),Sound.ENTITY_RAVAGER_ROAR,3,1);
    }

    private void charge(LivingEntity entity){
        Location from = entity.getLocation();
        Location rayStart = from.clone().add(0,0.9,0);
        Vector rayOrigin = rayStart.toVector();
        RayTraceResult blockHit = entity.getWorld().rayTraceBlocks(
                rayStart,
                direction,
                STEP + 0.1,
                FluidCollisionMode.NEVER,
                true
        );
        double blockDistance = blockHit == null ? Double.MAX_VALUE : blockHit.getHitPosition().distance(rayOrigin);

        // 沿冲撞线检测玩家，避免隔着墙用范围检测误伤
        RayTraceResult playerHit = entity.getWorld().rayTraceEntities(
                rayStart,
                direction,
                Math.min(STEP,blockDistance) + 0.6,
                0.6,
                e -> e instanceof Player p && isValidTarget(p)
        );
        Player hit = null;
        double hitDistance = 0;
        if(playerHit != null && playerHit.getHitEntity() instanceof Player p && isValidTarget(p)){
            hitDistance = playerHit.getHitPosition().distance(rayOrigin);
            if(hitDistance <= blockDistance + 0.05){
                hit = p;
            }
        }

        if(hit != null){
            Location to = from.clone().add(direction.clone().multiply(Math.min(hitDistance,STEP)));
            entity.teleport(to);
            entity.setVelocity(new Vector(0,0,0));
            hitPlayer(entity,hit);
            finish();
            return;
        }

        double allowed = STEP;
        if(blockHit != null){
            allowed = Math.min(STEP,blockDistance - 0.4);
            if(allowed <= 0){
                finish();
                return;
            }
        }

        Location next = from.clone().add(direction.clone().multiply(allowed));
        entity.teleport(next);
        entity.setVelocity(new Vector(0,0,0));
        entity.getWorld().spawnParticle(Particle.CRIT,next.clone().add(0,1,0),10);

        traveled += allowed;
        if(blockHit != null){
            entity.getWorld().playSound(next,Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR,2,0.8f);
            finish();
            return;
        }
        if(traveled >= MAX_DISTANCE){
            finish();
        }
    }

    private void hitPlayer(LivingEntity entity, Player player){
        player.damage(6.0,entity);
        entity.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR,player.getLocation().add(0,1,0),10);
        entity.getWorld().playSound(player.getLocation(),Sound.ENTITY_PLAYER_ATTACK_CRIT,3,1);
    }

    private void face(LivingEntity entity, Location target){
        Location loc = entity.getLocation().clone();
        Vector dir = target.toVector().subtract(loc.toVector());
        dir.setY(0);
        if(dir.lengthSquared() < 0.01) return;
        loc.setDirection(dir);
        entity.teleport(loc);
    }

    private void finish(){
        if(finished) return;
        finished = true;
        LivingEntity entity = service.getEntity();
        if(entity.isValid()){
            entity.setAI(true);
        }
        service.in_skill = false;
        cancel();
    }
}
enum SkillType{
    SPAWN,
    JUMP,
    RUN
}
