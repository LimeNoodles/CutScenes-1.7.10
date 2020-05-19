package com.gendeathrow.cutscene.core.proxies;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{

	public boolean isClient()
	{
		return false;
	}
	
	public boolean isOpenToLAN()
	{
		return false;
	}
	
	public void preInit(FMLPreInitializationEvent event) 
	{

		
	}

	public void init(FMLInitializationEvent event) 
	{
		registerEventHandlers();
		registerTickHandlers();

	}

	public void postInit(FMLPostInitializationEvent event)
	{
		// TODO Auto-generated method stub
		
	}

	public void registerTickHandlers() 
	{
			
	}

	public void registerEventHandlers() 
	{

	}
	
	public void registerRenders()
	{
		
	}

}
