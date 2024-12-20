package com.github.masongulu.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;

import static com.github.masongulu.ModBlocks.*;
import static com.github.masongulu.item.ModItems.SROM_ITEM;

public class DataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.addProvider(TagGenerator::new);
        generator.addProvider(ItemModelGenerator::new);
    }

    private static class TagGenerator extends FabricTagProvider.BlockTagProvider {
        public TagGenerator(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        protected void generateTags() {
            getOrCreateTagBuilder(DEVICE_CABLE)
                    .add(CABLE_BLOCK.get());
            getOrCreateTagBuilder(SERIAL_CABLE)
                    .add(SERIAL_CABLE_BLOCK.get())
                    .add(SERIAL_TERMINAL_BLOCK.get())
                    .add(SERIAL_DEVICE_BLOCK.get());
        }
    }

    private static class ItemModelGenerator extends FabricModelProvider {

        public ItemModelGenerator(FabricDataGenerator dataGenerator) {
            super(dataGenerator);
        }

        @Override
        public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        }

        @Override
        public void generateItemModels(ItemModelGenerators itemModelGenerator) {
            itemModelGenerator.generateFlatItem(SROM_ITEM.get(), ModelTemplates.FLAT_ITEM);
        }
    }
}
