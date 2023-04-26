package net.guizhanss.villagertrade.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import net.guizhanss.villagertrade.api.configurations.TraderType;

import lombok.Builder;
import lombok.Data;

/**
 * The mapping of a section in trades.
 *
 * @author ybw0014
 */
@Data
@Builder
public class TradeConfiguration implements ConfigurationSerializable {

    private final List<TraderType> traderTypes;
    private final ItemStack output;
    private final ItemStack input1;
    private final ItemStack input2;

    public static TradeConfiguration deserialize(Map<String, Object> map) {
        List<String> traderTypesRaw = (List<String>) map.get("traders");
        List<TraderType> traderTypes = traderTypesRaw.stream()
            .map(TraderType::valueOf)
            .collect(Collectors.toList());
        return TradeConfiguration.builder()
            .traderTypes(traderTypes)
            .build();
    }

    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.of(
            "traders", traderTypes
        );
    }
}
