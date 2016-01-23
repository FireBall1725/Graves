package com.fireball1725.graves.entity;

import com.fireball1725.graves.helpers.IDeadPlayerEntity;
import com.google.common.base.Predicate;
import com.mojang.authlib.GameProfile;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.*;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class EntityPlayerZombie extends EntityMob implements IRangedAttackMob, IDeadPlayerEntity {
    private static final int NAME = 13;
    private static final int CHILD = 14;
    private static final int WIDTH = 16;
    private static final int HEIGHT = 17;
    private static List<String> names = new LinkedList<String>();
    private final EntityAIBreakDoor breakDoorAI = new EntityAIBreakDoor(this);
    private final EntityAIArrowAttack arrowAI = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
    private double prevCapeX, prevCapeY, prevCapeZ;
    private double capeX, capeY, capeZ;
    private GameProfile profile;
    private EntityPlayer player;

    public EntityPlayerZombie(World world) {
        super(world);

        //this.noClip = true;
        this.isImmuneToFire = true;
        this.moveHelper = new EntityMoveHelper(this);
        this.isAirBorne = true;

        ((PathNavigateGround) getNavigator()).setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIOpenDoor(this, true));
        tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        tasks.addTask(7, new EntityAIWander(this, 1.0D));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 0, true, false, new Predicate<EntityPlayer>() {

            @Override
            public boolean apply(EntityPlayer input) {
                return input.getName().equals(getUsername());
            }
        }));

        setSize(0.6F, 1.8F);
    }

    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public boolean canPickUpLoot() {
        return true;
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        Entity entity = damageSrc.getEntity();
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;

            if (!player.getName().equals(this.getName())) {
                damageAmount = 0;
            }
        }

        super.damageEntity(damageSrc, damageAmount);
    }

	/* ENTITY INIT */

    @Override
    public void onUpdate() {
        prevCapeX = capeX;
        prevCapeY = capeY;
        prevCapeZ = capeZ;
        double x = posX - capeX;
        double y = posY - capeY;
        double z = posZ - capeZ;
        double maxCapeAngle = 10.0;

        if (x > maxCapeAngle)
            prevCapeX = capeX = posX;
        if (z > maxCapeAngle)
            prevCapeZ = capeZ = posZ;
        if (y > maxCapeAngle)
            prevCapeY = capeY = posY;
        if (x < -maxCapeAngle)
            prevCapeX = capeX = posX;
        if (z < -maxCapeAngle)
            prevCapeZ = capeZ = posZ;
        if (y < -maxCapeAngle)
            prevCapeY = capeY = posY;

        capeX += x * 0.25;
        capeZ += z * 0.25;
        capeY += y * 0.25;

        if (worldObj.isRemote) {
            float w = dataWatcher.getWatchableObjectFloat(WIDTH);
            if (w != width)
                width = w;
            float h = dataWatcher.getWatchableObjectFloat(HEIGHT);
            if (h != height)
                height = h;
        }

        if (this.player != null) {
            if (this.player.isDead) {
                this.setDead();
                return;
            }
        }

        super.onUpdate();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.40);
        getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0);
    }

	/* SOUNDS */

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataWatcher().addObject(NAME, "");
        getDataWatcher().addObject(CHILD, (byte) 0);
        getDataWatcher().addObject(WIDTH, width);
        getDataWatcher().addObject(HEIGHT, height);
    }

    @Override
    protected String getLivingSound() {
        return null;
    }

    @Override
    protected String getHurtSound() {
        return "game.hostile.hurt";
    }

	/* DROPS */

    @Override
    protected String getDeathSound() {
        return "game.hostile.die";
    }

    @Override
    protected void dropFewItems(boolean recentHit, int looting) {

    }

	/* EQUIPAMENT AND ITEMS */

    @Override
    protected void addRandomDrop() {

    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);

        if (rand.nextFloat() < (worldObj.getDifficulty() == EnumDifficulty.HARD ? 0.1F : 0.05F)) {
            int i = rand.nextInt(3);

            if (i == 0)
                setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
            else if (i == 1)
                setCurrentItemOrArmor(0, new ItemStack(Items.bow));
        }
    }

    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack stack) {
        super.setCurrentItemOrArmor(slot, stack);
        setCombatAI();
    }

	/* SPAWN AND DESPAWN */

    @Override
    public ItemStack getPickedResult(MovingObjectPosition target) {
        ItemStack stack = new ItemStack(Items.spawn_egg);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("entity_name", EntityList.classToStringMapping.get(getClass()));
        stack.setTagCompound(nbt);
        return stack;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        setUsername("FireBall1725");
        setEquipmentBasedOnDifficulty(difficulty);
        setEnchantmentBasedOnDifficulty(difficulty);
        setCombatAI();

        float additionalDifficulty = difficulty.getClampedAdditionalDifficulty();
        getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Knockback Resistance Bonus", rand.nextDouble() * 0.05, 0));

        double rangeBonus = rand.nextDouble() * 1.5 * additionalDifficulty;
        if (rangeBonus > 1.0)
            getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Range Bonus", rangeBonus, 2));

        if (rand.nextFloat() < additionalDifficulty * 0.05F)
            getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier("Health Bonus", rand.nextDouble() * 3.0 + 1.0, 2));

        if (rand.nextFloat() < additionalDifficulty * 0.15F)
            getEntityAttribute(SharedMonsterAttributes.attackDamage).applyModifier(new AttributeModifier("Damage Bonus", rand.nextDouble() + 0.5, 2));

        if (rand.nextFloat() < additionalDifficulty * 0.2F)
            getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(new AttributeModifier("Speed Bonus", rand.nextDouble() * 2.0 * 0.24 + 0.01, 2));

        if (rand.nextFloat() < additionalDifficulty * 0.1F)
            tasks.addTask(1, breakDoorAI);

        this.setCurrentItemOrArmor(0, new ItemStack(Items.diamond_sword));
        this.setCurrentItemOrArmor(1, new ItemStack(Items.diamond_boots));
        this.setCurrentItemOrArmor(2, new ItemStack(Items.diamond_leggings));
        this.setCurrentItemOrArmor(3, new ItemStack(Items.diamond_chestplate));
        this.setCurrentItemOrArmor(4, new ItemStack(Items.diamond_helmet));

        return null;
    }

	/* RENDERING */

    @Override
    protected void despawnEntity() {
        super.despawnEntity();
        if (isDead && ridingEntity != null)
            ridingEntity.setDead();
    }

	/* SAVE AND LOAD */

    @Override
    public double getYOffset() {
        return -0.3;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);

        String username = getUsername();
        if (!StringUtils.isBlank(username))
            nbt.setString("Username", username);
    }

	/* ATTACK STUFF */

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);

        String username;
        if (nbt.hasKey("Username")) {
            username = nbt.getString("Username");
        } else
            username = "FireBall1725";
        setUsername(username);

        setCombatAI();
    }

    @Override
    public boolean attackEntityAsMob(Entity target) {
        boolean result = super.attackEntityAsMob(target);
        if (result)
            swingItem();
        return result;
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float damage) {
        if (!hasBow())
            return;

        EntityArrow arrow = new EntityArrow(worldObj, this, target, 1.6F, 14 - worldObj.getDifficulty().getDifficultyId() * 4);
        int power = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, getHeldItem());
        int punch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, getHeldItem());
        arrow.setDamage(damage * 2.0F + rand.nextGaussian() * 0.25D + worldObj.getDifficulty().getDifficultyId() * 0.11F);

        if (power > 0)
            arrow.setDamage(arrow.getDamage() + power * 0.5D + 0.5D);

        if (punch > 0)
            arrow.setKnockbackStrength(punch);

        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, getHeldItem()) > 0)
            arrow.setFire(100);

        playSound("random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
        worldObj.spawnEntityInWorld(arrow);
    }

    private boolean hasBow() {
        return getHeldItem() != null && getHeldItem().getItem() instanceof ItemBow;
    }

	/* USERNAME */

    private void setCombatAI() {
        if (hasBow())
            tasks.addTask(1, arrowAI);
        else
            tasks.removeTask(arrowAI);
    }

    @Override
    public String getName() {
        return getUsername();
    }

    @Override
    public String getCustomNameTag() {
        return getUsername();
    }

    @Override
    public void setCustomNameTag(String name) {
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(getName()) {

            private ChatStyle style;

            @Override
            public ChatStyle getChatStyle() {
                if (style == null) {
                    style = new ChatStyle() {

                        @Override
                        @SideOnly(Side.CLIENT)
                        public String getFormattingCode() {
                            return "";
                        }

                    };
                    Iterator<?> iterator = siblings.iterator();

                    while (iterator.hasNext()) {
                        IChatComponent ichatcomponent = (IChatComponent) iterator.next();
                        ichatcomponent.getChatStyle().setParentStyle(style);
                    }
                }

                return style;
            }
        };
    }

    @Override
    public GameProfile getProfile() {
        if (profile == null)
            setProfile(TileEntitySkull.updateGameprofile(new GameProfile(null, getUsername())));
        return profile;
    }

    // IHumanEntity

    public void setProfile(GameProfile profile) {
        this.profile = profile;
    }

    @Override
    public String getUsername() {
        String username = getDataWatcher().getWatchableObjectString(NAME);
        if (StringUtils.isBlank(username))
            getDataWatcher().updateObject(NAME, "FireBall1725");
        return username;
    }

    @Override
    public void setUsername(String name) {
        getDataWatcher().updateObject(NAME, name);

        if ("Herobrine".equals(name)) {
            getEntityAttribute(SharedMonsterAttributes.attackDamage).applyModifier(new AttributeModifier("Herobrine Damage Bonus", 1, 2));
            getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(new AttributeModifier("Herobrine Speed Bonus", 0.5, 2));
        }
    }

    @Override
    public double getInterpolatedCapeX(float partialTickTime) {
        return prevCapeX + (capeX - prevCapeX) * partialTickTime - (prevPosX + (posX - prevPosX) * partialTickTime);
    }

    @Override
    public double getInterpolatedCapeY(float partialTickTime) {
        return prevCapeY + (capeY - prevCapeY) * partialTickTime - (prevPosY + (posY - prevPosY) * partialTickTime);
    }

    @Override
    public double getInterpolatedCapeZ(float partialTickTime) {
        return prevCapeZ + (capeZ - prevCapeZ) * partialTickTime - (prevPosZ + (posZ - prevPosZ) * partialTickTime);
    }

    // Child stuff

    @Override
    public boolean isChild() {
        return getDataWatcher().getWatchableObjectByte(CHILD) == 1;
    }

    @Override
    protected void setSize(float width, float height) {
        super.setSize(width, height);
        dataWatcher.updateObject(WIDTH, this.width);
        dataWatcher.updateObject(HEIGHT, this.height);
    }

    static class PlayerZombieMoveHelper extends EntityMoveHelper {
        private EntityPlayerZombie parentEntity;
        private int courseChangeCooldown;

        public PlayerZombieMoveHelper(EntityGhast p_i45838_1_) {
            super(p_i45838_1_);
            //this.parentEntity = p_i45838_1_;
        }

        public void onUpdateMoveHelper() {
            if (this.update) {
                double d0 = this.posX - this.parentEntity.posX;
                double d1 = this.posY - this.parentEntity.posY;
                double d2 = this.posZ - this.parentEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (this.courseChangeCooldown-- <= 0) {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                    d3 = (double) MathHelper.sqrt_double(d3);

                    if (this.isNotColliding(this.posX, this.posY, this.posZ, d3)) {
                        this.parentEntity.motionX += d0 / d3 * 0.1D;
                        this.parentEntity.motionY += d1 / d3 * 0.1D;
                        this.parentEntity.motionZ += d2 / d3 * 0.1D;
                    } else {
                        this.update = false;
                    }
                }
            }
        }

        /**
         * Checks if entity bounding box is not colliding with terrain
         */
        private boolean isNotColliding(double p_179926_1_, double p_179926_3_, double p_179926_5_, double p_179926_7_) {
            double d0 = (p_179926_1_ - this.parentEntity.posX) / p_179926_7_;
            double d1 = (p_179926_3_ - this.parentEntity.posY) / p_179926_7_;
            double d2 = (p_179926_5_ - this.parentEntity.posZ) / p_179926_7_;
            AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();

            for (int i = 1; (double) i < p_179926_7_; ++i) {
                axisalignedbb = axisalignedbb.offset(d0, d1, d2);

                if (!this.parentEntity.worldObj.getCollidingBoundingBoxes(this.parentEntity, axisalignedbb).isEmpty()) {
                    return false;
                }
            }

            return true;
        }
    }
}
