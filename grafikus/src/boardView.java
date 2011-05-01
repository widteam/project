/* Importok */
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;


/**
 * Osztaly mely a DigitalBoard megjelenitesere szolgal. Lekerdezi a modell, a
 * DigitalBoard getMainComposit()-jenek hierarchiajat, majd a hierarchizalt
 * elemeket kirajzolja szintenkent, ezt kovetoen a kapcsolatokat is megjeleniti
 * az egyes elemek kozott.
 */
public class boardView extends JPanel {

	/** UID, mert kell... */
	private static final long serialVersionUID = 1L;

	/** Privat referencia a modellre */
	private DigitalBoard digiBoard;

	/**
	 * KONSTRUKTOR
	 * 
	 * @param db
	 *            Referenciakent megkapja a modellt
	 */
	public boardView(DigitalBoard db) {
		digiBoard = db;
	}

	private int feeds = 0;
	/**
	 * Egy Y koordinata, mely megadja a legmelyebben fekvo elem Y koordinatajat.
	 * ez ahhoz kell, hogy a visszacsatolas ezen Y alatt fusson
	 */

	/**
	 * Eltárolja, hogy a szintek között hány vonal van már.. 
	 * Ezzel elkerülhetõ a vonalak egymásra rajzolása.
	 * ha szintSzamlalo[3]=3, akkor a 3ik és 4ik szint közti térben 3 wire megy már keresztül.
	 */
	private ArrayList<Integer> szintSzamlalo = new ArrayList<Integer>();

	private int maxy = 0;

	/**
	 * Lista melyben az egyes elemekhez tartozo {@link viewElem} tipusu
	 * objektumokat taroljuk
	 */
	public ArrayList<viewElem> ViewList = new ArrayList<viewElem>();

	public viewElem findInList(String ElemID) {
		for (int i = 0; i < ViewList.size(); i++) {
			if (ElemID.equals(ViewList.get(i).ID))
				return ViewList.get(i);
		}
		return null;
	}

	/**
	 * Fuggveny ami kirajzolja a ComponentListben talalhato elemeket.
	 */
	private boolean drawComponentList(Graphics g) {
		/* Grafikai inicializalas */
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(Color.black);

		maxy = 0;
		/*
		 * Egy kettos forach-csel bejarjuk a ComponentListet. (azert foreach-re
		 * irtam at a for(int i...)-t mert igy kisebb)
		 */
		int szint = 0, hanyadikelem = 0; // Segedvaltozok a koordinatak
											// kiszamolasahz.
		if (digiBoard.getMainComposit()==null) return false;
		for (List<DigitalObject> SubList : digiBoard.getMainComposit()
				.getComponentList()) {
			for (DigitalObject obj : SubList) {

				/*
				 * Koordinatak kiszamolasa. Fugg, hogy hanyadik
				 * hierarchiaszinten van (szint) es azon belul is hanyadik
				 * elem(hanyadikelem), hogy ne csusszanak ossze
				 */
				int x = szint  * 150 + 200;
				int y = hanyadikelem  * 100 + 200;

				/* Egy kis logolas, debug */
				Logger.Log(Logger.log_type.DEBUG, x + " " + y);

				/*
				 * Most letrehozzuk a ComponentList adott elemehez tartozo View
				 * objektumot, melyben eltaroljuk az elem kiszamitott X, Y
				 * koordinatajat tovabba az ID-jet , majd ezt az objektumot
				 * hozzaadjuk a listahoz mely ezeket tarolja
				 */

				
				viewElem current=new viewElem(x, y, szint, obj.GetID());
				ViewList.add(current);

				/*
				 * most gyorsan kirjzoljuk a gyonyoru mintat. Teglalap. Ezt nem
				 * artana csicsazni .)
				 */
				g2.draw(new Rectangle(x, y, 100, 50));

				/* Kiirjuk hozza az objektum nevet is. */
				g2.drawString(obj.GetName(), x + 5, y + 20);

				/*
				 * Ha ez a legmelyebben fekvo elem, akkor modositani kell a maxy
				 * koordinatat a feedback kirajzolasahoz.
				 */
				if (y > maxy)
					maxy = y;

				
				if(obj.GetType().equals("Composit")){
					Composit comp=(Composit)(obj);
					for(int i=0; i<comp.pins_in.size();i++){
						current.addPinIn(new viewElem(x-5, 5+y+10*i, szint, comp.pins_in.get(i).ID));
						g2.draw(new Rectangle(x-5, 5+y+10*i-2, 5, 5));
					}
					for(int i=0; i<comp.pins_out.size();i++){
						current.addPinOut(new viewElem(100+x+5, 5+y+10*i+2, szint, comp.pins_out.get(i).ID));
						g2.draw(new Rectangle(100+x, 5+y+10*i, 5, 5));
					}
				} else {
					if(obj.wireIn!=null){
						if(obj.wireIn.size()==1){
							current.addPinIn(new viewElem(x, y+25, szint, current.ID+"#graphic_pin"));
						}
						if(obj.wireIn.size()==2){
							current.addPinIn(new viewElem(x, y+15, szint, current.ID+"#graphic_pin"));
							current.addPinIn(new viewElem(x, y+35, szint, current.ID+"#graphic_pin"));
						}
						if(obj.wireIn.size()>2){
							for(int i=0; i<obj.wireIn.size();i++){
								current.addPinIn(new viewElem(x, y+10*i+5, szint, current.ID+"#graphic_pin"));
							}
						}
						
					}
						
					if(obj.wireOut!=null){
						if(obj.wireOut.size()==1){
							current.addPinOut(new viewElem(x+100, y+25, szint, current.ID+"#graphic_pin"));
						}
						if(obj.wireOut.size()==2){
							current.addPinOut(new viewElem(x+100, y+15, szint, current.ID+"#graphic_pin"));
							current.addPinOut(new viewElem(x+100, y+35, szint, current.ID+"#graphic_pin"));
						}
						if(obj.wireOut.size()>2){
							for(int i=0; i<obj.wireOut.size();i++){
								current.addPinOut(new viewElem(x+100, y+10*i+5, szint, current.ID+"#graphic_pin"));
							}
						}
					}
					
					
					
				}
				
				
				/*
				 * Noveljuk a segedvaltozot ami megmondja hogy adott sinten
				 * hanyadik elemnel jarunk
				 */
				hanyadikelem++;
			}
			/*
			 * es noveljuk azt a segedvaltozot is ami mgmondja, hanyadik szinten
			 * jarunk
			 */
			hanyadikelem=0;
			szintSzamlalo.add(0);
			szint++;
		}
		return true;
	}

