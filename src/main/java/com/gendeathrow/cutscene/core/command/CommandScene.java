package com.gendeathrow.cutscene.core.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import com.gendeathrow.cutscene.core.CutScene;
import com.gendeathrow.cutscene.network.packet.PacketScene;
import com.gendeathrow.cutscene.network.packet.PacketScene.SceneControls;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CommandScene extends CommandBase 
{

	@Override
	public String getName()
	{
		return "cutscene";
	}

	@Override
	public String getUsage(ICommandSender p_71518_1_)
	{
		return "/cutscene <playername> <play> <CutScene ID>";
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
	
	@Override
	public void execute(MinecraftServer server, ICommandSender commandsender, String[] astring) throws CommandException {
		EntityPlayerMP player = getPlayer(server, commandsender, astring[0]);
		
		String sceneID = astring[2];
		
		CutScene.instance.network.sendTo(new PacketScene(sceneID, SceneControls.Play), player);
	}
	
	@SuppressWarnings("unchecked")
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] strings)
    {
        if(strings.length == 1)
        {
        	return getListOfStringsMatchingLastWord(strings, FMLCommonHandler.instance().getMinecraftServerInstance().getServer().getOnlinePlayerNames());
        } else if(strings.length == 2)
        {
        	return getListOfStringsMatchingLastWord(strings, new String[]{"play"});
        } 

        else
        {
        	return new ArrayList<String>();
        }
    }

}
