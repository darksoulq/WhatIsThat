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

import java.util.ArrayList;
import java.util.List;

public class MTMachines {
    private List<Class<?>> generators = new ArrayList<>();
    private List<Class<?>> devices = new ArrayList<>();

    public MTMachines() {
        setupGenerators();
        setupDevices();
    }

    private void setupGenerators() {
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
    }

    private void setupDevices() {
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
    
    public List<Class<?>> getGenerators() {
        return generators;
    }

    public boolean isGenerator(Device device) {
        for (Class<?> generator : generators) {
            if (generator.isInstance(device)) {
                return true;
            }
        }
        return false;
    }

    public List<Class<?>> getDevices() {
        return devices;
    }
}
