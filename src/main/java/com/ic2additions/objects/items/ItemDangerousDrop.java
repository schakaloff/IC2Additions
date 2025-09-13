package com.ic2additions.objects.items;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDangerousDrop extends Item {
    private final float explosionPower;
    private final boolean flaming;
    private final boolean damagesTerrain;

    public ItemDangerousDrop(String name, float explosionPower, boolean flaming, boolean damagesTerrain) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        ItemInit.ITEMS.add(this);

        this.explosionPower = explosionPower;
        this.flaming = flaming;
        this.damagesTerrain = damagesTerrain;
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        World world = entityItem.world;
        if (!world.isRemote) {
            if (entityItem.onGround && entityItem.ticksExisted > 1) {
                ItemStack stack = entityItem.getItem();
                stack.shrink(stack.getCount());
                world.newExplosion(null, entityItem.posX, entityItem.posY, entityItem.posZ, explosionPower, flaming, damagesTerrain);
                entityItem.setDead();
                return true;
            }
        }
        return super.onEntityItemUpdate(entityItem);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.DARK_PURPLE + I18n.format("tooltip.ic2additions.dangerous_drop"));
    }

}
