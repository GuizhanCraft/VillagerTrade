# VillagerTrade 村民交易

[English](README.md) | [中文](README.zh_CN.md)

VillagerTrade 是一个 Slimefun 附属插件，允许 Slimefun 物品添加到村民交易（也包括流浪商人）中。

## 下载

官方构建站（需要翻墙）：
[![Build status](https://thebusybiscuit.github.io/builds/ybw0014/VillagerTrade/master/badge.svg)](https://thebusybiscuit.github.io/builds/ybw0014/VillagerTrade/master)

鬼斩构建站：
[![Build status](https://builds.guizhanss.com/ybw0014/VillagerTrade/master/badge.svg)](https://builds.guizhanss.com/ybw0014/VillagerTrade/master)

## Requirements

Java 版本：16 及以上  
服务端：Spigot 或衍生服务端  
Minecraft 版本：1.16.5 及以上  
Slimefun 版本：DEV 1040 及以上  

## 功能

- 添加原版/自定义/粘液物品作为交易输入/输出。
- 游戏内也可配置交易。

## 交易配置 (trades.yml)

该文件包含了所有的交易配置。该配置默认为关闭，你需要在`config.yml`中设置`enable-trades`为`true`来启用。（防止某些服主只管装，不进行配置）

示例交易如下：

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

所有的配置项已在下方说明：

| 配置项              | 说明。                                                |
|--------------------|---------------------------------------------------- -|
| example_trade      | 每个交易的唯一识别名称。大小写无所谓，但是不要用奇怪的符号。   |
| `traders`          | 允许交易的村民职业列表。流浪商人则填写`WANDERING_TRADER`。  |
| `output`           | 交易的输出物品。见下方物品部分。                          |
| `input.1`          | 交易的输入物品1。见下方物品部分。                         |
| `input.2`          | 交易的输入物品2。见下方物品部分。                         |
| `max-uses`         | 交易的最大次数。（村民补货后重置）                         |
| `exp-reward`       | 交易是否为玩家提供经验。                                 |
| `exp-villager`     | 交易为村民提供的经验。                                   |
| `price-multiplier` | 价格乘数。                                             |

前往 [交易](https://minecraft.fandom.com/zh/wiki/%E4%BA%A4%E6%98%93) 页面了解这些配置项。

### 物品

物品部分拥有3个配置项：`type`, `id` 与 `amount`.

`amount`指定了物品的数量。必须是有效的数量。

#### 原版物品

原版物品指的是没有自定义名称、说明、属性、附魔与NBT标签的物品。通常来说，从创造模式物品栏拿出来的物品都是原版物品。

要定义一个原版物品，设置 `type` 为 `VANILLA`，`id` 为物品的 [Material](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html) 名称。

#### 自定义物品

自定义物品是指非 Slimefun 物品，但拥有自定义名称、说明、属性、附魔或NBT标签的物品。

要定义一个自定义物品，你需要先将物品保存至 VillagerTrade。使用 `/sfvt savecustom <ID>` 来保存一个自定义物品，`<ID>` 为自定义物品的唯一标识符。（不要使用奇怪的符号）
如果你是用的是游戏内的交易编辑器，你可以直接把自定义物品放到对应的栏位中，VillagerTrade 会自动保存该物品，并生成一个自增 ID。

在保存物品后，只需要设置 `type` 为 `CUSTOM`，`id` 为自定义物品的ID。

#### Slimefun 物品

要定义一个 Slimefun 物品，设置 `type` 为 `SLIMEFUN`，`id` 为 Slimefun 物品的 ID。

需要注意的是，我们目前不支持带有额外状态的 Slimefun 物品，例如无尽存储、量子存储等。Slimefun 物品将会是其默认状态下的物品。

## 游戏内编辑器

VillagerTrades 提供了一个游戏内编辑器来编辑交易。你可以通过 `/sfvt list` 查看所有的交易列表，并点击一个交易来进行编辑。

你也可以直接使用 `/sfvt edit <key>` 来编辑交易。

## 感谢

感谢任何在附属开发期间帮助过我的人。

感谢 [minecraft-heads.com](https://minecraft-heads.com/) 提供的头颅使用权。

[![](https://minecraft-heads.com/images/banners/minecraft-heads_fullbanner_468x60.png)](https://minecraft-heads.com/) 
