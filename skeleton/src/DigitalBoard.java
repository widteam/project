/*
* Név: 			DigitalBoard
* Típus: 		Class
* Interfacek:	---
* Szülõk		---
* 
*********** Leírás **********
* A digitális áramkört nyilvántartó és a vezérlést  biztosító objektum.
* Az áramkör összes elemét és * a köztük lévõ kapcsolatokat létrehozza,
* kezeli és tárolja. Új áramkör megnyitásakor betölti az áramkört egy
* fájlból – eközben ellenõrzi a szintaktikai helyességét, 
* létrehoz minden digitális elemet, hierarchia szerint sorrendezi,
* felfedezi a visszacsatolásokat. 
* További feladata az áramkör számításait vezérelni, 
* így ha a felhasználó léptetést kér, a generátorokat lépteti, 
* és újraszámolja az áramkör komponenseinek állapotát. 
* Ha a felhasználó a futtatást választja, változtatható idõközönként 
* lépteti a jelgenerátort, és a komponensek értékeit újraszámolja.

*/
/*  IMPORTOK  */
import java.util.*;	// List, ArrayList-hez

public class DigitalBoard {
	/*	ATTRIBÚTUMOK	*/
	private Status SimStatus; 
	// Leírás: Háromállapotú változó, amely a szimuláció aktuális állapotát tárolja 

	private ArrayList< List<iComponent> > ComponentList;
	/* Leírás: Ez az attribútum tárolja az összes kaput, kimenetet, bemenetet
	 * hierarchikus sorrendben
	 * Ez nem más, mint egy listából szervezett tömb. A tömb indexe
	 * azonosítja a hierarchia szintet 
	 * (0-Források, 1-a forrásokhoz csatlakozó elemek, stb)
	 * az egyes szinteken pedig egy lista van az elemekrõl
	*/	
	
	private List<Wire> WireList;
	// Leírás: Egyszerû lista a Wire objektumokból

	/*  KONSTRUKTOR  */ 
	public DigitalBoard(){		
		SimStatus = Status.STOPPED;
		ComponentList = new ArrayList<List<iComponent>>();
		WireList = new ArrayList<Wire>();	
	}
	/*	METÓDUSOK	*/
	public iComponent GetElementByID(String ElementID){
	// Leírás: Megkeres egy adott elemet a 	ComponentList illetve a WireList listákban
		// A metódusnak megfelelõ szignóhoz szükséges 
		_TEST_Stack stack = new _TEST_Stack();		// A Stackbõl kinyert adatokat tartalmazza
		stack.PrintHeader("ElementID:String", "component:iComponent");
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		// ...
		stack.PrintTail("ElementID:String", "component:iComponent");
		// TODO: Ha van visszatérési érték, ide kell írni!
		return null;
		
	};
	
