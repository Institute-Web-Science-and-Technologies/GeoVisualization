package getPixelFromPicture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;


public class GoogleStaticMapLoader {
	private BufferedImage img;
	private String []argb;
	GoogleStaticMapLoader(){
	}
	
	//createFlag creates a Flag in a radius (actually it's up to a square) around x and y
	public double [] createFlag(double x,double y, double radius) throws IOException{
		//standard radius 0.005000
		//last decimal point of x and y is about 0.1m
		boolean created=false;
		ImageConverter ic=new ImageConverter();
		double []xy=new double [2];  
		int i=0;
		while(created==false){
			// formula taken from
			// http://stackoverflow.com/questions/2839533/adding-distance-to-a-gps-coordinate
			xy[0]=x+(180 / Math.PI) * (Math.random() * radius / 6378137);
			xy[1]=y+ (180 / Math.PI) * (Math.random() * radius / 6378137)
					/ Math.cos(Math.PI / 180.0 * x);
			String s="https://maps.googleapis.com/maps/api/staticmap?center="+xy[0]+","+xy[1]+"&zoom=20&size=1x1&key=AIzaSyADfFDCH3mrZQc6Y3Vxj7m5_l-8I1JBAHQ";
			URL url=new URL(s);
			this.img=ImageIO.read(url);
			ic.setImage(img);
			created=isValid(ic.getSinglePixelColor(0,0));
			i++;
			//System.out.println(i);
			/*try {
			    Thread.sleep(1000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}*/
		}
		System.out.println(i);
		return xy;
	}
	
	//returns the color of the pixel of google maps at the position x and y (LatLng) at the maximum zoom (20)
	//color values are in binary and distributed as follows
	//Alpha (transparence) in String[0]
	//red in String[1]
	//green in String[2]
	//blue in String[3) 
	public int []getPosColor(double x, double y) throws IOException{
		int result[];
		String s="https://maps.googleapis.com/maps/api/staticmap?center="+x+","+y+"&zoom=20&size=400x400";
		URL url=new URL (s);
		this.img=ImageIO.read(url);
		ImageConverter ic=new ImageConverter();
		ic.setImage(img);
		result=ic.getMiddlePixel();
		return result;
	}
	//checks if the String []s equals the color of streets
	public boolean isValid(int []s){
		if (s[0]==255 && s[1]>=225 && s[2]>=225 && s[3]>=225)return true;
		else return false;
	}
}