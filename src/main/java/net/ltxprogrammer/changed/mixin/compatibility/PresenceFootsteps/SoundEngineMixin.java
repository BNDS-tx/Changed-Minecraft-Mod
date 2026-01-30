package net.ltxprogrammer.changed.mixin.compatibility.PresenceFootsteps;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import eu.ha3.presencefootsteps.sound.PFIsolator;
import eu.ha3.presencefootsteps.sound.SoundEngine;
import eu.ha3.presencefootsteps.sound.generator.Locomotion;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.extension.RequiredMods;
import net.ltxprogrammer.changed.extension.presencefootsteps.ChangedPresenceFootsteps;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SoundEngine.class, remap = false)
@RequiredMods("presencefootsteps")
public abstract class SoundEngineMixin {
    @Shadow private PFIsolator isolator;

    @Inject(method = "reloadEverything", at = @At("RETURN"))
    public void reloadEverything(ResourceManager manager, CallbackInfo callbackInfo) {
        var event = new ChangedPresenceFootsteps.LoadModdedFootstepsEvent(manager, isolator);
        event.loadBlockMap(ChangedPresenceFootsteps.BLOCK_MAP);
        event.loadLocomotionMap(ChangedPresenceFootsteps.LOCOMOTION_MAP);

        Changed.postModEvent(event);
    }

    @WrapMethod(method = "getLocomotion")
    public Locomotion getLocomotion(LivingEntity entity, Operation<Locomotion> original) {
        return original.call(EntityUtil.maybeGetOverlaying(entity));
    }
}
