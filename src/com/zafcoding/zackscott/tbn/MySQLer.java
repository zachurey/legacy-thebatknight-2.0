package com.zafcoding.zackscott.tbn;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;

public class MySQLer {

	TBN tbn = TBN.tbn;
	boolean exist = false;
	boolean mysql = true;

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
			ResultSet res = statement
					.executeQuery("SELECT * FROM Minigame_Tokens WHERE UUID = '"
							+ player.getUniqueId() + "';");
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
		TBN.debugMsg("INSERT INTO `Minigame_Tokens`(`UUID`, `Username`, `Token`) VALUES ("
				+ br
				+ ""
				+ player.getUniqueId().toString()
				+ br
				+ ","
				+ br
				+ player.getDisplayName() + br + ",0)");
		Statement statement = c.createStatement();
		statement
				.executeUpdate("INSERT INTO `Minigame_Tokens`(`UUID`, `Username`, `Token`) VALUES ("
						+ br
						+ ""
						+ player.getUniqueId().toString()
						+ br
						+ ","
						+ br + player.getDisplayName() + br + ",0)");
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
		statement.executeUpdate("UPDATE `Minigame_Tokens` SET `Token`=" + i
				+ " WHERE UUID = '" + player.getUniqueId().toString() + "'");
		TBN.debugMsg("UPDATE `Minigame_Tokens` SET `Token`=" + i
				+ " WHERE UUID = '" + player.getUniqueId().toString() + "'");
		c.close();
	}

	public int getCoins(Player player) throws SQLException,
			ClassNotFoundException {
		TBN.debugMsg("SELECT * FROM Minigame_Tokens WHERE UUID = '"
				+ player.getUniqueId().toString() + "';");
		int get = 0;
		if (!mysql) {
			return 0;
		}
		if (c == null || c.isClosed()) {
			connect();
		}
		Statement statement = c.createStatement();
		ResultSet res = statement
				.executeQuery("SELECT * FROM Minigame_Tokens WHERE UUID = '"
						+ player.getUniqueId().toString() + "';");
		res.next();
		get = res.getInt("Token");
		c.close();
		return get;
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

}
