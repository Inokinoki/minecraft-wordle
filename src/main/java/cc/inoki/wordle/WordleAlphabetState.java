package cc.inoki.wordle;

import net.minecraft.util.IStringSerializable;

public enum WordleAlphabetState implements IStringSerializable {
    DEFAULT,
    CORRECT,
    EXIST;
 
    public String toString() {
       return this.getSerializedName();
    }
 
    public String getSerializedName() {
       return this == DEFAULT ? "default" : (this == CORRECT ? "right" : "exist");
    }
}
