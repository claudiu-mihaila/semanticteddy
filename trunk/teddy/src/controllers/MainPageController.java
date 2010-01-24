package controllers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class MainPageController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1039034704660534296L;

	private boolean dotRendered = false;
	
	public void emptyMethod (){
		
	}
	
	public void getDot (){
		this.setDotRendered(true);
	}

	public void setDotRendered(boolean dotRendered) {
		this.dotRendered = dotRendered;
	}

	public boolean isDotRendered() {
		return dotRendered;
	}
	
	public void paint(OutputStream out, Object data) {
		if (data instanceof Long) {
			BufferedImage img = null;
			try {
				img = this.createBufferedImageFormDOT();
			}
			catch (IOException e) {
				if (img == null) {
					img = this.createBufferedImageFormOtherFile("jpg");
				}
			}
			try {
				ImageIO.write(img, "bmp", out);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (Exception e) {}
				}
			}
		}
	}

	public BufferedImage createBufferedImageFormOtherFile(String extension) {		
		BufferedImage img = new BufferedImage(100, 100,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = img.createGraphics();
		graphics2D.setBackground(Color.WHITE);
		graphics2D.setColor(Color.BLACK);
		graphics2D.clearRect(0, 0, 100, 100);
		graphics2D.drawString(extension, 40, 40);
		return img;
	}

	public BufferedImage createBufferedImageFormDOT() throws IOException {
		
		String input = "digraph G {a->b[label=\"a to b\"]; b->c[label=\"b to c\"]; c->a[label=\"c to a\"];}";
		String[] comms = {"\"C:/Program Files (x86)/Graphviz2.26/bin/dot.exe\"","-Tjpg"};

		Runtime rt = Runtime.getRuntime() ;
        Process p = rt.exec(comms);
         
        InputStream in = p.getInputStream();
        OutputStream out = p.getOutputStream();
        InputStream err = p.getErrorStream() ;

        out.write(input.getBytes());
        out.flush();
        out.close();

        BufferedImage img = null;
		ImageInputStream iis = null;
		try {
			iis = ImageIO.createImageInputStream(in);

			Iterator irIt = ImageIO.getImageReaders(iis);
			ImageReader imgReader = null;
			while (irIt.hasNext()) {
				imgReader = (ImageReader) irIt.next();
				imgReader.setInput(iis);

				try {
					img = imgReader.read(0);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (iis != null) {
				try {
					iis.close();
				} catch (Exception e) {
				}
				;
			}
		}

        in.close();
        err.close();
        
        p.destroy();		
		
		
		return img;
	}

	public long getTimeStamp() {
		return new Date().getTime();
	}
}
