package net.ltxprogrammer.changed.aaBackport;

import java.nio.FloatBuffer;

// 引入你的 repack 库
// 注意：根据你实际使用的库，包名可能是 org.joml 或 repack.joml


public class JomlConverter {

    // 缓存一个 FloatBuffer 防止每帧 GC，线程不安全，如果涉及多线程请用 ThreadLocal
//    private static final FloatBuffer BUFFER_9 = FloatBuffer.allocate(9);
//    private static final FloatBuffer BUFFER_16 = FloatBuffer.allocate(16);

    // ================= Vector3f =================

    // Mojang -> JOML
    public repack.joml.Vector3f toJoml(com.mojang.math.Vector3f vec) {
        return new repack.joml.Vector3f(vec.x(), vec.y(), vec.z());
    }

    // JOML -> Mojang
    public com.mojang.math.Vector3f toMojang(repack.joml.Vector3f vec) {
        return new com.mojang.math.Vector3f(vec.x, vec.y, vec.z);
    }

    // ================= Vector4f =================

    public repack.joml.Vector4f toJoml(com.mojang.math.Vector4f vec) {
        return new repack.joml.Vector4f(vec.x(), vec.y(), vec.z(), vec.w());
    }

    public com.mojang.math.Vector4f toMojang(repack.joml.Vector4f vec) {
        return new com.mojang.math.Vector4f(vec.x, vec.y, vec.z, vec.w);
    }

    // ================= Quaternion =================

    public repack.joml.Quaternionf toJoml(com.mojang.math.Quaternion quat) {
        // 注意 Mojang 的 Quaternion 也是 i, j, k, r (或者 x, y, z, w)
        return new repack.joml.Quaternionf(quat.i(), quat.j(), quat.k(), quat.r());
    }

    public com.mojang.math.Quaternion toMojang(repack.joml.Quaternionf quat) {
        return new com.mojang.math.Quaternion(quat.x, quat.y, quat.z, quat.w);
    }

    // ================= Matrix3f (重点) =================

    /**
     * 将 Mojang 矩阵转换为 JOML 矩阵
     */
    public repack.joml.Matrix3f toJoml(com.mojang.math.Matrix3f mojangMat) {
        var jomlMat = new repack.joml.Matrix3f();
        jomlMat.m00(mojangMat.m00);
        jomlMat.m01(mojangMat.m10);
        jomlMat.m02(mojangMat.m20);
        jomlMat.m10(mojangMat.m01);
        jomlMat.m11(mojangMat.m11);
        jomlMat.m12(mojangMat.m21);
        jomlMat.m20(mojangMat.m02);
        jomlMat.m21(mojangMat.m12);
        jomlMat.m22(mojangMat.m22);
        return jomlMat; // JOML 从 Buffer 读取
    }

    /**
     * 将 JOML 矩阵转换为 Mojang 矩阵
     */
    public com.mojang.math.Matrix3f toMojang(repack.joml.Matrix3f jomlMat) {
        var mojangMat = new com.mojang.math.Matrix3f();
        mojangMat.m00 = jomlMat.m00();
        mojangMat.m01 = jomlMat.m10();
        mojangMat.m02 = jomlMat.m20();
        mojangMat.m10 = jomlMat.m01();
        mojangMat.m11 = jomlMat.m11();
        mojangMat.m12 = jomlMat.m21();
        mojangMat.m20 = jomlMat.m02();
        mojangMat.m21 = jomlMat.m12();
        mojangMat.m22 = jomlMat.m22();
        return mojangMat;
    }

    // ================= Matrix4f (重点) =================

    /**
     * 将 Mojang 矩阵转换为 JOML 矩阵
     */
    public repack.joml.Matrix4f toJoml(com.mojang.math.Matrix4f mojangMat) {
        var jomlMat = new repack.joml.Matrix4f();
        jomlMat.m00(mojangMat.m00);
        jomlMat.m01(mojangMat.m10);
        jomlMat.m02(mojangMat.m20);
        jomlMat.m03(mojangMat.m30);
        jomlMat.m10(mojangMat.m01);
        jomlMat.m11(mojangMat.m11);
        jomlMat.m12(mojangMat.m21);
        jomlMat.m13(mojangMat.m31);
        jomlMat.m20(mojangMat.m02);
        jomlMat.m21(mojangMat.m12);
        jomlMat.m22(mojangMat.m22);
        jomlMat.m23(mojangMat.m32);
        jomlMat.m30(mojangMat.m03);
        jomlMat.m31(mojangMat.m13);
        jomlMat.m32(mojangMat.m23);
        jomlMat.m33(mojangMat.m33);
        return jomlMat;
    }

    /**
     * 将 JOML 矩阵转换为 Mojang 矩阵
     */
    public com.mojang.math.Matrix4f toMojang(repack.joml.Matrix4f jomlMat) {
        var mojangMat = new com.mojang.math.Matrix4f();
        mojangMat.m00 = jomlMat.m00();
        mojangMat.m01 = jomlMat.m10();
        mojangMat.m02 = jomlMat.m20();
        mojangMat.m03 = jomlMat.m30();
        mojangMat.m10 = jomlMat.m01();
        mojangMat.m11 = jomlMat.m11();
        mojangMat.m12 = jomlMat.m21();
        mojangMat.m13 = jomlMat.m31();
        mojangMat.m20 = jomlMat.m02();
        mojangMat.m21 = jomlMat.m12();
        mojangMat.m22 = jomlMat.m22();
        mojangMat.m23 = jomlMat.m32();
        mojangMat.m30 = jomlMat.m03();
        mojangMat.m31 = jomlMat.m13();
        mojangMat.m32 = jomlMat.m23();
        mojangMat.m33 = jomlMat.m33();
        return mojangMat;
    }
}