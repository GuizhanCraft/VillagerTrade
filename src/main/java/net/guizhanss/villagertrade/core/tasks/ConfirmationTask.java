package net.guizhanss.villagertrade.core.tasks;

import java.util.UUID;

import net.guizhanss.villagertrade.VillagerTrade;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A confirmation task is executed when player executes confirm command.
 *
 * @author ybw0014
 */
@RequiredArgsConstructor
public abstract class ConfirmationTask {
    @Getter
    private final long expireAfter;

    /**
     * Create a confirmation task.
     *
     * @param uuid
     *     The player's uuid.
     * @param expireTime
     *     The time in milliseconds for how long the confirmation will expire.
     * @param runnable
     *     The runnable to be executed when the confirmation is confirmed.
     */
    public static void create(UUID uuid, long expireTime, Runnable runnable) {
        long expireAfter = System.currentTimeMillis() + expireTime;
        VillagerTrade.getRegistry().getConfirmationTasks().put(uuid, new ConfirmationTask(expireAfter) {
            @Override
            void run() {
                runnable.run();
                VillagerTrade.getRegistry().getConfirmationTasks().remove(uuid);
            }
        });
    }

    public final boolean execute() {
        if (System.currentTimeMillis() < expireAfter) {
            run();
            return true;
        } else {
            return false;
        }
    }

    abstract void run();
}
