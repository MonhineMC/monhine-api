package me.pjoh3.monhinemc.command.litecommands.locale;

import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import lombok.Builder;
import lombok.Data;

/**
 * @author pjoh3
 */
@Data
@Builder
public class LiteCommandsLocale {
    private LiteBukkitMessages invalidUsage;
    private LiteBukkitMessages playerOnly;
    private LiteBukkitMessages playerNotFound;
    private LiteBukkitMessages missingPermissions;
    private LiteBukkitMessages worldNotExist;
    private LiteBukkitMessages worldPlayerOnly;
    private LiteBukkitMessages uuidInvalidFormat;
    private LiteBukkitMessages locationInvalidFormat;
    private LiteBukkitMessages invalidNumber;
    private LiteBukkitMessages instantInvalidFormat;
}
