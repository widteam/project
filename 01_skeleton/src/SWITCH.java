import java.util.ArrayList;

/*
* Nev: 			SWITCH
* Tipus: 		Class
* Interfacek:	iComponent
* Szulok		DigitalObject-->Input
* 
*********** Leiras **********
* Specialis Input objektum, mely a kimenetet ciklikusan valtoztatja egy,
* felhasznalo altal szerkesztheto szekvencia alapjan. 
* Az objektum nyilvantartja a szekvenciat, illetve az aktualis poziciot
* a mintaban. A generator Reset utasitasanak hivasaval a pozicio a 
* minta elejere allithato.

*/
public class SWITCH extends Input{
	/*  ATTRIBuTUMOK  */
	private static int SWITCHCount;
	
	
	/*  KONSTRUKTOR  */
	public SWITCH(Wire WiresOut){
		wireIn = null;
		wireOut = new ArrayList<Wire>();
		wireOut.add(WiresOut);
		ID = "SWITCH" + SWITCHCount++;
		
		Value = 0;
	}

	
	
	/*  METoDUSOK  */
	public void Toggle(){
	// Leiras: A Value valtozo erteket allitja nullabol egybe, egybol nullaba
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"","");	/* TEST */
		/*if(Value == 0) Value = 1;
		else Value = 0;			*/
		stack.PrintTail(ID,"","");		/* TEST */
	};
	public int Count(){
	// Leiras: Kiszamolja egy DigitalObject erteket	
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"","");	/* TEST */
	
		/* Az oSSZES kimenetre kiadjuk a kiszamitott eredmenyt. Skeletonnal csak egyre */
		/*for(Wire OutPut:wireOut){
			OutPut.SetValue(Value);	
		}*/
		Wire wire_out = new Wire();
		wire_out.SetValue(0);
		
		stack.PrintTail(ID,"","");		/* TEST */
		return Value;	
	};					
	public boolean Step(){
	/* Leiras: Feladata az adott elem ertekenek kiszamitasa, 
	 * ill. annak eldontese, hogy a DigitalObject stabil-e
	 */
			_TEST stack = new _TEST();				/* TEST */
			stack.PrintHeader(ID,"","");			/* TEST */		
			Count();								// MEghivja a Count metodust
			stack.PrintTail(ID,"","true:boolean");	/* TEST */	
			return true;							// A SWITCH mindig igazzal ter vissza
		};

}
