package com.ic2additions.objects.items.armor;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import com.ic2additions.util.Reference;
import ic2.api.item.ElectricItem;
import ic2.api.item.HudMode;
import ic2.api.item.IHazmatLike;
import ic2.api.item.IItemHudProvider;
import ic2.core.IC2;
import ic2.core.IC2Potion;
import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.item.ItemTinCan;
import ic2.core.item.armor.ItemArmorElectric;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import ic2.core.item.armor.jetpack.IJetpack;
import ic2.core.ref.ItemName;
import ic2.core.util.ConfigUtil;
import ic2.core.util.StackUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ItemQuantumArmor extends ItemArmorElectric implements IJetpack, IHazmatLike, IItemHudProvider{
    private static final double MAX_CHARGE = 100_000_000D;
    private static final double TRANSFER    = 12000.0;
    private static final int TIER           = 4;

    protected static final Map<Potion, Integer> potionRemovalCost = new IdentityHashMap<>();
    private float jumpCharge;

    public ItemQuantumArmor(String name, EntityEquipmentSlot armorType) {
        super(null, "quantum_suit", armorType, MAX_CHARGE, TRANSFER, TIER);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(IC2.tabIC2);
        setMaxStackSize(1);
        ItemInit.ITEMS.add(this);

        if (armorType == EntityEquipmentSlot.FEET) {
            MinecraftForge.EVENT_BUS.register(this);
        }

        // Same cures as Quantum
        potionRemovalCost.put(MobEffects.POISON, 10000);
        potionRemovalCost.put(IC2Potion.radiation, 10000);
        potionRemovalCost.put(MobEffects.WITHER, 25000);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "ic2additions:textures/armor/quantum_" + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
    }

    @Override
    public boolean addsProtection(EntityLivingBase entity, EntityEquipmentSlot slot, ItemStack stack) {
        return ElectricItem.manager.getCharge(stack) > 0.0D;
    }

    @Override
    public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase entity, ItemStack armor, DamageSource source, double damage, int slot) {
        int energyPerDamage = this.getEnergyPerDamage();
        int damageLimit = Integer.MAX_VALUE;
        if (energyPerDamage > 0) {
            damageLimit = (int) Math.min(damageLimit, 25.0D * ElectricItem.manager.getCharge(armor) / energyPerDamage);
        }

        if (source == DamageSource.FALL) {
            if (this.armorType == EntityEquipmentSlot.FEET) {
                return new ISpecialArmor.ArmorProperties(10, 1.0D, damageLimit);
            }
            if (this.armorType == EntityEquipmentSlot.LEGS) {
                return new ISpecialArmor.ArmorProperties(9, 0.8D, damageLimit);
            }
        }

        double absorptionRatio = this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio();
        return new ISpecialArmor.ArmorProperties(8, absorptionRatio, damageLimit);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onEntityLivingFallEvent(LivingFallEvent event) {
        if (IC2.platform.isSimulating() && event.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            ItemStack armor = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (!StackUtil.isEmpty(armor) && armor.getItem() == this) {
                int fallDamage = Math.max((int) event.getDistance() - 10, 0);
                double energyCost = this.getEnergyPerDamage() * fallDamage;
                if (energyCost <= ElectricItem.manager.getCharge(armor)) {
                    ElectricItem.manager.discharge(armor, energyCost, Integer.MAX_VALUE, true, false, false);
                    event.setCanceled(true);
                }
            }
        }
    }

    @Override
    public double getDamageAbsorptionRatio() {
        return this.armorType == EntityEquipmentSlot.CHEST ? 1.2D : 1.0D;
    }

    @Override
    public int getEnergyPerDamage() {
        return 20000;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        byte toggleTimer = nbtData.getByte("toggleTimer");
        boolean ret = false;

        switch (this.armorType) {
            case HEAD:
                IC2.platform.profilerStartSection("QuantumHelmet");
                int air = player.getAir();
                if (ElectricItem.manager.canUse(stack, 1000.0D) && air < 100) {
                    player.setAir(air + 200);
                    ElectricItem.manager.use(stack, 1000.0D, null);
                    ret = true;
                } else if (air <= 0) {
                    IC2.achievements.issueAchievement(player, "starveWithQHelmet");
                }

                if (ElectricItem.manager.canUse(stack, 1000.0D) && player.getFoodStats().needFood()) {
                    int slot = -1;
                    for (int i = 0; i < player.inventory.mainInventory.size(); ++i) {
                        ItemStack playerStack = player.inventory.mainInventory.get(i);
                        if (!StackUtil.isEmpty(playerStack) && playerStack.getItem() == ItemName.filled_tin_can.getInstance()) {
                            slot = i;
                            break;
                        }
                    }

                    if (slot > -1) {
                        ItemStack playerStack = player.inventory.mainInventory.get(slot);
                        ItemTinCan can = (ItemTinCan) playerStack.getItem();
                        ActionResult<ItemStack> result = can.onEaten(player, playerStack);
                        playerStack = result.getResult();
                        if (StackUtil.isEmpty(playerStack)) {
                            player.inventory.mainInventory.set(slot, StackUtil.emptyStack);
                        }
                        if (result.getType() == EnumActionResult.SUCCESS) {
                            ElectricItem.manager.use(stack, 1000.0D, null);
                        }
                        ret = true;
                    }
                } else if (player.getFoodStats().getFoodLevel() <= 0) {
                    IC2.achievements.issueAchievement(player, "starveWithQHelmet");
                }

                for (PotionEffect effect : new LinkedList<>(player.getActivePotionEffects())) {
                    Potion potion = effect.getPotion();
                    Integer cost = potionRemovalCost.get(potion);
                    if (cost != null) {
                        cost = cost * (effect.getAmplifier() + 1);
                        if (ElectricItem.manager.canUse(stack, cost)) {
                            ElectricItem.manager.use(stack, cost, null);
                            IC2.platform.removePotion(player, potion);
                        }
                    }
                }

                boolean Nightvision = nbtData.getBoolean("Nightvision");
                short hubmode = nbtData.getShort("HudMode");
                if (IC2.keyboard.isAltKeyDown(player) && IC2.keyboard.isModeSwitchKeyDown(player) && toggleTimer == 0) {
                    toggleTimer = 10;
                    Nightvision = !Nightvision;
                    if (IC2.platform.isSimulating()) {
                        nbtData.setBoolean("Nightvision", Nightvision);
                        IC2.platform.messagePlayer(player, Nightvision ? "Nightvision enabled." : "Nightvision disabled.");
                    }
                }

                if (IC2.keyboard.isAltKeyDown(player) && IC2.keyboard.isHudModeKeyDown(player) && toggleTimer == 0) {
                    toggleTimer = 10;
                    if (hubmode == HudMode.getMaxMode()) hubmode = 0; else ++hubmode;
                    if (IC2.platform.isSimulating()) {
                        nbtData.setShort("HudMode", hubmode);
                        IC2.platform.messagePlayer(player, Localization.translate(HudMode.getFromID(hubmode).getTranslationKey()));
                    }
                }

                if (IC2.platform.isSimulating() && toggleTimer > 0) {
                    --toggleTimer;
                    nbtData.setByte("toggleTimer", toggleTimer);
                }

                if (Nightvision && IC2.platform.isSimulating() && ElectricItem.manager.use(stack, 1.0D, player)) {
                    BlockPos pos = new BlockPos((int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ));
                    int skylight = player.getEntityWorld().getLightFromNeighbors(pos);
                    if (skylight > 8) {
                        IC2.platform.removePotion(player, MobEffects.NIGHT_VISION);
                        player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0, true, true));
                    } else {
                        IC2.platform.removePotion(player, MobEffects.BLINDNESS);
                        player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0, true, true));
                    }
                    ret = true;
                }

                IC2.platform.profilerEndSection();
                break;

            case CHEST:
                IC2.platform.profilerStartSection("QuantumBodyarmor");
                player.extinguish();
                IC2.platform.profilerEndSection();
                break;

            case LEGS:
                IC2.platform.profilerStartSection("QuantumLeggings");
                boolean enableQuantumSpeedOnSprint = IC2.platform.isRendering()
                        ? ConfigUtil.getBool(MainConfig.get(), "misc/quantumSpeedOnSprint")
                        : true;

                if (ElectricItem.manager.canUse(stack, 1000.0D)
                        && (player.onGround || player.isInWater())
                        && IC2.keyboard.isForwardKeyDown(player)
                        && ((enableQuantumSpeedOnSprint && player.isSprinting())
                        || (!enableQuantumSpeedOnSprint && IC2.keyboard.isBoostKeyDown(player)))) {

                    byte speedTicker = nbtData.getByte("speedTicker");
                    ++speedTicker;
                    if (speedTicker >= 10) {
                        speedTicker = 0;
                        ElectricItem.manager.use(stack, 1000.0D, null);
                        ret = true;
                    }
                    nbtData.setByte("speedTicker", speedTicker);

                    float speed = 0.22F;
                    if (player.isInWater()) {
                        speed = 0.1F;
                        if (IC2.keyboard.isJumpKeyDown(player)) {
                            player.motionY += 0.1D;
                        }
                    }
                    if (speed > 0.0F) player.moveRelative(0.0F, 0.0F, 1.0F, speed);
                }

                IC2.platform.profilerEndSection();
                break;

            case FEET:
                IC2.platform.profilerStartSection("QuantumBoots");
                if (IC2.platform.isSimulating()) {
                    boolean wasOnGround = nbtData.hasKey("wasOnGround") ? nbtData.getBoolean("wasOnGround") : true;
                    if (wasOnGround && !player.onGround && IC2.keyboard.isJumpKeyDown(player) && IC2.keyboard.isBoostKeyDown(player)) {
                        ElectricItem.manager.use(stack, 4000.0D, null);
                        ret = true;
                    }
                    if (player.onGround != wasOnGround) {
                        nbtData.setBoolean("wasOnGround", player.onGround);
                    }
                } else {
                    if (ElectricItem.manager.canUse(stack, 4000.0D) && player.onGround) {
                        this.jumpCharge = 1.0F;
                    }
                    if (player.motionY >= 0.0D && this.jumpCharge > 0.0F && !player.isInWater()) {
                        if (IC2.keyboard.isJumpKeyDown(player) && IC2.keyboard.isBoostKeyDown(player)) {
                            if (this.jumpCharge == 1.0F) {
                                player.motionX *= 3.5D;
                                player.motionZ *= 3.5D;
                            }
                            player.motionY += this.jumpCharge * 0.3F;
                            this.jumpCharge *= 0.75F;
                        } else if (this.jumpCharge < 1.0F) {
                            this.jumpCharge = 0.0F;
                        }
                    }
                }
                IC2.platform.profilerEndSection();
                break;
        }

        if (ret) {
            player.inventoryContainer.detectAndSendChanges();
        }
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean drainEnergy(ItemStack pack, int amount) {
        return ElectricItem.manager.discharge(pack, amount + 6.0D, Integer.MAX_VALUE, true, false, false) > 0.0D;
    }

    @Override
    public float getPower(ItemStack stack) {
        return 1.0F;
    }

    @Override
    public float getDropPercentage(ItemStack stack) {
        return 0.05F;
    }

    @Override
    public double getChargeLevel(ItemStack stack) {
        return ElectricItem.manager.getCharge(stack) / this.getMaxCharge(stack);
    }

    @Override
    public boolean isJetpackActive(ItemStack stack) {
        return true;
    }

    @Override
    public float getHoverMultiplier(ItemStack stack, boolean upwards) {
        return 0.1F;
    }

    @Override
    public float getWorldHeightDivisor(ItemStack stack) {
        return 0.9F;
    }

    @Override
    public boolean doesProvideHUD(ItemStack stack) {
        return this.armorType == EntityEquipmentSlot.HEAD && ElectricItem.manager.getCharge(stack) > 0.0D;
    }

    @Override
    public HudMode getHudMode(ItemStack stack) {
        return HudMode.getFromID(StackUtil.getOrCreateNbtData(stack).getShort("HudMode"));
    }

    // Match your addon’s unlocalized name pattern
    @Override
    public String getUnlocalizedName() {
        return "item." + Reference.MODID + "." + getRegistryName().getResourcePath();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName();
    }

    @Override
    public String getUnlocalizedNameInefficiently(ItemStack stack) {
        return getUnlocalizedName();
    }

    public static boolean hasFullSet(EntityLivingBase e) {
        if (e == null) return false;
        int count = 0;
        for (ItemStack s : e.getArmorInventoryList()) {
            if (!s.isEmpty() && s.getItem() instanceof ItemAdvancedQuantumArmor) count++;
        }
        return count == 4;
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!hasFullSet(event.getEntityLiving())) return;
        EntityLivingBase entity = event.getEntityLiving();
        if (entity.isBurning()) {
            entity.extinguish();
        }
        entity.addPotionEffect(new PotionEffect(MobEffects.HASTE, 20, 1, true, false));
        entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 20, 1, true, false));
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (!hasFullSet(event.getEntityLiving())) return;
        DamageSource src = event.getSource();
        if (src.isFireDamage() || src == DamageSource.HOT_FLOOR || src == DamageSource.LAVA) {
            event.setCanceled(true);
        }
    }

    @Override
    public boolean fullyProtects(EntityLivingBase entity, EntityEquipmentSlot slot, ItemStack stack) {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

//        tooltip.add(TextFormatting.BLUE + I18n.format("tooltip.ic2additions.nightvision"));
//        tooltip.add(TextFormatting.BLUE + I18n.format("tooltip.ic2additions.jetpack"));
//        tooltip.add(TextFormatting.BLUE + I18n.format("tooltip.ic2additions.highjump"));
//        tooltip.add(TextFormatting.BLUE + I18n.format("tooltip.ic2additions.fastwalk"));
//
//        tooltip.add(TextFormatting.GOLD + I18n.format("tooltip.ic2additions.thermohazmat_set"));
//        tooltip.add("  " + TextFormatting.AQUA + I18n.format("tooltip.ic2additions.haste_2"));
//        tooltip.add("  " + TextFormatting.DARK_RED + I18n.format("tooltip.ic2additions.strength_2"));
//        tooltip.add("  " + TextFormatting.DARK_GREEN + I18n.format("tooltip.ic2additions.radiation_protection"));
//        tooltip.add("  " + TextFormatting.RED + I18n.format("tooltip.ic2additions.fire_prox.protects_fire"));
    }


}

