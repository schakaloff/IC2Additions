package com.ic2additions.objects.items;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

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

                // Create explosion
                world.newExplosion(null, entityItem.posX, entityItem.posY, entityItem.posZ,
                        explosionPower, flaming, damagesTerrain);

                // 🔥 Spawn some random lava blocks around explosion
                spawnRandomLava(world, entityItem.posX, entityItem.posY, entityItem.posZ);

                entityItem.setDead();
                return true;
            }
        }
        return super.onEntityItemUpdate(entityItem);
    }

    private void spawnRandomLava(World world, double x, double y, double z) {
        Random rand = new Random();

        // Number of lava spots
        int count = 3 + rand.nextInt(3); // between 3–5

        for (int i = 0; i < count; i++) {
            // Random offset around explosion
            int dx = rand.nextInt(5) - 2;
            int dy = rand.nextInt(3) - 1;
            int dz = rand.nextInt(5) - 2;

            BlockPos pos = new BlockPos(x + dx, y + dy, z + dz);

            // Only place lava if block is air and not already filled
            if (world.isAirBlock(pos)) {
                // 50% chance to be a source, 50% flowing
                if (rand.nextBoolean()) {
                    world.setBlockState(pos, Blocks.LAVA.getDefaultState());
                } else {
                    world.setBlockState(pos, Blocks.FLOWING_LAVA.getDefaultState());
                }
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.DARK_PURPLE + I18n.format("tooltip.ic2additions.dangerous_drop"));
    }
}
