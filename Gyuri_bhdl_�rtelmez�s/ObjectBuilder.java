package bhdlparser;


import java.util.ArrayList;
import java.util.Scanner;

public class ObjectBuilder {
	
	//Ellenõrizzük, hogy egy DigitalObjectet hozunk-e létre (true: igen azt, false: valami mást csinálunk)
	public static boolean isNewObject(String s){
		
		Scanner scanner = new Scanner(s);
		//NEM MÛKÖDIK EZ A RÉSZ REGULÁRIS KIFEJEZÉS MIATT (GONDOLOM)
		/***
		while(scanner.hasNext()){
			System.out.println(scanner.next());
			if(scanner.hasNext("&")){
				System.out.println("Ez egy andgate.");
				return false;
			}
			else if(scanner.hasNext("|")){
				System.out.println("Ez egy orgate.");
				return false;
			}
		}
		***/
		
		if(scanner.hasNext("composit")){
        	System.out.println("Ez egy composit.");
        	return true;
        }
        else if(scanner.hasNext("led")){
        	System.out.println("Ez egy led.");
        	return true;
        }
        else if(scanner.hasNext("switch")){
        	System.out.println("Ez egy switch.");
        	return true;
        }
        else if(scanner.hasNext("generator")){
        	System.out.println("Ez egy generator.");
        	return true;
        }
        else if(scanner.hasNext("oscilloscope")){
        	System.out.println("Ez egy oscilloscope.");
        	return true;
        }
        else return false;
	}
	
	//Ellenõrizzük, hogy Wire-e
	public static boolean isNewWire(String s){
		Scanner scanner = new Scanner(s);
		
		if(scanner.hasNext("wire")){
			System.out.println("Ez egy wire.");
			return true;
		}
		else return false;
	}
	
	public static boolean isAssign(String s){
		Scanner scanner = new Scanner(s);
		
		if(scanner.hasNext("assign")){
			System.out.println("Ez egy assign.");
			return true;
		}
		else return false;
	}
	
	//Visszaad csinál egy olyan DigitalObjectet, amilyet a paraméter leír 
	public static DigitalObject buildObject(String s){

        Scanner scanner = new Scanner(s);
        if(scanner.hasNext("composit")){
        	scanner.skip("composit");
        	return new Composit(scanner.next());
        }
        else if(scanner.hasNext("led")){
        	scanner.skip("led");
        	return new LED(scanner.next(), null);
        }
        else if(scanner.hasNext("switch")){
        	scanner.skip("switch");
        	return new SWITCH(scanner.next(), null);
        }
        else if(scanner.hasNext("generator")){
        	scanner.skip("generator");
        	return new GENERATOR(scanner.next(), 0, 0, null);
        }
        else if(scanner.hasNext("oscilloscope")){
        	scanner.skip("oscilloscope");
        	return new Oscilloscope(scanner.next(), null, 0);
        }
        else{
        	return null;
        }
	}
	
	//Paraméterben megadott nevá Wire-t ad vissza
	public static Wire buildWire(String s){
		Scanner scanner = new Scanner(s);
		scanner.skip("wire");
		return new Wire(scanner.next());
	}
	
	//összekapcsolást végzi el, ha be lehetne állítani a DigiObjectek WireIn és WireOut tömbjét valahol.
	public static ArrayList<DigitalObject> assign(String s, ArrayList<DigitalObject> objectList){
		Scanner scanner = new Scanner(s);
		while(scanner.hasNext())
		{
			System.out.println(scanner.next());
		}
		return objectList;
	}
}