/*
* N�v: 			DigitalBoard
* T�pus: 		Class
* Interfacek:	---
* Sz�l�k		---
* 
*********** Le�r�s **********
* A digit�lis �ramk�rt nyilv�ntart� �s a vez�rl�st  biztos�t� objektum.
* Az �ramk�r �sszes elem�t �s * a k�zt�k l�v� kapcsolatokat l�trehozza,
* kezeli �s t�rolja. �j �ramk�r megnyit�sakor bet�lti az �ramk�rt egy
* f�jlb�l � ek�zben ellen�rzi a szintaktikai helyess�g�t, 
* l�trehoz minden digit�lis elemet, hierarchia szerint sorrendezi,
* felfedezi a visszacsatol�sokat. 
* Tov�bbi feladata az �ramk�r sz�m�t�sait vez�relni, 
* �gy ha a felhaszn�l� l�ptet�st k�r, a gener�torokat l�pteti, 
* �s �jrasz�molja az �ramk�r komponenseinek �llapot�t. 
* Ha a felhaszn�l� a futtat�st v�lasztja, v�ltoztathat� id�k�z�nk�nt 
* l�pteti a jelgener�tort, �s a komponensek �rt�keit �jrasz�molja.

*/
/*  IMPORTOK  */
import java.util.*;	// List, ArrayList-hez

public class DigitalBoard {
	/*	ATTRIB�TUMOK	*/
	private Status SimStatus; 
	private String ID;
	private static int DigitalBoardCounts;

	// Le�r�s: H�rom�llapot� v�ltoz�, amely a szimul�ci� aktu�lis �llapot�t t�rolja 

	private ArrayList< List<iComponent> > ComponentList;
	/* Le�r�s: Ez az attrib�tum t�rolja az �sszes kaput, kimenetet, bemenetet
	 * hierarchikus sorrendben
	 * Ez nem m�s, mint egy list�b�l szervezett t�mb. A t�mb indexe
	 * azonos�tja a hierarchia szintet 
	 * (0-Forr�sok, 1-a forr�sokhoz csatlakoz� elemek, stb)
	 * az egyes szinteken pedig egy lista van az elemekr�l
	*/	
	
	private List<Wire> WireList;
	// Le�r�s: Egyszer� lista a Wire objektumokb�l

	/*  KONSTRUKTOR  */ 
	public DigitalBoard(){		
		SimStatus = Status.STOPPED;
		ComponentList = new ArrayList<List<iComponent>>();
		WireList = new ArrayList<Wire>();	
		ID = "DB" + String.valueOf(DigitalBoardCounts++);
	}
	/*	MET�DUSOK	*/
	public iComponent GetElementByID(String ElementID){
	// Le�r�s: Megkeres egy adott elemet a 	ComponentList illetve a WireList list�kban
		_TEST stack = new _TEST();	
		/* TEST */
		stack.PrintHeader(ID,"ElementID:String", ElementID+":iComponent");	/* TEST */
		stack.PrintTail(ID,"ElementID:String", ElementID+":iComponent"); 	/* TEST */
		return null;		
	};
	
	public void LoadBoard(String strFilePath){
	// Le�r�s: A megfelel� param�terrel megh�vja a ParseFile(String strFilePath) met�dust.
		_TEST stack = new _TEST();						/* TEST */
		stack.PrintHeader(ID,"strFilePath:String", "");	/* TEST */
		ParseFile(strFilePath);
		stack.PrintTail(ID,"strFilePath:String", ""); 		/* TEST */

	};

