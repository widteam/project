import java.util.ArrayList;

/*
* N�v: 			GENERATOR
* T�pus: 		Class
* Interfacek:	iComponent
* Sz�l�k		DigitalObject-->Input
* 
*********** Le�r�s **********
* Speci�lis Input objektum, mely a kimenet�t ciklikusan v�ltoztatja egy,
* felhaszn�l� �ltal szerkeszthet� szekvencia alapj�n. 
* Az objektum nyilv�ntartja a szekvenci�t, illetve az aktu�lis poz�ci�t
* a mint�ban. A gener�tor Reset utas�t�s�nak h�v�s�val a poz�ci� a 
* minta elej�re �ll�that�.

*/
public class GENERATOR extends Input{
	/*  ATTRIB�TUMOK  */
	private static int GENERATORCounts;
	
	private int Frequency;
	/* Le�r�s: A gener�tor l�ptet�s�nek a gyakoris�g�t t�roljuk ebben. 
	 * Az itt megadott sz�m� Step() h�v�s ut�n fog csak l�pni.
	*/
	
	private int Sequence;
	// LE�r�s: Ebben t�roljuk a kimenetre kik�ldend� mint�t. �rtelmez�se bin�ris.
	
	private int SequencePos;
	/* Le�r�s: Az attrib�tum tartja nyilv�n az aktu�lis poz�ci�t a szekvenci�ban. 
	 * A Reset() h�v�s 0 �rt�kre �ll�tja
	*/
	private int FrequencyCounter;
	/*Le�r�s:
	 * Egy seg�dsz�ml�l�, mely nyilv�ntartja, hogy m�g mennyi Count maradt h�tra
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
	
	
	/*  MET�DUSOK  */
	public void Reset(){
	// Le�r�s: A SequencePos. �rt�k�t �ll�tja alap�rtelmezettre, azaz a minta elej�re
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"","");	/* TEST */
		//SequencePos = 0;				// Poz�ci�t alapra
		//Value = Integer.toBinaryString(Sequence).charAt(SequencePos++); //Bin�riss� alak�tjuk a sz�mot �s vessz�k az MSB bitet
		stack.PrintTail(ID,"","");		/* TEST */	
	};
	public void SetSequence(int NewSequence){
	// Le�r�s: a Frequency �rt�k�t �ll�tja be, a param�terben megadott �rt�kre.
		_TEST stack = new _TEST();					/* TEST */
		stack.PrintHeader(ID,"NewSequence:int","");	/* TEST */
		//Sequence = NewSequence;					// Be�ll�tjuk a szekvenci�t
		//SequencePos = 0;							// Poz�ci�t alapra
		//Value = Integer.toBinaryString(Sequence).charAt(SequencePos++); //Bin�riss� alak�tjuk a sz�mot �s vessz�k az MSB bitet
		stack.PrintTail(ID,"","");					/* TEST */	
	};
	public void SetFrequency(int NewFrequency){
	// Le�r�s: a minta �rt�k�t �ll�tja be, a param�terben megadott �rt�kre.
		_TEST stack = new _TEST();						/* TEST */
		stack.PrintHeader(ID,"NewFrequency:int","");	/* TEST */
		//Frequency = NewFrequency;						// Be�ll�tjuk a frekvenci�t
		//StartFrequency = NewFrequency					// H�ny count maradt m�g h�tra
		stack.PrintTail(ID,"","");						/* TEST */
	};
	public int Count(){
	// Le�r�s: Kisz�molja egy DigitalObject �rt�k�t	
		_TEST stack = new _TEST();		
		stack.PrintHeader(ID,"","");
	
		/* Az �SSZES kimenetre kiadjuk a kisz�m�tott eredm�nyt. Skeletonn�l csak egyre */
		/*FrequencyCounter--;					// Cs�kkentj�k a sz�ml�l�t
		if(!FrequencyCounter){				// HA megfell� sz�m� count eltelt m�r
			for(Wire OutPut:wireOut){
				OutPut.SetValue(Value);					//Kiadjuk a kimenetre az aktu�lis �rt�ker
				if(SequencePos >= Integer.toBinaryString(Sequence).length()){	// a szekvenci�ban el�re megy�nk.. m�r ha lehet
					SequencePos = 0;				
				}else{
					SequencePos++;
				}			
				Value = Integer.toBinaryString(Sequence).charAt(SequencePos++);	// Kisz�moljuk az �j �rt�ket
			}
			FrequencyCounter = Frequency;	// �jra az elej�r�l sz�molunk
		}*/
		Wire wire_out = new Wire();
		wire_out.SetValue(0);
		
		stack.PrintTail(ID,"","");
		return Value;	
	};					
	public boolean Step(){
	/* Le�r�s: Feladata az adott elem �rt�k�nek kisz�m�t�sa, 
	 * ill. annak eld�nt�se, hogy a DigitalObject stabil-e
	*/
		_TEST stack = new _TEST();				/* TEST */
		stack.PrintHeader(ID,"","");			/* TEST */		
		Count();								// MEgh�vja a Count met�dust
		stack.PrintTail(ID,"","true:boolean");	/* TEST */	
		return true;							// A GENERATOR mindig igazzal t�r vissza
	};

}
