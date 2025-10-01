package com.ic2additions.objects.items.armor;

import com.ic2additions.util.IC2AdditionsKeys;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.init.Localization;
import ic2.core.item.armor.jetpack.IBoostingJetpack;
import ic2.core.util.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemUltimateEnergyPack extends ItemBaseEnergyPack implements IBoostingJetpack {
    public static final String TAG_FLY = "isFlyActive";
    public static final String TAG_HOVER = "hoverMode";
    public static final String TAG_TOGGLE_TIMER = "toggleTimer";

    public ItemUltimateEnergyPack(){
        super("ultimate_pack", 60_000_000, 70000.0, 4);
    }
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }
    public static boolean isJetpackOn(ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack).getBoolean(TAG_FLY);
    }

    public static boolean isHovering(ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack).getBoolean(TAG_HOVER);
    }

    public static boolean switchJetpack(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        boolean newMode = !nbt.getBoolean(TAG_FLY);
        nbt.setBoolean(TAG_FLY, newMode);
        return newMode;
    }

    public static boolean switchHover(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        boolean newMode = !nbt.getBoolean(TAG_HOVER);
        nbt.setBoolean(TAG_HOVER, newMode);
        return newMode;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        byte toggleTimer = nbt.getByte(TAG_TOGGLE_TIMER);

        if (IC2AdditionsKeys.isFlyKeyDown(player) && toggleTimer == 0) {
            nbt.setByte(TAG_TOGGLE_TIMER, (byte) 10);
            if (!world.isRemote) {
                boolean on = switchJetpack(stack);
                String mode = on
                        ? TextFormatting.DARK_GREEN + Localization.translate("ic2additions.message.on")
                        : TextFormatting.DARK_RED + Localization.translate("ic2additions.message.off");
                sendStatus(player, "ic2additions.message.jetpackSwitch", TextFormatting.YELLOW, mode);
            }
        }

        if (IC2AdditionsKeys.isHoverKeyDown(player) && toggleTimer == 0) {
            nbt.setByte(TAG_TOGGLE_TIMER, (byte) 10);
            if (!world.isRemote) {
                boolean hover = switchHover(stack);
                String mode = hover
                        ? TextFormatting.DARK_GREEN + Localization.translate("ic2additions.message.hover_on")
                        : TextFormatting.DARK_RED + Localization.translate("ic2additions.message.hover_off");
                sendStatus(player, "ic2additions.message.hoverSwitch", TextFormatting.YELLOW, mode);
            }
        }

        if (toggleTimer > 0) {
            nbt.setByte(TAG_TOGGLE_TIMER, (byte) (toggleTimer - 1));
        }

    }

    private static void sendStatus(EntityPlayer player, String key, TextFormatting color, Object... args) {
        TextComponentTranslation msg = new TextComponentTranslation(key, args);
        msg.setStyle(new Style().setColor(color));
        player.sendMessage(msg);
    }

    @Override
    public boolean isJetpackActive(ItemStack stack) {
        return isJetpackOn(stack);
    }

    @Override
    public double getChargeLevel(ItemStack stack) {
        return ElectricItem.manager.getCharge(stack) / getMaxCharge(stack);
    }

    @Override
    public float getPower(ItemStack stack) {
        return 1.0f;
    }

    @Override
    public float getDropPercentage(ItemStack stack) {
        return 0.05f;
    }

    @Override
    public float getBaseThrust(ItemStack stack, boolean hover) {
        return hover ? 0.65f : 0.30f;
    }

    @Override
    public float getBoostThrust(EntityPlayer player, ItemStack stack, boolean hover) {
        return IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 60.0 ? (hover ? 0.07f : 0.09f) : 0.0f;
    }

    @Override
    public boolean useBoostPower(ItemStack stack, float boostAmount) {
        return ElectricItem.manager.discharge(stack, 60.0, Integer.MAX_VALUE, true, false, false) > 0.0;
    }

    @Override
    public float getWorldHeightDivisor(ItemStack stack) {
        return 1.0f;
    }

    @Override
    public float getHoverMultiplier(ItemStack stack, boolean upwards) {
        return 0.2f;
    }

    @Override
    public float getHoverBoost(EntityPlayer player, ItemStack stack, boolean up) {
        if (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 60.0) {
            if (!player.onGround) {
                ElectricItem.manager.discharge(stack, 60.0, Integer.MAX_VALUE, true, false, false);
            }
            return 2.0f;
        }
        return 1.0f;
    }

    @Override
    public boolean drainEnergy(ItemStack pack, int amount) {
        return ElectricItem.manager.discharge(pack, amount * 6.0, Integer.MAX_VALUE, true, false, false) > 0.0;
    }

    @Override
    public boolean canProvideEnergy(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnergyPerDamage() {
        return 0;
    }

    @Override
    public double getDamageAbsorptionRatio() {
        return 0.0;
    }
}
