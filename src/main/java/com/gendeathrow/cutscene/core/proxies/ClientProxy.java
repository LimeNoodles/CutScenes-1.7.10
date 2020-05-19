package com.gendeathrow.cutscene.core.proxies;

import org.apache.logging.log4j.Level;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import com.gendeathrow.cutscene.client.ClientTickHandler;
import com.gendeathrow.cutscene.client.Gui_EventHandler;
import com.gendeathrow.cutscene.core.ConfigHandler;
import com.gendeathrow.cutscene.core.CutScene;
import com.gendeathrow.cutscene.network.packet.PacketScene;
import com.gendeathrow.cutscene.utils.Utils;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	private final Minecraft mc = Minecraft.getMinecraft();

	@Override
	public boolean isClient() {
		return true;
	}

	@Override
	public boolean isOpenToLAN() {
		if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
			return Minecraft.getMinecraft().getIntegratedServer().getPublic();
		} else {
			return false;
		}
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		// TODO Auto-generated method stub
		
		
		CutScene.instance.config = new ConfigHandler(event);
	    try
	    {
	    	CutScene.instance.config.load();
	    }
	    catch (Exception e)
	    {
	    	CutScene.logger.log(Level.ERROR, "Error while loading config file. Why does this always happen");
	    	throw new RuntimeException(e);
	    }
	    
	    
		Utils.LoadResources();
		//SoundAssist.LoadAlData();
		
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {

		super.init(event);
		
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		// TODO Auto-generated method stub
		super.postInit(event);

	}

	@Override
	public void registerTickHandlers() 
	{
		super.registerTickHandlers();
		FMLCommonHandler.instance().bus().register(new ClientTickHandler());

	}

	@Override
	public void registerEventHandlers()
	{

		super.registerEventHandlers();
		MinecraftForge.EVENT_BUS.register(new Gui_EventHandler());
		super.registerEventHandlers();
	}
	
	@Override
	public void registerRenders()
	{

	}

}
