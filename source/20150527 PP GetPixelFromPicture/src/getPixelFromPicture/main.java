package getPixelFromPicture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		//BufferedImage img=ImageIO.read(new File("white.png"));
		GoogleStaticMapLoader gsml=new GoogleStaticMapLoader();
		
		double d []=gsml.createFlag(50.476980,7.739340, 500);
		
		//int argb[]=gsml.getPosColor(50.476980,7.739340);
		for (double i:d)System.out.println(i);
		//ImageConverter converter=new ImageConverter();
//		String rgb[][][]=converter.use();
//		for (int i=0; i<4;i++){
//		System.out.println(rgb[0][0][i]);
	//	}
	}

}
