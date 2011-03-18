import java.util.ArrayList;

/*
* Név: 			GENERATOR
* Típus: 		Class
* Interfacek:	iComponent
* Szülõk		DigitalObject-->Input
* 
*********** Leírás **********
* Speciális Input objektum, mely a kimenetét ciklikusan változtatja egy,
* felhasználó által szerkeszthetõ szekvencia alapján. 
* Az objektum nyilvántartja a szekvenciát, illetve az aktuális pozíciót
* a mintában. A generátor Reset utasításának hívásával a pozíció a 
* minta elejére állítható.

*/
public class GENERATOR extends Input{
	/*  ATTRIBÚTUMOK  */
	private static int GENERATORCounts;
	
	private int Frequency;
	/* Leírás: A generátor léptetésének a gyakoriságát tároljuk ebben. 
	 * Az itt megadott számú Step() hívás után fog csak lépni.
	*/
	
	private int Sequence;
	// LEírás: Ebben tároljuk a kimenetre kiküldendõ mintát. Értelmezése bináris.
	
	private int SequencePos;
	/* Leírás: Az attribútum tartja nyilván az aktuális pozíciót a szekvenciában. 
	 * A Reset() hívás 0 értékre állítja
	*/
	
	
	/*  KONSTRUKTOR  */
	public GENERATOR(int StartFrequency, int StartSequence, Wire WiresOut){
		wireIn = null;
		wireOut = new ArrayList<Wire>();
		
		ID = "GENERATOR" + GENERATORCounts++;
		Frequency = StartFrequency;
		Sequence = StartSequence;
		SequencePos = 0;
		
		Value = 0;

	}
	
	
	/*  METÓDUSOK  */
	public void Reset(){
	// Leírás: A SequencePos. értékét állítja alapértelmezettre, azaz a minta elejére
	};
	public void SetSequence(int NewSequence){
	// Leírás: a Frequency értékét állítja be, a paraméterben megadott értékre.
	};
	public void SetFrequency(int NewFrequency){
	// Leírás: a minta értékét állítja be, a paraméterben megadott értékre.
	};
	public void Count(){
	// Leírás: Kiszámolja egy DigitalObject értékét	
	};					
	public boolean Step(){
	/* Leírás: Feladata az adott elem értékének kiszámítása, 
	 * ill. annak eldöntése, hogy a DigitalObject stabil-e
	*/
		return true;
	};

}
