/*
* N�v: 			ANDGate
* T�pus: 		Class
* Interfacek:	iComponent
* Sz�l�k		DigitalObject-->Gate
* 
*********** Le�r�s **********
* Logikai �S kaput megval�s�t� objektum. A bemeneteir�l beol-
* vasott �rt�kekb�l kisz�molja �s tov�bbadja a kimenet�re az
* �j �rt�ket. Az �j �rt�k�t az �S kapu igazs�gt�bl�ja szerint
* sz�molja ki, mely k�t bemenet eset�n a k�vetkez�:
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
* Jelent�sek: 0: logikai HAMIS �rt�k | 1: logikai IGAZ �rt�k | X: don�t care

*/

/*  IMPORTOK  */
import java.util.*;


public class ANDGate extends Gate{
	/*  ATTRIB�TUMOK  */
	private static int ANDCounts;	// Statikus v�ltoz� az egyedi ID �rt�khez
	
	
	/*  KONSTRUKTOR  */
	public ANDGate(Wire wirein1, Wire wirein2){
		wireIn = new ArrayList<Wire>();	// Inicializ�ljuk a wireIn list�t
		wireOut = new ArrayList<Wire>();// Inicializ�ljuk a wireOut list�t		
		ID = "AND" + ANDCounts++;		// ID-t el��ll�tjuk
		wireIn.add(wirein1);			// a konstruktorban megadott bemenetet bedr�tozzuk
		wireIn.add(wirein2);			// a m�sik bemenetet is bedr�tozzuk
	}
	
	
	/*	MET�DUSOK	*/
	public int Count(){
		// Le�r�s: Kisz�molja egy DigitalObject �rt�k�t	
			int Result=0;
			_TEST stack = new _TEST();		
			stack.PrintHeader(ID,"",Result +":int");

			/* Lek�rdezz�k a bemenetek �rt�keit */
			wireIn.get(0).GetValue();
			wireIn.get(1).GetValue();	
			
			//Result = wireIn.get(0).GetValue() & wireIn.get(1).GetValue(); // Eredm�ny kisz�m�t�sa
			
			/* Az �SSZES kimenetre kiadjuk a kisz�m�tott eredm�nyt. Skeletonn�l csak egyre */
			
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
			/* Le�r�s: Feladata az adott elem �rt�k�nek kisz�m�t�sa, 
			 * ill. annak eld�nt�se, hogy a DigitalObject stabil-e
			*/
				boolean Result = true;						// A v�gs� eredm�ny: Stabil-e az �ramk�r
				_TEST stack = new _TEST();					/* TEST */
				stack.PrintHeader(ID,"","true:boolean");	/* TEST */
				PreviousValue = Count();					// Megn�zz�k az els� fut�s erredm�ny�t
				if(Feedbacks != null && !Feedbacks.isEmpty()){// Ha nem �res a Feedback t�mb
					int NewValue ;						// Lok�lis v�ltoz�
					for(DigitalObject obj:Feedbacks){	// Feedback �ssezs elem�n v�gig
						obj.Count();
					}
					NewValue = Count();					// Megn�zz�k �j�l az eredm�nyt
					Result = (PreviousValue==NewValue);	// Elt�r-e a kett�?( Prev �s a mostani) 
					
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
