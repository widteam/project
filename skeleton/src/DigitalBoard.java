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
	}
	/*	MET�DUSOK	*/
	public iComponent GetElementByID(String ElementID){
	// Le�r�s: Megkeres egy adott elemet a 	ComponentList illetve a WireList list�kban
		// A met�dusnak megfelel� szign�hoz sz�ks�ges 
		_TEST_Stack stack = new _TEST_Stack();		// A Stackb�l kinyert adatokat tartalmazza
		stack.PrintHeader("ElementID:String", "component:iComponent");
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		// ...
		stack.PrintTail("ElementID:String", "component:iComponent");
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		return null;
		
	};
	
	public void LoadBoard(String strFilePath){
	// Le�r�s: A megfelel� param�terrel megh�vja a ParseFile(String strFilePath) met�dust.
		_TEST_Stack stack = new _TEST_Stack();		// A Stackb�l kinyert adatokat tartalmazza
		stack.PrintHeader("strFilePath:String","");
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
			ParseFile(strFilePath);
		stack.PrintTail("strFilePAth:String","");
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		//return null;
	};
	public void ParseFile(String strFilePath){
	// Le�r�s: A megadott �tvonalon tal�lhat� f�jlt olvassa be �s soronk�nt �rtelmezi az �llom�nyt
		_TEST_Stack stack = new _TEST_Stack();		// A Stackb�l kinyert adatokat tartalmazza
		stack.PrintHeader("strFilePath:String","");
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		switch(Integer.valueOf(strFilePath)){
		case 0:		
			/*
			* ********************************
			* *******  Egyszer� �ramk�r ******
			* ********************************
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
			//sw0:Switch, and0:andGate, led0:LED, sw0_and0:Wire, and0_and0:Wire,and0_led0:Wire
			Wire sw01_and0 = new Wire();	
			Wire and0_and0 = new Wire();
			Wire and0_led01 = new Wire();
			
			SWITCH sw01 = new SWITCH(sw01_and0);
			ANDGate and0 = new ANDGate(and0_and0,sw01_and0);
			LED led01 = new LED(and0_led01);
			
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
		}
		stack.PrintTail("strFilePAth:String","");
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		//return null;
	};	
	public void Run(){
	// Le�r�s: met�dus megh�vja a SetStatus met�dust RUNING param�terrel	
		_TEST_Stack stack = new _TEST_Stack();		// A Stackb�l kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		//return null;
	};	
	public void Pause(){
	// Le�r�s: A met�dus megh�vja a SetStatus met�dust PAUSED param�terrel		
		_TEST_Stack stack = new _TEST_Stack();		// A Stackb�l kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		//return null;
	};	
	public void Stop(){
	// Le�r�s:  A met�dus megh�vja a SetStatus met�dust STOPPED param�terrel
		_TEST_Stack stack = new _TEST_Stack();		// A Stackb�l kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		//return null;
	};		
	private void SetStatus(Status NewStatus){
	// Le�r�s: �t�ll�tja a SimStatus attrib�tumot a param�terben megadott �rt�kre
		_TEST_Stack stack = new _TEST_Stack();		// A Stackb�l kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		//return null;
	};	
	public void SetFrequency(int Frequency, String ElementID){
	// Le�r�s: A param�terben megadott azonos�t�j� GENERATOR objektum frekvenci�j�t m�dos�tja
		_TEST_Stack stack = new _TEST_Stack();		// A Stackb�l kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		//return null;
	};
	public void SetSequence(int Sequence, String ElementID){
	// Le�r�s: A param�terben megadott azonos�t�j� GENERATOR objektum szekvenci�j�t m�dos�tja
		_TEST_Stack stack = new _TEST_Stack();		// A Stackb�l kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		//return null;
	};	
	public void Toggle(String ElementID){
	/* Le�r�s: A param�terben megadott azonos�t�j� SWITCH objektum �rt�k�t az ellenkez�re 
	 * �ll�tja az�ltal, hogy megh�vja az objektum hasonl� nev� param�ter�t
	*/
		_TEST_Stack stack = new _TEST_Stack();		// A Stackb�l kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		//return null;
	};	
	public void StepComponents(){
	// Le�r�s: Megh�vja az �sszes iComponent interf�szt megval�s�t� objektum Step() met�dus�t.
		_TEST_Stack stack = new _TEST_Stack();		// A Stackb�l kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak f�ggv�nyh�v�sok, el kell helyezni ide �ket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszat�r�si �rt�k, ide kell �rni!
		//return null;
	};
}
