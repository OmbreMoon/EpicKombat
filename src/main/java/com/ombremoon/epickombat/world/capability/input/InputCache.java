package com.ombremoon.epickombat.world.capability.input;

import com.ombremoon.epickombat.skill.EpicKombatDataKeys;
import com.ombremoon.epickombat.skill.KombatSlots;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;

import java.util.List;

public class InputCache {
    private final ObjectArrayList<Input> savedInputs = new ObjectArrayList<>();
    private final LocalPlayerPatch playerPatch;

    protected InputCache(LocalPlayerPatch playerPatch) {
        this.playerPatch = playerPatch;
    }

    public void cacheInput(Input input) {
        this.savedInputs.add(input);
    }

    public Input getFirst() {
        return this.get(0);
    }

    public Input get(int index) {
        return this.savedInputs.get(index);
    }

    public int size() {
        return this.savedInputs.size();
    }

    public boolean isEmpty() {
        return this.savedInputs.isEmpty();
    }

    public void appendLast(Input input) {
        int index = this.savedInputs.size() - 1;
        this.savedInputs.set(index, this.savedInputs.get(index).append(input));
    }

    public void clearCache() {
        this.savedInputs.clear();
        this.playerPatch.getSkill(KombatSlots.BASIC).getDataManager().setDataSync(EpicKombatDataKeys.FIRST_INPUT.get(), Input.EMPTY, playerPatch.getOriginal());;
    }

    public Input getInput() {
        return new Input().append(this.savedInputs);
    }

    public void serialize(ByteBuf buf) {
        buf.writeInt(this.savedInputs.size());
        for (Input input : this.savedInputs) {
            input.write(buf);
        }
    }

    public static List<Input> deserialize(ByteBuf buf) {
        int size = buf.readInt();
        List<Input> inputs = new ObjectArrayList<>();
        for (int i = 0; i < size; i++) {
            Input input = Input.read(buf);
            inputs.add(input);
        }

        return inputs;
    }

    @Override
    public String toString() {
        return "Cache: " + this.savedInputs;
    }
}
