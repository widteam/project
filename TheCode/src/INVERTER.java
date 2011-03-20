import java.util.ArrayList;

/*
* Nev: 			INVERTER
* Tipus: 		Class
* Interfacek:	iComponent
* Szulok		DigitalObject-->Gate
* 
*********** Leiras **********
* Logikai invertert megvalosito objektum. Egyetlen egy bemenettel rendelkezik, 
* es kimenetere ennek inverzet adja, ha annak van ertelme. 
* Igazsagtablaja a kovetkezo:
* 
* bemenet	kimenet
*	A		  NOT A
* 	0	  		1
*	1	  		0
*	X	  		X
* Jelentesek: 0: logikai HAMIS ertek | 1: logikai IGAZ ertek | X: don't care

 */
public class INVERTER extends Gate{
	/*  ATTRIBuTUMOK  */
	private static int INVERTERCounts;
	
	
	/*  KONSTRUKTOR  */
	public INVERTER(Wire wirein1){
		wireIn = new ArrayList<Wire>();
		wireOut = new ArrayList<Wire>();
		ID = "INVERTER" + INVERTERCounts++;
		PreviousValue = -1;
		wireIn.add(wirein1);
	}
	
	
	/*	METoDUSOK	*/
	public int Count(){
		// Leiras: Kiszamolja egy DigitalObject erteket	
			int Result=0;
			_TEST stack = new _TEST();		
			stack.PrintHeader(ID,"",Result +":int");

			/* Lekerdezzuk a bemenetek ertekeit */
			wireIn.get(0).GetValue();
			
			// Eredmeny kiszamitasa
			//if(wireIn.get(0).GetValue() ==0)Result =1;
			//if(wireIn.get(0).GetValue() ==1)Result =0;
			//if(wireIn.get(0).GetValue() ==-1)Result =-1
			
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