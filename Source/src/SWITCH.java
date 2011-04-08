import java.util.ArrayList;

/*
* Név: 			SWITCH
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
public class SWITCH extends Input{
	/*  ATTRIBÚTUMOK  */
	private static int SWITCHCount;
	
	
	/*  KONSTRUKTOR  */
	public SWITCH(String strCompositName, Wire WireOut){
		final String strIDDelimiter = "#";
		String strIDNumber  = String.valueOf(SWITCHCount++);
		final String strIDName  = this.getClass().getName();
		ID = strCompositName + strIDDelimiter + strIDName + strIDDelimiter + strIDNumber;
			
		wireIn = null;
		wireOut = new ArrayList<Wire>();
		wireOut.add(WireOut);	
		Value = 0;
	}

	
	
	/*  METÓDUSOK  */
	public void Toggle(){
	// Leírás: A Value változó értékét állítja nullából egybe, egybõl nullába
		if(Value == 0) Value = 1;
		else Value = 0;		
	};
	public int Count(){
	// Leírás: Kiszámolja egy DigitalObject értékét	
		/* Az ÖSSZES kimenetre kiadjuk a kiszámított eredményt.*/
		
		if(wireOut == null || wireOut.isEmpty()){
			// throw InputNotConnectedWarning
		}
		for(Wire OutPut:wireOut){
			OutPut.SetValue(Value);	
		}
		return Value;	
	};					
	public boolean Step(){
	/* Leírás: Feladata az adott elem értékének kiszámítása, 
	 * ill. annak eldöntése, hogy a DigitalObject stabil-e
	 */
			Count();								// MEghívja a Count metódust
			return true;							// A SWITCH mindig igazzal tér vissza
		}
}
