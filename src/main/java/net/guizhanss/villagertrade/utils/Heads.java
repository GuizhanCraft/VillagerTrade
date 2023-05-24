package net.guizhanss.villagertrade.utils;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

public enum Heads {
    // https://minecraft-heads.com/custom-heads/humanoid/41283-wandering-trader
    WANDERING_TRADER("499d585a9abf59fae277bb684d24070cef21e35609a3e18a9bd5dcf73a46ab93"),
    // https://minecraft-heads.com/custom-heads/humanoid/32853-armorer-villager
    ARMORER("f522db92f188ebc7713cf35b4cbaed1cfe2642a5986c3bde993f5cfb3727664c"),
    // https://minecraft-heads.com/custom-heads/humanoid/32860-butcher-villager
    BUTCHER("c6774d2df515eceae9eed291c1b40f94adf71df0ab81c7191402e1a45b3a2087"),
    // https://minecraft-heads.com/custom-heads/humanoid/32867-cartographer-villager
    CARTOGRAPHER("94248dd0680305ad73b214e8c6b00094e27a4ddd8034676921f905130b858bdb"),
    // https://minecraft-heads.com/custom-heads/humanoid/32874-cleric-villager
    CLERIC("a8856eaafad96d76fa3b5edd0e3b5f45ee49a3067306ad94df9ab3bd5b2d142d"),
    // https://minecraft-heads.com/custom-heads/humanoid/30558-farmer-villager
    FARMER("d01e035a3d8d6126072bcbe52a97913ace93552a99995b5d4070d6783a31e909"),
    // https://minecraft-heads.com/custom-heads/humanoid/32888-fisherman-villager
    FISHERMAN("ac15e5fb56fa16b0747b1bcb05335f55d1fa31561c082b5e3643db5565410852"),
    // https://minecraft-heads.com/custom-heads/humanoid/53600-fletcher-villager
    FLETCHER("d831830a7bd3b1ab05beb98dc2f9fc5ea550b3cf649fd94d483da7cd39f7c063"),
    // https://minecraft-heads.com/custom-heads/humanoid/32902-leatherworker-villager
    LEATHERWORKER("f76cf8b7378e889395d538e6354a17a3de6b294bb6bf8db9c701951c68d3c0e6"),
    // https://minecraft-heads.com/custom-heads/humanoid/32909-librarian-villager
    LIBRARIAN("e66a53fc707ce1ff88a576ef40200ce8d49fae4acad1e3b3789c7d1cc1cc541a"),
    // https://minecraft-heads.com/custom-heads/humanoid/32913-mason-villager
    MASON("4c4d7ea038187770cc2e4817c9209e19b74f5d288ed633281ecccaf5c8ebc767"),
    // https://minecraft-heads.com/custom-heads/humanoid/32930-shepherd-villager
    SHEPHERD("19e04a752596f939f581930414561b175454d45a0506501e7d2488295a5d5de"),
    // https://minecraft-heads.com/custom-heads/humanoid/32935-toolsmith-villager
    TOOLSMITH("16ec61097e11bfe6f10aaa12e5c0a54c829bdbd9d9d7a32fc627e6b5a931e77"),
    // https://minecraft-heads.com/custom-heads/humanoid/32944-weaponsmith-villager
    WEAPONSMITH("5e409b958bc4fe045e95d325e6e97a533137e33fec7042ac027b30bb693a9d42");

    private final String hash;

    Heads(@Nonnull String hash) {
        this.hash = hash;
    }

    @Nonnull
    public ItemStack getItem() {
        return SlimefunUtils.getCustomHead(hash);
    }
}
