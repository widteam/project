import java.util.*;	// List, ArrayList-hez

//committok miatt k�l�n class, v�g�n majd bele lehet rakni egybe, de igy nem ny�lok keresztbe nektek

public class HierarchyCounter {
	
	public void CountHierarchy(List<Wire> wires, ArrayList<List<DigitalObject>> components){
		List<DigitalObject> sources=new ArrayList<DigitalObject>(); //forr�sokat k�l�nvessz�k
		List<DigitalObject> list=new ArrayList<DigitalObject>(); //bej�ratlan
		list.clear();
		//componentn�l m�s a source!! ez todo
		for(int i=0 ; i<components.size(); i++){
			for(int j=0; j<components.get(i).size();j++){
				String classs=components.get(i).get(j).getClass().toString();
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
						if(-1==list.indexOf(components.get(szint).get(i).wireOut.get(j).objectsOut.get(k))){
							//ha m�r volt, 
							//a) feedback
							//b) hierarchia n�het
							
						} else {
							//ha m�g nem volt
							if(components.size()<szint+1) components.add(new ArrayList<DigitalObject>()); //ha nincs m�g szint+1 hierarchia
							components.get(szint+1).add(components.get(szint).get(i).wireOut.get(j).objectsOut.get(k));//hozz�adjuk a k�v. hierarchi�ra
							list.remove(components.get(szint).get(i).wireOut.get(j).objectsOut.get(k)); //�s levessz�k a bej�ratlan list�r�l
						}
					}
				}
			}
			
			szint++;
		}
		
	}
}
