package net.guizhanss.villagertrade.core.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import net.guizhanss.villagertrade.VillagerTrade;
import net.guizhanss.villagertrade.utils.ItemUtils;

/**
 * This class holds all the methods about custom items.
 * <p>
 * Custom items are items that have extra meta but not Slimefun items.
 *
 * @author ybw0014
 */
public final class CustomItemService {

    private static final String FOLDER_ITEMS = "items";

    private final VillagerTrade plugin;
    private final Map<String, ItemStack> customItems = new HashMap<>();

    private File itemsFolder;

    public CustomItemService(@Nonnull VillagerTrade plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        // cleanup
        customItems.clear();

        // create items folder if not exists
        itemsFolder = new File(plugin.getDataFolder(), FOLDER_ITEMS);
        if (!itemsFolder.exists()) {
            try {
                Files.createDirectory(itemsFolder.toPath());
            } catch (IOException ex) {
                VillagerTrade.log(Level.SEVERE, "Failed to create items folder, " +
                    "custom items will be recognized as NONE.");
                return;
            }
        }

        // load item ids
        final List<String> customItemIds;
        try (Stream<Path> fileStream = Files.list(itemsFolder.toPath())) {
            customItemIds = fileStream
                .map(Path::toFile)
                .filter(File::isFile) // check if it is a file
                .map(File::getName)
                .filter(name -> name.endsWith(".yml")) // get only yml files
                .map(filename -> filename.substring(0, filename.length() - 4)) // remove .yml
                .collect(Collectors.toList());
        } catch (IOException ex) {
            VillagerTrade.log(Level.SEVERE, ex, "Failed to load custom items.");
            return;
        }

        // load items
        for (String id : customItemIds) {
            ItemStack item = loadItem(id);
            customItems.put(id, item);
        }
    }

    /**
     * Get the custom item with the specified id.
     *
     * @param id
     *     The id of the custom item.
     *
     * @return The custom item, or null if the item does not exist.
     */
    @Nullable
    public ItemStack getItem(@Nonnull String id) {
        Preconditions.checkArgument(id != null, "custom item id cannot be null");

        return customItems.get(id);
    }

    /**
     * Get the custom item id of the specified {@link ItemStack}.
     *
     * @param item
     *     The {@link ItemStack}.
     *
     * @return The custom item id, or null if the item is not a registered custom item.
     */
    @Nullable
    public String getId(@Nullable ItemStack item) {
        // no item or air
        if (item == null || item.getType().isAir()) {
            return null;
        }

        // it is a vanilla item
        if (!item.hasItemMeta()) {
            return null;
        }

        for (Map.Entry<String, ItemStack> entry : customItems.entrySet()) {
            // add this to prevent some weird issues
            if (entry.getValue() == null) {
                continue;
            }

            if (ItemUtils.canStack(item, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Add a custom item, save to file. The id will be "Material_Number", the number starts from 1.
     *
     * @param item
     *     The custom item.
     *
     * @return The generated id of the custom item.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    public String addItem(ItemStack item) {
        Preconditions.checkArgument(item != null, "custom item cannot be null");

        int n = 1;
        while (true) {
            String id = item.getType().name() + "_" + n;
            if (!customItems.containsKey(id)) {
                addItem(id, item);
                return id;
            }
            n++;
        }
    }

    /**
     * Add a custom item, save to file. If the item already exists, this will not be executed.
     *
     * @param id
     *     The id of the custom item.
     * @param item
     *     The custom item.
     */
    @ParametersAreNonnullByDefault
    public void addItem(String id, ItemStack item) {
        Preconditions.checkArgument(id != null, "custom item id cannot be null");
        Preconditions.checkArgument(item != null, "custom item cannot be null");

        final File itemFile = new File(itemsFolder, id + ".yml");
        if (itemFile.exists()) {
            VillagerTrade.log(Level.SEVERE, "Custom item file already exists: " + id);
            return;
        }

        final YamlConfiguration config = new YamlConfiguration();
        config.set("item", item);
        try {
            config.save(itemFile);
        } catch (IOException ex) {
            VillagerTrade.log(Level.SEVERE, ex, "Failed to save custom item file: " + id);
        }

        customItems.put(id, item);
    }

    @Nullable
    private ItemStack loadItem(@Nonnull String id) {
        Preconditions.checkArgument(id != null, "custom item id cannot be null");

        final File itemFile = new File(itemsFolder, id + ".yml");
        // WTF, why the file is disappeared???
        if (!itemFile.exists()) {
            VillagerTrade.log(Level.SEVERE, "Custom item file does not exist: " + id);
            return null;
        }

        return YamlConfiguration.loadConfiguration(itemFile).getItemStack("item");
    }
}
