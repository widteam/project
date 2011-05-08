import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * Seged view class az elemek megjelenitesehez. Tarolja az egyes elemek X, Y
 * koordintait, tovabba az elem ID-jet, amely a kapcsolatot jelenti az elem es a
 * hozza tartozo View kozott
 * 
 */
public class viewElem {
	/** az elem ID-ja, amihez a View osztaly tartozik */
	public String ID;
	/** Az elem X koordinataja */
	public int X;
	/** Az elem Y koordinataja */
	public int Y;

	public int szint;
	
	private ArrayList<viewElem> pins_in;
	private ArrayList<viewElem> pins_out;
	private int pinInNo=0;
	private int pinOuNo=0;
	
	

	public class ImagePanel extends JPanel{
		private static final long serialVersionUID = -8103459490901853605L;
		private Image img;		
	    public void paintComponent(Graphics g) {
	    	super.paintComponents(g);
	    	this.setBackground(Color.WHITE);
	        g.drawImage(img, 0, 0, null);
	    }
	    
	    public void refreshImage(Image im){
	    	img=im;
	    	int width = img.getWidth(null);
	        int height = img.getHeight(null);	        
	        this.setSize(width, height);
	        repaint();
	    }
	}

	public ImagePanel ImageContainer;
	
	/** Konstruktor. letrehozzuk az adott IDju elemhez tartozo View objektumot. */
	public viewElem(int xa, int ya, int szinta, String ida) {
		X = xa;
		Y = ya;
		ID = ida;
		szint=szinta;
		pins_in=new ArrayList<viewElem>();
		pins_out=new ArrayList<viewElem>();
		
		ImageContainer = new ImagePanel(); 
		ImageContainer.setName(ID);
		
	}

	public void addPinIn(viewElem e){
		pins_in.add(e);
		pinInNo=pins_in.size()-1;
	}
	public void addPinOut(viewElem e){
		pins_out.add(e);
		pinOuNo=pins_out.size()-1;
	}
	public viewElem getNextPinIn(){
		pinInNo++;
		if(pinInNo>pins_in.size()-1) pinInNo=0;
		if(pins_in.size()==0) 
			return null;//hiba
		return pins_in.get(pinInNo);
	}
	public viewElem getNextPinOut(){
		pinOuNo++;
		if(pinOuNo>pins_out.size()-1) pinOuNo=0;
		if(pins_out.size()==0) 
			return null;
		return pins_out.get(pinOuNo);
	}

}
