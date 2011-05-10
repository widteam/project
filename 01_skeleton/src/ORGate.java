import java.util.ArrayList;

/*
* Nev: 			ORGate
* Tipus: 		Class
* Interfacek:	iComponent
* Szulok		DigitalObject-->Gate
* 
*********** Leiras **********
* Logikai VAGY kaput megvalosito objektum. A bemeneteirol beol-
* vasott ertekekbol kiszamolja es tovabbadja a kimenetere az
* uj erteket. Az uj erteket a VAGY kapu igazsagtablaja szerint
* szamolja ki, mely ket bemenet eseten a kovetkezo:
* 
* kimenet   bemenet
*	A	B	A  OR B
*	0	0		0
*	0	1		1
*	1	0		1
*	1	1		1
*	X	0		X
*	X	1		1
*	0	X		X
*	1	X		1
*	X	X		X
* Jelentesek: 0: logikai HAMIS ertek | 1: logikai IGAZ ertek | X: don't care

 */
public class ORGate extends Gate{
	/*  ATTRIBuTUMOK  */
	private static int ORCounts;
	
	
	/*  KONSTRUKTOR  */
	public ORGate(Wire wirein1, Wire wirein2){
		wireIn = new ArrayList<Wire>();
		wireOut = new ArrayList<Wire>();
		ID = "OR" + ORCounts++;
		
		wireIn.add(wirein1);
		wireIn.add(wirein2);
	}
	
	
	/*	METoDUSOK	*/
	public int Count(){
		// Leiras: Kiszamolja egy DigitalObject erteket	
			int Result=0;
			_TEST stack = new _TEST();		
			stack.PrintHeader(ID,"",Result +":int");

			/* Lekerdezzuk a bemenetek ertekeit */
			wireIn.get(0).GetValue();
			wireIn.get(1).GetValue();	
			
			//Result = wireIn.get(0).GetValue() & wireIn.get(1).GetValue(); // Eredmeny kiszamitasa
			
			/* Az oSSZES kimenetre kiadjuk a kiszamitott eredmenyt. Skeletonnal csak egyre */
			
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
			/* Leiras: Feladata az adott elem ertekenek kiszamitasa, 
			 * ill. annak eldontese, hogy a DigitalObject stabil-e
			*/
				boolean Result = true;						// A vegso eredmeny: Stabil-e az aramkor
				_TEST stack = new _TEST();					/* TEST */
				stack.PrintHeader(ID,"","true:boolean");	/* TEST */
				PreviousValue = Count();					// Megnezzuk az elso futas erredmenyet
				if(Feedbacks != null && !Feedbacks.isEmpty()){// Ha nem ures a Feedback tomb
					int NewValue ;						// Lokalis valtozo
					for(DigitalObject obj:Feedbacks){	// Feedback ossezs elemen vegig
						obj.Count();
					}
					NewValue = Count();					// Megnezzuk ujol az eredmenyt
					Result = (PreviousValue==NewValue);	// Elter-e a ketto?( Prev es a mostani) 
					
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