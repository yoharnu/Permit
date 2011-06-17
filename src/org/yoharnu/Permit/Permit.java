// Package
package org.yoharnu.Permit;

//Imports
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * IPGet for Bukkit - Acquire IP of any online player
 * 
 * @author yoharnu
 */

// Starts the class
public class Permit extends JavaPlugin {

	@Override
	public void onDisable() {
		System.out.println("Permit Disabled");
	}

	@Override
	public void onEnable() {
		// Get the information from the yml file.
		PluginDescriptionFile pdfFile = this.getDescription();
		
		// Print that the plugin has been enabled!
		System.out.println(pdfFile.getName() + " version "
				+ pdfFile.getVersion() + " is enabled!");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		String commandName = cmd.getName();
		if(args.length < 2){
			return false;
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (commandName.equalsIgnoreCase("permit")){
				String holder = args[1].toLowerCase();
				if(!this.getDataFolder().exists()){
					this.getDataFolder().mkdirs();
				}
				File configFile = new File(this.getDataFolder(), holder + ".txt");
				if(args[0].equalsIgnoreCase("issue")){
					try {
						configFile.createNewFile();
					} catch (IOException e) {}
					FileInputStream fstream = null;
					try {
						fstream = new FileInputStream(configFile);
					} catch (FileNotFoundException e1) {}
					DataInputStream in = new DataInputStream(fstream);
					String tempFile = "";
					String newline = System.getProperty("line.separator");
					try {
						while (in.available() !=0)
						{
							String temp = in.readLine();
							if(temp.equalsIgnoreCase(args[2])){
								player.sendMessage(holder + " already has a permit for " + temp);
								return true;
							}
							tempFile += temp + newline;
						}
					} catch (IOException e1) {}
					FileOutputStream out;
					PrintStream p;
					try {
						out = new FileOutputStream(configFile);
						p = new PrintStream(out);
						p.println(tempFile + args[2]);
						p.close();
					} catch (FileNotFoundException e) {}
					player.sendMessage(holder + " now has a permit for " + args[2]);
					List<Player> Matches = getServer().matchPlayer(holder);
					if (Matches.size()>0){
						Player match = Matches.get(0);
						match.sendMessage(player.getName() + " issued you a permit for " + args[2]);
					}
				}
				else if(args[0].equalsIgnoreCase("view")){
					if(!configFile.exists()){
						sender.sendMessage(ChatColor.YELLOW + holder + " does not have any permits.");
						return true;
					}
					try {
						configFile.createNewFile();
					} catch (IOException e) {}
					FileInputStream fstream = null;
					try {
						fstream = new FileInputStream(configFile);
					} catch (FileNotFoundException e1) {}
					DataInputStream in = new DataInputStream(fstream);
					player.sendMessage(holder + "'s Permits:");
					try {
						while (in.available() !=0)
						{
							player.sendMessage(in.readLine());
						}
					} catch (IOException e1) {}
				}
				else if(args[0].equalsIgnoreCase("revoke")){
					if(!configFile.exists()){
						sender.sendMessage(ChatColor.YELLOW + holder + " does not have any permits.");
						return true;
					}
					FileInputStream fstream = null;
					try {
						fstream = new FileInputStream(configFile);
					} catch (FileNotFoundException e1) {}
					DataInputStream in = new DataInputStream(fstream);
					String tempFile = "";
					String newline = System.getProperty("line.separator");
					try {
						while (in.available() !=0)
						{
							String temp = in.readLine();
							if(!temp.equalsIgnoreCase(args[2])){
								tempFile += temp + newline;
							}
						}
					} catch (IOException e1) {}
					FileOutputStream out;
					PrintStream p;
					try {
						out = new FileOutputStream(configFile);
						p = new PrintStream(out);
						p.close();
					} catch (FileNotFoundException e) {}
					player.sendMessage(holder + " no longer has a permit for " + args[2]);
					List<Player> Matches = getServer().matchPlayer(holder);
					if (Matches.size()>0){
						Player match = Matches.get(0);
						match.sendMessage(player.getName() + " has revoked your permit for " + args[2]);
					}
				}
			}
		}
		return true;
	}
}