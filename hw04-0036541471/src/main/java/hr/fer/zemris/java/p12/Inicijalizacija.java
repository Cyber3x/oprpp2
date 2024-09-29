package hr.fer.zemris.java.p12;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@WebListener
public class Inicijalizacija implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String DBPropertiesPathString = sce.getServletContext().getRealPath("/WEB-INF/dbsettings.properties");
        Path DBPropertiesPath = Path.of(DBPropertiesPathString);

        if (!DBPropertiesPath.toFile().exists()) {
            throw new RuntimeException("DB properties file not found");
        }

        Properties DBProperties = new Properties();
        try (FileInputStream fis = new FileInputStream(DBPropertiesPath.toFile())) {
            DBProperties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!DBProperties.stringPropertyNames().containsAll(List.of("host", "port", "name", "user", "password"))) {
            throw new RuntimeException("Some properties are missing");
        }

        String jdbcUrl = String.format("jdbc:derby://%s:%s/%s",
                                       DBProperties.getProperty("host"),
                                       DBProperties.getProperty("port"),
                                       DBProperties.getProperty("name")
                                      );

        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass("org.apache.derby.client.ClientAutoloadedDriver");
            cpds.setJdbcUrl(jdbcUrl);
            cpds.setUser(DBProperties.getProperty("user"));
            cpds.setPassword(DBProperties.getProperty("password"));
        } catch (PropertyVetoException e1) {
            throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
        }

        boolean createdTablePolls = createTableIfNotExists(cpds, "Polls", """
                    CREATE TABLE Polls (
                                                                                 id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                                                                 title VARCHAR(150) NOT NULL,
                                                                                 message CLOB(2048) NOT NULL
                                                                             )
                """);

        boolean createdTablePollOptions = createTableIfNotExists(cpds, "PollOptions", """
                  CREATE TABLE PollOptions (
                                                                            id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                                                            optionTitle VARCHAR(100) NOT NULL,
                                                                            optionLink VARCHAR(150) NOT NULL,
                                                                            pollID BIGINT,
                                                                            likeCount BIGINT,
                                                                            dislikeCount BIGINT,
                                                                            FOREIGN KEY (pollID) REFERENCES Polls(id)
                                                                        )
                """);

        populatePollsTable(cpds);

        sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext()
                .getAttribute("hr.fer.zemris.dbpool");
        if (cpds != null) {
            try {
                DataSources.destroy(cpds);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean doesTableExist(ComboPooledDataSource pooledDataSource, String tableName) {
        try {
            ResultSet resultSet = pooledDataSource.getConnection()
                    .getMetaData()
                    .getTables(null, null, tableName.toUpperCase(), new String[]{ "TABLE" });
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean createTableIfNotExists(ComboPooledDataSource cpds, String tableName, String sqlQuery) {
        if (!doesTableExist(cpds, tableName)) {
            try (
                    Connection conn = cpds.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ) {
                ps.executeUpdate();
                System.out.println("Table " + tableName + " created");
                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Table " + tableName + " already exists");
            return false;
        }
    }

    private void populatePollsTable(ComboPooledDataSource cpds) {
        int numberOfPolls = getNumberOfRows(cpds, "Polls");

        if (numberOfPolls >= 1) {
            System.out.println("There are " + numberOfPolls + " polls, skipping generation.");
            return;
        };
        System.out.println("Creating polls...");
        int pollsCreated = 0;
        int optionsCreated = 0;

        Integer lastOptionKey = null;

        try (Connection conn = cpds.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Polls (TITLE, MESSAGE) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psInsertOptions = conn.prepareStatement("INSERT INTO POLLOPTIONS (OPTIONTITLE, OPTIONLINK, POLLID, LIKECOUNT, DISLIKECOUNT) VALUES (?, ?, ?, ?, ?)");

        ){
            List<String> lines = Files.readAllLines(Path.of("/home/cyber/faks/sem6/oprpp2/hw04-0036541471/SQLData/polls.txt"));
            System.out.println(lines);
            for (String line : lines) {
                if (line.isBlank()) {
                    lastOptionKey = null;
                    continue;
                }

                String[] lineParts = line.split(",");

                if (lastOptionKey == null) {
                    ps.setString(1, lineParts[0]);
                    ps.setString(2, lineParts[1]);

                    pollsCreated += ps.executeUpdate();
                    ResultSet generatedKeys = ps.getGeneratedKeys();
                    generatedKeys.next();
                    lastOptionKey = generatedKeys.getInt(1);
                } else {
                    psInsertOptions.setString(1, lineParts[0]);
                    psInsertOptions.setString(2, lineParts[1]);
                    psInsertOptions.setLong(3, lastOptionKey);
                    psInsertOptions.setInt(4, 0);
                    psInsertOptions.setInt(5, 0);
                    optionsCreated += psInsertOptions.executeUpdate();
                }
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Number of polls created: " + pollsCreated);
        System.out.println("Number of options created: " + optionsCreated);
    }

    private int getNumberOfRows(ComboPooledDataSource cpds, String tableName) {
        try (Connection conn = cpds.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM " + tableName.toUpperCase())
        ){
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
