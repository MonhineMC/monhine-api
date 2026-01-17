package me.lightgr3y.orm.table;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * @author Light_Gr3y
 */
@UtilityClass
public class TableManager {
    @SneakyThrows
    public void create(@NotNull JdbcPooledConnectionSource connectionSource, @NotNull Class<?>... modelClasses) {
        for(Class<?> modelClass: modelClasses) {
            TableUtils.createTableIfNotExists(connectionSource, modelClass);
        }
    }
}
