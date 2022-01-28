package cc.inoki.wordle;

import java.util.Optional;
import java.util.HashSet;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WordleBlock extends Block {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final EnumProperty<WordleAlphabetState> WORDLE_STATE
        = EnumProperty.create("wordle_state", WordleAlphabetState.class);
    public static final IntegerProperty WORDLE_CHAR
        = IntegerProperty.create("wordle_char", 97, 123);

    public WordleBlock(Properties p, Integer i) {
        super(p);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(WORDLE_STATE, WordleAlphabetState.DEFAULT)
            .setValue(WORDLE_CHAR, i));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WORDLE_STATE, WORDLE_CHAR);
    }

    private enum WordleMatchedDirection {
        NOT_MATCHED,
        WEST,
        EAST,
        NORTH,
        SOUTH
    }

    private void updateSuccessWordleBlockState(String wordToMatch, World worldIn, BlockPos pos, WordleMatchedDirection dir) {
        int length = wordToMatch.length();
        for (int j = 1; j <= length; j++) {
            BlockPos detectPos;
            switch (dir) {
                case EAST:
                    detectPos = pos.east(j);
                    break;
                case WEST:
                    detectPos = pos.west(j);
                    break;
                case SOUTH:
                    detectPos = pos.south(j);
                    break;
                case NORTH:
                    detectPos = pos.north(j);
                    break;
                default:
                    detectPos = BlockPos.ZERO;
                    break;
            }

            Optional<Integer> wordleCharState = worldIn.getBlockState(detectPos).getOptionalValue(WORDLE_CHAR);
            if (wordleCharState.isPresent()) {
                BlockState state = worldIn.getBlockState(detectPos);
                state = state.setValue(WORDLE_STATE, WordleAlphabetState.CORRECT);
                worldIn.setBlockAndUpdate(detectPos, state);
            }
        }
    }

    private void updateBlockStateByWordle(String wordToMatch, World worldIn, BlockPos pos, WordleMatchedDirection dir) {
        int length = wordToMatch.length();
        
        HashSet<Integer> letterSet = new HashSet(length);
        for (int i = 0; i < length; i++) {
            letterSet.add((int)wordToMatch.charAt(i));
        }

        for (int j = 1; j <= length; j++) {
            BlockPos detectPos;
            switch (dir) {
                case EAST:
                    detectPos = pos.east(j);
                    break;
                case WEST:
                    detectPos = pos.west(j);
                    break;
                case SOUTH:
                    detectPos = pos.south(j);
                    break;
                case NORTH:
                    detectPos = pos.north(j);
                    break;
                default:
                    detectPos = BlockPos.ZERO;
                    break;
            }

            Optional<Integer> wordleCharState = worldIn.getBlockState(detectPos).getOptionalValue(WORDLE_CHAR);
            if (wordleCharState.isPresent()) {
                if (wordleCharState.get().intValue() == (int)wordToMatch.charAt(length - j)) {
                    // Matched
                    BlockState state = worldIn.getBlockState(detectPos);
                    state = state.setValue(WORDLE_STATE, WordleAlphabetState.CORRECT);
                    worldIn.setBlockAndUpdate(detectPos, state);
                } else {
                    // Test if it exists
                    if (letterSet.contains(wordleCharState.get().intValue())) {
                        // Exist
                        BlockState state = worldIn.getBlockState(detectPos);
                        state = state.setValue(WORDLE_STATE, WordleAlphabetState.EXIST);
                        worldIn.setBlockAndUpdate(detectPos, state);
                    }
                }
            }
        }
    }

    private WordleMatchedDirection findAndSetWordleBlock(String wordToMatch, World worldIn, BlockPos pos) {
        int length = wordToMatch.length();
        if (length == 0) {
            return WordleMatchedDirection.NOT_MATCHED;
        }
        // Update the first unmatched
        int matchedDirectionCount = 0;

        // Find the word coming from east
        boolean allOK = true;
        for (int j = 1; j <= length; j++) {
            BlockPos detectPos = pos.east(j);

            Optional<Integer> wordleCharState = worldIn.getBlockState(detectPos).getOptionalValue(WORDLE_CHAR);
            if (wordleCharState.isPresent()) {
                if (wordleCharState.get().intValue() != (int)wordToMatch.charAt(length - j)) {
                    allOK = false;
                }
                matchedDirectionCount++;
            } else {
                allOK = false;
                break;
            }
        }
        if (allOK) {
            LOGGER.info("Word matched from east");
            return WordleMatchedDirection.EAST;
        }
        if (matchedDirectionCount == length) updateBlockStateByWordle(wordToMatch, worldIn, pos, WordleMatchedDirection.EAST);

        // Find the word coming from west
        matchedDirectionCount = 0;
        allOK = true;
        for (int j = 1; j <= length; j++) {
            BlockPos detectPos = pos.west(j);

            Optional<Integer> wordleCharState = worldIn.getBlockState(detectPos).getOptionalValue(WORDLE_CHAR);
            if (wordleCharState.isPresent()) {
                if (wordleCharState.get().intValue() != (int)wordToMatch.charAt(length - j)) {
                    allOK = false;
                }
                matchedDirectionCount++;
            } else {
                allOK = false;
                break;
            }
        }
        if (allOK) {
            LOGGER.info("Word matched from west");
            return WordleMatchedDirection.WEST;
        }
        if (matchedDirectionCount == length) updateBlockStateByWordle(wordToMatch, worldIn, pos, WordleMatchedDirection.WEST);

        // Find the word coming from south
        matchedDirectionCount = 0;
        allOK = true;
        for (int j = 1; j <= length; j++) {
            BlockPos detectPos = pos.south(j);

            Optional<Integer> wordleCharState = worldIn.getBlockState(detectPos).getOptionalValue(WORDLE_CHAR);
            if (wordleCharState.isPresent()) {
                if (wordleCharState.get().intValue() != (int)wordToMatch.charAt(length - j)) {
                    allOK = false;
                }
                matchedDirectionCount++;
            } else {
                allOK = false;
                break;
            }
        }
        if (allOK) {
            LOGGER.info("Word matched from south");
            return WordleMatchedDirection.SOUTH;
        }
        if (matchedDirectionCount == length) updateBlockStateByWordle(wordToMatch, worldIn, pos, WordleMatchedDirection.SOUTH);

        // Find the word coming from north
        matchedDirectionCount = 0;
        allOK = true;
        for (int j = 1; j <= length; j++) {
            BlockPos detectPos = pos.north(j);

            Optional<Integer> wordleCharState = worldIn.getBlockState(detectPos).getOptionalValue(WORDLE_CHAR);
            if (wordleCharState.isPresent()) {
                if (wordleCharState.get().intValue() != (int)wordToMatch.charAt(length - j)) {
                    allOK = false;
                }
                matchedDirectionCount++;
            } else {
                allOK = false;
                break;
            }
        }
        if (allOK) {
            LOGGER.info("Word matched from north");
            return WordleMatchedDirection.NORTH;
        }
        if (matchedDirectionCount == length) updateBlockStateByWordle(wordToMatch, worldIn, pos, WordleMatchedDirection.NORTH);

        return WordleMatchedDirection.NOT_MATCHED;
    }

    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        LOGGER.info("Block set at " + pos.toShortString() + " " + state.toString());
        if (state.getValue(WORDLE_CHAR) > (int)'z') {
            // Only handle when enter clicked
            switch (state.getValue(WORDLE_STATE)) {
                case DEFAULT:
                    // Find and update the word
                    WordleMatchedDirection direction = this.findAndSetWordleBlock("aaaaa", worldIn, pos);
                    if (direction != WordleMatchedDirection.NOT_MATCHED) {
                        updateSuccessWordleBlockState("aaaaa", worldIn, pos, direction);
                        state = state.setValue(WORDLE_STATE, WordleAlphabetState.CORRECT);
                        worldIn.setBlockAndUpdate(pos, state);
                    } else {
                        LOGGER.info("Not yet matched");
                    }
                    break;
                default:
            }
        }
    }

    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        LOGGER.info("Block destroyed at " + pos.toShortString() + " " + state.toString());
        super.playerWillDestroy(worldIn, pos, state, player);
    }
}

