package com.ic2additions.objects.items.armor;
import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import com.ic2additions.init.SoundInit;
import com.ic2additions.util.IC2AdditionsKeys;
import com.ic2additions.util.Reference;
import ic2.api.item.ElectricItem;
import ic2.api.item.HudMode;
import ic2.api.item.IHazmatLike;
import ic2.api.item.IItemHudProvider;
import ic2.core.IC2;
import ic2.core.init.Localization;
import ic2.core.item.armor.ItemArmorElectric;
import ic2.core.item.armor.jetpack.IBoostingJetpack;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public class ItemArmorMolecular extends ItemArmorElectric implements IBoostingJetpack, IItemHudProvider, IHazmatLike {

    // Existing tags
    public static final String TAG_FLY = "isFlyActive";
    public static final String TAG_HOVER = "hoverMode";
    public static final String TAG_TOGGLE_TIMER = "toggleTimer";
    public static final String TAG_FIRE_MODE = "fireMode";

    // Hand fastbreak enable (true in HAND or HAND_SILK)
    public static final String TAG_HAND_FASTBREAK = "handFastbreak";

    // Puncher mode enable
    public static final String TAG_PUNCH_MODE = "punchMode";

    // Hand sub-mode (like Vajra)
    private static final String NBT_HAND_MODE = "handMode"; // "NONE" or "SILK"
    private static final String MODE_NONE = "NONE";
    private static final String MODE_SILK = "SILK";

    // Mode cursor: 0=FIRE, 1=HAND, 2=HAND_SILK, 3=PUNCH, 4=NONE
    private static final String TAG_MODE_CURSOR = "modeCursor";

    // Balance
    private static final float IGNORE_BELOW = 20.0F;
    private static final float HAND_SPEED = 20000.0f;
    private static final double FASTBREAK_COST_PER_PIECE = 1_000_000D; // your last value
    private static final double PUNCH_COST_TOTAL = 5_000_000D;         // total per punch (spread-drain)
    private static final float PUNCH_DAMAGE = 20.0F;
    private static final float PUNCH_KNOCKBACK = 6.0F;

    private float jumpCharge = 0.0F;

    public ItemArmorMolecular(String name, EntityEquipmentSlot slot){
        super(null, "molecular", slot, 750_000_000, 131072D, 7);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setMaxStackSize(1);
        ItemInit.ITEMS.add(this);
        if (armorType == EntityEquipmentSlot.FEET) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    // ---------- Armor behavior ----------
    @Override public double getDamageAbsorptionRatio() { return 2.5D; }
    @Override public int getEnergyPerDamage() { return 50000; }

    private static boolean isChest(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemArmorMolecular
                && ((ItemArmorMolecular) stack.getItem()).armorType == EntityEquipmentSlot.CHEST;
    }

    // ---------- Jetpack ----------
    public static boolean isJetpackOn(ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack).getBoolean(TAG_FLY);
    }
    public static boolean isHovering(ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack).getBoolean(TAG_HOVER);
    }
    public static boolean switchJetpack(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        boolean on = !nbt.getBoolean(TAG_FLY);
        nbt.setBoolean(TAG_FLY, on);
        return on;
    }
    public static boolean switchHover(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        boolean on = !nbt.getBoolean(TAG_HOVER);
        nbt.setBoolean(TAG_HOVER, on);
        return on;
    }

    // ---------- Modes ----------
    private static String readHandMode(ItemStack stack) {
        String m = StackUtil.getOrCreateNbtData(stack).getString(NBT_HAND_MODE);
        return MODE_SILK.equals(m) ? MODE_SILK : MODE_NONE;
    }
    private static void writeHandMode(ItemStack stack, String mode) {
        StackUtil.getOrCreateNbtData(stack).setString(NBT_HAND_MODE, MODE_SILK.equals(mode) ? MODE_SILK : MODE_NONE);
    }
    private static boolean isHandSilk(ItemStack stack) {
        return MODE_SILK.equals(readHandMode(stack));
    }
    private static boolean isHandFastbreak(ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack).getBoolean(TAG_HAND_FASTBREAK);
    }
    private static void setHandFastbreak(ItemStack stack, boolean on) {
        StackUtil.getOrCreateNbtData(stack).setBoolean(TAG_HAND_FASTBREAK, on);
    }
    private static boolean isPunchMode(ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack).getBoolean(TAG_PUNCH_MODE);
    }
    private static void setPunchMode(ItemStack stack, boolean on) {
        StackUtil.getOrCreateNbtData(stack).setBoolean(TAG_PUNCH_MODE, on);
    }

    private static int getModeCursor(ItemStack chest) {
        return StackUtil.getOrCreateNbtData(chest).getInteger(TAG_MODE_CURSOR);
    }
    private static void setModeCursor(ItemStack chest, int v) {
        // 5 modes now
        StackUtil.getOrCreateNbtData(chest).setInteger(TAG_MODE_CURSOR, ((v % 5) + 5) % 5);
    }
    private static int nextCursor(int v) { return (v + 1) % 5; }

    private static String loc(String key) { return Localization.translate(key); }

    private static boolean anyModeActive(ItemStack chest) {
        if (StackUtil.isEmpty(chest)) return false;
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(chest);
        return nbt.getBoolean(TAG_FIRE_MODE)
                || nbt.getBoolean(TAG_HAND_FASTBREAK)
                || MODE_SILK.equals(nbt.getString(NBT_HAND_MODE))
                || nbt.getBoolean(TAG_PUNCH_MODE);
    }

    private static String currentModeLabel(ItemStack chest) {
        boolean fire = StackUtil.getOrCreateNbtData(chest).getBoolean(TAG_FIRE_MODE);
        boolean hand = isHandFastbreak(chest);
        boolean silk = isHandSilk(chest);
        boolean punch = isPunchMode(chest);

        if (fire)  return loc("ic2additions.mode.fire");
        if (hand)  return silk ? loc("ic2additions.mode.hand_silk") : loc("ic2additions.mode.hand");
        if (punch) return loc("ic2additions.mode.punch"); // NEW
        return loc("ic2additions.mode.none");
    }

    private static void sendSuitModeMsg(EntityPlayer player, ItemStack chest) {
        if (player.world.isRemote) return;
        String label = currentModeLabel(chest);
        TextComponentTranslation msg = new TextComponentTranslation("ic2additions.message.suit_mode", label);
        msg.setStyle(new Style().setColor(TextFormatting.YELLOW));
        player.sendMessage(msg);
    }

    private static void setSuitFireModeSilent(ItemStack chest, boolean on) {
        StackUtil.getOrCreateNbtData(chest).setBoolean(TAG_FIRE_MODE, on);
    }

    private static void setNoneAndAnnounce(EntityPlayer player, ItemStack chest) {
        // force none
        setSuitFireModeSilent(chest, false);
        setHandFastbreak(chest, false);
        setPunchMode(chest, false);
        writeHandMode(chest, MODE_NONE);
        setModeCursor(chest, 4); // NONE index in 5-mode cycle
        sendSuitModeMsg(player, chest); // "Suit Mode: None"
    }

    private static void applyModeByCursor(EntityPlayer player, ItemStack chest, int cursor) {
        // reset all
        setSuitFireModeSilent(chest, false);
        setHandFastbreak(chest, false);
        setPunchMode(chest, false);
        writeHandMode(chest, MODE_NONE);

        switch (cursor) {
            case 0: // FIRE
                setSuitFireModeSilent(chest, true);
                break;
            case 1: // HAND
                setHandFastbreak(chest, true);
                break;
            case 2: // HAND_SILK
                setHandFastbreak(chest, true);
                writeHandMode(chest, MODE_SILK);
                break;
            case 3: // PUNCH
                setPunchMode(chest, true);
                break;
            case 4: // NONE
            default:
                // all off
                break;
        }
        sendSuitModeMsg(player, chest);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);

        // ====================== HEAD ======================
        if (this.armorType == EntityEquipmentSlot.HEAD) {
            byte timer = nbt.getByte(TAG_TOGGLE_TIMER);
            boolean nightvision = nbt.getBoolean("Nightvision");
            short hudMode = nbt.getShort("HudMode");

            if (IC2.keyboard.isAltKeyDown(player) && IC2.keyboard.isModeSwitchKeyDown(player) && timer == 0) {
                nbt.setByte(TAG_TOGGLE_TIMER, (byte)10);
                nightvision = !nightvision;
                nbt.setBoolean("Nightvision", nightvision);
                if (IC2.platform.isSimulating()) {
                    IC2.platform.messagePlayer(player, nightvision ? "Nightvision enabled." : "Nightvision disabled.");
                }
            }
            if (IC2.keyboard.isAltKeyDown(player) && IC2.keyboard.isHudModeKeyDown(player) && timer == 0) {
                nbt.setByte(TAG_TOGGLE_TIMER, (byte)10);
                if (hudMode == HudMode.getMaxMode()) hudMode = 0; else ++hudMode;
                nbt.setShort("HudMode", hudMode);
                if (IC2.platform.isSimulating()) {
                    IC2.platform.messagePlayer(player, Localization.translate(HudMode.getFromID(hudMode).getTranslationKey()));
                }
            }
            if (nightvision && IC2.platform.isSimulating() && ElectricItem.manager.use(stack, 1.0D, player)) {
                BlockPos pos = new BlockPos((int)player.posX, (int)player.posY, (int)player.posZ);
                int skylight = player.world.getLightFromNeighbors(pos);
                if (skylight > 8) {
                    IC2.platform.removePotion(player, MobEffects.NIGHT_VISION);
                    player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0, true, true));
                } else {
                    IC2.platform.removePotion(player, MobEffects.BLINDNESS);
                    player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0, true, true));
                }
            }

            byte cur = nbt.getByte(TAG_TOGGLE_TIMER);
            if (cur > 0) nbt.setByte(TAG_TOGGLE_TIMER, (byte)(cur - 1));
            return;
        }

        // ====================== CHEST ======================
        if (isChest(stack)) {
            byte timer = nbt.getByte(TAG_TOGGLE_TIMER);

            // If the set is no longer complete, hard-drop to NONE once.
            if (!hasFullSet(player) && anyModeActive(stack)) {
                setNoneAndAnnounce(player, stack);
            }

            // C key: cycle FIRE -> HAND -> HAND_SILK -> PUNCH -> NONE, single localized message
            if (IC2AdditionsKeys.isModeKeyDown(player) && timer == 0) {
                nbt.setByte(TAG_TOGGLE_TIMER, (byte)10);
                ItemStack chest = stack;
                int cursor = getModeCursor(chest);
                applyModeByCursor(player, chest, cursor);
                setModeCursor(chest, nextCursor(cursor));
            }

            // Jetpack toggle
            if (IC2AdditionsKeys.isFlyKeyDown(player) && timer == 0) {
                nbt.setByte(TAG_TOGGLE_TIMER, (byte)10);
                if (!world.isRemote) {
                    boolean on = switchJetpack(stack);
                    String mode = on
                            ? TextFormatting.DARK_GREEN + Localization.translate("ic2additions.message.on")
                            : TextFormatting.DARK_RED   + Localization.translate("ic2additions.message.off");
                    TextComponentTranslation msg = new TextComponentTranslation("ic2additions.message.jetpackSwitch", mode);
                    msg.setStyle(new Style().setColor(TextFormatting.YELLOW));
                    player.sendMessage(msg);
                }
            }

            // Hover toggle
            if (IC2AdditionsKeys.isHoverKeyDown(player) && timer == 0) {
                nbt.setByte(TAG_TOGGLE_TIMER, (byte)10);
                if (!world.isRemote) {
                    boolean hover = switchHover(stack);
                    String mode = hover
                            ? TextFormatting.DARK_GREEN + Localization.translate("ic2additions.message.hover_on")
                            : TextFormatting.DARK_RED   + Localization.translate("ic2additions.message.hover_off");
                    TextComponentTranslation msg = new TextComponentTranslation("ic2additions.message.hoverSwitch", mode);
                    msg.setStyle(new Style().setColor(TextFormatting.YELLOW));
                    player.sendMessage(msg);
                }
            }

            // Silent upkeep for fire mode
            if (!world.isRemote && hasFullSet(player) && nbt.getBoolean(TAG_FIRE_MODE)) {
                if (!drainFromSet(player, 50_000D)) {
                    setSuitFireModeSilent(stack, false);
                    TextComponentTranslation msg = new TextComponentTranslation("ic2additions.message.suit_fire_mode_auto_off");
                    msg.setStyle(new Style().setColor(TextFormatting.DARK_RED));
                    player.sendMessage(msg);
                }
            }

            byte cur = nbt.getByte(TAG_TOGGLE_TIMER);
            if (cur > 0) nbt.setByte(TAG_TOGGLE_TIMER, (byte)(cur - 1));
            return;
        }

        // ====================== LEGS ======================
        if (this.armorType == EntityEquipmentSlot.LEGS) {
            boolean forward = ic2.core.IC2.keyboard.isForwardKeyDown(player);
            boolean sprintOrBoost = player.isSprinting() || ic2.core.IC2.keyboard.isBoostKeyDown(player);
            boolean canUse = ElectricItem.manager.canUse(stack, 1000.0D);

            if (canUse && forward && (player.onGround || player.isInWater()) && sprintOrBoost) {
                byte speedTicker = nbt.getByte("speedTicker");
                speedTicker++;
                if (speedTicker >= 10) {
                    speedTicker = 0;
                    ElectricItem.manager.use(stack, 1000.0D, null);
                }
                nbt.setByte("speedTicker", speedTicker);

                float speed = 0.22F;
                if (player.isInWater()) {
                    speed = 0.10F;
                    if (ic2.core.IC2.keyboard.isJumpKeyDown(player)) {
                        player.motionY += 0.10D;
                    }
                }
                if (speed > 0.0F) {
                    player.moveRelative(0.0F, 0.0F, 1.0F, speed);
                }
            }
            return;
        }

        // ====================== FEET ======================
        if (this.armorType == EntityEquipmentSlot.FEET) {
            if (ic2.core.IC2.platform.isSimulating()) {
                boolean wasOnGround = nbt.hasKey("wasOnGround") ? nbt.getBoolean("wasOnGround") : true;

                if (wasOnGround && !player.onGround && ic2.core.IC2.keyboard.isJumpKeyDown(player) && ic2.core.IC2.keyboard.isBoostKeyDown(player)) {
                    if (ElectricItem.manager.canUse(stack, 4000.0D)) {
                        ElectricItem.manager.use(stack, 4000.0D, null);
                    }
                }

                if (player.onGround != wasOnGround) {
                    nbt.setBoolean("wasOnGround", player.onGround);
                }
            } else {
                if (ElectricItem.manager.canUse(stack, 4000.0D) && player.onGround) {
                    this.jumpCharge = 1.0F;
                }
                if (player.motionY >= 0.0D && this.jumpCharge > 0.0F && !player.isInWater()) {
                    if (ic2.core.IC2.keyboard.isJumpKeyDown(player) && ic2.core.IC2.keyboard.isBoostKeyDown(player)) {
                        if (this.jumpCharge == 1.0F) {
                            player.motionX *= 3.5D;
                            player.motionZ *= 3.5D;
                        }
                        player.motionY += this.jumpCharge * 0.30F;
                        this.jumpCharge *= 0.75F;
                    } else if (this.jumpCharge < 1.0F) {
                        this.jumpCharge = 0.0F;
                    }
                }
            }
        }
    }

    // IBoostingJetpack
    @Override public boolean isJetpackActive(ItemStack stack) { return isChest(stack) && isJetpackOn(stack); }
    @Override public double getChargeLevel(ItemStack stack) { return ElectricItem.manager.getCharge(stack) / getMaxCharge(stack); }
    @Override public float getPower(ItemStack stack) { return 1.0F; }
    @Override public float getDropPercentage(ItemStack stack) { return 0.05F; }
    @Override public float getWorldHeightDivisor(ItemStack stack) { return 1.0F; }
    @Override public float getHoverMultiplier(ItemStack stack, boolean upwards) { return 0.3F; }
    @Override public float getBaseThrust(ItemStack stack, boolean hover) { return hover ? 0.65f : 0.30f; }
    @Override public float getBoostThrust(EntityPlayer p, ItemStack s, boolean hover) {return IC2.keyboard.isBoostKeyDown(p) && ElectricItem.manager.getCharge(s) >= 60.0 ? (hover ? 0.07f : 0.09f) : 0.0f;}
    @Override public boolean useBoostPower(ItemStack s, float amt) {return ElectricItem.manager.discharge(s, 60.0, Integer.MAX_VALUE, true, false, false) > 0.0;}
    @Override public float getHoverBoost(EntityPlayer p, ItemStack s, boolean up) {
        if (IC2.keyboard.isBoostKeyDown(p) && ElectricItem.manager.getCharge(s) >= 60.0) {
            if (!p.onGround) ElectricItem.manager.discharge(s, 60.0, Integer.MAX_VALUE, true, false, false);
            return 2.0f;
        }
        return 1.0f;
    }
    @Override public boolean drainEnergy(ItemStack s, int amt) { return ElectricItem.manager.discharge(s, amt * 6.0, Integer.MAX_VALUE, true, false, false) > 0.0; }
    @Override public boolean doesProvideHUD(ItemStack stack) { return armorType == EntityEquipmentSlot.HEAD && ElectricItem.manager.getCharge(stack) > 0; }
    @Override public HudMode getHudMode(ItemStack stack) { return HudMode.getFromID(StackUtil.getOrCreateNbtData(stack).getShort("HudMode")); }

    // Fall protection
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

    // Kill protection
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onCommand(CommandEvent event) {
        if (event.getCommand() == null) return;
        if (!"kill".equalsIgnoreCase(event.getCommand().getName())) return;
        if (!(event.getSender() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.getSender();
        if (!hasFullSet(player)) return;
        String[] args = event.getParameters();
        boolean selfKill = args.length == 0 || player.getName().equalsIgnoreCase(args[0]);
        if (!selfKill) return;
        event.setCanceled(true);
        if (!player.world.isRemote) {
            TextComponentTranslation msg = new TextComponentTranslation("ic2additions.message.kill_protection");
            msg.setStyle(new Style().setColor(TextFormatting.DARK_RED));
            player.sendMessage(msg);
        }
    }

    // Fire retaliation
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer p = (EntityPlayer) event.getEntityLiving();
        if (!hasFullSet(p)) return;

        ItemStack chest = p.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (!StackUtil.isEmpty(chest) && chest.getItem() instanceof ItemArmorMolecular) {
            if (StackUtil.getOrCreateNbtData(chest).getBoolean(TAG_FIRE_MODE)) {
                EntityLivingBase src = (event.getSource().getTrueSource() instanceof EntityLivingBase)
                        ? (EntityLivingBase) event.getSource().getTrueSource() : null;
                if (src != null) src.setFire(5);
            }
        }
        if (!hasFullSet(event.getEntityLiving())) return;
        DamageSource src = event.getSource();
        if (src.isFireDamage() || src == DamageSource.HOT_FLOOR || src == DamageSource.LAVA) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerAttack(net.minecraftforge.event.entity.player.AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player == null || player.capabilities.isCreativeMode) return;

        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (StackUtil.isEmpty(chest) || !(chest.getItem() instanceof ItemArmorMolecular)) return;

        // Check punch mode
        if (StackUtil.getOrCreateNbtData(chest).getBoolean(TAG_PUNCH_MODE)) {
            if (event.getTarget() instanceof EntityLivingBase) {
                EntityLivingBase target = (EntityLivingBase) event.getTarget();

                if (ElectricItem.manager.canUse(chest, 1_000_000D)) {
                    ElectricItem.manager.use(chest, 1_000_000D, null);

                    // Apply damage + knockback
                    target.attackEntityFrom(DamageSource.causePlayerDamage(player), 20.0F);
                    target.knockBack(player, 6.0F,
                            Math.sin(Math.toRadians(player.rotationYaw)),
                            -Math.cos(Math.toRadians(player.rotationYaw)));

                    // Play sound: client guarantees local playback; server broadcasts
                    if (player.world.isRemote) {
                        player.playSound(SoundInit.MOLECULAR_PUNCH, 1.0F, 1.0F);
                    } else {
                        player.world.playSound(
                                null,
                                target.posX, target.posY, target.posZ,
                                SoundInit.MOLECULAR_PUNCH,
                                target.getSoundCategory(),
                                1.0F, 1.0F
                        );
                    }

                    // Cancel vanilla weak punch
                    event.setCanceled(true);
                }
            }
        }
    }

    // Minor damage ignore
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer p = (EntityPlayer) event.getEntityLiving();
        if (!hasFullSet(p)) return;

        if (event.getAmount() < IGNORE_BELOW) {
            event.setCanceled(true);
            event.setAmount(0.0F);
            p.hurtTime = 0;
            p.maxHurtTime = 0;
            p.attackedAtYaw = 0;
            p.hurtResistantTime = 0;
        }
    }

    // Auto-extinguish when full set
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (hasFullSet(event.getEntityLiving()) && event.getEntityLiving().isBurning()) {
            event.getEntityLiving().extinguish();
        }
    }

    // NEW: drop mode to None on equipment change
    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();

        if (!hasFullSet(player)) {
            ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (!StackUtil.isEmpty(chest) && chest.getItem() instanceof ItemArmorMolecular) {
                if (anyModeActive(chest)) {
                    setNoneAndAnnounce(player, chest);
                }
            }
        }
    }

    public static boolean hasFullSet(EntityLivingBase e) {
        if (e == null) return false;
        int count = 0;
        for (ItemStack s : e.getArmorInventoryList()) {
            if (!StackUtil.isEmpty(s) && s.getItem() instanceof ItemArmorMolecular) count++;
        }
        return count == 4;
    }
    private static boolean isMolecularPiece(ItemStack s, EntityEquipmentSlot slot) {
        return !StackUtil.isEmpty(s) && s.getItem() instanceof ItemArmorMolecular
                && ((ItemArmorMolecular) s.getItem()).armorType == slot;
    }
    private static double charge(ItemStack s) {
        return StackUtil.isEmpty(s) ? 0 : ElectricItem.manager.getCharge(s);
    }

    public static boolean drainFromSet(EntityPlayer p, double amountPerTick) {
        ItemStack head  = p.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack chest = p.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack legs  = p.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        ItemStack feet  = p.getItemStackFromSlot(EntityEquipmentSlot.FEET);

        ArrayList<ItemStack> pieces = new ArrayList<>(4);
        if (isMolecularPiece(head,  EntityEquipmentSlot.HEAD)  && charge(head)  > 0) pieces.add(head);
        if (isMolecularPiece(chest, EntityEquipmentSlot.CHEST) && charge(chest) > 0) pieces.add(chest);
        if (isMolecularPiece(legs,  EntityEquipmentSlot.LEGS)  && charge(legs)  > 0) pieces.add(legs);
        if (isMolecularPiece(feet,  EntityEquipmentSlot.FEET)  && charge(feet)  > 0) pieces.add(feet);
        if (pieces.isEmpty()) return false;

        double total = 0;
        for (ItemStack s : pieces) total += charge(s);
        if (total + 1e-6 < amountPerTick) return false;

        double remaining = amountPerTick;
        for (int pass = 0; pass < 4 && remaining > 1e-6 && !pieces.isEmpty(); pass++) {
            int n = pieces.size();
            double perShare = remaining / n;

            Iterator<ItemStack> it = pieces.iterator();
            while (it.hasNext()) {
                ItemStack s = it.next();
                double avail = charge(s);
                double want = Math.min(perShare, avail);
                if (want <= 0) { it.remove(); continue; }
                double pulled = ElectricItem.manager.discharge(s, want, Integer.MAX_VALUE, true, false, false);
                remaining -= pulled;
            }
            pieces.removeIf(ps -> charge(ps) <= 1e-6);
        }
        return remaining <= 1e-6;
    }

    private static boolean drainEachPiece(EntityPlayer p, double amountPerPiece) {
        ItemStack head  = p.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack chest = p.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack legs  = p.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        ItemStack feet  = p.getItemStackFromSlot(EntityEquipmentSlot.FEET);

        List<ItemStack> pieces = new ArrayList<>(4);
        if (isMolecularPiece(head,  EntityEquipmentSlot.HEAD))  pieces.add(head);
        if (isMolecularPiece(chest, EntityEquipmentSlot.CHEST)) pieces.add(chest);
        if (isMolecularPiece(legs,  EntityEquipmentSlot.LEGS))  pieces.add(legs);
        if (isMolecularPiece(feet,  EntityEquipmentSlot.FEET))  pieces.add(feet);

        if (pieces.size() != 4) return false;

        for (ItemStack s : pieces) {
            if (ElectricItem.manager.getCharge(s) + 1e-6 < amountPerPiece) return false;
        }
        for (ItemStack s : pieces) {
            double drained = ElectricItem.manager.discharge(s, amountPerPiece, Integer.MAX_VALUE, true, false, false);
            if (drained + 1e-6 < amountPerPiece) return false;
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.BLUE + I18n.format("tooltip.ic2additions.nightvision"));
        tooltip.add(TextFormatting.BLUE + I18n.format("tooltip.ic2additions.jetpack"));
        tooltip.add(TextFormatting.BLUE + I18n.format("tooltip.ic2additions.highjump"));
        tooltip.add(TextFormatting.BLUE + I18n.format("tooltip.ic2additions.fastwalk"));
        tooltip.add(TextFormatting.AQUA + I18n.format("tooltip.ic2additions.thermohazmat_set"));
        tooltip.add("  " + TextFormatting.BOLD + I18n.format("tooltip.ic2additions.armor_firemode"));
        tooltip.add("  " + TextFormatting.BOLD + I18n.format("tooltip.ic2additions.armor_fastbreak"));
        tooltip.add("  " + TextFormatting.BOLD + I18n.format("tooltip.ic2additions.armor_fastbreaksilk"));
        tooltip.add("  " + TextFormatting.BOLD + I18n.format("tooltip.ic2additions.armor_hardpunch"));
        tooltip.add("  " + TextFormatting.DARK_GREEN + I18n.format("tooltip.ic2additions.radiation_protection"));
        tooltip.add("  " + TextFormatting.RED + I18n.format("tooltip.ic2additions.fire_prox.protects_fire"));
        tooltip.add("  " + TextFormatting.RED + I18n.format("tooltip.ic2additions.fall_damage_neutralization"));
        tooltip.add("  " + TextFormatting.DARK_RED + I18n.format("tooltip.ic2additions.armor_cannotbekilled"));
    }

    //Fastbreak
    private static boolean fullSetAndFastbreakOn(EntityPlayer player) {
        if (player == null) return false;
        if (!hasFullSet(player)) return false;
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        return !StackUtil.isEmpty(chest)
                && chest.getItem() instanceof ItemArmorMolecular
                && isHandFastbreak(chest);
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        EntityPlayer p = event.getEntityPlayer();
        if (p == null || p.capabilities.isCreativeMode) return;
        if (!fullSetAndFastbreakOn(p)) return;

        IBlockState state = event.getState();
        if (state == null) return;
        Block block = state.getBlock();
        if (block == Blocks.BEDROCK) return;

        if (!p.getHeldItemMainhand().isEmpty()) return;

        event.setNewSpeed(HAND_SPEED);
    }

    @SubscribeEvent
    public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        EntityPlayer p = event.getEntityPlayer();
        if (p == null) return;
        if (!fullSetAndFastbreakOn(p)) return;

        IBlockState state = event.getTargetBlock();
        if (state == null) return;
        if (state.getBlock() == Blocks.BEDROCK) return;

        event.setCanHarvest(true);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer p = event.getPlayer();
        if (p == null || p.capabilities.isCreativeMode) return;
        if (!fullSetAndFastbreakOn(p)) return;

        IBlockState state = event.getState();
        if (state == null) return;
        if (state.getBlock() == Blocks.BEDROCK) return;

        if (!p.getHeldItemMainhand().isEmpty()) return;

        if (!drainEachPiece(p, FASTBREAK_COST_PER_PIECE)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        EntityPlayer ep = event.getEntityPlayer();
        if (!(ep instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP) ep;
        if (player.capabilities.isCreativeMode) return;
        if (!fullSetAndFastbreakOn(player)) return;
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (!isHandSilk(chest)) return;
        if (!player.getHeldItemMainhand().isEmpty()) return;
        World world = player.world;
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
        if (state == null || state.getBlock() == Blocks.BEDROCK) return;
        GameType gt = player.interactionManager.getGameType();
        int xp = ForgeHooks.onBlockBreakEvent(world, gt, player, pos);
        if (xp < 0) return;
        if (!drainFromSet(player, FASTBREAK_COST_PER_PIECE * 4)) return; // keep parity if you want total cost; adjust as needed
        ItemStack fake = new ItemStack(Blocks.STONE);
        Map<Enchantment,Integer> ench = new HashMap<>();
        ench.put(Enchantments.SILK_TOUCH, 1);
        EnchantmentHelper.setEnchantments(ench, fake);
        state.getBlock().onBlockHarvested(world, pos, state, player);
        if (state.getBlock().removedByPlayer(state, world, pos, player, true)) {
            state.getBlock().onBlockDestroyedByPlayer(world, pos, state);
            state.getBlock().harvestBlock(world, player, pos, state, world.getTileEntity(pos), fake);
            if (xp > 0) state.getBlock().dropXpOnBlockBreak(world, pos, xp);
        }
        world.playEvent(2001, pos, Block.getStateId(state));
        player.connection.sendPacket(new SPacketBlockChange(world, pos));
        event.setCanceled(true);
    }

    // NEW: Puncher mode combat handler
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAttackEntity(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player == null || player.world.isRemote) return;
        if (player.capabilities.isCreativeMode) return;
        if (!hasFullSet(player)) return;

        // must have chestplate with punch mode on
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (StackUtil.isEmpty(chest) || !(chest.getItem() instanceof ItemArmorMolecular)) return;
        if (!isPunchMode(chest)) return;

        // only unarmed punches are empowered
        if (!player.getHeldItemMainhand().isEmpty()) return;

        Entity target = event.getTarget();
        if (!(target instanceof EntityLivingBase)) return;
        EntityLivingBase victim = (EntityLivingBase) target;

        // drain total 1,000,000 EU spread across the set
        if (!drainFromSet(player, PUNCH_COST_TOTAL)) {
            // not enough power; normal punch goes through
            return;
        }

        // cancel vanilla attack and apply our damage + knockback
        event.setCanceled(true);

        // deal damage
        victim.attackEntityFrom(DamageSource.causePlayerDamage(player), PUNCH_DAMAGE);

        // knockback 6
        double dx = player.posX - victim.posX;
        double dz = player.posZ - victim.posZ;
        // EntityLivingBase has knockBack(attacker, strength, xRatio, zRatio)
        victim.knockBack(player, PUNCH_KNOCKBACK, dx, dz);

        // tiny cooldown feel
        player.swingArm(player.getActiveHand());
    }

    // Hazmat
    @Override public boolean addsProtection(EntityLivingBase entity, EntityEquipmentSlot slot, ItemStack stack) {
        return ElectricItem.manager.getCharge(stack) > 0.0D;
    }
    @Override public boolean fullyProtects(EntityLivingBase entity, EntityEquipmentSlot slot, ItemStack stack) { return false; }
    @Override public String getUnlocalizedName() { return "item." + Reference.MODID + "." + getRegistryName().getResourcePath(); }
    @Override public String getUnlocalizedName(ItemStack stack) { return getUnlocalizedName(); }
    @Override public String getUnlocalizedNameInefficiently(ItemStack stack) { return getUnlocalizedName(); }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) { return EnumRarity.EPIC; }

    @Override public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type){
        return "ic2additions:textures/armor/molecular_" + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
    }
}

