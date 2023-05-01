package net.guizhanss.villagertrade.api.trades;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import lombok.Builder;

import net.guizhanss.villagertrade.api.ConfigurationSerializable;

import org.bukkit.configuration.ConfigurationSection;

@Builder
public final class TraderTypes implements ConfigurationSerializable {
    private final List<TraderType> traderTypes;

    @Nonnull
    public static TraderTypes loadFromConfig(@Nonnull List<String> traderList) {
        Preconditions.checkArgument(traderList != null, "The trader list cannot be null");

        List<TraderType> types = new ArrayList<>();
        for (String trader : traderList) {
            TraderType type = TraderType.valueOf(trader);
            types.add(type);
        }

        return TraderTypes.builder().traderTypes(types).build();
    }

    @Override
    public void saveToConfig(@Nonnull ConfigurationSection section) {
        Preconditions.checkArgument(section != null, "The configuration section cannot be null");

        List<String> traderList = new ArrayList<>();
        for (TraderType type : traderTypes) {
            traderList.add(type.toString());
        }

        section.set("traders", traderList);
    }

    @Nonnull
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TraderTypes{");
        for (TraderType type : traderTypes) {
            builder.append(type.toString()).append(",");
        }
        builder.append("}");
        return builder.toString();
    }
}
