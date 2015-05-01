package org.halvors.ConsoleTweaks;
import java.util.Map;

import net.minecraft.launchwrapper.Launch;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.7.10")
public class ConsoleTweaksCoremod implements IFMLLoadingPlugin {
    public ConsoleTweaksCoremod() {

        // Let's get this party started
        MixinBootstrap.init();

        // Add pre-init mixins
        MixinEnvironment.getEnvironment(Phase.PREINIT)
                .addConfiguration("mixins.forge.base.json");

        SpongeLaunch.initialize(null, null, null);
        Sponge.getGlobalConfig(); // Load config

        // Add default mixins
        MixinEnvironment.getDefaultEnvironment()
                .addConfiguration("mixins.common.api.json")
                .addConfiguration("mixins.common.core.json")
                .addConfiguration("mixins.forge.core.json")
                .addConfiguration("mixins.forge.entityactivation.json");

        // Classloader exclusions - TODO: revise when event pkg refactor reaches impl
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.api.event.cause.CauseTracked");
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.api.event.Cancellable");
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.api.util.event.callback.CallbackList");

        // Transformer exclusions
        Launch.classLoader.addTransformerExclusion("ninja.leaping.configurate.");
        Launch.classLoader.addTransformerExclusion("org.apache.commons.lang3.");
    }

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return "org.spongepowered.mod.SpongeMod";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        if ((Boolean)data.get("runtimeDeobfuscationEnabled")) {
            MixinEnvironment.getDefaultEnvironment()
                    .registerErrorHandlerClass("org.spongepowered.mod.mixin.handler.MixinErrorHandler");
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return "org.spongepowered.mod.asm.transformers.SpongeAccessTransformer";
    }

}