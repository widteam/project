import java.util.ArrayList;

/*
* Név: 			GENERATOR
* Típus: 		Class
* Interfacek:	---
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
	public GENERATOR(String strCompositName, int StartFrequency, int StartSequence, Wire WiresOut){
		final String strIDDelimiter = "#";
		String strIDNumber  = String.valueOf(GENERATORCounts++);
		final String strIDName  = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strIDName + strIDDelimiter + strIDNumber;
			
		wireIn = null;
		wireOut = new ArrayList<Wire>();
		Frequency = StartFrequency;
		FrequencyCounter = StartFrequency;
		Sequence = StartSequence;
		SequencePos = 0;
		
		Value = 0;

	}
	
	
	/*  METÓDUSOK  */
	public void Reset(){
	// Leírás: A SequencePos. értékét állítja alapértelmezettre, azaz a minta elejére
		SequencePos = 0;				// Pozíciót alapra
		Value = Integer.toBinaryString(Sequence).charAt(SequencePos++); //Binárissá alakítjuk a számot és vesszük az MSB bitet
	};
	public void SetSequence(int NewSequence){
	// Leírás: a Frequency értékét állítja be, a paraméterben megadott értékre.
		Sequence = NewSequence;					// Beállítjuk a szekvenciát
		SequencePos = 0;							// Pozíciót alapra
		Value = Integer.toBinaryString(Sequence).charAt(SequencePos++); //Binárissá alakítjuk a számot és vesszük az MSB bitet
	};
	public void SetFrequency(int NewFrequency){
	// Leírás: a minta értékét állítja be, a paraméterben megadott értékre.
		Frequency = NewFrequency;						// Beállítjuk a frekvenciát
		FrequencyCounter = NewFrequency;					// Hány count maradt még hátra
	};
	public int Count(){
	// Leírás: Kiszámolja egy DigitalObject értékét	
		int Result=0;
		/* Az ÖSSZES kimenetre kiadjuk a kiszámított eredményt. Skeletonnál csak egyre */
		FrequencyCounter--;					// Csökkentjük a számlálót
		if(FrequencyCounter==0){				// HA megfellõ számú count eltelt már
			if(wireOut == null || wireOut.isEmpty()){	// ha nincs csatlakoztatva semmihez, hibát dob
				// throw InputNotConnectedWarning
			}else{
				for(Wire OutPut:wireOut){
					OutPut.SetValue(Value);					//Kiadjuk a kimenetre az aktuális értéker
					if(SequencePos >= Integer.toBinaryString(Sequence).length()){	// a szekvenciában elõre megyünk.. már ha lehet
						SequencePos = 0;				
					}else{
						SequencePos++;
					}						
				}//end for
			}
			Value = Integer.toBinaryString(Sequence).charAt(SequencePos++);	// Kiszámoljuk az új értéket
			FrequencyCounter = Frequency;	// Újra az elejérõl számolunk
		}else{
			for(Wire OutPut:wireOut){
				OutPut.SetValue(Result);
			}
		}
		return Value;	
	};					
	public boolean Step(){
	/* Leírás: Feladata az adott elem értékének kiszámítása, 
	 * ill. annak eldöntése, hogy a DigitalObject stabil-e
	*/
		Count();								// MEghívja a Count metódust
		return true;							// A GENERATOR mindig igazzal tér vissza
	};

}
