import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

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
	
	private Icon LabelIcon;
	private JLabel ImageContainer;
	
	/** Konstruktor. letrehozzuk az adott IDju elemhez tartozo View objektumot. */
	public viewElem(int xa, int ya, int szinta, String ida) {
		X = xa;
		Y = ya;
		ID = ida;
		szint=szinta;
		pins_in=new ArrayList<viewElem>();
		pins_out=new ArrayList<viewElem>();
		
		ImageContainer = new JLabel(ID,new ImageIcon(), javax.swing.SwingConstants.RIGHT); 
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
	
	public JLabel getImageContainer(){
		return ImageContainer;
	}
	
	public void setElemIcon(Icon ic){
		LabelIcon = ic;
		ImageContainer.setIcon(LabelIcon);
		ImageContainer.setLocation(X,Y);
	}

}
