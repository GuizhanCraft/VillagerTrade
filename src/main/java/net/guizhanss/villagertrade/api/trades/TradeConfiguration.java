package net.guizhanss.villagertrade.api.trades;

import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import org.bukkit.configuration.ConfigurationSection;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.utils.constants.Keys;

import lombok.Builder;
import lombok.Data;

/**
 * The mapping of a whole section.
 *
 * @author ybw0014
 */
@Data
@Builder
public final class TradeConfiguration {

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
                .traderTypes(TraderTypes.loadFromConfig(section.getStringList(Keys.TRADES_TRADER_TYPES)))
                .output(TradeItem.loadFromConfig(section.getConfigurationSection(Keys.TRADES_OUTPUT)))
                .input1(TradeItem.loadFromConfig(section.getConfigurationSection(Keys.TRADES_INPUT_1)))
                .input2(TradeItem.loadFromConfig(section.getConfigurationSection(Keys.TRADES_INPUT_2)))
                .maxUses(section.getInt(Keys.TRADES_MAX_USES))
                .expReward(section.getInt(Keys.TRADES_EXP_REWARD))
                .expVillager(section.getInt(Keys.TRADES_EXP_VILLAGER))
                .build();
        } catch (IllegalArgumentException | NullPointerException ex) {
            VillagerTrade.log(Level.SEVERE, ex, "An error has occurred while loading trade configuration");
            return null;
        }
    }

    public void saveToConfig(@Nonnull ConfigurationSection section) {
        Preconditions.checkArgument(section != null, "ConfigurationSection should not be null");

        section.set(Keys.TRADES_TRADER_TYPES, traderTypes.toStringList());
        output.saveToConfig(section.getConfigurationSection(Keys.TRADES_OUTPUT));
        input1.saveToConfig(section.getConfigurationSection(Keys.TRADES_INPUT_1));
        input2.saveToConfig(section.getConfigurationSection(Keys.TRADES_INPUT_2));
        section.set(Keys.TRADES_MAX_USES, maxUses);
        section.set(Keys.TRADES_EXP_REWARD, expReward);
        section.set(Keys.TRADES_EXP_VILLAGER, expVillager);
    }

    public void register() {
        // check if the trade is valid
        if (output.getType() == TradeItem.TraderItemType.NONE) {
            VillagerTrade.log(Level.SEVERE, "Trade output is not set or invalid, skipping...");
            return;
        }
        if (input1.getType() == TradeItem.TraderItemType.NONE) {
            VillagerTrade.log(Level.SEVERE, "Trade input 1 is not set or invalid, skipping...");
            return;
        }

        // registration
        VillagerTrade.getRegistry().getTradeConfigurations().add(this);
        if (traderTypes.hasWanderingTrader()) {
            VillagerTrade.getRegistry().getWanderingTraderConfigurations().add(this);
        }
        if (traderTypes.hasVillager()) {
            VillagerTrade.getRegistry().getVillagerConfigurations().add(this);
        }
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
