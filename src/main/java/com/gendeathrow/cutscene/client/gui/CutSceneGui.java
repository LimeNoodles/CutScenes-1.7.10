package com.gendeathrow.cutscene.client.gui;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import com.gendeathrow.cutscene.MovieRender.MovieReader;
import com.gendeathrow.cutscene.SceneRender.SceneObject;
import com.gendeathrow.cutscene.SceneRender.SegmentObject;
import com.gendeathrow.cutscene.utils.GsonReader;
import com.gendeathrow.cutscene.utils.RenderAssist;
import com.gendeathrow.cutscene.utils.Utils;
import com.ibm.icu.text.DateFormat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CutSceneGui extends GuiScreen
{
	private String scenePath;
	public int renderTicks;
	public SceneObject scene;
	public int currentPhase;
	public FontRenderer fontObj;
	public long startTime;
	
	
	
	public CutSceneGui() {}
	
	public CutSceneGui(String scenePath)
	{

		this.startTime = Minecraft.getSystemTime();
	
        if (scenePath.endsWith(".json"))
        {
        	scenePath = scenePath.substring(0, scenePath.length() - 5);
        }
		
		this.scenePath = scenePath + ".json";
		this.currentPhase = 0;
		
		this.scene = GsonReader.GsonReadFromFile(this.scenePath);
		
	
	}
	
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
	
	GuiButton reloadButton;
	GuiButton stopButton;
	
	@Override
    public void initGui() 
    {
		this.fontObj = this.fontRendererObj;
		
		this.reloadButton = new GuiButton(0, this.width - 50, this.height - 30, 15, 20,  "R");
		this.reloadButton.visible = false;
		
		this.stopButton = new GuiButton(1, this.width - 35, this.height - 30, 15, 20,  "S");
		
		this.buttonList.add(this.reloadButton);
		
		if(this.scene != null)	this.scene.init(this);

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
           	break;
	    }
	 }
	
	boolean firstload = true;
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		if (firstload) { firstload = false;  this.startTime = Minecraft.getSystemTime();}
		
		if (this.scene == null) {mc.displayGuiScreen(null); return;}
		if(!this.scene.finalize)    this.reloadButton.visible = true;
		if(this.scene.closeScene && this.scene.finalize) 	
		{
			mc.displayGuiScreen(null);
			return;
		}
		
		this.drawBackground();

		this.scene.DrawCutScene();
		
		if(this.scene.showDebug) this.fontRendererObj.drawString("Render Duration: "+Utils.getTimeFormater(Minecraft.getSystemTime() - this.startTime),0, 0, RenderAssist.getColorFromRGBA(255, 255, 255, 255));
		
		super.drawScreen(par1, par2, par3);
		this.renderTicks++;
	}
	
	public void drawBackground()
	{
		RenderAssist.drawRect(0, 0, this.width, this.height, this.scene.getBackgroundColor());
	}
	
}
