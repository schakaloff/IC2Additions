package com.ic2additions.objects.items.armor;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import com.ic2additions.util.IC2AdditionsKeys;
import com.ic2additions.util.Reference;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.init.Localization;
import ic2.core.item.armor.ItemArmorElectric;
import ic2.core.item.armor.jetpack.IBoostingJetpack;
import ic2.core.util.StackUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAdvancedNanoArmour extends ItemArmorElectric implements IBoostingJetpack {
    public static final String TAG_FLY = "isFlyActive";
    public static final String TAG_HOVER = "hoverMode";
    public static final String TAG_TOGGLE_TIMER = "toggleTimer";

    public ItemAdvancedNanoArmour(String name, EntityEquipmentSlot slot){
        super(null, "advanced_nano_suit", slot, 10_000_000, 4096.0D, 4);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setMaxStackSize(1);
        ItemInit.ITEMS.add(this);
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
        if (this.armorType != EntityEquipmentSlot.CHEST) return;

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
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type){
        return "ic2additions:textures/armor/advanced_nano_" + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
    }
    @Override
    public double getDamageAbsorptionRatio() {
        return 0.80;
    }

    @Override
    public int getEnergyPerDamage() {
        return 50000;
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

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

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.BLUE + I18n.format("tooltip.ic2additions.nightvision"));
        tooltip.add(TextFormatting.BLUE + I18n.format("tooltip.ic2additions.jetpack"));
    }
}
