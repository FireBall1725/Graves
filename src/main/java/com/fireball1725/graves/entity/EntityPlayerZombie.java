package com.fireball1725.graves.entity;

import com.fireball1725.graves.configuration.ConfigZombie;
import com.fireball1725.graves.helpers.IDeadPlayerEntity;
import com.fireball1725.graves.helpers.LogHelper;
import com.google.common.base.Predicate;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class EntityPlayerZombie extends EntityFlying implements IDeadPlayerEntity
{
	private static final DataParameter<String> NAME = EntityDataManager.createKey(EntityPlayerZombie.class, DataSerializers.STRING);
	private static final DataParameter<Byte> CHILD = EntityDataManager.createKey(EntityPlayerZombie.class, DataSerializers.BYTE);
	private static final DataParameter<Float> WIDTH = EntityDataManager.createKey(EntityPlayerZombie.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> HEIGHT = EntityDataManager.createKey(EntityPlayerZombie.class, DataSerializers.FLOAT);
	private final EntityAIBreakDoor breakDoorAI = new EntityAIBreakDoor(this);
	private double prevCapeX, prevCapeY, prevCapeZ;
    private double capeX, capeY, capeZ;
    private GameProfile profile;
    private EntityPlayer player;

	private BlockPos graveMaster;

	public EntityPlayerZombie(World world) {
        super(world);

        this.noClip = true;
        this.isImmuneToFire = true;
        this.moveHelper = new PlayerZombieMoveHelper();

        tasks.addTask(3, new AIPlayerZombieRandomFly());
        tasks.addTask(4, new AIPlayerZombieAttackTarget());

        tasks.addTask(1, new EntityAIOpenDoor(this, true));
		tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
		tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));

        tasks.addTask(7, new EntityAIWander(this, 1.0D));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));

        targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, 0, true, false, new Predicate<EntityPlayer>() {
            @Override
            public boolean apply(EntityPlayer input) {
                return input.getName().equals(getUsername());
            }
        }));

        setSize(0.6F, 1.8F);
    }

	public EntityPlayerZombie(World world, BlockPos pos)
	{
		this(world);
		graveMaster = pos;
	}

    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }

	@Override
	public void writeToNBT(NBTTagCompound tagCompund)
	{
		tagCompund.setBoolean("[GoldenLassoPrevent]", true); // Make it so ExU2 cursed lassos are disabled
		super.writeToNBT(tagCompund);
	}

    @Override
    public boolean canPickUpLoot() {
        return true;
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        //Entity entity = damageSrc.getEntity();
        //if (entity instanceof EntityPlayer) {
        //    EntityPlayer player = (EntityPlayer) entity;

            //if (!player.getName().equals(this.getName())) {
            //    damageAmount = 0;
            //}
        //}

        super.damageEntity(damageSrc, damageAmount);
    }

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
			float w = dataWatcher.get(WIDTH);
			if(w != width)
				width = w;
			float h = dataWatcher.get(HEIGHT);
			if(h != height)
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
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(ConfigZombie.configZombieDefaultFollowRange);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(ConfigZombie.configZombieDefaultSpeed);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(ConfigZombie.configZombieDefaultBaseDamage);
	}

    @Override
    protected void entityInit() {
        super.entityInit();
		dataWatcher.register(NAME, "");
		dataWatcher.register(CHILD, (byte) 0);
		dataWatcher.register(WIDTH, width);
		dataWatcher.register(HEIGHT, height);
	}

	//TODO: Add Sounds when not completely broken.
	//	@Override
	//	protected SoundEvent getAmbientSound()
	//	{
	//		return SoundHelper.getRegisteredSoundEvent("graves:graveZombieIdle");
	//	}
	//
	//	@Override
	//	protected SoundEvent getHurtSound()
	//	{
	//		return SoundHelper.getRegisteredSoundEvent("graves:graveZombieAttack");
	//	}
	//
	//	@Override
	//	public SoundCategory getSoundCategory()
	//	{
	//		return SoundCategory.HOSTILE;
	//	}
	//
	//	@Override
	//    protected String getDeathSound() {
	//        return "graves:graveZombieDeath";
	//    }

    @Override
    protected void dropFewItems(boolean recentHit, int looting) {

    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);

        if (rand.nextFloat() < (worldObj.getDifficulty() == EnumDifficulty.HARD ? 0.1F : 0.05F)) {
            int i = rand.nextInt(3);

			if(i == 0)
			{ setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.stone_sword)); }
			else if(i == 1)
			{ setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.bow)); }
		}
	}

	@Override
	public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack)
	{
		super.setItemStackToSlot(slotIn, stack);
	}

    @Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		ItemStack stack = new ItemStack(Items.spawn_egg);
		NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("entity_name", EntityList.classToStringMapping.get(getClass()));
        stack.setTagCompound(nbt);
        return stack;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        boolean hardcoreEnabled = worldObj.getWorldInfo().isHardcoreModeEnabled();
        EnumDifficulty gameDifficulty = worldObj.getDifficulty();

        setEquipmentBasedOnDifficulty(difficulty);
        setEnchantmentBasedOnDifficulty(difficulty);

		float additionalDifficulty = difficulty.getClampedAdditionalDifficulty();
		getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Knockback Resistance Bonus", rand.nextDouble() * 50, 0));

        double rangeBonus = rand.nextDouble() * 1.5 * additionalDifficulty;
		if(rangeBonus > 1.0)
		{ getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Range Bonus", rangeBonus, 2)); }

		if(rand.nextFloat() < additionalDifficulty * 0.05F)
		{ getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Health Bonus", rand.nextDouble() * 3.0 + 1.0, 2)); }

		if(rand.nextFloat() < additionalDifficulty * 0.15F)
		{ getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier("Damage Bonus", rand.nextDouble() + 0.5, 2)); }

		if(rand.nextFloat() < additionalDifficulty * 0.2F)
		{ getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier("Speed Bonus", rand.nextDouble() * 2.0 * 0.24 + 0.01, 2)); }

        if (rand.nextFloat() < additionalDifficulty * 0.1F)
            tasks.addTask(1, breakDoorAI);

        Random random = new Random();
        int rng = random.nextInt(100);

        ItemStack slot0 = null;
        ItemStack slot1 = null;
        ItemStack slot2 = null;
        ItemStack slot3 = null;
        ItemStack slot4 = null;

        int rngNothing = 0;
        int rngSword = 0;
        int rngLeatherKit = 0;
        int rngIronKit = 0;
        int rngGoldKit = 0;
        int rngDiamondKit = 0;

        switch (gameDifficulty) {
            case EASY:
                rngNothing = ConfigZombie.configZombieArmorChanceEasyNone;
                rngSword = ConfigZombie.configZombieArmorChanceEasyWoodSword;
                rngLeatherKit = ConfigZombie.configZombieArmorChanceEasyLeatherKit;
                rngIronKit = ConfigZombie.configZombieArmorChanceEasyIronKit;
                rngGoldKit = ConfigZombie.configZombieArmorChanceEasyGoldKit;
                rngDiamondKit = ConfigZombie.configZombieArmorChanceEasyDiamondKit;
                break;

            case NORMAL:
                rngNothing = ConfigZombie.configZombieArmorChanceNormalNone;
                rngSword = ConfigZombie.configZombieArmorChanceNormalWoodSword;
                rngLeatherKit = ConfigZombie.configZombieArmorChanceNormalLeatherKit;
                rngIronKit = ConfigZombie.configZombieArmorChanceNormalIronKit;
                rngGoldKit = ConfigZombie.configZombieArmorChanceNormalGoldKit;
                rngDiamondKit = ConfigZombie.configZombieArmorChanceNormalDiamondKit;
                break;

            case HARD:
                rngNothing = ConfigZombie.configZombieArmorChanceHardNone;
                rngSword = ConfigZombie.configZombieArmorChanceHardWoodSword;
                rngLeatherKit = ConfigZombie.configZombieArmorChanceHardLeatherKit;
                rngIronKit = ConfigZombie.configZombieArmorChanceHardIronKit;
                rngGoldKit = ConfigZombie.configZombieArmorChanceHardGoldKit;
                rngDiamondKit = ConfigZombie.configZombieArmorChanceHardDiamondKit;
                break;
        }

        if (hardcoreEnabled) {
            rngNothing = ConfigZombie.configZombieArmorChanceHardCoreNone;
            rngSword = ConfigZombie.configZombieArmorChanceHardCoreWoodSword;
            rngLeatherKit = ConfigZombie.configZombieArmorChanceHardCoreLeatherKit;
            rngIronKit = ConfigZombie.configZombieArmorChanceHardCoreIronKit;
            rngGoldKit = ConfigZombie.configZombieArmorChanceHardCoreGoldKit;
            rngDiamondKit = ConfigZombie.configZombieArmorChanceHardCoreDiamondKit;
        }

        if (rngNothing + rngSword + rngLeatherKit + rngIronKit + rngGoldKit + rngDiamondKit != 100) {
            rngNothing = 30;
            rngSword = 20;
            rngLeatherKit = 20;
            rngIronKit = 16;
            rngGoldKit = 8;
            rngDiamondKit = 6;
            LogHelper.error("RNG Values did not add up to 100, please check your config and fix this! using default RNG values for Zombie Armor!");
        }

        if (rng > rngNothing + rngSword + rngLeatherKit + rngIronKit + rngGoldKit) { // Diamond Kit
            slot0 = new ItemStack(Items.diamond_sword);
            slot1 = new ItemStack(Items.diamond_boots);
            slot2 = new ItemStack(Items.diamond_leggings);
            slot3 = new ItemStack(Items.diamond_chestplate);
            slot4 = new ItemStack(Items.diamond_helmet);
        } else if (rng > rngNothing + rngSword + rngLeatherKit + rngIronKit) { // Gold Kit
            slot0 = new ItemStack(Items.golden_sword);
            slot1 = new ItemStack(Items.golden_boots);
            slot2 = new ItemStack(Items.golden_leggings);
            slot3 = new ItemStack(Items.golden_chestplate);
            slot4 = new ItemStack(Items.golden_helmet);
        } else if (rng > rngNothing + rngSword + rngLeatherKit) { // Iron Kit
            slot0 = new ItemStack(Items.iron_sword);
            slot1 = new ItemStack(Items.iron_boots);
            slot2 = new ItemStack(Items.iron_leggings);
            slot3 = new ItemStack(Items.iron_chestplate);
            slot4 = new ItemStack(Items.iron_helmet);
        } else if (rng > rngNothing + rngSword) { // Leather Kit
            slot0 = new ItemStack(Items.wooden_sword);
            slot1 = new ItemStack(Items.leather_boots);
            slot2 = new ItemStack(Items.leather_leggings);
            slot3 = new ItemStack(Items.leather_chestplate);
            slot4 = new ItemStack(Items.leather_helmet);
        } else if (rng > rngNothing) { // Wooden Sword
            slot0 = new ItemStack(Items.wooden_sword);
        }

        if (ConfigZombie.configZombieArmorEnabled) {
			if(slot0 != null)
			{ this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, slot0); }

			if(slot1 != null)
			{ this.setItemStackToSlot(EntityEquipmentSlot.FEET, slot1); }

			if(slot2 != null)
			{ this.setItemStackToSlot(EntityEquipmentSlot.LEGS, slot2); }

			if(slot3 != null)
			{ this.setItemStackToSlot(EntityEquipmentSlot.CHEST, slot3); }

			if(slot4 != null)
			{ this.setItemStackToSlot(EntityEquipmentSlot.HEAD, slot4); }
		}

        this.setHealth(ConfigZombie.configZombieDefaultHealth);

        return null;
    }

    @Override
    protected void despawnEntity() {
        super.despawnEntity();
		if(isDead && this.getRidingEntity() != null)
		{ getRidingEntity().setDead(); }
	}

    @Override
    public double getYOffset() {
        return -0.3;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
		if(graveMaster != null)
		{ nbt.setLong("graveMaster", graveMaster.toLong()); }

        String username = getUsername();
        if (!StringUtils.isBlank(username))
            nbt.setString("Username", username);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);

        String username;
        if (nbt.hasKey("Username")) {
            username = nbt.getString("Username");
        } else
            username = "FireBall1725";
        setUsername(username);
		if(nbt.hasKey("graveMaster"))
		{ graveMaster = BlockPos.fromLong(nbt.getLong("graveMaster")); }
	}

    @Override
    public boolean attackEntityAsMob(Entity target) {
        boolean result = super.attackEntityAsMob(target);
		if(result)
		{ swingArm(EnumHand.MAIN_HAND); }
		return result;
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
	public ITextComponent getDisplayName()
	{
		return new TextComponentString(getName())
		{

			private Style style;

            @Override
			public Style getChatStyle()
			{
				if(style == null)
				{
					style = new Style()
					{

                        @Override
                        @SideOnly(Side.CLIENT)
                        public String getFormattingCode() {
                            return "";
                        }

                    };
                    Iterator<?> iterator = siblings.iterator();

                    while (iterator.hasNext()) {
						ITextComponent ITextComponent = (ITextComponent) iterator.next();
						ITextComponent.getChatStyle().setParentStyle(style);
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

    public void setProfile(GameProfile profile) {
		if(profile != null)
		{
			this.profile = profile;
			this.setUsername(profile.getName());
			return;
		}
		this.setUsername("FireBall1725");
	}

    @Override
    public String getUsername() {
		String username = dataWatcher.get(NAME);
		if(StringUtils.isBlank(username))
		{ dataWatcher.set(NAME, "FireBall1725"); }
		if(username.equals("Soaryn"))
			return "direwolf20";
        if (username.equals("direwolf20"))
            return "Soaryn";
        return username;
    }

    @Override
    public void setUsername(String name) {
        String newName = "";

        if ("Herobrine".equals(name)) {
			getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier("Herobrine Damage Bonus", 1, 2));
			getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier("Herobrine Speed Bonus", 0.5, 2));
		}

        if ("direwolf20".equals(name)) {
            newName = "Soaryn";
        }

        if ("Soaryn".equals(name)) {
            newName = "direwolf20";
        }

		if(!newName.isEmpty())
		{
			name = newName;
		}
		dataWatcher.set(NAME, name);
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

    @Override
    public boolean isChild() {
		return dataWatcher.get(CHILD) == 1;
	}

    @Override
    protected void setSize(float width, float height) {
        super.setSize(width, height);
		dataWatcher.set(WIDTH, this.width);
		dataWatcher.set(HEIGHT, this.height);
	}

	public BlockPos getGraveMaster()
	{
		return graveMaster;
	}

	/**
	 * Returns false if this Entity is a boss, true otherwise.
	 */
	public boolean isNonBoss()
	{
		return false;
	}

	class PlayerZombieMoveTargetPos
	{
		public double posX;
		public double posY;
        public double posZ;
        public double distX;
        public double distY;
        public double distZ;
        public double dist;
        public double aimX;
        public double aimY;
        public double aimZ;
		private EntityPlayerZombie playerZombie = EntityPlayerZombie.this;

        public PlayerZombieMoveTargetPos() {
            this(0, 0, 0);
        }

        public PlayerZombieMoveTargetPos(double posX, double posY, double posZ) {
            this.setTarget(posX, posY, posZ);
        }

        public void setTarget(double posX, double posY, double posZ) {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.refresh();
        }

        public void refresh() {
            this.distX = this.posX - this.playerZombie.posX;
            this.distY = this.posY - this.playerZombie.posY;
            this.distZ = this.posZ - this.playerZombie.posZ;

            this.dist = (double) MathHelper.sqrt_double(this.distX * this.distX + this.distY * this.distY + this.distZ * this.distZ);

            // (aimX,aimY,aimZ) is a unit vector in the direction we want to go
            if (this.dist == 0.0D) {
                this.aimX = 0.0D;
                this.aimY = 0.0D;
                this.aimZ = 0.0D;
            } else {
                this.aimX = this.distX / this.dist;
                this.aimY = this.distY / this.dist;
                this.aimZ = this.distZ / this.dist;
            }
        }

        public boolean isBoxBlocked(AxisAlignedBB box) {
            //return !this.playerZombie.worldObj.getCollidingBoundingBoxes(this.playerZombie, box).isEmpty();
            return false;
        }

        // check nothing will collide with the playerZombie in the direction of aim, for howFar units (or until the destination - whichever is closer)
        public boolean isPathClear(double howFar) {
            howFar = Math.min(howFar, this.dist);
            AxisAlignedBB box = this.playerZombie.getEntityBoundingBox();
            for (double i = 0.5D; i < howFar; ++i) {
                // check there's nothing in the way
                if (this.isBoxBlocked(box.offset(this.aimX * i, this.aimY * i, this.aimZ * i))) {
                    return false;
				}
			}
			return !this.isBoxBlocked(box.offset(this.aimX * howFar, this.aimY * howFar, this.aimZ * howFar));
		}

	}



    class PlayerZombieMoveHelper extends EntityMoveHelper {
        private EntityPlayerZombie playerZombie = EntityPlayerZombie.this;
        private int courseChangeCooldown = 0;
        private double closeEnough = 0.3D;
        private PlayerZombieMoveTargetPos targetPos = new PlayerZombieMoveTargetPos();

		public PlayerZombieMoveHelper()
		{
			super(EntityPlayerZombie.this);
		}

        @Override
        public void setMoveTo(double x, double y, double z, double speedIn) {
            super.setMoveTo(x, y, z, speedIn);
            this.targetPos.setTarget(x, y, z);
        }

        @Override
        public void onUpdateMoveHelper() {
			if(!this.isUpdating())
			{
				return;
            }

            if (this.courseChangeCooldown-- > 0) {
                // limit the rate at which we change course
                return;
            }
            this.courseChangeCooldown += this.playerZombie.getRNG().nextInt(5) + 2;

            // update the target position
            this.targetPos.refresh();

            // accelerate the playerZombie towards the target
            double acceleration = 0.1D;
            this.playerZombie.motionX += this.targetPos.aimX * acceleration;
            this.playerZombie.motionY += this.targetPos.aimY * acceleration;
            this.playerZombie.motionZ += this.targetPos.aimZ * acceleration;

            // rotate to point at target
            this.playerZombie.renderYawOffset = this.playerZombie.rotationYaw = -((float) Math.atan2(this.targetPos.distX, this.targetPos.distZ)) * 180.0F / (float) Math.PI;

            // occasionally jerk to the side - makes them more difficult to hit
            if (this.playerZombie.getRNG().nextInt(5) == 0) {
                float strafeAmount = (this.playerZombie.getRNG().nextFloat() * 0.4F) - 0.2F;
                this.playerZombie.motionX += (double) (strafeAmount * MathHelper.cos(this.playerZombie.rotationYaw * (float) Math.PI / 180.0F));
                this.playerZombie.motionZ += (double) (strafeAmount * MathHelper.sin(this.playerZombie.rotationYaw * (float) Math.PI / 180.0F));
            }
		}

	}



	// AI class for implementing the random flying behaviour
	class AIPlayerZombieRandomFly extends EntityAIBase {
        private EntityPlayerZombie playerZombie = EntityPlayerZombie.this;
        private PlayerZombieMoveTargetPos targetPos = new PlayerZombieMoveTargetPos();

        public AIPlayerZombieRandomFly() {
            this.setMutexBits(1);
        }

        // should we choose a new random destination for the playerZombie to fly to?
        // yes, if the playerZombie doesn't already have a destination
        @Override
        public boolean shouldExecute() {
            //LogHelper.info(this.playerZombie.getMoveHelper().isUpdating() ? ">>> has a move target" : ">>> no move target");
            return !this.playerZombie.getMoveHelper().isUpdating();
        }

        @Override
        public boolean continueExecuting() {
            return false;
        }

        // choose a a new random destination for the playerZombie to fly to
        @Override
        public void startExecuting() {
            Random rand = this.playerZombie.getRNG();
            // pick a random nearby point and see if we can fly to it
            if (this.tryGoingRandomDirection(rand, 6.0D)) {
                return;
            }
            // pick a random closer point to fly to instead
            if (this.tryGoingRandomDirection(rand, 2.0D)) {
                return;
            }
            // try going straight along axes (try all 6 directions in random order)
            List<EnumFacing> directions = Arrays.asList(EnumFacing.values());
            Collections.shuffle(directions);
            for (EnumFacing facing : directions) {
                if (this.tryGoingAlongAxis(rand, facing, 1.0D)) {
                    return;
                }
            }
        }


        // note y direction has a slight downward bias to stop them flying too high
        public boolean tryGoingRandomDirection(Random rand, double maxDistance) {
            double dirX = ((rand.nextDouble() * 2.0D - 1.0D) * maxDistance);
            double dirY = ((rand.nextDouble() * 2.0D - 1.1D) * maxDistance);
            double dirZ = ((rand.nextDouble() * 2.0D - 1.0D) * maxDistance);
            return this.tryGoing(dirX, dirY, dirZ);
        }

        public boolean tryGoingAlongAxis(Random rand, EnumFacing facing, double maxDistance) {
            double dirX = 0.0D;
            double dirY = 0.0D;
            double dirZ = 0.0D;
            switch (facing.getAxis()) {
                case X:
                    dirX = rand.nextDouble() * facing.getAxisDirection().getOffset() * maxDistance;
                    break;
                case Y:
                    dirY = rand.nextDouble() * facing.getAxisDirection().getOffset() * maxDistance;
                    break;
                case Z:
                default:
                    dirZ = rand.nextDouble() * facing.getAxisDirection().getOffset() * maxDistance;
                    break;
            }
            return this.tryGoing(dirX, dirY, dirZ);
        }

        public boolean tryGoing(double dirX, double dirY, double dirZ) {
            //System.out.println("("+dirX+","+dirY+","+dirZ+")");
            this.targetPos.setTarget(this.playerZombie.posX + dirX, this.playerZombie.posY + dirY, this.playerZombie.posZ + dirZ);
            //LogHelper.info(">>> Testing random move target distance:" + this.targetPos.dist + " direction:(" + this.targetPos.aimX + "," + this.targetPos.aimY + "," + this.targetPos.aimZ + ")");
            boolean result = this.targetPos.isPathClear(5.0F);
            if (result) {
                this.playerZombie.getMoveHelper().setMoveTo(this.targetPos.posX, this.targetPos.posY, this.targetPos.posZ, 1.0D);
            }
            return result;
        }
	}



	// AI class for implementing the behaviour to target and attack players
	class AIPlayerZombieAttackTarget extends EntityAIBase {
        private EntityPlayerZombie playerZombie = EntityPlayerZombie.this;
        private int attackTick = 0;
        private PlayerZombieMoveTargetPos targetPos = new PlayerZombieMoveTargetPos();

        public AIPlayerZombieAttackTarget() {
            this.setMutexBits(2);
        }

        public boolean attackTargetExists() {
            // see if there's actually a living attack target to aim for
            EntityLivingBase attackTarget = this.playerZombie.getAttackTarget();
            return (attackTarget != null && attackTarget.isEntityAlive());
        }

        @Override
        public boolean shouldExecute() {
            // decrement time since last attack
            if (this.attackTick > 0) {
                --this.attackTick;
            }

            return this.attackTargetExists();
        }

        @Override
        public boolean continueExecuting() {
            // decrement time since last attack
            if (this.attackTick > 0) {
                --this.attackTick;
            }

            if (!this.attackTargetExists()) {
                return false;
            }

            // focus attack on target position
            EntityLivingBase attackTarget = this.playerZombie.getAttackTarget();
            this.targetPos.setTarget(attackTarget.posX, attackTarget.posY, attackTarget.posZ);

            // damage the target if it's in range, and it has been long enough since the last attack
            double damageRange = (double) (this.playerZombie.width + attackTarget.width);
            if (this.attackTick <= 0 && this.targetPos.dist < damageRange) {
                this.playerZombie.attackEntityAsMob(attackTarget);
                this.attackTick = 16; // 16 ticks before next attack
            }

            // see if there's a straight path to the target, if there is, aim for it
            if (this.targetPos.isPathClear(5.0D)) {
                //LogHelper.info(">>> Setting attack target");
                this.playerZombie.getMoveHelper().setMoveTo(attackTarget.posX, attackTarget.posY, attackTarget.posZ, 1.0D);
            }
            //System.out.println("dist:"+this.targetPos.dist+" damageRange:"+damageRange+" attackTick:"+attackTick);
            return true;
        }
    }
}
