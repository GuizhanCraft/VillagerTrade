package net.guizhanss.villagertrade.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import net.guizhanss.villagertrade.api.configurations.TraderType;
import net.guizhanss.villagertrade.utils.Debug;

import lombok.Data;

/**
 * The mapping of a section in trades.
 *
 * @author ybw0014
 */
@Data
public class TradeConfiguration implements ConfigurationSerializable {

    private final List<TraderType> traderTypes;
    private final ItemStack output;
    private final ItemStack input1;
    private final ItemStack input2;

    public TradeConfiguration(@Nonnull Map<String, Object> map) {
        Preconditions.checkArgument(map != null, "Map should not be null");

        Debug.log("The deserialize in TradeConfiguration is triggered");
        // traders
        List<String> traderTypesRaw = (List<String>) map.get("traders");
        this.traderTypes = traderTypesRaw.stream()
            .map(TraderType::valueOf)
            .collect(Collectors.toList());

        // output and input
        this.output = (ItemStack) map.get("output");
        this.input1 = (ItemStack) map.get("input1");
        this.input2 = (ItemStack) map.get("input2");
    }

    @ParametersAreNonnullByDefault
    public TradeConfiguration(List<TraderType> traderTypes, ItemStack output, ItemStack input1, ItemStack input2) {
        this.traderTypes = traderTypes;
        this.output = output;
        this.input1 = input1;
        this.input2 = input2;
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


    public static final class Builder {
        private List<TraderType> traderTypes;
        private ItemStack output;
        private ItemStack input1;
        private ItemStack input2;

        public Builder withTraderTypes(List<TraderType> traderTypes) {
            this.traderTypes = traderTypes;
            return this;
        }

        public Builder withOutput(ItemStack output) {
            this.output = output;
            return this;
        }

        public Builder withInput1(ItemStack input1) {
            this.input1 = input1;
            return this;
        }

        public Builder withInput2(ItemStack input2) {
            this.input2 = input2;
            return this;
        }

        public TradeConfiguration build() {
            return new TradeConfiguration(traderTypes, output, input1, input2);
        }
    }
}
