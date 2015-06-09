package getPixelFromPicture;

import java.awt.image.BufferedImage;
import java.util.Date;


public class ImageConverter {
	private BufferedImage img;
	private int [][]pixels;
	
	public ImageConverter (){
	}

	public void setImage(BufferedImage img) {
		this.img = img;
		this.pixels = new int[img.getWidth()][img.getHeight()];
	}

	public void use() {
		for (int i = 0; i < img.getWidth(); i++){
			for (int j = 0; j < img.getHeight(); j++) {
				int s = (img.getRGB(i, j));
				pixels[i][j] = s;
			}
		}
	}
	
	public int []getMiddlePixel(){
		int x,y;
		x=(img.getWidth()-1/2);
		y=(img.getHeight()-1)/2;
		int result[]=convertToRGB(img.getRGB(x,y));
		return result;
	}
	public int []getSinglePixelColor(int x, int y){
		int result[]=convertToRGB(img.getRGB(x,y));
		return result;
	}
	
	/*gets an int and returns an int array with the following values:
	*int [0] = alpa
	*int [1] = red
	*int [2] = green
	*int [3] = blue
	*/
	public int []convertToRGB(int input){
		int []argb=new int[4];
		argb[0]=input>>>24;
		argb[1]=(input&0x00ff0000)>>>16;
		argb[2]=(input&0x0000ff00)>>>8;
		argb[3]=(input&0x000000ff);		
        return argb;
	}
}
