package com.ic2additions.objects.items;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import com.ic2additions.util.Reference;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemMusicDisc extends ItemRecord {
    private final ResourceLocation soundRL;

    public ItemMusicDisc(String id, String soundPath) {
        super(id, null);
        this.soundRL = new ResourceLocation(Reference.MODID, soundPath);

        setRegistryName(new ResourceLocation(Reference.MODID, id));
        setUnlocalizedName(Reference.MODID + "." + id);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setMaxStackSize(1);
        ItemInit.ITEMS.add(this);
    }

    @Override
    public SoundEvent getSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(soundRL);
    }
}
