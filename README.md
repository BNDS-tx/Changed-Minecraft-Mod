<p align="center">
    <img src="images/icon@400.png" alt="Mod Logo" width="200"><br><br>
    <a href="https://discord.com/invite/MpynqpRN6p" rel="Discord"><img src="/images/discord-custom_vector.svg"></a>
    <a href="https://www.patreon.com/ltxprogrammer" rel="Discord"><img src="https://raw.githubusercontent.com/intergrav/devins-badges/1aec26abb75544baec37249f42008b2fcc0e731f/assets/cozy/donate/patreon-singular_vector.svg"></a>
    <a href="https://modrinth.com/mod/changed-minecraft-mod" rel="Discord"><img src="https://raw.githubusercontent.com/intergrav/devins-badges/1aec26abb75544baec37249f42008b2fcc0e731f/assets/cozy/available/modrinth_vector.svg"></a>
    <a href="https://www.curseforge.com/minecraft/mc-mods/changed-minecraft-mod" rel="Discord"><img src="https://raw.githubusercontent.com/intergrav/devins-badges/1aec26abb75544baec37249f42008b2fcc0e731f/assets/cozy/available/curseforge_vector.svg"></a>
</p>

---

This repository holds the source code for the **Changed: Minecraft Mod Modefied by Azurebyss(BNDS-tx)**. Releases **of original mod only** are compiled and published to both Modrinth and Curseforge. Credits for contributors **of original mod** are available on [GitHub Insights of Original Mod](https://github.com/LtxProgrammer/Changed-Minecraft-Mod/graphs/contributors) and in the mod menu.

**Unauthorised redistribution, promotion, or commercial use is strictly prohibited! LtxProgrammer holds the copyright for Changed: Minecraft Mod, while Azurebyss (BNDS-tx) only holds the copyright and ownership rights for this modification. This modification possesses no rights for and must not be used for distribution, promotion, or commercial purposes. All matters concerning Changed: Minecraft Mod should be addressed directly with LtxProgrammer. Azurebyss (BNDS-tx) holds no related rights of Changed: Minecraft Mod and assumes no liability for associated copyright risks or responsibilities related with Changed: Minecraft Mod.**

All descriptions bellow and icons with links above are for the **original Changed: Minecraft Mod by LtxProgrammer**, and this modification is currently based on Changed: Minecraft Mod v1.14.1c for Minecraft 1.18.2 and not sure whether will stay up-to-date with the original mod.

---

本仓库为 **Changed: Minecraft Mod 改编版 by Azurebyss(蓝夜深空)** 的源码仓库。所有**仅限由 LtxProgrammer 开发制作的原版 Mod 内容**的编译与发布都已由 LtxProgrammer 亲自发布至 Modrinth 和 Curseforge 平台，本改编版暂不对外发布。如有自用或学习需要请自行下载编译。

**未经允许不得擅自对外再分发、宣传、商用！LtxProgrammer 为 Changed: Minecraft Mod 的版权持有者，Azurebyss(蓝夜深空) 仅为本改编版的版权与所有权持有者。本改编版不具备且不得用于分发、宣传与商用用途，任何有关 Changed: Minecraft Mod 的相关事宜请直接与 LtxProgrammer 联系，Azurebyss(蓝夜深空) 不持有相关权利，不承担相关版权风险与责任。**

所有**原版 Mod** 的贡献者都在[原版的贡献列表](https://github.com/LtxProgrammer/Changed-Minecraft-Mod/graphs/contributors)以及本改编版的 Mod 菜单中陈列。当前本改编版基于**原版 Changed: Minecraft Mod by LtxProgrammer** 的 v1.14.1c 版本二次开发，仅适用于我的世界（Minecraft）Java 版 1.18.2 版本 Forge 平台，未来是否会继续跟随原版版本更新对齐暂且未定。

---

## How can I help?
Any aspiring developer is welcome to fork and create a pull request to submit their content. Programmers, texture artists, and 3D modelers all have a place here.
- Textures are kept in *src/main/resources/assets/changed/textures*
- Java code is in *src/main/java/net/ltxprogrammer/changed*
- 3D models are kept in *3dmodels*

Even if you aren't a developer, you can help with translations, documentation, or other simple issues. However, any changes you make to the code or files should be on your own fork. Create a pull request when you are ready to submit any changes.

## Expectation of Quality
We strive to keep the code and assets of the Changed: Minecraft Mod at a excellent level of quality. All pull requests and issues are subject to be reviewed for quality assurance. Some quality points to keep in mind:
- Keep translation files organized (reference `en_us.json` for order/layout)
- Code should be efficient, well thought out, and should be able to handle most variations of mod environments.
- Code comments (if any) should be written in English. Comments are only necessary if the code may be difficult to understand
- Textures should fit Minecraft's vanilla style.
- 3D should use textures to show small/medium detail, and should reserve additional parts for large details.
- Issues should be written clearly in English

Submitting content that fails to meet quality expectations isn't an issue, and can be a good learning experience on what can be improved. However, excessively submitting issues/pull requests that repeatedly fail to meet quality expectations will result in a warning, and then a ban from contributing.

## How can I make my own extension mod?

Please refer to the [wiki](https://github.com/LtxProgrammer/Changed-Minecraft-Mod/wiki) for tutorials on setting up, and using the API.

## How can I compile the mod?

Without a IDE and assuming you have `git` installed and Java 17 as your default java:
- Clone the repo `git clone https://github.com/LtxProgrammer/Changed-Minecraft-Mod.git`
- Navigate into the directory `cd Changed-Minecraft-Mod`
- Run Gradlew `./gradlew build` (Linux/MacOS) or `gradlew build` (Windows)
- Once completed, check builds/libs for the results.

Now you can test the latest commit of the mod, enjoy.
