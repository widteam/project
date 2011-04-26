import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.*;
import javax.swing.plaf.ViewportUI;


public class boardView extends JPanel {
	/**	
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DigitalBoard digiBoard;
	
	public boardView(DigitalBoard db){
		digiBoard=db;
	}
	
	private int feeds=0;
	private int maxy=0;
	
	public ArrayList<viewElem> list=new ArrayList<viewElem>();
	
	public viewElem findInList(String ida){
		for(int i=0; i<list.size();i++){
			if(ida.equals(list.get(i).id)) return list.get(i);
		}
		return null;
	}
	
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			        RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setPaint(Color.black);

		for(int i=0; i<digiBoard.MainComposit.ComponentList.size(); i++){
			for(int j=0; j<digiBoard.MainComposit.ComponentList.get(i).size(); j++){
				int x=i*150+200;
				int y=j*100+200;
				System.out.println(x+" "+y);
				list.add(new viewElem(x, y, digiBoard.MainComposit.ComponentList.get(i).get(j).GetID()));
				g2.draw(new Rectangle(x, y, 100, 50));
				g2.drawString(
						digiBoard.MainComposit.ComponentList.get(i).get(j).GetName(),
						x+5, y+20);
				if(y>maxy) maxy=y;
			}
		}
		
		for(int i=0; i<digiBoard.MainComposit.WireList.size();i++){
			viewElem from=findInList(digiBoard.MainComposit.WireList.get(i).objectsIn.get(0).ID);
			if(from==null){
				PIN pinem=(PIN)((digiBoard.MainComposit.WireList.get(i).objectsIn.get(0)));
				from=findInList(pinem.ContainerComposit.ID);
			}
			for(int j=0; j<digiBoard.MainComposit.WireList.get(i).objectsOut.size();j++){
				viewElem to=findInList(digiBoard.MainComposit.WireList.get(i).objectsOut.get(j).ID);
				if(to==null){
					PIN pinem=(PIN)((digiBoard.MainComposit.WireList.get(i).objectsOut.get(j)));
					to=findInList(pinem.ContainerComposit.ID);
				}
				
				g2.setPaint(Color.gray);
				if(digiBoard.MainComposit.WireList.get(i).GetValue()>0) g2.setPaint(Color.black);
				
				if(from.x<to.x)
					g2.drawLine(from.x+100, from.y+25, to.x, to.y+25);
				else {
					feeds++;
					g2.drawLine(to.x, to.y+25, to.x-10, maxy+feeds*5+60); //balról bal le
					g2.drawLine(from.x+100, from.y+25, from.x+110, maxy+feeds*5+60);//jobbról jobb lent
					g2.drawLine( to.x-10, maxy+feeds*5+60, from.x+110, maxy+feeds*5+60);//bal lent jobb lent
				}
			}
		}
		
	  }
}
