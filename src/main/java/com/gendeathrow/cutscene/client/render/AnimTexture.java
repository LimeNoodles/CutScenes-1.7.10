package com.gendeathrow.cutscene.client.render;

import static org.lwjgl.opengl.GL11.glDeleteTextures;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.NodeList;

import com.gendeathrow.cutscene.utils.RenderAssist;

public class AnimTexture 
{
	private static TextureManager manager = Minecraft.getMinecraft().getTextureManager();


	private ResourceLocation location;
	private long startTime;

	private static IntBuffer buf = BufferUtils.createIntBuffer(4 * 1024 * 1024);
	private int name;
	private int width;
	private int height;
	private int frames;

	private int loop;
	private int loopcnt = 1;

	private int curFrame;

	private int size;

	private BufferedImage[] imageFrames;

	private Frame[] framedata;




	private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName) {
		int nNodes = rootNode.getLength();
		for (int i = 0; i < nNodes; i++) {
			if (rootNode.item(i).getNodeName().compareToIgnoreCase(nodeName)== 0) {
				return((IIOMetadataNode) rootNode.item(i));
			}
		}
		IIOMetadataNode node = new IIOMetadataNode(nodeName);
		rootNode.appendChild(node);
		return(node);
	}


	public AnimTexture(ResourceLocation location)
	{
		InputStream s = null;
		try
		{

			String[] imageatt = new String[]{
					"imageLeftPosition",
					"imageTopPosition",
					"imageWidth",
					"imageHeight",
					"delayTime"
			};

			this.location = location;
			this.curFrame = 0;

			ImageInputStream stream = ImageIO.createImageInputStream(Minecraft.getMinecraft().getResourceManager().getResource(this.location).getInputStream());

			Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
			if(!readers.hasNext()) throw new IOException("No suitable reader found for image" + location);
			ImageReader reader = readers.next();
			reader.setInput(stream);
			frames = reader.getNumImages(true);
			imageFrames = new BufferedImage[frames];
			framedata = new Frame[frames];
			for(int i = 0; i < frames; i++)
			{
				IIOMetadata imageMetaData =  reader.getImageMetadata(i);
				String metaFormatName = imageMetaData.getNativeMetadataFormatName();

				IIOMetadataNode root = (IIOMetadataNode)imageMetaData.getAsTree(metaFormatName);
				NodeList children = root.getChildNodes();

				IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");

				BufferedImage img = reader.read(i);
				String delay = graphicsControlExtensionNode.getAttribute("delayTime");

				Frame frame = new Frame(img, delay);

				if(frame.equals(null)) System.out.println("Null Frame");
				else framedata[i] = frame;
			}

			reader.dispose();

			int size = 1;
			width = framedata[0].img.getWidth();
			height = framedata[0].img.getHeight();
			while((size / width) * (size / height) < frames) size *= 2;
			this.size = size;

		}
		catch(IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		finally
		{
			IOUtils.closeQuietly(s);
		}
	}

	public ResourceLocation getLocation()
	{
		return location;
	}

	public int getName()
	{
		return name;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int getFrames()
	{
		return frames;
	}

	public int getSize()
	{
		return size;
	}

	public void delete()
	{
		for(Frame frame : framedata)
		{
			frame.removeTexture();
		}
	}

	public void setLoop(int loop)
	{
		this.loop = loop;
	}


	public void bind()
	{
		Frame frame = this.framedata[this.curFrame];

		frame.bind();

		if(frame.nextFrame())
		{

			if(this.curFrame+1 <= this.frames-1)
			{
				this.curFrame++;
			}else if(shouldLoop())
			{
				this.curFrame = 0;
			}

		}
	}

	public void Draw()
	{
		Frame frame = this.framedata[this.curFrame];

		frame.draw();

		if(frame.nextFrame())
		{

			if(this.curFrame+1 <= this.frames-1)
			{
				this.curFrame++;
			}else if(shouldLoop())
			{
				this.curFrame = 0;
			}

		}
	}

	private boolean shouldLoop()
	{

		if(loop == -1) return true;
		else if(loopcnt <= loop)
		{
			loopcnt++;
			return true;
		}
		else return false;

	}

	private class Frame
	{
		public final BufferedImage img;
		public final int delay;
		long frameStart;

		private int glID;

		public Frame(BufferedImage bufferedImage, String delay)
		{
			this.img = bufferedImage;
			this.delay = Integer.parseInt(delay) * 10;
			this.glID = -1;
		}


		public void bind() {
			if(firstRun) { frameStart = Minecraft.getSystemTime(); firstRun = false;}

			if(glID == -1) glID = RenderAssist.loadTexture(img);
			else GL11.glBindTexture(GL11.GL_TEXTURE_2D, glID);
		}

		public void removeTexture()
		{
			glDeleteTextures(glID);

			TextureUtil.deleteTexture(glID);
		}


		private boolean firstRun = true;
		public void draw()
		{
			if(firstRun) { frameStart = Minecraft.getSystemTime(); firstRun = false; System.out.println("First Run");}

			int id = RenderAssist.loadTexture(img);

			RenderAssist.drawTexturedModalRect(0, 0, 100, 100);

			glDeleteTextures(id);

			TextureUtil.deleteTexture(id);
		}

		public boolean nextFrame()
		{
			if(Minecraft.getSystemTime() >= (frameStart + delay))
			{
				this.firstRun = true;
				return true;
			}
			return false;

		}

	}
     
}
