import java.util.ArrayList;

/*
* Név: 			SWITCH
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
public class SWITCH extends Input{
	/*  ATTRIBÚTUMOK  */
	private static int SWITCHCount;
	
	
	/*  KONSTRUKTOR  */
	public SWITCH(Wire WiresOut){
		wireIn = null;
		wireOut = new ArrayList<Wire>();
		
		ID = "SWITCH" + SWITCHCount++;
		
		Value = 0;
	}

	
	
	/*  METÓDUSOK  */
	public void Toggle(){
	// Leírás: A Value változó értékét állítja nullából egybe, egybõl nullába
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
