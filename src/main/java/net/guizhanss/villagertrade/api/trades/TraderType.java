package net.guizhanss.villagertrade.api.trades;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.WanderingTrader;

import lombok.Getter;

/**
 * {@link TraderType} contains the information of an acceptable trader.
 *
 * @author ybw0014
 */
public final class TraderType {
    @Getter
    private final EntityType type;
    @Getter
    private final Villager.Profession profession;

    /**
     * The trader is a {@link WanderingTrader}
     */
    public TraderType() {
        type = EntityType.WANDERING_TRADER;
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

        this.type = EntityType.VILLAGER;
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

    @Override
    @Nonnull
    public String toString() {
        if (type == EntityType.WANDERING_TRADER) {
            return "WANDERING_TRADER";
        } else {
            return profession.toString();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(this.getClass())) {
            return false;
        }
        TraderType other = (TraderType) obj;
        return this.type == other.type && this.profession == other.profession;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, profession);
    }
}
