import java.io.*;
public class main {

	public static void main(String[] args) {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		String line ="";
		Integer choice = -1;

		// DigitalBoardok letrehozasa
		DigitalBoard o0	 = new DigitalBoard();	// egyszeru
		DigitalBoard o1	 = new DigitalBoard();	// Stabil visszacsatolas
		DigitalBoard o2	 = new DigitalBoard();	// Instabil visszacsatolas

		while(!line.contentEquals("exit")){	//Mig nem akar kilepni a feslhasznaló nem lepunk ki			;

			System.out.println("TESZT SZAKASZ");
			System.out.println("Kerem valasszon a lehetosegek kozul!");
			System.out.println("  0 - Load Board (Egyszeru) ");
			System.out.println("  1 - Load Board (Visszacsatolassal) ");
			System.out.println("  2 - Load Board (Instabil) ");
			System.out.println("  3 - Toggle Switch");
			System.out.println("  4 - Set Sequence");
			System.out.println("  5 - Step Components(Egyszeru)");
			System.out.println("  6 - Step Components(Visszacsatolassal");
			System.out.println("  7 - Step Components(Visszacsatolassal");
			System.out.println("  8 - Count");
			System.out.println("  9 - Run");
			System.out.println("  10 - Pause");
			System.out.println("  11 - Stop");
			/* NEM LETT MEGVALÓSiTVA */ //System.out.println("  13 - Check Value");
			System.out.println("  exit - Kilepes");
			
			try{		// Szamma alakitjuk - ha tudjuk - a bevitt szoveget
				choice  = Integer.decode(line);				
			}catch(Exception e){
				//line = "exit" ;
			}

			// Szetvalogatjuk az esaeteket
			switch (choice){
			case 0:	 // LoadBoard egyszeru
				o0.LoadBoard("0");					
				break;
			case 1:  // LoadBoard visszacsatolassal
				o1.LoadBoard("1");
				break;
			case 2: // LoadBoard instabil
				o2.LoadBoard("2");
				break;
			case 3: // Toggle switch
				// a Toggle(...)meghivasa egy tesztkapcsolóra 
				o0.Toggle("SWITCH_to_toggle");
				break;
			case 4:	// SetSequence
				o0.SetSequence(5, "GEN_to_setsequence");
				break;
			case 8:	// Count
				SWITCH sw = new SWITCH(new Wire());
				System.out.println("SWITCH");
				sw.Count();
				GENERATOR gen = new GENERATOR(0,0,new Wire());
				System.out.println("GENERATOR");
				gen.Count();
				ORGate or = new ORGate(new Wire(),new Wire());
				or.AddOutput(new Wire());
				System.out.println("ORGATE");
				or.Count();
				ANDGate and = new ANDGate(new Wire(),new Wire());
				and.AddOutput(new Wire());
				System.out.println("AND GATE");
				and.Count();
				INVERTER inv = new INVERTER(new Wire());
				System.out.println("INVERTER");
				inv.Count();
				LED led = new LED(new Wire());
				System.out.println("LED");
				led.Count();
				break;	
			case 9:	// RUN
				o0.Run();
				break;	
			case 10:	// Pause
				o0.Pause();
				break;
			case 11:	// Stop
				o0.Stop();
				break;
			case 5:		// Step Component, egyszeru
				o0.StepComponents(0);
				break;
			case 6:		// Step Component, visszacsatolós
				o1.StepComponents(1);
				break;
			case 7:		// Step Component, visszacsatolós, instabil
				o2.StepComponents(2);
				break;
			default:
				break;
			}
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
