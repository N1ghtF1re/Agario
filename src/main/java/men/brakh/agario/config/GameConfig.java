package men.brakh.agario.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class GameConfig {
    private static GameConfig instance;

    private int spawnSize;
    private String[] availableColors;

    public int getSpawnSize() {
        return spawnSize;
    }

    public String[] getAvailableColors() {
        return availableColors;
    }

    public static synchronized GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    private GameConfig() {
        String appConfigPath = "src/main/resources/game.properties";

        Properties editorProps = new Properties();
        try {
            editorProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Заполняем значения
        spawnSize = Integer.valueOf(editorProps.getProperty("spawn.size"));
        availableColors = editorProps.getProperty("spawn.colors").split(",");
    }
}
