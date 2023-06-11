package net.guizhanss.villagertrade.utils.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Keys {
    // config keys in trades.yml
    public static final String TRADES_KEY = "key";
    public static final String TRADES_TRADER_TYPES = "traders";
    public static final String TRADES_OUTPUT = "output";
    public static final String TRADES_INPUT_1 = "input.1";
    public static final String TRADES_INPUT_2 = "input.2";
    public static final String TRADES_MAX_USES = "max-uses";
    public static final String TRADES_EXP_REWARD = "exp-reward";
    public static final String TRADES_EXP_VILLAGER = "exp-villager";
    public static final String TRADES_PRICE_MULTIPLIER = "price-multiplier";

    // other keys in language
    public static final String LANG_SAVE = "save";
    public static final String LANG_SAVE_INVALID = "save-invalid";
    public static final String LANG_REMOVE = "remove";
    public static final String LANG_ITEM_NAME = "name";
    public static final String LANG_ITEM_LORE = "lore";

    // replaceable variables in language
    public static final String VAR_USAGE = "%usage%";
    public static final String VAR_TRADE_KEY = "%tradeKey%";
}
