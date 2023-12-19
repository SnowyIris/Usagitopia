package usagitopia.world.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import usagitopia.world.CreativeModeTabs;
import usagitopia.world.entity.BBRabbit;
import usagitopia.world.registry.EntityTypeRegistry;

public class BBRabbitItem extends Item
{
    public static final String REGISTRY_NAME = "bb_rabbit";
    
    public BBRabbitItem()
    {
        super(new Properties().stacksTo(16).rarity(Rarity.RARE).tab(CreativeModeTabs.USAGITOPIA_TAB));
    }
    
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand)
    {
        ItemStack item = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        player.getCooldowns().addCooldown(this, 20);
        if(!level.isClientSide)
        {
            BBRabbit bbRabbit = EntityTypeRegistry.BB_RABBIT.get().create(level);
            assert bbRabbit != null;
            bbRabbit.throwOutFrom(player, 1.0F, 0.3F);
            level.addFreshEntity(bbRabbit);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if(!player.getAbilities().instabuild)
        {
            item.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(item, level.isClientSide());
    }
    
}