	public void LoadBoard(String strFilePath){
	// Leírás: A megfelelõ paraméterrel meghívja a ParseFile(String strFilePath) metódust.
		_TEST_Stack stack = new _TEST_Stack();		// A Stackbõl kinyert adatokat tartalmazza
		stack.PrintHeader("strFilePath:String","");
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
			ParseFile(strFilePath);
		stack.PrintTail("strFilePAth:String","");
		// TODO: Ha van visszatérési érték, ide kell írni!
		//return null;
	};
	public void ParseFile(String strFilePath){
	// Leírás: A megadott útvonalon található fájlt olvassa be és soronként értelmezi az állományt
		_TEST_Stack stack = new _TEST_Stack();		// A Stackbõl kinyert adatokat tartalmazza
		stack.PrintHeader("strFilePath:String","");
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		switch(Integer.valueOf(strFilePath)){
		case 0:		
			/*
			* ********************************
			* *******  Egyszerû áramkör ******
			* ********************************
			*/		
			//Összekötõ wire létrehozása		
			Wire sw0_or0 = new Wire();	
			Wire gen0_or0 = new Wire();	
			Wire or0_led0 = new Wire();	
			// egy switch , generátor egy Or kapu ill egy led létrehozása, konstruktorukban a wire-rel
			SWITCH sw0 = new SWITCH(sw0_or0);
			GENERATOR gen0 = new GENERATOR(3,5,gen0_or0);
			ORGate or0 = new ORGate(sw0_or0,gen0_or0);
			LED led0 = new LED(or0_led0);
			// Hozzáadjuk a hierarchiához
			ComponentList.add(new ArrayList<iComponent>());	//0. szint létrehozása
			ComponentList.get(0).add(sw0);
			ComponentList.get(0).add(gen0);
			ComponentList.add(new ArrayList<iComponent>()); //1 szint létrehozása
			ComponentList.get(1).add(or0);
			ComponentList.add(new ArrayList<iComponent>()); // 2. szint létrehozása
			ComponentList.get(2).add(led0);
			// hozzáadjuk a Wireket
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
			// Hozzáadjuk a hierarchiához
			ComponentList.add(new ArrayList<iComponent>());
			ComponentList.get(0).add(sw01);
			ComponentList.add(new ArrayList<iComponent>());
			ComponentList.get(1).add(and0);
			ComponentList.add(new ArrayList<iComponent>());
			ComponentList.get(2).add(led01);

			// hozzáadjuk a Wireket
			WireList.add(sw01_and0);
			WireList.add(and0_and0);
			WireList.add(and0_led01);
			break;
		}
		stack.PrintTail("strFilePAth:String","");
		// TODO: Ha van visszatérési érték, ide kell írni!
		//return null;
	};	
	public void Run(){
	// Leírás: metódus meghívja a SetStatus metódust RUNING paraméterrel	
		_TEST_Stack stack = new _TEST_Stack();		// A Stackbõl kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszatérési érték, ide kell írni!
		//return null;
	};	
	public void Pause(){
	// Leírás: A metódus meghívja a SetStatus metódust PAUSED paraméterrel		
		_TEST_Stack stack = new _TEST_Stack();		// A Stackbõl kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszatérési érték, ide kell írni!
		//return null;
	};	
	public void Stop(){
	// Leírás:  A metódus meghívja a SetStatus metódust STOPPED paraméterrel
		_TEST_Stack stack = new _TEST_Stack();		// A Stackbõl kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszatérési érték, ide kell írni!
		//return null;
	};		
	private void SetStatus(Status NewStatus){
	// Leírás: Átállítja a SimStatus attribútumot a paraméterben megadott értékre
		_TEST_Stack stack = new _TEST_Stack();		// A Stackbõl kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszatérési érték, ide kell írni!
		//return null;
	};	
	public void SetFrequency(int Frequency, String ElementID){
	// Leírás: A paraméterben megadott azonosítójú GENERATOR objektum frekvenciáját módosítja
		_TEST_Stack stack = new _TEST_Stack();		// A Stackbõl kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszatérési érték, ide kell írni!
		//return null;
	};
	public void SetSequence(int Sequence, String ElementID){
	// Leírás: A paraméterben megadott azonosítójú GENERATOR objektum szekvenciáját módosítja
		_TEST_Stack stack = new _TEST_Stack();		// A Stackbõl kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszatérési érték, ide kell írni!
		//return null;
	};	
	public void Toggle(String ElementID){
	/* Leírás: A paraméterben megadott azonosítójú SWITCH objektum értékét az ellenkezõre 
	 * állítja azáltal, hogy meghívja az objektum hasonló nevû paraméterét
	*/
		_TEST_Stack stack = new _TEST_Stack();		// A Stackbõl kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszatérési érték, ide kell írni!
		//return null;
	};	
	public void StepComponents(){
	// Leírás: Meghívja az összes iComponent interfészt megvalósító objektum Step() metódusát.
		_TEST_Stack stack = new _TEST_Stack();		// A Stackbõl kinyert adatokat tartalmazza
		stack.PrintHeader("","");
		// TODO: Ha vannak függvényhívások, el kell helyezni ide õket!
		// ...
		stack.PrintTail("","");
		// TODO: Ha van visszatérési érték, ide kell írni!
		//return null;
	};
}
