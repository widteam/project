import java.util.*;	// List, ArrayList-hez

//committok miatt k�l�n class, v�g�n majd bele lehet rakni egybe, de igy nem ny�lok keresztbe nektek

public class HierarchyCounter {
	List<DigitalObject> sources;
	List<DigitalObject> list;
	List<Wire> wires;
	ArrayList<List<DigitalObject>> components;
	
	public int getSzint(DigitalObject ennek){
		int ret=-1;
		for(int i=0; i<components.size(); i++){
			if(-1!=components.get(i).indexOf(ennek));
		}
		return ret;
	}
	
	public void moveToSzint(DigitalObject ezt, int ide){
		if(components.size()<ide) components.add(new ArrayList<DigitalObject>()); //ha nincs m�g szint+1 hierarchia
		if(getSzint(ezt)<0) list.remove(ezt); //ha nincs a hierarchi�ban, a bej�ratlanb�l vessz�k ki
		if(components.get(getSzint(ezt)).indexOf(ezt)!=-1) components.get(getSzint(ezt)).remove(ezt); //ha a list�ban van, kivessz�k
		components.get(ide).add(ezt);//hozz�adjuk a k�rt hierarchi�ra
	}
	
	public void foo(){}
	
	public void CountHierarchy(List<Wire> wiresArg, ArrayList<List<DigitalObject>> componentsArg){
		sources=new ArrayList<DigitalObject>(); //forr�sokat k�l�nvessz�k
		list=new ArrayList<DigitalObject>(); //bej�ratlan
		list.clear();
		wires=wiresArg;
		components=componentsArg;
				
		//componentn�l m�s a source tipusa!! ez TODO
		for(int i=0 ; i<components.size(); i++){
			for(int j=0; j<components.get(i).size();j++){
				String classs=components.get(i).get(j).getClass().toString();
				//hozz�adjuk a sourceokhoz vagy a bej�ratlanokhoz
				if(classs.equals("class SWITCH") || classs.equals("class GENERATOR")) sources.add(components.get(i).get(j));
				else list.add(components.get(i).get(j));
			}
		}
		System.out.println(list.size() + ", " +sources.size());
		//Megvan a source lista (nem komponensre), �s megvan a t�bbi elem egy m�sik t�mbben.
		components.get(0).clear();
		for(int i=0; i<sources.size(); i++){
			components.get(0).add(sources.get(i));
		}
		//components[0], azaz sources k�sz.
		
		
		int szint=0;
		while(list.size()>0){
			for(int i=0; i<components.get(szint).size(); i++){ //minden forr�s
				for(int j=0; j<components.get(szint).get(i).wireOut.size();j++){ //minden kimenet wire�nek
					for(int k=0; k<components.get(szint).get(i).wireOut.get(j).objectsOut.size();k++){ //minden kimenete
						DigitalObject current=components.get(szint).get(i).wireOut.get(j).objectsOut.get(k);
						if(-1==list.indexOf(current)){
							//ha m�r volt, 
							//a) feedback
							//b) hierarchia n�het
							if(getSzint(current)<szint+1) foo(); //feed back lesz TODO
							if(getSzint(current)>=szint+1) moveToSzint(current, szint+1); //n� a szintje
							
						} else {
							//ha m�g nem volt
							moveToSzint(components.get(szint).get(i).wireOut.get(j).objectsOut.get(k), szint+1);
						}
					}
				}
			}
			
			szint++;
		}//while list.size>0
		
		componentsArg=components;
		
	}//countHierarchy
}//class
