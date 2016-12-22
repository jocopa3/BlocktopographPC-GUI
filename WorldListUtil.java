package com.protolambda.blocktopograph.worldlist;



import com.protolambda.blocktopograph.R;
import com.protolambda.blocktopograph.world.World;

import java.text.DateFormat;

public class WorldListUtil {

    public static long getLastPlayedTimestamp(World world){
        try {
            return (long) world.level.getChildTagByKey("LastPlayed").getValue();
        } catch (Exception e){
            return 0;
        }
    }

    public static String getLastPlayedText(Context context, World world){
        long lastPlayed = getLastPlayedTimestamp(world);
        if(lastPlayed == 0) return "?";
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);

        return dateFormat.format(lastPlayed * 1000);
    }

    public static String getWorldGamemodeText(Context context, World world){
        String gameMode;
        try {
            int gameType = (int) world.level.getChildTagByKey("GameType").getValue();
            switch (gameType){
                case 0: gameMode = context.getString(R.string.gamemode_survival);
                    break;
                case 1: gameMode = context.getString(R.string.gamemode_creative);
                    break;
                case 2: gameMode = context.getString(R.string.gamemode_adventure);
                    break;
                default:
                    gameMode = "?";
            }
        } catch (Exception e){
            gameMode = "?";
        }
        return gameMode;
    }
    
    
}
