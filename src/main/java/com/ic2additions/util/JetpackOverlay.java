package com.ic2additions.util;

import com.ic2additions.objects.items.armor.AdvancedElectricJetpack;
import ic2.core.item.armor.jetpack.IJetpack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
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
        int percent = Math.round((float)(jp.getChargeLevel(chest) * 100f));
        String energy = "Energy: " + percent + "%";

        String status = "";
        if (jp.isJetpackActive(chest)) {
            boolean hover = AdvancedElectricJetpack.isHovering(chest);
            status = "Engine: ON" + (hover ? " [Hover]" : "");
        }

        FontRenderer fr = mc.fontRenderer;
        int fh = fr.FONT_HEIGHT;
        int x1 = 2, y1 = 2, x2 = 2, y2 = 4 + fh;
        ScaledResolution sr = new ScaledResolution(mc);

        switch (hudPos) {
            case 2: x1 = sr.getScaledWidth() - fr.getStringWidth(status) - 2;
                x2 = sr.getScaledWidth() - fr.getStringWidth(energy) - 2; break;
            case 3: y1 = sr.getScaledHeight() - fh - 2;
                y2 = y1 - 2 - fh; break;
            case 4: x1 = sr.getScaledWidth() - fr.getStringWidth(status) - 2;
                x2 = sr.getScaledWidth() - fr.getStringWidth(energy) - 2;
                y1 = sr.getScaledHeight() - fh - 2;
                y2 = y1 - 2 - fh; break;
            default: break;
        }

        if (!status.isEmpty()) {
            mc.ingameGUI.drawString(fr, status, x1, y1, 0xFFFFFF);
            mc.ingameGUI.drawString(fr, energy, x2, y2, 0xFFFFFF);
        } else {
            mc.ingameGUI.drawString(fr, energy, x2, y1, 0xFFFFFF);
        }
    }

}