	/**
	 * Fuggveny a drotok kirajzolasara
	 * @param g a gafikai objektum ahova rajzolunk majd
	 */
	private boolean drawWires(Graphics g) {
		/* Grafikai inicializalas */
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(Color.black);
		if (digiBoard.getMainComposit()==null) return false;

		feeds=0;
		for (Wire wire:digiBoard.getMainComposit().getWireList()) {
			for(DigitalObject  objOut:wire.objectsOut){
				viewElem from=findInList(wire.objectsIn.get(0).GetID());
				viewElem to=findInList(objOut.GetID());
				if(to==null) {
					PIN pin=(PIN)objOut;
					to=findInList(pin.ContainerComposit.ID);
				}
				if(from==null){
					PIN pin=(PIN)wire.objectsIn.get(0);
					from=findInList(pin.ContainerComposit.ID);
				}
				paintAWire(g2, from.getNextPinOut(), to.getNextPinIn());
			}
		}
//		/* Vegigmegyunk a WireListen */
//		for (Wire wire:digiBoard.getMainComposit().getWireList()) {
//			/* Minden Wire-nek egy bemeno objektuma van, ez az objectsIn.get(0) 
//			 * Ehhez az elemhez kikeressuk a View objektumokat tarolo 
//			 * listabol a hozza tartozo View-ot
//			 */ 
//			viewElem from = findInList(wire.objectsIn.get(0).ID);
//			viewElem to = findInList(wire.objectsOut.get(0).ID);
//			if(wire.objectsIn.get(0).ID.contains("PIN")){
//				from=
//			}
//			
//			if(wire.objectsIn.get(0).ID.contains("PIN")){
//				to=to;
//			} else if (wire.objectsOut.get(0).ID.contains("PIN")){
//				
//			} else paintAWire(g2, from.pins_out.get(0), to.pins_in.get(0));


//				/* Kikeressuk a Viewok listajabol a kimeno elemunket */
//				viewElem to = findInList(obj.ID);
//				/* ha NULL, az azt jelenti, hogy egy Composit bemeno PIN-je... 
//				 * ezen esetben az elemunk a PIN Compositja
//				 */
//				if (to == null) {
//					PIN pinem = (PIN) ((obj));
//					to = findInList(pinem.ContainerComposit.ID);
//				}
//				/* Beallitjuk a szint szurkere. ez az alapertelmezett. */
//				g2.setPaint(Color.gray);
//				/* ha a Wire-ben van aram, a szin fekete */
//				if (wire.GetValue() > 0)
//					g2.setPaint(Color.black);
//
//				
//				/* Ha a drot kiindulo pontja elorebb van mint a cel, azaz 
//				 * alsobb hierarchiabol huzunk feljebb, normalis egyenest rajzolunk
//				*/	
//				if (from.X < to.X)
//					g2.drawLine(from.X + 100, from.Y + 25, to.X, to.Y + 25);
//				else {
//					/* Kulonben visszacsatolas van, azt maskent kell kezelni */
//					feeds++;
//					g2.drawLine(to.X, to.Y + 25, to.X - 10, maxy + feeds * 5
//							+ 60); // balrol bal le
//					g2.drawLine(from.X + 100, from.Y + 25, from.X + 110, maxy
//							+ feeds * 5 + 60);// jobbrol jobb lent
//					g2.drawLine(to.X - 10, maxy + feeds * 5 + 60, from.X + 110,
//							maxy + feeds * 5 + 60);// bal lent jobb lent
//				}
			
		
		
		return true;
	}
	
	private void paintAWire(Graphics g, viewElem from, viewElem to ){
		int x;
		int y;
		x=from.X+10;
		y=from.Y;
		g.drawLine(from.X, y, x, y);
		//TODO
		g.drawLine(x,y, to.X-10, to.Y);
		
		g.drawLine(to.X-10, to.Y, to.X, to.Y);
		
		
	}

	public void paintComponent(Graphics g)  {
		super.paintComponent(g);
		drawComponentList(g);
		drawWires(g);
	}


}
