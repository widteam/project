package bhdlparser;
import java.util.ArrayList;
import java.util.List;

public class Composit extends DigitalObject{
	/*	ATTRIB�TUMOK	*/

	//**************************************************
	//Ezt Gyuri m�dos�totta CSAK A TESZTEL�SHEZ, VISSZACSIN�LJUK
	//**************************************************
	private ArrayList<DigitalObject> ComponentList;
	/* Le�r�s: Ez az attrib�tum t�rolja az �sszes kaput, kimenetet, bemenetet
	 * hierarchikus sorrendben
	 * Ez nem m�s, mint egy list�b�l szervezett t�mb. A t�mb indexe
	 * azonos�tja a hierarchia szintet 
	 * (0-Forr�sok, 1-a forr�sokhoz csatlakoz� elemek, stb)
	 * az egyes szinteken pedig egy lista van az elemekr�l
	*/		
	@SuppressWarnings("unused")
	private List<Wire> WireList;
	// Le�r�s: Egyszer� lista a Wire objektumokb�l

	/*  KONSTRUKTOR  */ 
	public Composit(String  strCompositName){				
		ComponentList = new ArrayList<DigitalObject>();
		WireList = new ArrayList<Wire>();
		ID =  strCompositName;
	}
	/*	MET�DUSOK	*/
	public void SetFrequency(int Frequency, String ElementID){
		// Le�r�s: A param�terben megadott azonos�t�j� GENERATOR objektum frekvenci�j�t m�dos�tja
			GENERATOR tmp;
			tmp = (GENERATOR) GetElementByID(ElementID);
			tmp.SetFrequency(Frequency);
		};
		public void SetSequence(int Sequence, String ElementID){
		// Le�r�s: A param�terben megadott azonos�t�j� GENERATOR objektum szekvenci�j�t m�dos�tja
			GENERATOR GEN_to_setsequence;	/* Tempor�lis v�ltoz� */
			GEN_to_setsequence = (GENERATOR) GetElementByID(ElementID);	/* GetElemetByIDvel megkapjuk, az objektumot	*/		
			GEN_to_setsequence.SetSequence(Sequence); 				 /* az gener�tor objektum SetSequence(...) met�dus�t megh�vjuk */
		
		};	
		public void Toggle(String ElementID){
		/* Le�r�s: A param�terben megadott azonos�t�j� SWITCH objektum �rt�k�t az ellenkez�re 
		 * �ll�tja az�ltal, hogy megh�vja az objektum hasonl� nev� param�ter�t
		*/
			SWITCH SWITCH_to_toggle;								/* Tempor�lis v�ltoz� */
			SWITCH_to_toggle = (SWITCH) GetElementByID(ElementID);	/* GetElemetByIDvel megkapjuk, majd  egyet	*/		
			SWITCH_to_toggle.Toggle();								/* az switch objektum Toggle() met�dus�t megh�vjuk */

		};	
	@Override
	public void AddToFeedbacks(DigitalObject feedback){
		// Le�r�s: Hozz�adja a param�terk�nt kapott DigitalObjectet a Feedbacks t�mbj�hez.
			if(Feedbacks != null)
				Feedbacks.add(feedback);		// Hozz�adjuk a feedbackshez	
		};	
	public DigitalObject GetElementByID(String ElementID){
	// Le�r�s: Megkeres egy adott elemet a 	ComponentList illetve a WireList list�kban
		if(ComponentList != null && !ComponentList.isEmpty()){
				for(DigitalObject o : ComponentList){
					if(o.GetID().equalsIgnoreCase(ElementID))
						return o;
				}
			}
		return null;		
	}

	/* 
	 * Itt van egy kisebb probl�ma. 
	 * K�v�lr�l kell stepet biztos�tani, de bl�lr�l meg step components van.
	 * Step h�vjon stepcomponentset �s mondja meg hogy a r�szobjektumok stabilak-e? 
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
		// Le�r�s: Megh�vja az �sszes iComponent interf�szt megval�s�t� objektum Step() met�dus�t.

		/* Elvileg m�r fel van �p�lve a hierarchia �gy nekem el�g megkapnom a ComponentListet */
		DigitalObject obj;
		for(DigitalObject o : ComponentList){
			obj = (DigitalObject) o;
			obj.Step();
		}
	}
	
	//**************************************************
	//Ezt Gyuri m�dos�totta
	//**************************************************
	public void AddToComponentList(DigitalObject obj){
		ComponentList.add(obj);
	}
	
	public void AddToWireList(Wire wire){
		WireList.add(wire);
	}
	public Wire GetWireByID(String WireID){
		if(WireList != null && !WireList.isEmpty()){
			for(Wire w : WireList){
				if(w.GetID().equalsIgnoreCase(WireID))
					return w;
			}
		}
	return null;
	}
};