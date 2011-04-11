import java.util.ArrayList;
import java.util.List;

public class Composit extends DigitalObject{
	/*	ATTRIBÚTUMOK	*/

	private ArrayList< List<DigitalObject> > ComponentList;
	/* Leírás: Ez az attribútum tárolja az összes kaput, kimenetet, bemenetet
	 * hierarchikus sorrendben
	 * Ez nem más, mint egy listából szervezett tömb. A tömb indexe
	 * azonosítja a hierarchia szintet 
	 * (0-Források, 1-a forrásokhoz csatlakozó elemek, stb)
	 * az egyes szinteken pedig egy lista van az elemekrõl
	*/		
	@SuppressWarnings("unused")
	private List<Wire> WireList;
	// Leírás: Egyszerû lista a Wire objektumokból

	/*  KONSTRUKTOR  */ 
	public Composit(String  strCompositName){				
		ComponentList = new ArrayList<List<DigitalObject>>();
		WireList = new ArrayList<Wire>();
		ID =  strCompositName;
	}
	/*	METÓDUSOK	*/
	public void SetFrequency(int Frequency, String ElementID){
		// Leírás: A paraméterben megadott azonosítójú GENERATOR objektum frekvenciáját módosítja
			GENERATOR tmp;
			tmp = (GENERATOR) GetElementByID(ElementID);
			tmp.SetFrequency(Frequency);
		};
		public void SetSequence(int Sequence, String ElementID){
		// Leírás: A paraméterben megadott azonosítójú GENERATOR objektum szekvenciáját módosítja
			GENERATOR GEN_to_setsequence;	/* Temporális változó */
			GEN_to_setsequence = (GENERATOR) GetElementByID(ElementID);	/* GetElemetByIDvel megkapjuk, az objektumot	*/		
			GEN_to_setsequence.SetSequence(Sequence); 				 /* az generátor objektum SetSequence(...) metódusát meghívjuk */
		
		};	
		public void Toggle(String ElementID){
		/* Leírás: A paraméterben megadott azonosítójú SWITCH objektum értékét az ellenkezõre 
		 * állítja azáltal, hogy meghívja az objektum hasonló nevû paraméterét
		*/
			SWITCH SWITCH_to_toggle;								/* Temporális változó */
			SWITCH_to_toggle = (SWITCH) GetElementByID(ElementID);	/* GetElemetByIDvel megkapjuk, majd  egyet	*/		
			SWITCH_to_toggle.Toggle();								/* az switch objektum Toggle() metódusát meghívjuk */

		};	
	@Override
	public void AddToFeedbacks(DigitalObject feedback){
		// Leírás: Hozzáadja a paraméterként kapott DigitalObjectet a Feedbacks tömbjéhez.
			if(Feedbacks != null)
				Feedbacks.add(feedback);		// Hozzáadjuk a feedbackshez	
		};	
	public DigitalObject GetElementByID(String ElementID){
	// Leírás: Megkeres egy adott elemet a 	ComponentList illetve a WireList listákban
		if(ComponentList != null && !ComponentList.isEmpty()){
			for(List<DigitalObject> sublist: ComponentList){
				for(DigitalObject o : sublist){
					if(o.ID == ElementID)
						return (DigitalObject) o;
				}
			}
		}
		return null;		
	}

	/* 
	 * Itt van egy kisebb probléma. 
	 * Kívülrõl kell stepet biztosítani, de blülrõl meg step components van.
	 * Step hívjon stepcomponentset és mondja meg hogy a részobjektumok stabilak-e? 
	 * 
	 * @see DigitalObject#Step()
	 */	
	@Override
	public boolean Step() {
		// TODO Auto-generated method stub
		return false;
	};

	public int Count() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void StepComponents(){
		// Leírás: Meghívja az összes iComponent interfészt megvalósító objektum Step() metódusát.

			/* Elvileg már fel van épülve a hierarchia így nekem elég megkapnom a ComponentListet */
			DigitalObject obj;
			for(List<DigitalObject> sublist: ComponentList){
				for(DigitalObject o : sublist){
					obj = (DigitalObject) o;
					obj.Step();
				}
			}
		};
}