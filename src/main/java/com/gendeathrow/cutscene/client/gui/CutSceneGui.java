package com.gendeathrow.cutscene.client.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;




import com.gendeathrow.cutscene.MovieRender.MovieReader;
import com.gendeathrow.cutscene.SceneRender.SceneObject;
import com.gendeathrow.cutscene.SceneRender.SegmentObject;
import com.gendeathrow.cutscene.utils.GsonReader;
import com.gendeathrow.cutscene.utils.RenderAssist;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CutSceneGui extends GuiScreen
{
	private String scenePath;
	public int renderTicks;
	public int closeOnTick;
	public SceneObject scene;
	public int currentPhase;
	public ArrayList<SegmentObject> segmentList;
	public MovieReader Movie;
	public BufferedImage[] frames;
	
	public CutSceneGui()
	{
		this.renderTicks = 0;
		this.closeOnTick = 90;
	}
	
	public CutSceneGui(String scenePath)
	{
		this.scenePath = scenePath;
		this.renderTicks = 0;
		this.currentPhase =0;
		this.scene = GsonReader.GsonReadFromFile(scenePath);
		this.closeOnTick = this.scene.getCloseOnTicks();
		this.segmentList = this.scene.screenSegments;
		this.scene.init();
//		this.Movie = new MovieReader();
//		try 
//		{
//			this.Movie.read(new File(Loader.instance().getConfigDir()+"/CustomCutScenes/assets/video1.avi"));
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//this.Movie = new AVIReader(new File("/config/CustomCutScenes/assets/video1.avi"));
		//this.Movie = Registry.getInstance().getReader(new File("/config/CustomCutScenes/assets/video1.avi"));
		
		
	}
	
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
	
	GuiButton button;
	
	@Override
    public void initGui() 
    {
		this.button = new GuiButton(0, this.width/2, this.height/2, 80, 20,  "Reload");
		this.button.visible = false;
		this.buttonList.add(this.button);
    }

    
	@Override
	public boolean doesGuiPauseGame()
	{
		return true;
	}
	
	
	protected void actionPerformed(GuiButton p_146284_1_)
	{
		switch (p_146284_1_.id)
		{
	       case 0:
           	mc.displayGuiScreen(new CutSceneGui(this.scenePath));
           	return;
	    }
	 }
	
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		
		
//		try {
//			this.Movie.draw();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if(this.renderTicks >= this.closeOnTick) 
		{
			this.button.visible = true;
			
			//mc.displayGuiScreen(null);
		}
		
		this.drawBackground();

		if(this.segmentList != null && this.currentPhase <= this.segmentList.size()-1)
		{
			((SegmentObject)this.segmentList.get(this.currentPhase)).DrawSegment(this, Minecraft.getMinecraft(), this.fontRendererObj);
		}
		
		this.fontRendererObj.drawString("Render Ticks: "+this.renderTicks,0, 0, RenderAssist.getColorFromRGBA(255, 255, 255, 255));
		
		super.drawScreen(par1, par2, par3);
		this.renderTicks++;
	}
	
	public void drawBackground()
	{
		RenderAssist.drawRect(0, 0, this.width, this.height, RenderAssist.getColorFromRGBA(0, 0, 0, 180));
		
	}
	
}