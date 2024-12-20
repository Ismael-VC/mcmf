package com.github.masongulu.serial.block;

import com.github.masongulu.devices.block.GenericDeviceBlock;
import com.github.masongulu.serial.block.entity.SerialDeviceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class SerialDeviceBlock extends GenericDeviceBlock {
    public SerialDeviceBlock() {
        super(Properties.of(Material.STONE));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SerialDeviceBlockEntity(blockPos, blockState);
    }
}
