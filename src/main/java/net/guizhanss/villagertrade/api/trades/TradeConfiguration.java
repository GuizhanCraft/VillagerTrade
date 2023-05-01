package net.guizhanss.villagertrade.api.trades;

import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import net.guizhanss.villagertrade.api.ConfigurationSerializable;

import org.bukkit.configuration.ConfigurationSection;

import net.guizhanss.villagertrade.VillagerTrade;

import lombok.Builder;
import lombok.Data;

/**
 * The mapping of a whole section.
 *
 * @author ybw0014
 */
@Data
@Builder
public final class TradeConfiguration implements ConfigurationSerializable {

    private static final String KEY_TRADER_TYPES = "traders";
    private static final String KEY_OUTPUT = "output";
    private static final String KEY_INPUT_1 = "input.1";
    private static final String KEY_INPUT_2 = "input.2";
    private static final String KEY_MAX_USES = "max-uses";
    private static final String KEY_EXP_REWARD = "exp-reward";
    private static final String KEY_EXP_VILLAGER = "exp-villager";

    private final TraderTypes traderTypes;
    private final TradeItem output;
    private final TradeItem input1;
    private final TradeItem input2;
    private final int maxUses;
    private final int expReward;
    private final int expVillager;

    /**
     * Loads the config options from the {@link ConfigurationSection} into a {@link TradeConfiguration}.
     *
     * @param section
     *     The {@link ConfigurationSection} to load from.
     *
     * @return The {@link TradeConfiguration} loaded, or null if the section is invalid.
     */
    @Nullable
    public static TradeConfiguration loadFromConfig(@Nonnull ConfigurationSection section) {
        Preconditions.checkArgument(section != null, "ConfigurationSection should not be null");

        try {
            return TradeConfiguration.builder()
                .traderTypes(TraderTypes.loadFromConfig(section.getStringList(KEY_TRADER_TYPES)))
                .output(TradeItem.loadFromConfig(section.getConfigurationSection(KEY_OUTPUT)))
                .input1(TradeItem.loadFromConfig(section.getConfigurationSection(KEY_INPUT_1)))
                .input2(TradeItem.loadFromConfig(section.getConfigurationSection(KEY_INPUT_2)))
                .maxUses(section.getInt(KEY_MAX_USES))
                .expReward(section.getInt(KEY_EXP_REWARD))
                .expVillager(section.getInt(KEY_EXP_VILLAGER))
                .build();
        } catch (IllegalArgumentException | NullPointerException ex) {
            VillagerTrade.log(Level.SEVERE, ex, "An error has occurred while loading trade configuration");
            return null;
        }
    }

    @Override
    public void saveToConfig(@Nonnull ConfigurationSection section) {
        Preconditions.checkArgument(section != null, "ConfigurationSection should not be null");

        traderTypes.saveToConfig(section.getConfigurationSection(KEY_TRADER_TYPES));
        output.saveToConfig(section.getConfigurationSection(KEY_OUTPUT));
        input1.saveToConfig(section.getConfigurationSection(KEY_INPUT_1));
        input2.saveToConfig(section.getConfigurationSection(KEY_INPUT_2));
        section.set(KEY_MAX_USES, maxUses);
        section.set(KEY_EXP_REWARD, expReward);
        section.set(KEY_EXP_VILLAGER, expVillager);
    }

    public void register() {
        VillagerTrade.getRegistry().getConfigurations().add(this);
    }

    @Nonnull
    public String toString() {
        return "TradeConfiguration(traderTypes=" + this.getTraderTypes()
            + ", output=" + this.getOutput()
            + ", input1=" + this.getInput1()
            + ", input2=" + this.getInput2()
            + ", maxUses=" + this.getMaxUses()
            + ", expReward=" + this.getExpReward()
            + ", expVillager=" + this.getExpVillager()
            + ")";
    }
}
