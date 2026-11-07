package me.pjoh3.orm.connection;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import lombok.Getter;
import lombok.SneakyThrows;
import me.pjoh3.orm.dao.DaoManager;
import me.pjoh3.orm.table.TableManager;
import org.jetbrains.annotations.NotNull;

/**
 * @author pjoh3
 */
@SuppressWarnings("unused")
public class ConnectionInitializer {
    @Getter
    private static JdbcPooledConnectionSource connectionSource;

    @SneakyThrows
    public void connectSQL(String host, String database, String user, String pass, boolean useSSL, @NotNull Class<?>... modelClasses) {
        String sslUseString = "";

        if (useSSL) {
            sslUseString = "sslMode=trust&";
        }

        String sqlString = "jdbc:mariadb://" + host + "/" + database + "?" + sslUseString + " autoReconnect=true";

        connectionSource = new JdbcPooledConnectionSource(sqlString);

        setUpTheConnection(connectionSource, user, pass);
        createModelDaoAndTable(connectionSource, modelClasses);
    }

    @SneakyThrows
    public void connectNoSQLDatabase(@NotNull String filePath, @NotNull Class<?>... modelClasses) {
        String sqlString = "jdbc:sqlite:" + filePath;

        connectionSource = new JdbcPooledConnectionSource(sqlString);
        createModelDaoAndTable(connectionSource, modelClasses);
    }

    private void setUpTheConnection(@NotNull JdbcPooledConnectionSource connectionSource, String user, String pass) {
        connectionSource.setUsername(user);
        connectionSource.setPassword(pass);
        connectionSource.setMaxConnectionsFree(5);
    }

    private void createModelDaoAndTable(@NotNull JdbcPooledConnectionSource connectionSource, @NotNull Class<?>... modelClasses) {
        TableManager.create(connectionSource, modelClasses);
        DaoManager.create(connectionSource, modelClasses);
    }

    @SneakyThrows
    public void closeConnection(boolean shouldDisableServices) {
        if(connectionSource != null) connectionSource.close();
    }
}