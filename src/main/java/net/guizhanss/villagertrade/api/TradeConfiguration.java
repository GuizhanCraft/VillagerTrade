package net.guizhanss.villagertrade.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;

import net.guizhanss.villagertrade.api.configurations.TraderType;
import net.guizhanss.villagertrade.utils.Debug;

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

    public static TradeConfiguration deserialize(@Nonnull Map<String, Object> map) {
        Preconditions.checkArgument(map != null, "Map should not be null");

        Debug.log("The deserialize in TradeConfiguration is triggered");
        // traders
        List<String> traderTypesRaw = (List<String>) map.get("traders");
        List<TraderType> traderTypes = traderTypesRaw.stream()
            .map(TraderType::valueOf)
            .collect(Collectors.toList());

        // output and input

        return TradeConfiguration.builder()
            .traderTypes(traderTypes)
            .output((ItemStack) map.get("output"))
            .input1((ItemStack) map.get("input1"))
            .input2((ItemStack) map.get("input2"))
            .build();
    }

    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.of(
            "traders", traderTypes,
            "output", output,
            "input1", input1,
            "input2", input2
        );
    }
}
