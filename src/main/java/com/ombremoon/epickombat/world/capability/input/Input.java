package com.ombremoon.epickombat.world.capability.input;

import net.minecraft.network.FriendlyByteBuf;

import java.util.Arrays;
import java.util.List;

public class Input {
    public static final Input W = new Input("w", true);
    public static final Input A = new Input("a", true);
    public static final Input S = new Input("s", true);
    public static final Input D = new Input("d", true);
    public static final Input JUMP = new Input("j", true);
    public static final Input CROUCH = new Input("c", true);
    public static final Input BLOCK = new Input("b", false);
    public static final Input FP = new Input("1", false);
    public static final Input BP = new Input("2", false);
    public static final Input FK = new Input("3", false);
    public static final Input BK = new Input("4", false);

    private String input;
    private boolean isMovement;
    private int size = 1;

    Input(String input, boolean isMovement) {
        this.input = input;
        this.isMovement = isMovement;
    }

    Input(String input, boolean isMovement, int size) {
        this(input, isMovement);
        this.size = size;
    }

    Input() {
        this("", true, 0);
    }

    public Input append(Input input) {
        return this.append(List.of(input));
    }

    public Input append(List<Input> inputs) {
        Input input = this;
        if (input.size >= 4) return input;

        if (inputs.size() > 4 || input.size + inputs.size() > 4)
            throw new IllegalArgumentException("Input length (" + inputs.size() + ") must not exceed max input length (4).");

        StringBuilder s = new StringBuilder(this.input);
        boolean isMovement = true;
        for (Input value : inputs) {
            if (value.size + input.size > 4)
                throw new IllegalArgumentException("Cannot append input of size " + value.size + "to input of size " + input.size);

            s.append(value.input);
            isMovement &= value.isMovement;
            input = new Input(sortString(s.toString()), isMovement, value.size + input.size);
        }
        return input;
    }

    public Input hold() {
        if (this.size > 1)
            throw new IllegalArgumentException("Input size (" + this.size + ") must not exceed max input hold size (1).");

        return this.append(this);
    }

    public boolean canAppend(Input input) {
        return this.size + input.size <= 4;
    }

    public boolean isPartialMatch(Input input) {
        /*CHECK IF INPUT STRING CONTAINS AT LEAST 50% OF THE COMBO STRING*/
        return this.input.equals("ccc");
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void clear() {
        this.input = "";
        this.isMovement = true;
        this.size = 0;
    }

    public boolean isOpposite(Input input) {
        return isOppositeOf(this, input);
    }

    private static boolean isOppositeOf(Input input, Input other) {
        return (input == Input.W && other == Input.S) || (input == Input.A && other == Input.D) || (input == Input.JUMP && other == Input.CROUCH)
                || (input == Input.S && other == Input.W) || (input == Input.D && other == Input.A) || (input == Input.CROUCH && other == Input.JUMP);
    }

    public boolean isMovement() {
        return this.isMovement;
    }

    public String getInput() {
        return this.input;
    }

    public static String sortString(String inputString) {
        char[] charArray = inputString.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }

    @Override
    public String toString() {
        return "Input: " + this.input + ", IsMovement: " + this.isMovement + ", Size: " + this.size;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof Input i && sortString(this.input).equals(sortString(i.input)) && this.size == i.size;
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.input);
        buf.writeBoolean(this.isMovement);
        buf.writeInt(this.size);
    }

    public static Input read(FriendlyByteBuf buf) {
        return new Input(buf.readUtf(), buf.readBoolean(), buf.readInt());
    }

    public enum Timing {
        SHORT(2),
        MEDIUM(3),
        LONG(4);

        int duration;

        Timing(int duration) {
            this.duration = duration;
        }

        public int getDuration() {
            return this.duration;
        }
    }
}
