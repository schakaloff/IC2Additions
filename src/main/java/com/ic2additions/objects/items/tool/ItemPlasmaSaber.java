package com.ic2additions.objects.items.tool;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import com.ic2additions.main.IC2Additions;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.item.ItemIC2;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ItemElectricTool;
import ic2.core.item.tool.ToolClass;
import ic2.core.ref.ItemName;
import ic2.core.slot.ArmorSlot;
import ic2.core.util.StackUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;

public class ItemPlasmaSaber extends ItemElectricTool {
    public static int ticker = 0;
    private int soundTicker = 0;

    public ItemPlasmaSaber() {
        super(null, 10, HarvestLevel.Diamond, EnumSet.of(ToolClass.Sword));
        this.maxCharge = 250000;
        this.transferLimit = 1000;
        this.tier = 3;
        setRegistryName("plasma_saber");
        setUnlocalizedName("plasma_saber");
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        ItemInit.ITEMS.add(this);
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        if (isActive(stack)) {
            ++this.soundTicker;
            if (IC2.platform.isRendering() && this.soundTicker % 4 == 0) {
                IC2.platform.playSoundSp(this.getRandomSwingSound(), 1.0F, 1.0F);
            }

            return state.getBlock() == Blocks.WEB ? 50.0F : 4.0F;
        } else {
            return 1.0F;
        }
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        if (slot != EntityEquipmentSlot.MAINHAND) {
            return super.getAttributeModifiers(slot, stack);
        } else {
            int dmg = 5;
            if (ElectricItem.manager.canUse(stack, (double)400.0F) && isActive(stack)) {
                dmg = 25;
            }

            Multimap<String, AttributeModifier> ret = HashMultimap.create();
            ret.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double)this.attackSpeed, 0));
            ret.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(Item.ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double)dmg, 0));
            return ret;
        }
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase source) {
        if (!isActive(stack)) {
            return true;
        } else {
            if (IC2.platform.isSimulating()) {
                drainSaber(stack, (double)400.0F, source);
                if (!(source instanceof EntityPlayerMP) || !(target instanceof EntityPlayer) || ((EntityPlayerMP)source).canAttackPlayer((EntityPlayer)target)) {
                    for(EntityEquipmentSlot slot : ArmorSlot.getAll()) {
                        if (!ElectricItem.manager.canUse(stack, (double)2000.0F)) {
                            break;
                        }

                        ItemStack armor = target.getItemStackFromSlot(slot);
                        if (armor != null) {
                            double amount = 0.0D;
                            if (armor.getItem().getRegistryName().toString().contains("nano")) {
                                amount = 48000.0D;
                            } else if (armor.getItem().getRegistryName().toString().contains("quantum")) {
                                amount = 300000.0D;
                            }

                            if (amount > 0.0D) {
                                ElectricItem.manager.discharge(armor, amount, this.tier, true, false, false);
                                if (!ElectricItem.manager.canUse(armor, 1.0D)) {
                                    target.setItemStackToSlot(slot, (ItemStack)null);
                                }

                                drainSaber(stack, (double)2000.0F, source);
                            }
                        }
                    }
                }
            }

            if (IC2.platform.isRendering()) {
                IC2.platform.playSoundSp(this.getRandomSwingSound(), 1.0F, 1.0F);
            }

            return true;
        }
    }

    public String getRandomSwingSound() {
        switch (IC2.random.nextInt(3)) {
            case 1:
                return "Tools/Nanosabre/NanosabreSwing2.ogg";
            case 2:
                return "Tools/Nanosabre/NanosabreSwing3.ogg";
            default:
                return "Tools/Nanosabre/NanosabreSwing1.ogg";
        }
    }

    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        return false;
    }

    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        if (isActive(stack)) {
            drainSaber(stack, (double)80.0F, player);
        }
        return false;
    }

    public boolean isFull3D() {
        return true;
    }

    public static void drainSaber(ItemStack stack, double amount, EntityLivingBase entity) {
        if (!ElectricItem.manager.use(stack, amount, entity)) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            setActive(nbt, false);
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = StackUtil.get(player, hand);
        if (world.isRemote) {
            return new ActionResult(EnumActionResult.PASS, stack);
        } else {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            if (isActive(nbt)) {
                setActive(nbt, false);
                return new ActionResult(EnumActionResult.SUCCESS, stack);
            } else if (ElectricItem.manager.canUse(stack, (double)16.0F)) {
                setActive(nbt, true);
                return new ActionResult(EnumActionResult.SUCCESS, stack);
            } else {
                return super.onItemRightClick(world, player, hand);
            }
        }
    }

    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean par5) {
        super.onUpdate(stack, world, entity, slot, par5 && isActive(stack));
        if (isActive(stack)) {
            if (ticker % 16 == 0 && entity instanceof EntityPlayerMP) {
                if (slot < 9) {
                    drainSaber(stack, (double)64.0F, (EntityPlayer)entity);
                } else if (ticker % 64 == 0) {
                    drainSaber(stack, (double)16.0F, (EntityPlayer)entity);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    public static boolean isActive(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        return isActive(nbt);
    }

    private static boolean isActive(NBTTagCompound nbt) {
        return nbt.getBoolean("active");
    }

    private static void setActive(NBTTagCompound nbt, boolean active) {
        nbt.setBoolean("active", active);
    }

    public boolean onEntitySwing(EntityLivingBase entity, ItemStack stack) {
        if (IC2.platform.isRendering() && isActive(stack)) {
            IC2.audioManager.playOnce(entity, PositionSpec.Hand, this.getRandomSwingSound(), true, IC2.audioManager.getDefaultVolume());
        }
        return false;
    }

    protected String getIdleSound(EntityLivingBase player, ItemStack stack) {
        return "Tools/Nanosabre/NanosabreIdle.ogg";
    }

    protected String getStartSound(EntityLivingBase player, ItemStack stack) {
        return "Tools/Nanosabre/NanosabrePowerup.ogg";
    }
}
