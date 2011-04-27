/* Importok */
import java.awt.*;
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
				ViewList.add(new viewElem(x, y, obj.GetID()));

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
		/* Vegigmegyunk a WireListen */

		/* Vegigmegyunk a WireListen */
		for (Wire wire:digiBoard.getMainComposit().getWireList()) {
			/* Minden Wire-nek egy bemeno objektuma van, ez az objectsIn.get(0) 
			 * Ehhez az elemhez kikeressuk a View objektumokat tarolo 
			 * listabol a hozza tartozo View-ot
			 */ 
			viewElem from = findInList(wire.objectsIn.get(0).ID);
			
			/* Ha ez NULL, azaz nem letezik hozza View objektum, akkor ez egy PIN
			 * Ezen esetbenaz elemunk a hozza tartozo Composit
			 */
			if (from == null) {
				PIN pinem = (PIN) ((wire.objectsIn.get(0)));
				from = findInList(pinem.ContainerComposit.ID);
			}
			for (DigitalObject obj:wire.objectsOut) {
				/* Kikeressuk a Viewok listajabol a kimeno elemunket */
				viewElem to = findInList(obj.ID);
				/* ha NULL, az azt jelenti, hogy egy Composit bemeno PIN-je... 
				 * ezen esetben az elemunk a PIN Compositja
				 */
				if (to == null) {
					PIN pinem = (PIN) ((obj));
					to = findInList(pinem.ContainerComposit.ID);
				}
				/* Beallitjuk a szint szurkere. ez az alapertelmezett. */
				g2.setPaint(Color.gray);
				/* ha a Wire-ben van aram, a szin fekete */
				if (wire.GetValue() > 0)
					g2.setPaint(Color.black);

				
				/* Ha a drot kiindulo pontja elorebb van mint a cel, azaz 
				 * alsobb hierarchiabol huzunk feljebb, normalis egyenest rajzolunk
				*/	
				if (from.X < to.X)
					g2.drawLine(from.X + 100, from.Y + 25, to.X, to.Y + 25);
				else {
					/* Kulonben visszacsatolas van, azt maskent kell kezelni */
					feeds++;
					g2.drawLine(to.X, to.Y + 25, to.X - 10, maxy + feeds * 5
							+ 60); // balrol bal le
					g2.drawLine(from.X + 100, from.Y + 25, from.X + 110, maxy
							+ feeds * 5 + 60);// jobbrol jobb lent
					g2.drawLine(to.X - 10, maxy + feeds * 5 + 60, from.X + 110,
							maxy + feeds * 5 + 60);// bal lent jobb lent
				}
			}
		}

		return true;
	}

	public void paintComponent(Graphics g)  {
		super.paintComponent(g);
		drawComponentList(g);
		drawWires(g);
	}


}
