/* Importok */

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
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
     * Az egyes aramkori elemek kepeit ebbe hasheljuk.
     */
    public HashMap<String, Image> images = new HashMap<String, Image>();
    
    /**
     * Eltarolja, hogy a szintek kozott hany vonal van mar..
     * Ezzel elkerulheto a vonalak egymasra rajzolasa.
     * ha szintSzamlalo[3]=3, akkor a 3ik es 4ik szint kozti terben 3 wire megy mar keresztul.
     */
    private ArrayList<Integer> szintSzamlalo = new ArrayList<Integer>();
   
    /**
     Funkcioja hasonlo a szintSzamlalo-hoz, csak itt nem szintek kozott vizsgalodunk
     */
    private ArrayList<Integer> oszlopSzamlalo = new ArrayList<Integer>();
   
    
    /**
     * Lista melyben az egyes elemekhez tartozo {@link viewElem} tipusu
     * objektumokat taroljuk
     */
    public ArrayList<viewElem> ViewList = new ArrayList<viewElem>();

    
    /**
     * KONSTRUKTOR
     *
     * @param db
     *            Referenciakent megkapja a modellt
     */
    public boardView(DigitalBoard db) {
        digiBoard = db;        
        loadImages();
    }

    /**
     * Betolti az images mappabol a megfello kepeket es elmenti a HashMapbe
     */
    private void loadImages() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        MediaTracker mediaTracker = new MediaTracker(this);
        int id = 0;

        try {

            Image andGate = toolkit.getImage(getClass().getResource("images/and.png"));
            mediaTracker.addImage(andGate, id);
            id++;
            images.put("ANDGate", andGate);

            Image orGate = toolkit.getImage(getClass().getResource("images/or.png"));
            mediaTracker.addImage(orGate, id);
            id++;
            images.put("ORGate", orGate);

            Image generator = toolkit.getImage(getClass().getResource("images/GENERATOR.png"));
            mediaTracker.addImage(generator, id);
            id++;
            images.put("GENERATOR", generator);

            Image inverter = toolkit.getImage(getClass().getResource("images/inverter.png"));
            mediaTracker.addImage(inverter, id);
            id++;
            images.put("INVERTER", inverter);

            Image ledOff = toolkit.getImage(getClass().getResource("images/ledoff.png"));
            mediaTracker.addImage(ledOff, id);
            id++;
            images.put("LED", ledOff);

            Image ledOn = toolkit.getImage(getClass().getResource("images/ledon.png"));
            mediaTracker.addImage(ledOn, id);
            id++;
            images.put("led", ledOn);

            Image oscilloscope = toolkit.getImage(getClass().getResource("images/OSCILLOSCOPE.png"));
            mediaTracker.addImage(oscilloscope, id);
            id++;
            images.put("Oscilloscope", oscilloscope);

            Image swOff = toolkit.getImage(getClass().getResource("images/swoff.png"));
            mediaTracker.addImage(swOff, id);
            id++;
            images.put("SWITCH", swOff);

            Image swOn = toolkit.getImage(getClass().getResource("images/swon.png"));
            mediaTracker.addImage(swOn, id);
            id++;
            images.put("switch", swOn);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        try {
            for (int i = 0; i < id; i++) {
                mediaTracker.waitForID(i);
            }
        } catch (InterruptedException ie) {
            System.err.println(ie);
            System.exit(-1);
        }

    }

    /**
     * Megkeres egy elemet a viewElem-ekbol epitett listaban
     * @param ElemID a keresett elem/view ID-je
     * @return a keresett view ha letezik, kulonben nulla
     */
    private viewElem findInList(String ElemID) {
        for (int i = 0; i < ViewList.size(); i++) {
            if (ElemID.equals(ViewList.get(i).ID)) {
                return ViewList.get(i);
            }
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

		ViewList = new ArrayList<viewElem>();
		/*
		 * Egy kettos forach-csel bejarjuk a ComponentListet. (azert foreach-re
		 * irtam at a for(int i...)-t mert igy kisebb)
		 */
		int szint = 0, hanyadikelem = 0; // Segedvaltozok a koordinatak
		// kiszamolasahz.
		if (digiBoard.getMainComposit() == null) {
			return false;
		}
		for (List<DigitalObject> SubList : digiBoard.getMainComposit()
				.getComponentList()) {
			for (DigitalObject obj : SubList) {

				/*
				 * Koordinatak kiszamolasa. Fugg, hogy hanyadik
				 * hierarchiaszinten van (szint) es azon belul is hanyadik
				 * elem(hanyadikelem), hogy ne csusszanak ossze
				 */
				int x = szint * 150 + 200;
				int y = hanyadikelem * 100 + 200;

				/*
				 * Most letrehozzuk a ComponentList adott elemehez tartozo View
				 * objektumot, melyben eltaroljuk az elem kiszamitott X, Y
				 * koordinatajat tovabba az ID-jet , majd ezt az objektumot
				 * hozzaadjuk a listahoz mely ezeket tarolja
				 */
				viewElem current = new viewElem(x, y, szint, obj.GetID());
				ViewList.add(current);


				/* Kiirjuk hozza az objektum nevet is. */
				if (obj.ID.contains("Composit")) {
					g2.draw(new Rectangle(x, y, 100, 50));
					g2.drawString(obj.GetName(), x + 15, y + 20);
				} else {
					g2.drawString(obj.GetName(), x + 15, y - 5);
				}

				// betoltjuk az objektumokhoz tartozo kepeket
				Image img = null;
				img = images.get(obj.GetType());

				if (obj.GetType().equals("LED")
						&& obj.wireIn.get(0).GetValue() > 0) {
					img = images.get("led");
				} else if (obj.GetType().equals("SWITCH")
						&& ((SWITCH) (obj)).Value > 0) {
					img = images.get("switch");
				}

				// g2.drawImage(img, x, y, this);

				if (obj.GetType().equals("Composit")) {
					Composit comp = (Composit) (obj);
					for (int i = 0; i < comp.pins_in.size(); i++) {
						current.addPinIn(new viewElem(x - 5, 5 + y + 10 * i,
								szint, comp.pins_in.get(i).ID));
						g2.draw(new Rectangle(x - 5, 5 + y + 10 * i - 2, 5, 5));
					}
					for (int i = 0; i < comp.pins_out.size(); i++) {
						current.addPinOut(new viewElem(100 + x + 5, 5 + y + 10
								* i + 2, szint, comp.pins_out.get(i).ID));
						g2.draw(new Rectangle(100 + x, 5 + y + 10 * i, 5, 5));
					}
				} else {
					if (obj.wireIn != null) {
						if (obj.wireIn.size() == 1) {
							current.addPinIn(new viewElem(x, y + 25, szint,
									current.ID + "#graphic_pin"));
						}
						if (obj.wireIn.size() == 2) {
							current.addPinIn(new viewElem(x, y + 15, szint,
									current.ID + "#graphic_pin"));
							current.addPinIn(new viewElem(x, y + 35, szint,
									current.ID + "#graphic_pin"));
						}
						if (obj.wireIn.size() > 2) {
							for (int i = 0; i < obj.wireIn.size(); i++) {
								current.addPinIn(new viewElem(x,
										y + 10 * i + 5, szint, current.ID
												+ "#graphic_pin"));
							}
						}
					}

					if (obj.wireOut != null) {
						if (obj.wireOut.size() == 1) {
							current.addPinOut(new viewElem(x + 100, y + 25,
									szint, current.ID + "#graphic_pin"));
						}
						if (obj.wireOut.size() == 2) {
							current.addPinOut(new viewElem(x + 100, y + 15,
									szint, current.ID + "#graphic_pin"));
							current.addPinOut(new viewElem(x + 100, y + 35,
									szint, current.ID + "#graphic_pin"));
						}
						if (obj.wireOut.size() > 2) {
							for (int i = 0; i < obj.wireOut.size(); i++) {
								current.addPinOut(new viewElem(x + 100, y + 10
										* i + 5, szint, current.ID
										+ "#graphic_pin"));
							}
						}
					}

				
					
					current.ImageContainer.setLocation(x, y);
					current.ImageContainer.refreshImage(img);
					current.ImageContainer.addMouseListener(
							this.getMouseListeners()[0]);					
					this.add(current.ImageContainer);
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
			hanyadikelem = 0;
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
        oszlopSzamlalo=new ArrayList<Integer>();
        szintSzamlalo=new ArrayList<Integer>();
    	for(int i=0; i<10;i++){
    		oszlopSzamlalo.add(1);
    		szintSzamlalo.add(1);
    	}
        
        
    	/* Grafikai inicializalas */
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.black);
        if (digiBoard.getMainComposit() == null) {
            return false;
        }

        for (Wire wire : digiBoard.getMainComposit().getWireList()) {
            for (DigitalObject objOut : wire.objectsOut) {
                viewElem from = findInList(wire.objectsIn.get(0).GetID());
                viewElem to = findInList(objOut.GetID());
                if (to == null) {
                    PIN pin = (PIN) objOut;
                    to = findInList(pin.ContainerComposit.ID);
                }
                if (from == null) {
                    PIN pin = (PIN) wire.objectsIn.get(0);
                    from = findInList(pin.ContainerComposit.ID);
                }
                paintAWire(g2, from.getNextPinOut(), to.getNextPinIn(), wire.GetValue());
            }
        }
        return true;
    }

    private void paintAWire(Graphics2D g, viewElem from, viewElem to, int value) {
    	int hanyadikFrom=(from.Y-200)/100; //hanyadik a szinten a from
    	int hanyadikTo=(to.Y-200)/100; //hanyadik a szinten a to
        int x=from.X + 5+ oszlopSzamlalo.get(hanyadikFrom)*3;
        int y=from.Y;
        int x1= to.X - 5 - oszlopSzamlalo.get(hanyadikTo)*3;
    	int y1= from.szint * 100 + 175 + szintSzamlalo.get(from.szint)*3;
    	int y2=to.Y;

        
        /**
         *    from   x        x1      
         * y  XX------   XX        
         *           |     
         * y1        ---------
         *                   |
         *               XX  |
         *                   |       to
         * y2                -------XX
         *                   
         */
        
        
        //from kis basza
        g.setPaint(Color.black);
        g.drawLine(from.X, y, from.X+5, y);

        g.setPaint(Color.gray);
        
        /* ha a Wire-ben van aram, a szin fekete */
        if (value > 0) {
            g.setPaint(Color.black);
        }

        g.drawLine(from.X+5, y, x, y);
        
        //ha szomsz�dok, sima vonal
        if (from.szint == to.szint - 1) {
            g.drawLine(x, y, x1, y);
            g.drawLine(x1, y, x1, y2);
            oszlopSzamlalo.set(hanyadikTo, oszlopSzamlalo.get(hanyadikTo)+1);
            //g.drawLine(x, y, x1, y2);
        } else {
        	        	
        	g.drawLine(x, y, x, y1);
            g.drawLine(x, y1, x1, y1);
            g.drawLine(x1, y1, x1, y2);
        
            oszlopSzamlalo.set(hanyadikFrom, oszlopSzamlalo.get(hanyadikFrom)+1);
            oszlopSzamlalo.set(hanyadikTo, oszlopSzamlalo.get(hanyadikTo)+1);
            szintSzamlalo.set(from.szint, szintSzamlalo.get(from.szint)+1);
            
        }
        


        g.drawLine(x1, y2, to.X, to.Y);
        //to kis basza
        g.setPaint(Color.black);
        g.drawLine(to.X-5, to.Y, to.X, to.Y);
    }

    public void paintComponent(Graphics g) {
    	this.removeAll();
        super.paintComponent(g);       
        drawComponentList(g);
        drawWires(g);        	
        
       // RepaintManager.setCurrentManager(new RepaintManager());
      	
    }
}
