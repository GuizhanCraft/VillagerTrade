# VillagerTrade

VillagerTrade is a Slimefun addon that allows Slimefun items to be added to villager trades (also wandering traders).

## Download

[![Build status](https://thebusybiscuit.github.io/builds/ybw0014/VillagerTrade/master/badge.svg)](https://thebusybiscuit.github.io/builds/ybw0014/VillagerTrade/master)

## Requirements

Java Version: 16 or higher  
Server Software: Spigot or its forks  
Minecraft Version: 1.16.5 or higher  
Slimefun Version: DEV 1040 or higher

## Features

- Add vanilla/custom/Slimefun items as input/output to trades.
- An in-game interactive GUI to configure trades.

## Trade configurations (trades.yml)

This file hosts all the trade configurations. The trades are disabled by default, you need to set `enable-trades` to `true` in order to enable them.

The example trade is shown below:

```yaml
example_trade:
  traders:
    - FARMER
  output:
    type: SLIMEFUN
    id: ZINC_INGOT
    amount: 32
  input:
    1:
      type: SLIMEFUN
      id: COPPER_INGOT
      amount: 64
    2:
      type: NONE
  max-uses: 4
  exp-reward: true
  exp-villager: 0
  price-multiplier: 0.0
```

All the fields are explained in the table below:

| Field              | Description                                                                                                       |
|--------------------|-------------------------------------------------------------------------------------------------------------------|
| example_trade      | The unique key of a trade. It doesn't need to be all uppercase or all lowercase, but do not use weird characters. |
| `traders`          | The list of villager professions that the trade will be applied to.                                               |
| `output`           | The result of the trade. See item section below.                                                                  |
| `input.1`          | The ingredient 1 of the trade. See item section below.                                                            |
| `input.2`          | The ingredient 2 of the trade. See item section below.                                                            |
| `max-uses`         | The maximum uses of the trade.                                                                                    |
| `exp-reward`       | Whether the trade will reward experiences to player.                                                              |
| `exp-villager`     | The amount of experiences that the trade will reward to player.                                                   |
| `price-multiplier` | The price multiplier of the trade.                                                                                |

See the [Trading](https://minecraft.fandom.com/wiki/Trading) to learn what each field means.

### Item

The item section has 3 fields to define the item: `type`, `id` and `amount`.

The `amount` field is the amount of the item, it must be a valid ItemStack size.

#### Vanilla items

Vanilla items are the items that have no custom name, lores, attributes, enchantments, nbt tags. Usually, the items extracted from creative mode inventory are vanilla items. 

To define a vanilla item, set the `type` field to `VANILLA` and set the `id` field to the item's [Material](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html) name.

#### Custom items

Custom items are non-Slimefun items that have custom name, lores, attributes, enchantments, nbt tags.

To define a custom item, you have to save the custom item to VillagerTrade first. Use `/sfvt savecustom <ID>` to save a custom item, where `<ID>` is the unique ID of the custom item.
If you are using the in-game editor, you can just put a custom item in the item slot, VillagerTrade will save the item for you, and generate a self-increment ID for it.

After saving the custom item, set the `type` field to `CUSTOM` and set the `id` field to the ID of the custom item.

#### Slimefun items

To define a Slimefun item, set the `type` field to `SLIMEFUN` and set the `id` field to the ID of the Slimefun item.

Note that currently we don't supported Slimefun items that can have extra states, like the storage barrels.

## In-game editor

VillagerTrades provides an in-game interactive GUI to configure trades. You can see all the trades by `/sfvt list`, then click on a trade to open the editor for that trade.

You can also use `/sfvt edit <key>` to open the editor directly for a trade.

## Thanks

Thanks to anyone who helped me during the development of this addon.

Thanks to [minecraft-heads.com](https://minecraft-heads.com/) for the heads used in this addon.

[![](https://minecraft-heads.com/images/banners/minecraft-heads_fullbanner_468x60.png)](https://minecraft-heads.com/) 
