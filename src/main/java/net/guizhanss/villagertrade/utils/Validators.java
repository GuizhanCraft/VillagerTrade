package net.guizhanss.villagertrade.utils;

import javax.annotation.Nonnull;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Validators {
    public static boolean isInteger(@Nonnull String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public static boolean isDouble(@Nonnull String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}
