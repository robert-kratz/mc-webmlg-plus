package us.rjks.utils;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.rjks.game.GameManager;
import us.rjks.game.Main;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 16:28
 *
 **************************************************************************/

public class ScoreBoard {

    public void setScoreBoard(Player player) {

        Scoreboard scoreboard = new Scoreboard();
        ScoreboardObjective obj = scoreboard.registerObjective("zagd", IScoreboardCriteria.b);
        obj.setDisplayName(Messages.getString("score-board-layout-title"));

        PacketPlayOutScoreboardObjective createPacket = new PacketPlayOutScoreboardObjective(obj, 0);
        PacketPlayOutScoreboardDisplayObjective display = new PacketPlayOutScoreboardDisplayObjective(1, obj);
        PacketPlayOutScoreboardObjective removePacket = new PacketPlayOutScoreboardObjective(obj, 1);

        sendPacket(removePacket, player);
        sendPacket(createPacket, player);
        sendPacket(display, player);

        Messages.getStringList("score-board-layout").forEach(element -> {
            ScoreboardScore score = new ScoreboardScore(scoreboard, obj, element
                    .replaceAll("%map%", Main.getGame().getCurrentMap().getName())
                    .replaceAll("%rank%", "0"));

            score.setScore(Messages.getStringList("score-board-layout").size() - Messages.getStringList("score-board-layout").indexOf(element));
            PacketPlayOutScoreboardScore packetPlayOutScoreboardScore = new PacketPlayOutScoreboardScore(score);
            sendPacket(packetPlayOutScoreboardScore, player);
        });
    }

    private void sendPacket(Packet packet, Player p) {
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
    }

}
