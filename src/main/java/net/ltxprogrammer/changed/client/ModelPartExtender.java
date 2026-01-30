package net.ltxprogrammer.changed.client;

import net.minecraft.client.model.geom.ModelPart;

import java.util.Random;


public interface ModelPartExtender {
    void addTriangle(Triangle triangle);

    ModelPart.Cube getRandomCubeWeighted(Random random);
}
