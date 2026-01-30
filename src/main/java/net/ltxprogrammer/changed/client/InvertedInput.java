package net.ltxprogrammer.changed.client;

import net.minecraft.client.player.Input;
import net.minecraft.world.phys.Vec2;

public class InvertedInput extends Input {
    public final Input wrappedInput;

    public InvertedInput(Input wrappedInput) {
        this.wrappedInput = wrappedInput;
    }

    @Override
    public void tick(boolean movingSlowly) {
        super.tick(movingSlowly);
        wrappedInput.tick(movingSlowly);

        this.down = wrappedInput.up;
        this.up = wrappedInput.down;
        this.left = wrappedInput.right;
        this.right = wrappedInput.left;

        this.forwardImpulse = -wrappedInput.forwardImpulse;
        this.leftImpulse = -wrappedInput.leftImpulse;
        this.jumping = wrappedInput.jumping;
        this.shiftKeyDown = wrappedInput.shiftKeyDown;
    }

    @Override
    public Vec2 getMoveVector() {
        return super.getMoveVector().scale(-1.0f);
    }
}
