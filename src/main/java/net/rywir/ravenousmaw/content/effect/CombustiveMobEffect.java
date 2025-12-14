package net.rywir.ravenousmaw.content.effect;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.rywir.ravenousmaw.registry.RavenousMobEffects;
import org.jetbrains.annotations.NotNull;

public class CombustiveMobEffect extends MobEffect {
    public CombustiveMobEffect(MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color, particle);
    }

    @Override
    public void onMobHurt(LivingEntity target, int amplifier, @NotNull DamageSource damageSource, float amount) {
        Level level = target.level();

        if (level.isClientSide()) {
            return;
        }

        TagKey<DamageType> type = DamageTypeTags.IS_FIRE;
        boolean canIgnite = damageSource.is(type);

        if (canIgnite) {
            level.explode(
                damageSource.getEntity(),
                target.getX(),
                target.getY() + 0.5,
                target.getZ(),
                0.15F * amplifier,
                false,
                Level.ExplosionInteraction.NONE
            );

            target.removeEffect(RavenousMobEffects.COMBUSTIVE);
        }
    }
}