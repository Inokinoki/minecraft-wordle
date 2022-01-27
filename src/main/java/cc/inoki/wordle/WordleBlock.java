package cc.inoki.wordle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;

public class WordleBlock extends Block {
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
}

