import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class main {

	public static void main(String[] args) {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);
		String line = "";
		Integer choice = -1;

		while (!line.contentEquals("exit")) { // Mig nem akar kilepni a
												// feslhasznalo nem lepunk ki ;

			System.out.println("Kérem töltse be az áramkört a loadboard xy.bhdl paranccsal! ");
			System.out.println("  1 - TesztEset1 - Egyszeru aramkor");
			System.out.println("  2 - TesztEset2 - Visszacsatolt stabil aramkor");
			System.out.println("  3 - TesztEset3 - Instabil aramkor");
			System.out.println("  4 - TesztEset3 - Bonyolult aramkor");
			System.out.println("  exit - Kilepes");
				
			
			
			try { // Szamma alakitjuk - ha tudjuk - a bevitt szoveget
				choice = Integer.decode(line);
			} catch (Exception e) {
				// line = "exit" ;
			}

			// Szetvalogatjuk az esaeteket
			switch (choice) {
			case 1: // LoadBoard egyszeru
				// DigitalBoard letrehozasa
				DigitalBoard o0 = new DigitalBoard();
				o0.LoadBoard("teszt1.bhdl");
				o0.HandleUserCommand(reader);
				break;
			case 2: // LoadBoard visszacsatolassal
				// DigitalBoard letrehozasa
				DigitalBoard o1 = new DigitalBoard();
				o1.LoadBoard("teszt2.bhdl");
				o1.HandleUserCommand(reader);
				break;
			case 3: // LoadBoard instabil
				// DigitalBoard letrehozasa
				DigitalBoard o2 = new DigitalBoard();
				o2.LoadBoard("teszt3.bhdl");
				o2.HandleUserCommand(reader);
				break;
			case 4: // LoadBoard instabil
				// DigitalBoard letrehozasa
				DigitalBoard o3 = new DigitalBoard();
				o3.LoadBoard("teszt4.bhdl");
				o3.HandleUserCommand(reader);
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
