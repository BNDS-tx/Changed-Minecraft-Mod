package net.ltxprogrammer.changed.item;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface SpecializedItemRendering {
    static boolean isGUI(ItemTransforms.TransformType type) {
        return type == ItemTransforms.TransformType.GUI || type == ItemTransforms.TransformType.GROUND || type == ItemTransforms.TransformType.FIXED;
    }

    @Nullable ModelResourceLocation getModelLocation(ItemStack itemStack, ItemTransforms.TransformType type);
    void loadSpecialModels(Consumer<ResourceLocation> loader);

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    class Event {
        @SubscribeEvent
        public static void onModelRegistryEvent(ModelRegistryEvent event) {
            ForgeRegistries.ITEMS.forEach(item -> {
                if (item instanceof SpecializedItemRendering specialized)
                    specialized.loadSpecialModels(ForgeModelBakery::addSpecialModel);
            });
        }
    }
}
