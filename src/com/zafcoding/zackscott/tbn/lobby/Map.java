package com.zafcoding.zackscott.tbn.lobby;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;

import com.zafcoding.zackscott.tbn.TBN;

public class Map {

	TBN tbn = TBN.tbn;
	int amount = 2;
	boolean defall = false;
	int defaller = 1;
	ArrayList<MapInt> maps = new ArrayList<MapInt>();
	
	public void setupMap(){
		if(defall){
			
		}
		Random rand = new Random();
		int re = rand.nextInt(2);
		while (re != 1 || re != 2){
			re = rand.nextInt(2);
		}
		if(re == 1){
			String worldName = "SamCity";
			TBN.info.setActiveWorld(Bukkit.getWorld("SamCity"));
			File playerFilesDir = new File(worldName + "/players");
			if (playerFilesDir.isDirectory()) {
				String[] playerDats = playerFilesDir.list();
				for (int i = 0; i < playerDats.length; i++) {
					File datFile = new File(playerFilesDir, playerDats[i]);
					datFile.delete();
				}
			}
		}if(re == 2){
			String worldName = "MallMap";
			TBN.info.setActiveWorld(Bukkit.getWorld("MallMap"));
			File playerFilesDir = new File(worldName + "/players");
			if (playerFilesDir.isDirectory()) {
				String[] playerDats = playerFilesDir.list();
				for (int i = 0; i < playerDats.length; i++) {
					File datFile = new File(playerFilesDir, playerDats[i]);
					datFile.delete();
				}
			}
		}
	}
	
	public void setupMap(int re){
		if(re == 1){
			String worldName = "SamCity";
			TBN.info.setActiveWorld(Bukkit.getWorld("SamCity"));
			File playerFilesDir = new File(worldName + "/players");
			if (playerFilesDir.isDirectory()) {
				String[] playerDats = playerFilesDir.list();
				for (int i = 0; i < playerDats.length; i++) {
					File datFile = new File(playerFilesDir, playerDats[i]);
					datFile.delete();
				}
			}
		}if(re == 2){
			String worldName = "MallMap";
			TBN.info.setActiveWorld(Bukkit.getWorld("MallMap"));
			File playerFilesDir = new File(worldName + "/players");
			if (playerFilesDir.isDirectory()) {
				String[] playerDats = playerFilesDir.list();
				for (int i = 0; i < playerDats.length; i++) {
					File datFile = new File(playerFilesDir, playerDats[i]);
					datFile.delete();
				}
			}
		}
	}
	
	public ArrayList<MapInt> getMaps(){
		return maps;
	}
	
}
