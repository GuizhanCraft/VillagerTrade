package net.guizhanss.villagertrade.api.configurations;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.WanderingTrader;

import lombok.Getter;

/**
 * {@link TraderType} contains the information of the trader.
 *
 * @author ybw0014
 */
public final class TraderType {
    @Getter
    private final Class<?> traderClass;
    @Getter
    private final Villager.Profession profession;

    /**
     * The trader is a {@link WanderingTrader}
     */
    public TraderType() {
        traderClass = WanderingTrader.class;
        profession = null;
    }

    /**
     * The trader is a {@link Villager}.
     *
     * @param profession
     *     The {@link Profession} of the {@link Villager}
     */
    public TraderType(@Nonnull Profession profession) {
        Preconditions.checkArgument(profession != null, "The villager profession cannot be null");
        Preconditions.checkArgument(profession != Profession.NONE, "The villager profession cannot be NONE");
        Preconditions.checkArgument(profession != Profession.NITWIT, "The villager profession cannot be NITWIT");

        this.traderClass = Villager.class;
        this.profession = profession;
    }

    public static TraderType valueOf(@Nonnull String value) {
        Preconditions.checkArgument(value != null, "The string cannot be null");

        try {
            Profession profession = Profession.valueOf(value.toUpperCase());
            return new TraderType(profession);
        } catch (IllegalArgumentException ex) {
            return new TraderType();
        }
    }

    public static TraderType valueOf(@Nonnull Profession profession) {
        Preconditions.checkArgument(profession != null, "The string cannot be null");
        return new TraderType(profession);
    }
}
