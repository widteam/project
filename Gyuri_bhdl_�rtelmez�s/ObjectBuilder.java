package bhdlparser;


import java.util.ArrayList;
import java.util.Scanner;

public class ObjectBuilder {
	
	//Ellen�rizz�k, hogy egy DigitalObjectet hozunk-e l�tre (true: igen azt, false: valami m�st csin�lunk)
	public static boolean isNewObject(String s){
		
		Scanner scanner = new Scanner(s);
		//NEM M�K�DIK EZ A R�SZ REGUL�RIS KIFEJEZ�S MIATT (GONDOLOM)
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
	
	//Ellen�rizz�k, hogy Wire-e
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
	
	//Visszaad csin�l egy olyan DigitalObjectet, amilyet a param�ter le�r 
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
	
	//Param�terben megadott nev� Wire-t ad vissza
	public static Wire buildWire(String s){
		Scanner scanner = new Scanner(s);
		scanner.skip("wire");
		return new Wire(scanner.next());
	}
	
	//�sszekapcsol�st v�gzi el, ha be lehetne �ll�tani a DigiObjectek WireIn �s WireOut t�mbj�t valahol.
	public static ArrayList<DigitalObject> assign(String s, ArrayList<DigitalObject> objectList){
		Scanner scanner = new Scanner(s);
		while(scanner.hasNext())
		{
			System.out.println(scanner.next());
		}
		return objectList;
	}
}