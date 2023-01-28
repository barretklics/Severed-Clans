package me.barret.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class UtilTeam {


    public static void reset(){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        for (Team t: board.getTeams()){
            t.unregister();
        }





    }
    public static void addColor(Entity e, ChatColor c){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();


        Team team = board.registerNewTeam(UUID.randomUUID().toString());
        team.setColor(c);
        team.addEntry(e.getUniqueId().toString());
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
    }


    public static void removeColor(Entity e) //untested
    {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        for (Team t: board.getTeams()){
            t.removeEntry(e.getUniqueId().toString());
        }
    }

    public static void removeCollision(Entity e){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        Team team = board.registerNewTeam(UUID.randomUUID().toString());
        team.addEntry(e.getUniqueId().toString());

        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
    }


}
