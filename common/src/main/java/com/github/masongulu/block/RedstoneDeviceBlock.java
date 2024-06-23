package com.github.masongulu.block;

import com.github.masongulu.block.entity.CableBlockEntity;
import com.github.masongulu.block.entity.ModBlockEntities;
import com.github.masongulu.block.entity.RedstoneDeviceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RedstoneDeviceBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING;

    public RedstoneDeviceBlock() {
        super(Properties.of(Material.STONE));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    public BlockEntity newBlockEntity(BlockPos arg, BlockState arg2) {
        return new RedstoneDeviceBlockEntity(arg, arg2);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }


    private Map<Direction, Integer> lastRedstone = new HashMap<>();
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (!level.isClientSide) {
            Direction facing = blockState.getValue(FACING);
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof RedstoneDeviceBlockEntity r) {
                Map<Direction, Integer> newRedstone = new HashMap<>();
                boolean different = false;
                for (Direction direction : Direction.values()) {
                    int rsLevel = level.getDirectSignal(blockPos, adjustForRotation(facing, direction));
                    newRedstone.put(direction, rsLevel);
                    if (rsLevel != lastRedstone.getOrDefault(direction, 0)) {
                        different = true;
                    }
                }
                if (different) {
                    r.updateRedstone(newRedstone);
                    lastRedstone = newRedstone;
                }
            }
        }
    }

    private Direction adjustForRotation(Direction facing, Direction direction) {
        if (direction != Direction.UP && direction != Direction.DOWN && facing != Direction.NORTH) {
            // There surely is a better way of doing this.
            if (facing == Direction.EAST) {
                return direction.getCounterClockWise();
            } else if (facing == Direction.SOUTH) {
                return direction.getOpposite();
            } else if (facing == Direction.WEST) {
                return direction.getClockWise();
            }
        }
        return direction;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (blockEntityType == ModBlockEntities.REDSTONE_DEVICE_BLOCK_ENTITY) {
            return RedstoneDeviceBlockEntity::tick;
        }
        return null;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        BlockEntity blockEntity = blockGetter.getBlockEntity(blockPos);
        if (blockEntity instanceof RedstoneDeviceBlockEntity r) {
            Direction facing = blockState.getValue(FACING);
            Direction adjusted = adjustForRotation(direction, facing);

            return r.redstoneOutputs.get(adjusted);
        }
        return super.getSignal(blockState, blockGetter, blockPos, direction);
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return getSignal(blockState, blockGetter, blockPos, direction);
    }


    static {
        FACING = HorizontalDirectionalBlock.FACING;
    }
}
