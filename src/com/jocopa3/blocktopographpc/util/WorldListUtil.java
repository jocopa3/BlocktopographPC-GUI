package com.jocopa3.blocktopographpc.util;

import com.protolambda.blocktopograph.world.World;
import com.sun.jna.Platform;
import java.io.File;

import java.text.DateFormat;
import javax.swing.filechooser.FileSystemView;

public class WorldListUtil {

    public static long getLastPlayedTimestamp(World world) {
        try {
            return (long) world.level.getChildTagByKey("LastPlayed").getValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getLastPlayedText(World world) {
        long lastPlayed = getLastPlayedTimestamp(world);
        if (lastPlayed == 0) {
            return "?";
        }
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        return dateFormat.format(lastPlayed * 1000);
    }

    public static String getWorldGamemodeText(World world) {
        String gameMode;
        try {
            int gameType = (int) world.level.getChildTagByKey("GameType").getValue();
            switch (gameType) {
                case 0:
                    gameMode = "Survival";
                    break;
                case 1:
                    gameMode = "Creative";
                    break;
                case 2:
                    gameMode = "Adventure";
                    break;
                default:
                    gameMode = "Unknown";
            }
        } catch (Exception e) {
            gameMode = "Unknown";
        }
        return gameMode;
    }

    public static final String MicrosoftPublisherID = "8wekyb3d8bbwe";

    private static String getMinecraftFolderName(String location) {
        File f = new File(location);

        if (f.exists()) {
            String folders[] = f.list();
            for (int i = 0; i < folders.length; i++) {
                if (folders[i].startsWith("Microsoft.Minecraft")) {
                    return "\\" + folders[i];
                }
            }
        }

        return null;
    }

    public static String getMinecraftFolderLocation() {
        if (Platform.isWindows()) {
            String worldFolder = System.getenv("APPDATA");

            if (worldFolder.endsWith("Roaming")) {
                worldFolder = worldFolder.replace("\\AppData\\Roaming", "\\AppData\\Local");
            }

            StringBuilder folder = new StringBuilder(worldFolder);
            folder.append("\\Packages")
                    //.append(getMinecraftFolderName(folder.toString()))
                    .append("\\Microsoft.MinecraftUWP_")
                    .append(MicrosoftPublisherID)
                    .append("\\LocalState\\games\\com.mojang\\minecraftWorlds");
            
            System.out.println(folder.toString());
            if (new File(folder.toString()).exists()) {
                return folder.toString();
            }
        }

        return FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
    }
}
