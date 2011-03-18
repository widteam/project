import java.io.*;
public class main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		char TesterChoice = 0;
		
		InputStreamReader in  = new InputStreamReader(System.in);
		OutputStreamWriter out = new OutputStreamWriter(System.out);
		
		// DigitalBoard létrehozása
		DigitalBoard o = new DigitalBoard();
		


		try {
			while(TesterChoice != '9'){
				System.out.println("TESZT SZAKASZ");
				System.out.println("Kérem válasszon a lehetõségek közül!");
				System.out.println("  0 - Load Board (Egyszerû) ");
				System.out.println("  1 - Load Board (Visszacsatolással) ");
				System.out.println("  2 - Load Board (Instabil) ");
				System.out.println("  3 - Toggle Switch");
				System.out.println("  4 - Set Sequence");
				System.out.println("  5 - Step all Components");
				System.out.println("  6 - Step");
				System.out.println("  7 - Count");
				System.out.println("  8 - Check Value");
				System.out.println("  9 - Kilépés");
				
				
				
				TesterChoice =  (char) in.read();	
			
				System.out.println(TesterChoice);
				
				switch (TesterChoice){
				case '0':			
					System.out.println("Load Egyszerû:");
					o.LoadBoard("0");
					
					break;
				case '1':
					System.out.println("Load Visszacsatolt:");
					o.LoadBoard("1");
					break;
				case '2':
					System.out.println("Load instabil:");
					o.LoadBoard("2");
					break;
				case '3':
					System.out.println("Toggle:");
					break;
				case '4':
					System.out.println("Set sequence:");
					break;
				case '5':
					System.out.println("Step all Components:");
					break;	
				case '6':
					System.out.println("Step:");
					break;
				case '7':
					System.out.println("count:");
					break;
				case '8':
					System.out.println("check value:");
					break;
				case '9':
					System.out.println("kilépés, viszlát!:");
					return;
				default:
					TesterChoice = 0;
				}
				

					
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

}
