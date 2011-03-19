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
	private int FrequencyCounter;
	/*Leírás:
	 * Egy segédszámláló, mely nyilvántartja, hogy még mennyi Count maradt hátra
	 */
	
	/*  KONSTRUKTOR  */
	public GENERATOR(int StartFrequency, int StartSequence, Wire WiresOut){
		wireIn = null;
		wireOut = new ArrayList<Wire>();
		
		ID = "GENERATOR" + GENERATORCounts++;
		Frequency = StartFrequency;
		FrequencyCounter = StartFrequency;
		Sequence = StartSequence;
		SequencePos = 0;
		
		Value = 0;

	}
	
	
	/*  METÓDUSOK  */
	public void Reset(){
	// Leírás: A SequencePos. értékét állítja alapértelmezettre, azaz a minta elejére
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"","");	/* TEST */
		//SequencePos = 0;				// Pozíciót alapra
		//Value = Integer.toBinaryString(Sequence).charAt(SequencePos++); //Binárissá alakítjuk a számot és vesszük az MSB bitet
		stack.PrintTail(ID,"","");		/* TEST */	
	};
	public void SetSequence(int NewSequence){
	// Leírás: a Frequency értékét állítja be, a paraméterben megadott értékre.
		_TEST stack = new _TEST();					/* TEST */
		stack.PrintHeader(ID,"NewSequence:int","");	/* TEST */
		//Sequence = NewSequence;					// Beállítjuk a szekvenciát
		//SequencePos = 0;							// Pozíciót alapra
		//Value = Integer.toBinaryString(Sequence).charAt(SequencePos++); //Binárissá alakítjuk a számot és vesszük az MSB bitet
		stack.PrintTail(ID,"","");					/* TEST */	
	};
	public void SetFrequency(int NewFrequency){
	// Leírás: a minta értékét állítja be, a paraméterben megadott értékre.
		_TEST stack = new _TEST();						/* TEST */
		stack.PrintHeader(ID,"NewFrequency:int","");	/* TEST */
		//Frequency = NewFrequency;						// Beállítjuk a frekvenciát
		//StartFrequency = NewFrequency					// Hány count maradt még hátra
		stack.PrintTail(ID,"","");						/* TEST */
	};
	public int Count(){
	// Leírás: Kiszámolja egy DigitalObject értékét	
		_TEST stack = new _TEST();		
		stack.PrintHeader(ID,"","");
	
		/* Az ÖSSZES kimenetre kiadjuk a kiszámított eredményt. Skeletonnál csak egyre */
		/*FrequencyCounter--;					// Csökkentjük a számlálót
		if(!FrequencyCounter){				// HA megfellõ számú count eltelt már
			for(Wire OutPut:wireOut){
				OutPut.SetValue(Value);					//Kiadjuk a kimenetre az aktuális értéker
				if(SequencePos >= Integer.toBinaryString(Sequence).length()){	// a szekvenciában elõre megyünk.. már ha lehet
					SequencePos = 0;				
				}else{
					SequencePos++;
				}			
				Value = Integer.toBinaryString(Sequence).charAt(SequencePos++);	// Kiszámoljuk az új értéket
			}
			FrequencyCounter = Frequency;	// Újra az elejérõl számolunk
		}*/
		Wire wire_out = new Wire();
		wire_out.SetValue(0);
		
		stack.PrintTail(ID,"","");
		return Value;	
	};					
	public boolean Step(){
	/* Leírás: Feladata az adott elem értékének kiszámítása, 
	 * ill. annak eldöntése, hogy a DigitalObject stabil-e
	*/
		_TEST stack = new _TEST();				/* TEST */
		stack.PrintHeader(ID,"","");			/* TEST */		
		Count();								// MEghívja a Count metódust
		stack.PrintTail(ID,"","true:boolean");	/* TEST */	
		return true;							// A GENERATOR mindig igazzal tér vissza
	};

}
