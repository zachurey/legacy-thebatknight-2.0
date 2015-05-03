package com.zafcoding.zackscott.tbn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.entity.Player;

public class MySQLer {

	TBN tbn = TBN.tbn;
	boolean exist = false;
	boolean mysql = true;
	String database = Data.Minigame_Tokens.toString();

	public MySQL MySQL = new MySQL(tbn, "23.229.139.232", "3306", "Server",
			"ttPlugin", "DoubleTT!");
	Connection c = null;

	public Connection getConnection() {
		return c;
	}

	public boolean getMySQL() {
		return mysql;
	}

	public void offMySQL() {
		mysql = false;
	}

	public void connect() throws SQLException, ClassNotFoundException {
		if (!mysql) {
			return;
		}
		c = MySQL.openConnection();
	}

	public void check() throws SQLException, ClassNotFoundException {
		if (!mysql) {
			return;
		}
		if (c == null) {
			connect();
		}
		return;
		/*
		 * DatabaseMetaData dbm = c.getMetaData(); ResultSet tables =
		 * dbm.getTables(null, null, "Minigame_Tokens", null); if
		 * (tables.next()) { exist = true; } else { Statement statement =
		 * c.createStatement(); statement .executeUpdate(
		 * "CREATE TABLE Minigame_Tokens(Table_ID INT NOT NULL AUTO_INCREMENT,UUID VARCHAR(100) NOT NULL,Username VARCHAR(100) NOT NULL,Token INT NOT NULL,PRIMARY KEY ( Table_ID ));"
		 * ); System.out .println(
		 * "CREATE TABLE Minigame_Tokens(Table_ID INT NOT NULL AUTO_INCREMENT,UUID VARCHAR(100) NOT NULL,Username VARCHAR(100) NOT NULL,Token INT NOT NULL,PRIMARY KEY ( Table_ID ));"
		 * ); exist = true; return; }
		 */
	}

	public boolean isPlayer(Player player) throws ClassNotFoundException,
			SQLException {
		boolean is = false;
		if (!mysql) {
			return false;
		}
		if (c == null || c.isClosed()) {
			connect();
		}
		try {
			Statement statement = c.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM " + database
					+ " WHERE UUID = '" + player.getUniqueId() + "';");
			res.next();
			if (res.getString("Username") != null) {
				is = true;
				TBN.debugMsg("The player " + player.getDisplayName()
						+ " has a profile: " + (res.getString("Username")));
			} else {
				is = false;
				TBN.debugMsg("The player " + player.getDisplayName()
						+ " does not have a profile!");
			}
		} catch (Exception e) {
			c.close();
			c = null;
			return false;
		}
		c.close();
		return is;
	}

	public void addPlayer(Player player) throws SQLException,
			ClassNotFoundException {
		if (!mysql) {
			return;
		}
		if (c == null || c.isClosed()) {
			connect();
		}
		String br = "'";
		TBN.debugMsg("Creating profile for " + player.getDisplayName() + "...");
		TBN.debugMsg("INSERT INTO `" + database
				+ "`(`UUID`, `Username`, `Token`, `Kills`, `Deaths`) VALUES ("
				+ br + "" + player.getUniqueId().toString() + br + "," + br
				+ player.getDisplayName() + br + ",0,0,0)");
		Statement statement = c.createStatement();
		statement.executeUpdate("INSERT INTO `" + database
				+ "`(`UUID`, `Username`, `Token`, `Kills`, `Deaths`) VALUES ("
				+ br + "" + player.getUniqueId().toString() + br + "," + br
				+ player.getDisplayName() + br + ",0,0,0)");
		c.close();
	}

	public void setCoins(Player player, int i) throws SQLException,
			ClassNotFoundException {
		if (!mysql) {
			return;
		}
		if (c == null || c.isClosed()) {
			connect();
		}
		Statement statement = c.createStatement();
		statement.executeUpdate("UPDATE `" + database + "` SET `Token`=" + i
				+ " WHERE UUID = '" + player.getUniqueId().toString() + "'");
		c.close();
	}

	public int getCoins(Player player) throws SQLException,
			ClassNotFoundException {
		int get = 0;
		if (!mysql) {
			return 0;
		}
		if (c == null || c.isClosed()) {
			connect();
		}
		Statement statement = c.createStatement();
		ResultSet res = statement.executeQuery("SELECT * FROM " + database
				+ " WHERE UUID = '" + player.getUniqueId().toString() + "';");
		res.next();
		get = res.getInt("Token");
		c.close();
		return get;
	}

	public void setKills(Player player, int i) throws SQLException,
			ClassNotFoundException {
		if (!mysql) {
			return;
		}
		if (c == null || c.isClosed()) {
			connect();
		}
		Statement statement = c.createStatement();
		statement.executeUpdate("UPDATE `" + database + "` SET `Kills`=" + i
				+ " WHERE UUID = '" + player.getUniqueId().toString() + "'");
		TBN.debugMsg("UPDATE `" + database + "` SET `Kills`=" + i
				+ " WHERE UUID = '" + player.getUniqueId().toString() + "'");
		c.close();
	}

	public int getKills(Player player) throws SQLException,
			ClassNotFoundException {
		int get = 0;
		if (!mysql) {
			return 0;
		}
		if (c == null || c.isClosed()) {
			connect();
		}
		Statement statement = c.createStatement();
		ResultSet res = statement.executeQuery("SELECT * FROM " + database
				+ " WHERE UUID = '" + player.getUniqueId().toString() + "';");
		res.next();
		get = res.getInt("Kills");
		c.close();
		return get;
	}

	public void setDeaths(Player player, int i) throws SQLException,
			ClassNotFoundException {
		if (!mysql) {
			return;
		}
		if (c == null || c.isClosed()) {
			connect();
		}
		Statement statement = c.createStatement();
		statement.executeUpdate("UPDATE `" + database + "` SET `Deaths`=" + i
				+ " WHERE UUID = '" + player.getUniqueId().toString() + "'");
		TBN.debugMsg("UPDATE `" + database + "` SET `Deaths`=" + i
				+ " WHERE UUID = '" + player.getUniqueId().toString() + "'");
		c.close();
	}

	public int getDeaths(Player player) throws SQLException,
			ClassNotFoundException {
		int get = 0;
		if (!mysql) {
			return 0;
		}
		if (c == null || c.isClosed()) {
			connect();
		}
		Statement statement = c.createStatement();
		ResultSet res = statement.executeQuery("SELECT * FROM " + database
				+ " WHERE UUID = '" + player.getUniqueId().toString() + "';");
		TBN.debugMsg("SELECT * FROM " + database + " WHERE UUID = '"
				+ player.getUniqueId().toString() + "';");
		res.next();
		get = res.getInt("Deaths");
		c.close();
		return get;
	}

	public void logChat(Player pl, String message){
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		System.out.println(df.format(cal));
	}
	
	public void synceToken(PlayerProfile pp) throws ClassNotFoundException,
			SQLException {
		if (!mysql) {
			return;
		}
		if (c == null || c.isClosed()) {
			connect();
		}
		pp.setCoins(getCoins(pp.getPlayer()));
		c.close();
	}

	public enum Data {
		Minigame_Tokens, Minigame_Tokens_Test
	}

}
