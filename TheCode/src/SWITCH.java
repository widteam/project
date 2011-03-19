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
		wireOut.add(WiresOut);
		ID = "SWITCH" + SWITCHCount++;
		
		Value = 0;
	}

	
	
	/*  METÓDUSOK  */
	public void Toggle(){
	// Leírás: A Value változó értékét állítja nullából egybe, egybõl nullába
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"","");	/* TEST */
		/*if(Value == 0) Value = 1;
		else Value = 0;			*/
		stack.PrintTail(ID,"","");		/* TEST */
	};
	public int Count(){
	// Leírás: Kiszámolja egy DigitalObject értékét	
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"","");	/* TEST */
	
		/* Az ÖSSZES kimenetre kiadjuk a kiszámított eredményt. Skeletonnál csak egyre */
		/*for(Wire OutPut:wireOut){
			OutPut.SetValue(Value);	
		}*/
		Wire wire_out = new Wire();
		wire_out.SetValue(0);
		
		stack.PrintTail(ID,"","");		/* TEST */
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
			return true;							// A SWITCH mindig igazzal tér vissza
		};

}
