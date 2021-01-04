package LiveWeather;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Test implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {
			Player p = (Player) sender;

			if (!(p.hasPermission("earthserver.tpblock"))) {
				p.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				return true;
			}
			
			if (args.length != 3) {
				p.sendMessage(ChatColor.RED + "/tpblock <x> <y> <z>");
				return true;
			}

			Location l = p.getLocation();
			l.setY(l.getY()-1);
			Block commandblock = p.getWorld().getBlockAt(l);
			commandblock.setType(Material.COMMAND);
			CommandBlock cmdBlock = (CommandBlock) commandblock.getState();
			cmdBlock.setCommand("teleport @p " + args[0] + " " + args[1] + " " + args[2]);
			cmdBlock.update();
		} else {
			sender.sendMessage("You must be a player to use this command!");
		}

		return true;
	}

}
