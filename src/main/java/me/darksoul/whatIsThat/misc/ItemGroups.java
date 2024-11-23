package me.darksoul.whatIsThat.misc;

import com.MT.xxxtrigger50xxx.Devices.*;
import com.MT.xxxtrigger50xxx.Devices.Defense.*;
import com.MT.xxxtrigger50xxx.Devices.Generators.*;
import com.MT.xxxtrigger50xxx.Devices.Liquids.BarrelPump;
import com.MT.xxxtrigger50xxx.Devices.Liquids.Boiler;
import com.MT.xxxtrigger50xxx.Devices.Liquids.Pump;
import com.MT.xxxtrigger50xxx.Devices.Manufactoring.*;
import com.MT.xxxtrigger50xxx.Devices.Producers.*;
import com.MT.xxxtrigger50xxx.Devices.Transport.*;
import com.MT.xxxtrigger50xxx.Devices.Utility.*;
import com.MT.xxxtrigger50xxx.Technology.ResearchLab;
import me.darksoul.whatIsThat.compatibility.Minetorio;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class ItemGroups {
    private static final List<Material> redstoneProviders = new ArrayList<>();
    private static final List<Material> redstoneComponents = new ArrayList<>();
    private static final List<Material> crops = new ArrayList<>();
    private static final List<Material> honeyProducers = new ArrayList<>();
    private static final List<Material> furnaces = new ArrayList<>();
    private static final List<Material> containers = new ArrayList<>();
    private static final List<Class<?>> generators = new ArrayList<>();
    private static final List<Class<?>> devices = new ArrayList<>();
    private static final List<EntityType> pets = new ArrayList<>();
    private static final List<EntityType> notRender = new ArrayList<>();

    static {
        // RedStone
        redstoneProviders.add(Material.OBSERVER);
        redstoneProviders.add(Material.TARGET);
        redstoneProviders.add(Material.REDSTONE_BLOCK);
        redstoneProviders.add(Material.REPEATER);
        for (Material type : Material.values()) {
            String typeString = type.toString();
            if (typeString.endsWith("_DOOR")
                    || typeString.endsWith("_TRAPDOOR")
                    || typeString.endsWith("_FENCE_GATE")
                    || typeString.endsWith("_PRESSURE_PLATE")
                    || typeString.endsWith("_BUTTON")
                    || typeString.endsWith("_BULB")
                    || type == Material.LEVER) {
                redstoneProviders.add(type);
            }
            if (typeString.endsWith("_WOOL")) {
                redstoneComponents.add(type);
            }
        }
        redstoneProviders.add(Material.BELL);
        redstoneProviders.add(Material.REDSTONE_LAMP);
        redstoneProviders.add(Material.ACTIVATOR_RAIL);
        redstoneProviders.add(Material.DETECTOR_RAIL);
        redstoneProviders.add(Material.DAYLIGHT_DETECTOR);
        redstoneProviders.add(Material.POWERED_RAIL);
        redstoneProviders.add(Material.NOTE_BLOCK);
        redstoneProviders.add(Material.OBSERVER);
        redstoneProviders.add(Material.JUKEBOX);
        redstoneProviders.add(Material.TRAPPED_CHEST);
        redstoneProviders.add(Material.HOPPER);
        redstoneProviders.add(Material.DROPPER);
        redstoneProviders.add(Material.TARGET);
        redstoneProviders.add(Material.PISTON);
        redstoneProviders.add(Material.PISTON_HEAD);
        redstoneProviders.add(Material.STICKY_PISTON);
        redstoneProviders.add(Material.CRAFTER);
        redstoneProviders.add(Material.DISPENSER);
        redstoneProviders.add(Material.TRIPWIRE_HOOK);
        redstoneProviders.add(Material.SCULK_SHRIEKER);
        redstoneProviders.add(Material.SCULK_SENSOR);
        redstoneProviders.add(Material.CALIBRATED_SCULK_SENSOR);
        redstoneProviders.add(Material.REDSTONE_TORCH);
        redstoneComponents.add(Material.REDSTONE_WIRE);
        redstoneComponents.add(Material.COMPARATOR);

        // Crops
        crops.add(Material.WHEAT);
        crops.add(Material.BEETROOTS);
        crops.add(Material.MELON_STEM);
        crops.add(Material.PUMPKIN_STEM);
        crops.add(Material.POTATOES);
        crops.add(Material.CARROTS);
        crops.add(Material.COCOA);

        // Honey blocks
        honeyProducers.add(Material.BEEHIVE);
        honeyProducers.add(Material.BEE_NEST);

        // Furnaces
        furnaces.add(Material.FURNACE);
        furnaces.add(Material.BLAST_FURNACE);
        furnaces.add(Material.SMOKER);

        // Containers
        containers.add(Material.CHEST);
        containers.add(Material.TRAPPED_CHEST);
        containers.add(Material.BARREL);
        containers.add(Material.HOPPER);

        // Minetorio
        if (Minetorio.getIsMTInstalled()) {
            generators.add(CrankGenerator.class);
            generators.add(MoonlightPanel.class);
            generators.add(CombustionGenerator.class);
            generators.add(ColdFusionReactor.class);
            generators.add(GeothermalGenerator.class);
            generators.add(HeatExchanger.class);
            generators.add(LightningGenerator.class);
            generators.add(HeavySteamGenerator.class);
            generators.add(NuclearReactor.class);
            generators.add(PetroleumEngine.class);
            generators.add(SolarPanel.class);
            generators.add(SteamEngine.class);
            generators.add(TidalGenerator.class);
            generators.add(WindTurbine.class);
            devices.add(CrankGenerator.class);
            devices.add(Centrifuge.class);
            devices.add(PollutionGenerator.class);
            devices.add(MoonlightPanel.class);
            devices.add(CombustionGenerator.class);
            devices.add(ColdFusionReactor.class);
            devices.add(GeothermalGenerator.class);
            devices.add(HeatExchanger.class);
            devices.add(LightningGenerator.class);
            devices.add(HeavySteamGenerator.class);
            devices.add(NuclearReactor.class);
            devices.add(PetroleumEngine.class);
            devices.add(SolarPanel.class);
            devices.add(SteamEngine.class);
            devices.add(TidalGenerator.class);
            devices.add(WindTurbine.class);
            devices.add(Battery2.class);
            devices.add(BatteryMonitor.class);
            devices.add(Mover.class);
            devices.add(OverDriver.class);
            devices.add(PowerPylon.class);
            devices.add(PowerPylonMk2.class);
            devices.add(PowerSubstation.class);
            devices.add(Turret.class);
            devices.add(ChunkLoader.class);
            devices.add(PowerMeter.class);
            devices.add(PowerProvider.class);
            devices.add(PowerReceiver.class);
            devices.add(PowerTransmitter.class);
            devices.add(ProductionMonitor.class);
            devices.add(RedstoneEmitter.class);
            devices.add(Elevator.class);
            devices.add(HumanTeleporter.class);
            devices.add(ItemTeleporter.class);
            devices.add(Locomotive.class);
            devices.add(LogisticBot.class);
            devices.add(ProviderChest.class);
            devices.add(RequesterChest.class);
            devices.add(Roboport.class);
            devices.add(RocketSilo.class);
            devices.add(StationFinder.class);
            devices.add(AutoBreaker.class);
            devices.add(AutoBreeder.class);
            devices.add(AutoButcher.class);
            devices.add(AutoCauldron.class);
            devices.add(AutoCollector.class);
            devices.add(AutoHarvester.class);
            devices.add(AutoLogger.class);
            devices.add(AutoMilker.class);
            devices.add(AutoMiner.class);
            devices.add(AutoPlacer.class);
            devices.add(AutoPlanter.class);
            devices.add(AutoPlucker.class);
            devices.add(AutoShearer.class);
            devices.add(DeepDrill.class);
            devices.add(ElectricFurnace.class);
            devices.add(AdvancedAssembler.class);
            devices.add(AutoTimer.class);
            devices.add(BasicAssembler.class);
            devices.add(Crafter2.class);
            devices.add(Crusher.class);
            devices.add(Incinerator.class);
            devices.add(Infuser.class);
            devices.add(ItemForge.class);
            devices.add(OilRefinery.class);
            devices.add(Sifter.class);
            devices.add(BarrelPump.class);
            devices.add(Boiler.class);
            devices.add(Pump.class);
            devices.add(AdvancedBowTurret.class);
            devices.add(ArtilleryDevice.class);
            devices.add(BiterDetector.class);
            devices.add(BowTurret.class);
            devices.add(FloodLight.class);
            devices.add(LaserTurret.class);
            devices.add(MineLayer.class);
            devices.add(MobBlocker.class);
            devices.add(ResearchLab.class);
        }

        // Pets
        pets.add(EntityType.CAT);
        pets.add(EntityType.WOLF);
        pets.add(EntityType.PARROT);
        pets.add(EntityType.HORSE);
        pets.add(EntityType.MULE);
        pets.add(EntityType.DONKEY);
        pets.add(EntityType.LLAMA);

        // Entities to not render for
        notRender.add(EntityType.ITEM);
        notRender.add(EntityType.ITEM_DISPLAY);
        notRender.add(EntityType.BLOCK_DISPLAY);
        notRender.add(EntityType.INTERACTION);
        notRender.add(EntityType.EVOKER_FANGS);
        notRender.add(EntityType.EXPERIENCE_BOTTLE);
        notRender.add(EntityType.EXPERIENCE_ORB);
        notRender.add(EntityType.FALLING_BLOCK);
        notRender.add(EntityType.LIGHTNING_BOLT);
        notRender.add(EntityType.MARKER);
        notRender.add(EntityType.POTION);
        notRender.add(EntityType.FIREBALL);
        notRender.add(EntityType.SMALL_FIREBALL);
        notRender.add(EntityType.SHULKER_BULLET);
        notRender.add(EntityType.FIREWORK_ROCKET);
        notRender.add(EntityType.EYE_OF_ENDER);
        notRender.add(EntityType.SNOWBALL);
        notRender.add(EntityType.ENDER_PEARL);
        notRender.add(EntityType.DRAGON_FIREBALL);
        notRender.add(EntityType.SPECTRAL_ARROW);
        notRender.add(EntityType.ARROW);
        notRender.add(EntityType.TRIDENT);
        notRender.add(EntityType.WITHER);
        notRender.add(EntityType.ENDER_DRAGON);
    }

    public static List<Material> getRedstoneProviders() {
        return redstoneProviders;
    }

    public static List<Material> getRedstoneComponents() {
        return redstoneComponents;
    }

    public static List<Material> getCrops() {
        return crops;
    }

    public static List<Material> getHoneyProducers() {
        return honeyProducers;
    }

    public static List<Material> getFurnaces() {
        return furnaces;
    }

    public static List<Material> getContainers() {
        return containers;
    }

    public static List<Class<?>> getDevices() {
        return devices;
    }

    public static List<Class<?>> getGenerators() {
        return generators;
    }

    public static List<EntityType> getPets() {
        return pets;
    }

    public static List<EntityType> getNotRender() {
        return notRender;
    }

    public static boolean isGenerator(Device device) {
        for (Class<?> generator : generators) {
            if (generator.isInstance(device)) {
                return true;
            }
        }
        return false;
    }
}