	public void ParseFile(String strFilePath){
	// Le�r�s: A megadott �tvonalon tal�lhat� f�jlt olvassa be �s soronk�nt �rtelmezi az �llom�nyt
		_TEST stack = new _TEST();						/* TEST */
		stack.PrintHeader(ID,"strFilePath:String", "");	/* TEST */
		switch(Integer.decode(strFilePath)){
		case 0:		
			/*
			* ********************************
			* *******  Egyszer� �ramk�r ******
			* ********************************
			*  led0 = sw0 + gen0
			*/		
			//�sszek�t� wire l�trehoz�sa		
			Wire sw0_or0 = new Wire();	
			Wire gen0_or0 = new Wire();	
			Wire or0_led0 = new Wire();	
			// egy switch , gener�tor egy Or kapu ill egy led l�trehoz�sa, konstruktorukban a wire-rel
			SWITCH sw0 = new SWITCH(sw0_or0);
			GENERATOR gen0 = new GENERATOR(3,5,gen0_or0);
			ORGate or0 = new ORGate(sw0_or0,gen0_or0);
			LED led0 = new LED(or0_led0);
			or0.AddOutput(or0_led0);
			// Hozz�adjuk a hierarchi�hoz
			ComponentList.add(new ArrayList<iComponent>());	//0. szint l�trehoz�sa
			ComponentList.get(0).add(sw0);
			ComponentList.get(0).add(gen0);
			ComponentList.add(new ArrayList<iComponent>()); //1 szint l�trehoz�sa
			ComponentList.get(1).add(or0);
			ComponentList.add(new ArrayList<iComponent>()); // 2. szint l�trehoz�sa
			ComponentList.get(2).add(led0);
			// hozz�adjuk a Wireket
			WireList.add(sw0_or0);
			WireList.add(gen0_or0);
			WireList.add(or0_led0);
			break;
			
		case 1:
			/*
			* *****************************************
			* *******  �ramk�r visszacsatol�ssal ******
			* *****************************************
			* and0_and0 = sw01 & and0_and0
			* led01 = and0_and0
			*/	
			//sw0:Switch, and0:andGate, led0:LED, sw0_and0:Wire, and0_and0:Wire,and0_led0:Wire
			Wire sw01_and0 = new Wire();	
			Wire and0_and0 = new Wire();
			Wire and0_led01 = new Wire();
			
			SWITCH sw01 = new SWITCH(sw01_and0);
			ANDGate and0 = new ANDGate(and0_and0,sw01_and0);
			LED led01 = new LED(and0_led01);
			
			and0.AddOutput(and0_and0);
			and0.AddOutput(and0_led01);
			
			and0.AddToFeedbacks(and0);
			// Hozz�adjuk a hierarchi�hoz
			ComponentList.add(new ArrayList<iComponent>());
			ComponentList.get(0).add(sw01);
			ComponentList.add(new ArrayList<iComponent>());
			ComponentList.get(1).add(and0);
			ComponentList.add(new ArrayList<iComponent>());
			ComponentList.get(2).add(led01);

			// hozz�adjuk a Wireket
			WireList.add(sw01_and0);
			WireList.add(and0_and0);
			WireList.add(and0_led01);
			break;
		case 2:
			/* ********************************
			* *******  Instabil �ramk�r ******
			* ********************************
			* inv0_and2 = !sw2 & inv0_and2
			* led3 = inv0_and2
			*/		
			//�sszek�t� wire l�trehoz�sa		
			Wire sw2_and2 = new Wire();	
			Wire and2_inv0 = new Wire();	
			Wire inv0_and2 = new Wire();
			Wire inv0_led3 = new Wire();
			// egy switch , inverter egy AND kapu ill egy led l�trehoz�sa, konstruktorukban a wire-rel
			SWITCH sw2 = new SWITCH(sw2_and2);
			INVERTER inv0 = new INVERTER(and2_inv0);
			ANDGate and2 = new ANDGate(sw2_and2,inv0_and2);
			LED led3 = new LED(inv0_led3);
			
			and2.AddOutput(and2_inv0);
			inv0.AddOutput(inv0_and2);
			// Feedbacks
			and2.AddToFeedbacks(and2);
			and2.AddToFeedbacks(inv0);
			// Hozz�adjuk a hierarchi�hoz
			ComponentList.add(new ArrayList<iComponent>());	//0. szint l�trehoz�sa
			ComponentList.get(0).add(sw2);
			ComponentList.add(new ArrayList<iComponent>()); //1 szint l�trehoz�sa
			ComponentList.get(1).add(and2);
			ComponentList.add(new ArrayList<iComponent>()); // 2. szint l�trehoz�sa
			ComponentList.get(2).add(inv0);
			ComponentList.add(new ArrayList<iComponent>()); // 3. szint l�trehoz�sa
			ComponentList.get(2).add(led3);
			// hozz�adjuk a Wireket
			WireList.add(sw2_and2);
			WireList.add(and2_inv0);
			WireList.add(inv0_and2);
			WireList.add(inv0_led3);
			break;
		}
		stack.PrintTail(ID,"strFilePAth:String","");
	};	
	public void Run(){
	// Le�r�s: met�dus megh�vja a SetStatus met�dust RUNING param�terrel	
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		
		SetStatus(Status.RUNNING);
		
		stack.PrintTail(ID,"", ""); 	/* TEST */
		
	};
	public void Pause(){
	// Le�r�s: A met�dus megh�vja a SetStatus met�dust PAUSED param�terrel		
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */
		
		SetStatus(Status.PAUSED);
		
		stack.PrintTail(ID,"", ""); 	/* TEST */
	};	
	public void Stop(){
	// Le�r�s:  A met�dus megh�vja a SetStatus met�dust STOPPED param�terrel
		_TEST stack = new _TEST();		/* TEST */
		stack.PrintHeader(ID,"", "");	/* TEST */

		SetStatus(Status.RUNNING);
		
		stack.PrintTail(ID,"", ""); 	/* TEST */
	};		
	private void SetStatus(Status NewStatus){
	// Le�r�s: �t�ll�tja a SimStatus attrib�tumot a param�terben megadott �rt�kre
		_TEST stack = new _TEST();		// A Stackb�l kinyert adatokat tartalmazza
		stack.PrintHeader(ID,NewStatus+":Status","");
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		// ...
		stack.PrintTail(ID,"","");
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		//return null;
	};	
	public void SetFrequency(int Frequency, String ElementID){
	// Le�r�s: A param�terben megadott azonos�t�j� GENERATOR objektum frekvenci�j�t m�dos�tja
		_TEST stack = new _TEST();		// A Stackb�l kinyert adatokat tartalmazza
		stack.PrintHeader(ID,"","");
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		// ...
		stack.PrintTail(ID,"","");
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		//return null;
	};
	public void SetSequence(int Sequence, String ElementID){
	// Le�r�s: A param�terben megadott azonos�t�j� GENERATOR objektum szekvenci�j�t m�dos�tja
		_TEST stack = new _TEST();								 /* TEST */
		stack.PrintHeader(ID,"Sequence:int, ElementID:String","");	 /* TEST */
		GENERATOR GEN_to_setsequence;	
		GEN_to_setsequence = new GENERATOR(0,0,null);			/* Tempor�lis v�ltoz� */
		GetElementByID(GEN_to_setsequence.ID);					/* GetElemetByIDvel megkapjuk, az objektumot	*/		
		GEN_to_setsequence.SetSequence(Sequence); 				 /* az gener�tor objektum SetSequence(...) met�dus�t megh�vjuk */
		stack.PrintTail(ID,"Sequence:int, ElementID:String","");									 /* TEST */
	
	};	
	public void Toggle(String ElementID){
	/* Le�r�s: A param�terben megadott azonos�t�j� SWITCH objektum �rt�k�t az ellenkez�re 
	 * �ll�tja az�ltal, hogy megh�vja az objektum hasonl� nev� param�ter�t
	*/
		_TEST stack = new _TEST();								/* TEST */
		stack.PrintHeader(ID,"ElementID:String","");			/* TEST */
		SWITCH SWITCH_to_toggle = new SWITCH(null);				/* Tempor�lis v�ltoz� */
		GetElementByID(SWITCH_to_toggle.ID);					/* GetElemetByIDvel megkapjuk, majd  egyet	*/		
		SWITCH_to_toggle.Toggle();								/* az switch objektum Toggle() met�dus�t megh�vjuk */
		stack.PrintTail(ID,"","");								/* TEST */
	};	
	public void StepComponents(int TestArg){
	// Le�r�s: Megh�vja az �sszes iComponent interf�szt megval�s�t� objektum Step() met�dus�t.
		_TEST stack = new _TEST();		/*TEST*/
		stack.PrintHeader(ID,"","");	/*TEST*/
		/* Elvileg m�r fel van �p�lve a hierarchia �gy nekem el�g megkapnom a ComponentListet */
		switch(TestArg){
		case 0:
			/*DigitalObject obj;
			List<iComponent> sublist;
			for(List<iComponent> sublist: ComponentList)
				for(iComponent o : sublist){
					obj = (DigitalObject) o;
					//System.out.println(obj.ID); // DEBUG
					obj.Step();
				}*/
			ComponentList.get(0).get(0).Step();
			ComponentList.get(0).get(1).Step();
			ComponentList.get(1).get(0).Step();
			ComponentList.get(2).get(0).Step();
			break;
		case 1:
			ComponentList.get(0).get(0).Step();
			ComponentList.get(1).get(0).Step();
			ComponentList.get(2).get(0).Step();
			break;
		case 2:
			ComponentList.get(0).get(0).Step();
			ComponentList.get(1).get(0).Step();
			ComponentList.get(2).get(0).Step();
			ComponentList.get(2).get(1).Step();
			break;
		}
		stack.PrintTail(ID,"","");		/*TEST*/
	};
}
