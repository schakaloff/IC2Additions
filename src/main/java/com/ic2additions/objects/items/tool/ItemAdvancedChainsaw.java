package com.ic2additions.objects.items.tool;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ItemElectricTool;
import ic2.core.item.tool.ToolClass;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class ItemAdvancedChainsaw extends ItemElectricTool {
    private static final String NBT_DISABLE_SHEAR = "disableShear";

    public ItemAdvancedChainsaw() {
        super(null, 100, HarvestLevel.Iron, EnumSet.of(ToolClass.Axe, ToolClass.Sword, ToolClass.Shears));
        this.maxCharge = 45000;
        this.transferLimit = 500;
        this.tier = 2;
        this.efficiency = 30.0f;
        setRegistryName("advanced_chainsaw");
        setUnlocalizedName("advanced_chainsaw");
        setMaxStackSize(1);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        MinecraftForge.EVENT_BUS.register(this);
        ItemInit.ITEMS.add(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote && IC2.keyboard.isModeSwitchKeyDown(player)) {
            ItemStack stack = player.getHeldItem(hand);
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);

            boolean wasDisabled = nbt.getBoolean(NBT_DISABLE_SHEAR);
            boolean nowDisabled = !wasDisabled;
            nbt.setBoolean(NBT_DISABLE_SHEAR, nowDisabled);

            String onOffKey = nowDisabled ? "ic2additions.message.off" : "ic2additions.message.on";
            TextFormatting color = nowDisabled ? TextFormatting.DARK_RED : TextFormatting.DARK_GREEN;

            ITextComponent msg = new TextComponentTranslation("ic2additions.advancedChainsaw.shear", new TextComponentTranslation(onOffKey)).setStyle(new Style().setColor(color));

            if (player instanceof EntityPlayerMP) {
                ((EntityPlayerMP) player).sendMessage(msg);
            } else {
                player.sendStatusMessage(msg, true);
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }


    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        ElectricItem.manager.use(stack, this.operationEnergyCost, attacker);
        if (attacker instanceof EntityPlayer && target instanceof EntityCreeper && target.getHealth() <= 0.0f) {
            IC2.achievements.issueAchievement((EntityPlayer) attacker, "killCreeperChainsaw");
        }
        return true;
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote) return;

        Entity targetEntity = event.getTarget();
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack.isEmpty() || stack.getItem() != this) return;

        if (!(targetEntity instanceof IShearable)) return;

        if (StackUtil.getOrCreateNbtData(stack).getBoolean(NBT_DISABLE_SHEAR)) return;

        if (!ElectricItem.manager.use(stack, this.operationEnergyCost, player)) return;

        IShearable shearable = (IShearable) targetEntity;
        BlockPos pos = new BlockPos(targetEntity);
        if (!shearable.isShearable(stack, targetEntity.world, pos)) return;
        List<ItemStack> drops = shearable.onSheared(
                stack, targetEntity.world, pos,
                EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack)
        );

        for (ItemStack drop : drops) {
            EntityItem item = targetEntity.entityDropItem(drop, 1.0f);
            if (item != null) {
                item.motionY += item.world.rand.nextFloat() * 0.05f;
                item.motionX += (item.world.rand.nextFloat() - item.world.rand.nextFloat()) * 0.1f;
                item.motionZ += (item.world.rand.nextFloat() - item.world.rand.nextFloat()) * 0.1f;
            }
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        World world = player.world;
        if (world.isRemote) return false;

        if (StackUtil.getOrCreateNbtData(stack).getBoolean(NBT_DISABLE_SHEAR)) return false;

        Block block = world.getBlockState(pos).getBlock();
        if (!(block instanceof IShearable)) return false;

        IShearable shearable = (IShearable) block;
        if (!shearable.isShearable(stack, world, pos)) return false;

        if (!ElectricItem.manager.use(stack, this.operationEnergyCost, player)) return false;

        List<ItemStack> drops = shearable.onSheared(
                stack, world, pos,
                EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack)
        );
        for (ItemStack drop : drops) {
            StackUtil.dropAsEntity(world, pos, drop);
        }
        player.addStat(StatList.getBlockStats(block), 1);
        return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        boolean disabled = StackUtil.getOrCreateNbtData(stack).getBoolean(NBT_DISABLE_SHEAR);
        String state = I18n.format(disabled ? "ic2additions.message.off" : "ic2additions.message.on");
        String line  = I18n.format("ic2additions.advancedChainsaw.shear", state);
        tooltip.add((disabled ? TextFormatting.DARK_RED : TextFormatting.DARK_GREEN) + line);
    }

    private static final UUID SPEED_MOD = UUID.fromString("8a92cbd6-2351-47b7-9e61-2e0fef7c8c86");
    private static final UUID DAMAGE_MOD = UUID.fromString("2f013b50-4d9e-4ac0-8319-1c6a45d2a4d9");

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        if (slot != EntityEquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot, stack);

        HashMultimap<String, AttributeModifier> ret = HashMultimap.create(super.getAttributeModifiers(slot, stack));
        if (ElectricItem.manager.canUse(stack, this.operationEnergyCost)) {
            ret.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
                    new AttributeModifier(SPEED_MOD, "Tool speed", this.attackSpeed, 0));
            ret.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                    new AttributeModifier(DAMAGE_MOD, "Tool damage", 13.0, 0));
        }
        return ret;
    }

    protected String getIdleSound(EntityLivingBase player, ItemStack stack) {
        return "Tools/Chainsaw/ChainsawIdle.ogg";
    }

    protected String getStopSound(EntityLivingBase player, ItemStack stack) {
        return "Tools/Chainsaw/ChainsawStop.ogg";
    }
}
