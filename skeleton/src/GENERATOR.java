import java.util.ArrayList;

/*
* Nev: 			GENERATOR
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
public class GENERATOR extends Input{
	/*  ATTRIBuTUMOK  */
	private static int GENERATORCounts;
	
	@SuppressWarnings("unused")
	private int Frequency;
	/* Leiras: A generator leptetesenek a gyakorisagat taroljuk ebben. 
	 * Az itt megadott szamu Step() hivas utan fog csak lepni.
	*/
	
	@SuppressWarnings("unused")
	private int Sequence;
	// LEiras: Ebben taroljuk a kimenetre kikuldendo mintat. ertelmezese binaris.
	
	@SuppressWarnings("unused")
	private int SequencePos;
	/* Leiras: Az attributum tartja nyilvan az aktualis poziciot a szekvenciaban. 
	 * A Reset() hivas 0 ertekre allitja
	*/
	@SuppressWarnings("unused")
	private int FrequencyCounter;
	/*Leiras:
	 * Egy segedszamlalo, mely nyilvantartja, hogy meg mennyi Count maradt hatra
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
	
	
	/*  METoDUSOK  */
	public void Reset(){
	// Leiras: A SequencePos. erteket allitja alapertelmezettre, azaz a minta elejere
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"","");	/* TEST */
		//SequencePos = 0;				// Poziciot alapra
		//Value = Integer.toBinaryString(Sequence).charAt(SequencePos++); //Binarissa alakitjuk a szamot es vesszuk az MSB bitet
		stack.PrintTail(ID,"","");		/* TEST */	
	};
	public void SetSequence(int NewSequence){
	// Leiras: a Frequency erteket allitja be, a parameterben megadott ertekre.
		_TEST stack = new _TEST();					/* TEST */
		stack.PrintHeader(ID,"NewSequence:int","");	/* TEST */
		//Sequence = NewSequence;					// Beallitjuk a szekvenciat
		//SequencePos = 0;							// Poziciot alapra
		//Value = Integer.toBinaryString(Sequence).charAt(SequencePos++); //Binarissa alakitjuk a szamot es vesszuk az MSB bitet
		stack.PrintTail(ID,"","");					/* TEST */	
	};
	public void SetFrequency(int NewFrequency){
	// Leiras: a minta erteket allitja be, a parameterben megadott ertekre.
		_TEST stack = new _TEST();						/* TEST */
		stack.PrintHeader(ID,"NewFrequency:int","");	/* TEST */
		//Frequency = NewFrequency;						// Beallitjuk a frekvenciat
		//StartFrequency = NewFrequency					// Hany count maradt meg hatra
		stack.PrintTail(ID,"","");						/* TEST */
	};
	public int Count(){
	// Leiras: Kiszamolja egy DigitalObject erteket	
		_TEST stack = new _TEST();		
		stack.PrintHeader(ID,"","");
	
		/* Az oSSZES kimenetre kiadjuk a kiszamitott eredmenyt. Skeletonnal csak egyre */
		/*FrequencyCounter--;					// Csokkentjuk a szamlalot
		if(!FrequencyCounter){				// HA megfello szamu count eltelt mar
			for(Wire OutPut:wireOut){
				OutPut.SetValue(Value);					//Kiadjuk a kimenetre az aktualis erteket
				if(SequencePos >= Integer.toBinaryString(Sequence).length()){	// a szekvenciaban elore megyunk.. mar ha lehet
					SequencePos = 0;				
				}else{
					SequencePos++;
				}			
				Value = Integer.toBinaryString(Sequence).charAt(SequencePos++);	// Kiszamoljuk az uj erteket
			}
			FrequencyCounter = Frequency;	// ujra az elejerol szamolunk
		}*/
		Wire wire_out = new Wire();
		wire_out.SetValue(0);
		
		stack.PrintTail(ID,"","");
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
		return true;							// A GENERATOR mindig igazzal ter vissza
	};

}
