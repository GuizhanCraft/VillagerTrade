package net.guizhanss.villagertrade.api.trades;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WanderingTrader;

import net.guizhanss.villagertrade.VillagerTrade;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Represents the list of valid types of traders.<br>
 * Available traders: {@link Villager} and {@link WanderingTrader}.
 */

@Getter
public final class TraderTypes {

    private static final String WANDERING_TRADER = "WANDERING_TRADER";

    @Accessors(fluent = true)
    private final boolean hasWanderingTrader;
    private final List<Villager.Profession> villagerProfessions;

    public TraderTypes(boolean hasWanderingTrader, @Nullable List<Villager.Profession> villagerProfessions) {
        this.hasWanderingTrader = hasWanderingTrader;
        this.villagerProfessions = Objects.requireNonNullElseGet(villagerProfessions, ArrayList::new);
    }

    /**
     * Load the trader types from the config.
     *
     * @param traderList
     *     The {@link List} of {@link String}s that represents the trader types.
     *
     * @return The {@link TraderTypes}.
     */
    @Nonnull
    public static TraderTypes loadFromConfig(@Nonnull List<String> traderList) {
        Preconditions.checkArgument(traderList != null, "The trader list cannot be null");

        boolean hasWanderingTrader = false;
        List<Villager.Profession> professions = new ArrayList<>();
        for (String trader : traderList) {
            if (trader.equalsIgnoreCase(WANDERING_TRADER)) {
                hasWanderingTrader = true;
            } else {
                try {
                    Villager.Profession profession = Villager.Profession.valueOf(trader.toUpperCase());
                    professions.add(profession);
                } catch (IllegalArgumentException ex) {
                    VillagerTrade.log(Level.WARNING, "Invalid trader type: " + trader
                        + ", must be " + WANDERING_TRADER + " or any villager profession");
                }
            }
        }

        return new TraderTypes(hasWanderingTrader, professions);
    }

    /**
     * Cast this {@link TraderTypes} to a {@link List} of {@link String}s for saving to config.
     *
     * @return The {@link List} of {@link String}s that represents the trader types.
     */
    public List<String> toStringList() {
        List<String> traderTypes = new ArrayList<>();
        if (hasWanderingTrader) {
            traderTypes.add(WANDERING_TRADER);
        }
        for (Villager.Profession profession : this.villagerProfessions) {
            traderTypes.add(profession.toString());
        }
        return traderTypes;
    }

    @Nonnull
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TraderTypes(");
        builder.append("hasWanderingTrader=").append(hasWanderingTrader).append(", ");
        builder.append("villagerProfessions={");
        for (Villager.Profession profession : this.villagerProfessions) {
            builder.append(profession.toString()).append(", ");
        }
        builder.append("})");
        return builder.toString();
    }

    public boolean hasVillager() {
        return !villagerProfessions.isEmpty();
    }

    public boolean isValid(@Nonnull Entity entity) {
        Preconditions.checkArgument(entity != null, "The entity cannot be null");

        if (hasWanderingTrader && entity instanceof WanderingTrader) {
            return true;
        } else if (entity instanceof Villager villager) {
            return villagerProfessions.contains(villager.getProfession());
        }

        return false;
    }
}
