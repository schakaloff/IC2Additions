package com.ic2additions.util;

import com.ic2additions.objects.items.armor.AdvancedElectricJetpack;
import com.ic2additions.objects.items.armor.ItemArmorMolecular;
import ic2.core.item.armor.jetpack.IJetpack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class JetpackOverlay {
    public static boolean hudEnabled = true;
    public static byte hudPos = 1;

    public JetpackOverlay() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent evt) {
        if (!hudEnabled) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.world == null || mc.player == null || !Minecraft.isGuiEnabled() || mc.gameSettings.showDebugInfo) return;

        ItemStack chest = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (chest.isEmpty()) return;
        Item item = chest.getItem();
        if (!(item instanceof IJetpack)) return;

        IJetpack jp = (IJetpack) item;
        int percent = Math.round((float) (jp.getChargeLevel(chest) * 100f));
        String energyLine = I18n.format("ic2additions.hud.energy", percent);

        String engineLine = "";
        if (jp.isJetpackActive(chest)) {
            engineLine = I18n.format("ic2additions.hud.engine",
                    I18n.format(jp.isJetpackActive(chest) ? "ic2additions.message.on" : "ic2additions.message.off"));
            if (AdvancedElectricJetpack.isHovering(chest)) {
                engineLine += " " + I18n.format("ic2additions.hud.hover_brackets"); // "[Hover]"
            }
        }

        // Suit mode line: Fire / Hand / Hand [Silk] / Punch / None
        String suitLine = "";
        if (item instanceof ItemArmorMolecular) {
            boolean fire  = chest.hasTagCompound() && chest.getTagCompound().getBoolean(ItemArmorMolecular.TAG_FIRE_MODE);
            boolean hand  = chest.hasTagCompound() && chest.getTagCompound().getBoolean(ItemArmorMolecular.TAG_HAND_FASTBREAK);
            boolean silk  = hand && chest.hasTagCompound() && "SILK".equals(chest.getTagCompound().getString("handMode"));
            boolean punch = chest.hasTagCompound() && chest.getTagCompound().getBoolean(ItemArmorMolecular.TAG_PUNCH_MODE);

            String modeKey;
            if (fire) {
                modeKey = "ic2additions.mode.fire";
            } else if (hand) {
                modeKey = silk ? "ic2additions.mode.hand_silk" : "ic2additions.mode.hand";
            } else if (punch) {
                modeKey = "ic2additions.mode.punch";
            } else {
                modeKey = "ic2additions.mode.none";
            }

            suitLine = I18n.format("ic2additions.message.suit_mode", I18n.format(modeKey));
        }

        java.util.ArrayList<String> lines = new java.util.ArrayList<>(3);
        if (!engineLine.isEmpty()) lines.add(engineLine);
        if (!suitLine.isEmpty())   lines.add(suitLine);
        lines.add(energyLine);

        FontRenderer fr = mc.fontRenderer;
        int fh = fr.FONT_HEIGHT;
        int pad = 2;

        ScaledResolution sr = new ScaledResolution(mc);
        boolean rightAlign = (hudPos == 2 || hudPos == 4);

        int startY = (hudPos == 3 || hudPos == 4)
                ? sr.getScaledHeight() - pad - fh
                : pad;

        for (int i = 0; i < lines.size(); i++) {
            String text = lines.get(i);
            int y;
            if (hudPos == 3 || hudPos == 4) {
                y = startY - (lines.size() - 1 - i) * (fh + pad);
            } else {
                y = startY + i * (fh + pad);
            }

            if (rightAlign) {
                int x = sr.getScaledWidth() - pad - fr.getStringWidth(text);
                mc.ingameGUI.drawString(fr, text, x, y, 0xFFFFFF);
            } else {
                mc.ingameGUI.drawString(fr, text, pad, y, 0xFFFFFF);
            }
        }
    }
}

