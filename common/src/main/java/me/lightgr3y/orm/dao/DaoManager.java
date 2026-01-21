package me.lightgr3y.orm.dao;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import me.lightgr3y.orm.connection.ConnectionInitializer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Light_Gr3y
 */
@UtilityClass
@SuppressWarnings("unused")
public class DaoManager {
    @SneakyThrows
    public void create(@NotNull JdbcPooledConnectionSource connectionSource, @NotNull Class<?>... modelClasses) {
        for(Class<?> modelClass: modelClasses) {
            com.j256.ormlite.dao.DaoManager.createDao(connectionSource, modelClass);
        }
    }

    public <D extends Dao<T, ?>, T> D getDao(Class<T> daoClass) {
        return com.j256.ormlite.dao.DaoManager.lookupDao(ConnectionInitializer.getConnectionSource(), daoClass);
    }
}