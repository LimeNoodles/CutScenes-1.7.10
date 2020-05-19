package com.gendeathrow.cutscene.core;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;

import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.Logger;

import com.gendeathrow.cutscene.core.command.CommandScene;
import com.gendeathrow.cutscene.core.proxies.CommonProxy;
import com.gendeathrow.cutscene.network.packet.PacketScene;
import com.gendeathrow.cutscene.utils.Utils;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = CutScene.MODID, version = CutScene.VERSION, name = CutScene.Name)
public class CutScene
{
	    public static final String MODID = "cutscene";
	    public static final String VERSION = "GD_CCS_VER";
	    //public static final String VERSION = "0.1.2";
	    public static final String Name = "CustomCutScene";
	    public static final String Proxy = "com.gendeathrow.cutscene.core.proxies";
	    public static final String Channel = "CCS_GenD";
		private static final String GsonRea = null;
	    
	    public static  Logger logger;
	    
	    @Instance(CutScene.MODID)
	    public static CutScene instance;
	    
		@SidedProxy(clientSide = CutScene.Proxy + ".ClientProxy", serverSide = CutScene.Proxy + ".CommonProxy")
		public static CommonProxy proxy;
		
		public static boolean updateCheck = true;

		public SimpleNetworkWrapper network;
		
		public ConfigHandler config;
		
		@EventHandler
		public void preInit(FMLPreInitializationEvent event)
		{
			System.out.println(Name +" VERSION:"+ VERSION);
			
			Utils.downloadDemoFile();
			
			logger = event.getModLog();
			
			proxy.preInit(event);

			this.network = NetworkRegistry.INSTANCE.newSimpleChannel(CutScene.Channel);
			this.network.registerMessage(PacketScene.HandlerClient.class, PacketScene.class, 0, Side.CLIENT);	
			
		}
	    @EventHandler
	    public void init(FMLInitializationEvent event)
	    {
	    	proxy.init(event);
	    }
	    @EventHandler
		public void postInit(FMLPostInitializationEvent event)
		{
			proxy.postInit(event);
			
		}
		
		@EventHandler
		public void serverStart(FMLServerStartingEvent event)
		{
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getServer();
			ICommandManager command = server.getCommandManager();
			ServerCommandManager manager = (ServerCommandManager) command;
			
			manager.registerCommand(new CommandScene());

		}
}

