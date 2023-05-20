package net.guizhanss.villagertrade.api.trades;

import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.MerchantRecipe;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.core.Registry;
import net.guizhanss.villagertrade.utils.constants.Keys;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * A {@link TradeConfiguration} represents the configuration of a {@link MerchantRecipe}.<br>
 * It can be read from config file or created using the builder.
 *
 * @author ybw0014
 *
 * @see TraderTypes
 * @see TradeItem
 */
@Getter
@EqualsAndHashCode
@Builder
@SuppressWarnings("ConstantConditions")
public final class TradeConfiguration {

    private final String key;
    private final TraderTypes traderTypes;
    private final TradeItem output;
    private final TradeItem input1;
    private final TradeItem input2;
    private final int maxUses;
    private final boolean expReward;
    private final int expVillager;
    private final float priceMultiplier;

    @Setter(AccessLevel.NONE)
    private SlimefunAddon addon;
    @Setter(AccessLevel.NONE)
    private RegistrationState state;

    /**
     * Loads the config options from the {@link ConfigurationSection} into a {@link TradeConfiguration}.
     *
     * @param section
     *     The {@link ConfigurationSection} to load from.
     *
     * @return The {@link TradeConfiguration} loaded, or null if the section is invalid.
     */
    @ParametersAreNonnullByDefault
    @Nullable
    public static TradeConfiguration loadFromConfig(String key, ConfigurationSection section) {
        Preconditions.checkArgument(section != null, "ConfigurationSection should not be null");

        try {
            return TradeConfiguration.builder()
                .key(key)
                .traderTypes(TraderTypes.loadFromConfig(section.getStringList(Keys.TRADES_TRADER_TYPES)))
                .output(TradeItem.loadFromConfig(section.getConfigurationSection(Keys.TRADES_OUTPUT)))
                .input1(TradeItem.loadFromConfig(section.getConfigurationSection(Keys.TRADES_INPUT_1)))
                .input2(TradeItem.loadFromConfig(section.getConfigurationSection(Keys.TRADES_INPUT_2)))
                .maxUses(section.getInt(Keys.TRADES_MAX_USES))
                .expReward(section.getBoolean(Keys.TRADES_EXP_REWARD))
                .expVillager(section.getInt(Keys.TRADES_EXP_VILLAGER))
                .priceMultiplier((float) section.getDouble(Keys.TRADES_PRICE_MULTIPLIER))
                .state(RegistrationState.UNREGISTERED)
                .build();
        } catch (IllegalArgumentException | NullPointerException ex) {
            VillagerTrade.log(Level.SEVERE, ex, "An error has occurred while loading trade configuration");
            return null;
        }
    }

    /**
     * Save the {@link TradeConfiguration} to the {@link ConfigurationSection}.
     *
     * @param section
     *     The {@link ConfigurationSection} to save to.
     */
    public void saveToConfig(@Nonnull ConfigurationSection section) {
        Preconditions.checkArgument(section != null, "ConfigurationSection should not be null");

        section.set(Keys.TRADES_TRADER_TYPES, traderTypes.toStringList());
        output.saveToConfig(section.getConfigurationSection(Keys.TRADES_OUTPUT));
        input1.saveToConfig(section.getConfigurationSection(Keys.TRADES_INPUT_1));
        input2.saveToConfig(section.getConfigurationSection(Keys.TRADES_INPUT_2));
        section.set(Keys.TRADES_MAX_USES, maxUses);
        section.set(Keys.TRADES_EXP_REWARD, expReward);
        section.set(Keys.TRADES_EXP_VILLAGER, expVillager);
        section.set(Keys.TRADES_PRICE_MULTIPLIER, priceMultiplier);
    }

    /**
     * Register this {@link TradeConfiguration}.
     *
     * @param addon
     *     The {@link SlimefunAddon} that register this {@link TradeConfiguration}.
     */
    public void register(@Nonnull SlimefunAddon addon) {
        if (state == RegistrationState.REGISTERED) {
            VillagerTrade.log(Level.SEVERE, "This TradeConfiguration is already registered!");
            return;
        }
        state = RegistrationState.INVALID;

        // check if the trade is valid
        if (this.addon != null) {
            VillagerTrade.log(Level.SEVERE, "This TradeConfiguration is already registered! "
                + "Make sure you do not set 'addon' before registering it.");
            return;
        }
        if (addon == null || addon.getJavaPlugin() == null) {
            VillagerTrade.log(Level.SEVERE, "SlimefunAddon is invalid! Skipping...");
            return;
        }
        if (output.getType() == TradeItem.TradeItemType.NONE) {
            VillagerTrade.log(Level.SEVERE, "Trade output is not set or invalid, skipping...");
            return;
        }
        if (input1.getType() == TradeItem.TradeItemType.NONE) {
            VillagerTrade.log(Level.SEVERE, "Trade input 1 is not set or invalid, skipping...");
            return;
        }

        // registration
        final Registry registry = VillagerTrade.getRegistry();
        state = RegistrationState.REGISTERED;
        this.addon = addon;
        registry.getTradeConfigurations().put(key, this);
        if (traderTypes.hasWanderingTrader()) {
            registry.getWanderingTraderConfigurations().add(this);
        }
        if (traderTypes.hasVillager()) {
            registry.getVillagerConfigurations().add(this);
        }

        // slimefun input items
        if (input1.getType() == TradeItem.TradeItemType.SLIMEFUN) {
            registry.getSlimefunTradeInputs().add(input1);
        }
        if (input2.getType() == TradeItem.TradeItemType.SLIMEFUN) {
            registry.getSlimefunTradeInputs().add(input2);
        }
    }

    /**
     * Get the {@link MerchantRecipe} of this {@link TradeConfiguration}.
     *
     * @return The {@link MerchantRecipe}, null if the recipe is not registered.
     */
    @Nullable
    public MerchantRecipe getMerchantRecipe() {
        if (state != RegistrationState.REGISTERED) {
            return null;
        }

        MerchantRecipe recipe = new MerchantRecipe(
            output.getItem(), 0, maxUses, expReward,
            expVillager, priceMultiplier
        );
        recipe.addIngredient(input1.getItem());
        if (input2.getType() != TradeItem.TradeItemType.NONE) {
            recipe.addIngredient(input2.getItem());
        }
        return recipe;
    }

    @Nonnull
    @Override
    public String toString() {
        return "TradeConfiguration(traderTypes = " + this.getTraderTypes().toString()
            + ", output = " + this.getOutput().toString()
            + ", input1 = " + this.getInput1().toString()
            + ", input2 = " + this.getInput2().toString()
            + ", maxUses = " + this.getMaxUses()
            + ", expReward = " + this.isExpReward()
            + ", expVillager = " + this.getExpVillager()
            + ", priceMultiplier = " + this.getPriceMultiplier()
            + ")";
    }

    public enum RegistrationState {
        UNREGISTERED,
        INVALID,
        REGISTERED
    }
}
