/*
* Név: 			ANDGate
* Típus: 		Class
* Interfacek:	iComponent
* Szülõk		DigitalObject-->Gate
* 
*********** Leírás **********
* Logikai ÉS kaput megvalósító objektum. A bemeneteirõl beol-
* vasott értékekbõl kiszámolja és továbbadja a kimenetére az
* új értéket. Az új értékét az ÉS kapu igazságtáblája szerint
* számolja ki, mely két bemenet esetén a következõ:
* 
* kimenet   bemenet
*	A	B	A  AND B
*	0	0		0
*	0	1		0
*	1	0		0
*	1	1		1
*	X	0		0
*	X	1		X
*	0	X		0
*	1	X		X
*	X	X		X
* Jelentések: 0: logikai HAMIS érték | 1: logikai IGAZ érték | X: don’t care

*/

/*  IMPORTOK  */
import java.util.*;


public class ANDGate extends Gate{
	/*  ATTRIBÚTUMOK  */
	private static int ANDCounts;	// Statikus változó az egyedi ID értékhez
	
	
	/*  KONSTRUKTOR  */
	public ANDGate(Wire wirein1, Wire wirein2){
		wireIn = new ArrayList<Wire>();	// Inicializáljuk a wireIn listát
		wireOut = new ArrayList<Wire>();// Inicializáljuk a wireOut listát		
		ID = "AND" + ANDCounts++;		// ID-t elõállítjuk
		wireIn.add(wirein1);			// a konstruktorban megadott bemenetet bedrótozzuk
		wireIn.add(wirein2);			// a másik bemenetet is bedrótozzuk
	}
	
	
	/*	METÓDUSOK	*/
	public int Count(){
		// Leírás: Kiszámolja egy DigitalObject értékét	
			int Result=0;
			_TEST stack = new _TEST();		
			stack.PrintHeader(ID,"",Result +":int");

			/* Lekérdezzük a bemenetek értékeit */
			wireIn.get(0).GetValue();
			wireIn.get(1).GetValue();	
			
			//Result = wireIn.get(0).GetValue() & wireIn.get(1).GetValue(); // Eredmény kiszámítása
			
			/* Az ÖSSZES kimenetre kiadjuk a kiszámított eredményt. Skeletonnál csak egyre */
			
			/*for(Wire OutPut:wireOut){
				OutPut.SetValue(Result);
			}
			*/
			Wire wire_out = new Wire();
			wire_out.SetValue(0);
			
			stack.PrintTail(ID,"",Result +":int");
			return Result;
		};		
		public boolean Step(){
			/* Leírás: Feladata az adott elem értékének kiszámítása, 
			 * ill. annak eldöntése, hogy a DigitalObject stabil-e
			*/
				boolean Result = true;						// A végsõ eredmény: Stabil-e az áramkör
				_TEST stack = new _TEST();					/* TEST */
				stack.PrintHeader(ID,"","true:boolean");	/* TEST */
				PreviousValue = Count();					// Megnézzük az elsõ futás erredményét
				if(Feedbacks != null && !Feedbacks.isEmpty()){// Ha nem üres a Feedback tömb
					int NewValue ;						// Lokális változó
					for(DigitalObject obj:Feedbacks){	// Feedback össezs elemén végig
						obj.Count();
					}
					NewValue = Count();					// Megnézzük újól az eredményt
					Result = (PreviousValue==NewValue);	// Eltér-e a kettõ?( Prev és a mostani) 
					
					for(DigitalObject obj:Feedbacks){
						obj.Count();
					}
					NewValue = Count();
					Result = (PreviousValue==NewValue);
					
					for(DigitalObject obj:Feedbacks){
						obj.Count();
					}
					NewValue = Count();
					Result = (PreviousValue==NewValue);
				}
				stack.PrintTail(ID,"",Result + ":boolean");	/* TEST */
				return Result;
			};
}
