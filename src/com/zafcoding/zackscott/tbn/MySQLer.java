package com.zafcoding.zackscott.tbn;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;

public class MySQLer {

	TBN tbn = TBN.tbn;
	boolean exist = false;
	boolean mysql = true;

	public MySQL MySQL = new MySQL(tbn, "localhost", "3306", "database",
			"root", "");
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
		DatabaseMetaData dbm = c.getMetaData();
		// check if "employee" table is there
		ResultSet tables = dbm.getTables(null, null, "Minigame_Tokens", null);
		if (tables.next()) {
			exist = true;
		} else {
			Statement statement = c.createStatement();
			statement
					.executeUpdate("CREATE TABLE Minigame_Tokens(Table_ID INT NOT NULL AUTO_INCREMENT,UUID VARCHAR(100) NOT NULL,Username VARCHAR(100) NOT NULL,Token INT NOT NULL,PRIMARY KEY ( Table_ID ));");
			System.out
					.println("CREATE TABLE Minigame_Tokens(Table_ID INT NOT NULL AUTO_INCREMENT,UUID VARCHAR(100) NOT NULL,Username VARCHAR(100) NOT NULL,Token INT NOT NULL,PRIMARY KEY ( Table_ID ));");
			exist = true;
			return;
		}
	}

	public boolean isPlayer(Player player) throws SQLException,
			ClassNotFoundException {
		boolean is = false;
		if (!mysql) {
			return false;
		}
		if (c == null) {
			connect();
		}
		Statement statement = c.createStatement();
		ResultSet res = statement
				.executeQuery("SELECT * FROM Minigame_Tokens WHERE UUID = '"
						+ player.getUniqueId() + "';");
		res.next();
		TBN.debugMsg("The player " + player.getDisplayName()
				+ " has a profile: " + (res.getString("Username")));
		if (res.getString("DisplayName") != null) {
			is = true;
		} else {
			is = false;
		}
		;
		return is;
	}

	public void addPlayer(Player player) throws SQLException,
			ClassNotFoundException {
		if (!mysql) {
			return;
		}
		if (c == null) {
			connect();
		}
		TBN.debugMsg("Creating profile for " + player.getDisplayName() + "...");
		Statement statement = c.createStatement();
		statement
				.executeUpdate("INSERT INTO `Minigame_Tokens`(`Table_ID`, `UUID`, `Username`, `Token`) VALUES ('',"
						+ player.getUniqueId().toString()
						+ ","
						+ player.getDisplayName() + ",0)");
		TBN.debugMsg("INSERT INTO `Minigame_Tokens`(`Table_ID`, `UUID`, `Username`, `Token`) VALUES ('',"
				+ player.getUniqueId().toString()
				+ ","
				+ player.getDisplayName() + ",0)");
		;
	}

	public void setCoins(Player player, int i) throws SQLException,
			ClassNotFoundException {
		if (!mysql) {
			return;
		}
		if (c == null) {
			connect();
		}
		Statement statement = c.createStatement();
		statement.executeUpdate("UPDATE `Minigame_Tokens` SET `Token`=" + i
				+ " WHERE UUID = '" + player.getUniqueId().toString() + "'");
		TBN.debugMsg("UPDATE `Minigame_Tokens` SET `Token`=" + i
				+ " WHERE UUID = '" + player.getUniqueId().toString() + "'");
		;
	}

	public int getCoins(Player player) throws SQLException,
			ClassNotFoundException {
		int get = 0;
		if (!mysql) {
			return 0;
		}
		if (c == null) {
			connect();
		}
		Statement statement = c.createStatement();
		ResultSet res = statement
				.executeQuery("SELECT * FROM Minigame_Tokens WHERE UUID = '"
						+ player.getUniqueId().toString() + "';");
		res.next();
		get = res.getInt("Token");
		return get;
	}

}
