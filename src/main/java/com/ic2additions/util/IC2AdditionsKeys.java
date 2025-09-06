package com.ic2additions.util;

import ic2.core.IC2;
import ic2.core.util.Keyboard;
import ic2.core.util.ReflectionUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Set;

public class IC2AdditionsKeys extends Keyboard {
    private static final IKeyWatcher FLY_KEY = new KeyWatcher(ic2Additionskeys.FLY);
    private static final IKeyWatcher HOVER_KEY = new KeyWatcher(ic2Additionskeys.HOVER);

    private IC2AdditionsKeys() {}

    public static void register() {
        IC2.keyboard.addKeyWatcher(FLY_KEY);
        IC2.keyboard.addKeyWatcher(HOVER_KEY);
    }

    public static boolean isFlyKeyDown(EntityPlayer player) {
        return IC2.keyboard.isKeyDown(player, FLY_KEY);
    }
    public static boolean isHoverKeyDown(EntityPlayer player) {
        return IC2.keyboard.isKeyDown(player, HOVER_KEY);
    }

    private static class KeyWatcher implements IKeyWatcher {
        private final ic2Additionskeys key;
        KeyWatcher(ic2Additionskeys key) { this.key = key; }
        @Override public Key getRepresentation() { return key.key; }

        @SideOnly(Side.CLIENT)
        @Override public void checkForKey(Set<Key> pressed) {
            if (net.minecraft.client.settings.GameSettings.isKeyDown(key.binding)) {
                pressed.add(getRepresentation());
            }
        }
    }

    private enum ic2Additionskeys {
        FLY(33, "Fly"),
        HOVER(35, "Hover");

        private final Key key = addKey(name());
        @SideOnly(Side.CLIENT)
        private KeyBinding binding;

        ic2Additionskeys(int defaultKey, String desc) {
            if (ic2.core.IC2.platform.isRendering()) {
                String cat = "IC2Additions".substring(0, 1).toUpperCase(Locale.ENGLISH) + "IC2Additions".substring(1);
                binding = new KeyBinding(desc, defaultKey, cat);
                ClientRegistry.registerKeyBinding(binding);
            }
        }
        private static Field keysField() {
            try {
                Field f = ic2.core.util.ReflectionUtil.getField(Key.class, "keys");
                ReflectionUtil.getField(Field.class, "modifiers").setInt(f, f.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
                return f;
            } catch (Exception e) {
                throw new RuntimeException("Failed to reflect IC2 Keyboard.Key.keys", e);
            }
        }
        private static Key addKey(String name) {
            Key key = EnumHelper.addEnum(Key.class, name, new Class[0], new Object[0]);
            ReflectionUtil.setValue(null, keysField(), ArrayUtils.add(Keyboard.Key.keys, key));
            return key;
        }
    }
}
