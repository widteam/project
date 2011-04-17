import java.util.ArrayList;
import java.util.List;

public class Composit extends DigitalObject {
	/* ATTRIB�TUMOK */

	private ArrayList<List<DigitalObject>> ComponentList;
	/*
	 * Le�r�s: Ez az attrib�tum t�rolja az �sszes kaput, kimenetet, bemenetet
	 * hierarchikus sorrendben Ez nem m�s, mint egy list�b�l szervezett t�mb. A
	 * t�mb indexe azonos�tja a hierarchia szintet (0-Forr�sok, 1-a forr�sokhoz
	 * csatlakoz� elemek, stb) az egyes szinteken pedig egy lista van az
	 * elemekr�l
	 */
	private List<Wire> WireList;

	// Le�r�s: Egyszer� lista a Wire objektumokb�l

	/* KONSTRUKTOR */
	public Composit(String strOwnerCompositName,String CompositName) {		
		final String strClassName = this.getClass().getName();
		ID = strOwnerCompositName + strIDDelimiter + strClassName + strIDDelimiter + CompositName;
		WireList = new ArrayList<Wire>();
		wireIn = new ArrayList<Wire>(); // Inicializaljuk a wireIn listat
		wireOut = new ArrayList<Wire>();// Inicializaljuk a wireOut listat
		ComponentList = new ArrayList<List<DigitalObject>>();
		ComponentList.add(new ArrayList<DigitalObject>());
	}


	/* MET�DUSOK */
	public void SetFrequency(int Frequency, String ElementID) {
		// Le�r�s: A param�terben megadott azonos�t�j� GENERATOR objektum
		// frekvenci�j�t m�dos�tja
		GENERATOR tmp;
		tmp = (GENERATOR) GetElementByID(ElementID);
		tmp.SetFrequency(Frequency);
	};

	public void SetSequence(int Sequence, String ElementID) {
		// Le�r�s: A param�terben megadott azonos�t�j� GENERATOR objektum
		// szekvenci�j�t m�dos�tja
		GENERATOR GEN_to_setsequence; /* Tempor�lis v�ltoz� */
		GEN_to_setsequence = (GENERATOR) GetElementByID(ElementID); /*
																	 * GetElemetByIDvel
																	 * megkapjuk
																	 * , az
																	 * objektumot
																	 */
		GEN_to_setsequence.SetSequence(Sequence); /*
												 * az gener�tor objektum
												 * SetSequence(...) met�dus�t
												 * megh�vjuk
												 */

	};
	public void SetSwitch(int Value, String ElementID) {
		SWITCH SW_to_set; /* Tempor�lis v�ltoz� */
		SW_to_set = (SWITCH) GetElementByID(ElementID); 
		SW_to_set.Value = Value;

	};
	public void Toggle(String ElementID) {
		/*
		 * Le�r�s: A param�terben megadott azonos�t�j� SWITCH objektum �rt�k�t
		 * az ellenkez�re �ll�tja az�ltal, hogy megh�vja az objektum hasonl�
		 * nev� param�ter�t
		 */
		SWITCH SWITCH_to_toggle; /* Tempor�lis v�ltoz� */
		SWITCH_to_toggle = (SWITCH) GetElementByID(ElementID); /*
																 * GetElemetByIDvel
																 * megkapjuk,
																 * majd egyet
																 */
		SWITCH_to_toggle.Toggle(); /*
									 * az switch objektum Toggle() met�dus�t
									 * megh�vjuk
									 */

	};

	@Override
	public void AddToFeedbacks(DigitalObject feedback) {
		// Le�r�s: Hozz�adja a param�terk�nt kapott DigitalObjectet a Feedbacks
		// t�mbj�hez.
		if (Feedbacks != null)
			Feedbacks.add(feedback); // Hozz�adjuk a feedbackshez
	};

	public DigitalObject GetElementByID(String ElementID) {
		// Le�r�s: Megkeres egy adott elemet a ComponentList list�kban
		
		/**
		 * Teljes ID alapjan
		 */
		if (ComponentList != null && !ComponentList.isEmpty()) {
			for (List<DigitalObject> sublist : ComponentList) {
				for (DigitalObject o : sublist) {
					if (o.ID == ElementID)
						return  o;
				}
			}
		}
		return null;
	}
	public DigitalObject GetElementByName(String ElementName) {
		/**
		 * Nev alapjan
		 */
		if (ComponentList != null && !ComponentList.isEmpty()) {
			for (List<DigitalObject> sublist : ComponentList) {
				for (DigitalObject o : sublist) {
					if(o.GetName().equalsIgnoreCase(ElementName))
						return  o;
				}
			}
		}
		return null;
	}
	
	public Wire GetWireByID(String WireID) {
		// Le�r�s: Megkeres egy adott dr�tot a WireList list�kban
		if (WireList != null && !WireList.isEmpty()) {
			for (Wire w : WireList) {
				if (w.GetID() == WireID)
					return w;
			}
		}
		return null;
	}
	public Wire GetWireByName(String WireName) {
		// Le�r�s: Megkeres egy adott dr�tot a WireList list�kban
		if (WireList != null && !WireList.isEmpty()) {
			for (Wire w : WireList) {
				if(w.GetName().equalsIgnoreCase(WireName))
					return w;
			}
		}
		return null;
	}	

	/*
	 * Itt van egy kisebb probl�ma. K�v�lr�l kell stepet biztos�tani, de bl�lr�l
	 * meg step components van. Step h�vjon stepcomponentset �s mondja meg hogy
	 * a r�szobjektumok stabilak-e?
	 * 
	 * @see DigitalObject#Step()
	 */
	@Override
	public boolean Step() {
		// TODO Auto-generated method stub
		return false;
	};

	public int Count() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void StepComponents() {
		// Le�r�s: Megh�vja az �sszes iComponent interf�szt megval�s�t� objektum
		// Step() met�dus�t.

		/*
		 * Elvileg m�r fel van �p�lve a hierarchia �gy nekem el�g megkapnom a
		 * ComponentListet
		 */
		DigitalObject obj;
		for (List<DigitalObject> sublist : ComponentList) {
			for (DigitalObject o : sublist) {
				obj = (DigitalObject) o;
				obj.Step();
			}
		}
	}

	/**
	 * Hozzaad a WireList-hez egy elemet
	 * @param wire
	 */
	public void AddToWireList(Wire wire) {
		WireList.add(wire);
	}
	/**
	 * Elvesz a WireList-bol egy elemet
	 * @param wire
	 */
	public boolean RemoveFromWireList(Wire w){
		return WireList.remove(w);
	}
	
	public void buildHierarchy(List<DigitalObject> tmp_components){
		/*
		 * Elso lepesben az osszes Input objektumot hozzaadjuk a Hierarchia 0.
		 * szintjehez
		 */
		// 0. szint letrehozasa
		ComponentList.add(new ArrayList<DigitalObject>());
		for (DigitalObject obj : tmp_components) {
			if (obj.GetType().equalsIgnoreCase("SWITCH")
					|| obj.GetType().equalsIgnoreCase("GENERATOR")) {
				/* Hozzadjuk a szinthez az elemet */
				ComponentList.get(0).add(obj);
			}
		}
		/* Eltavolitjuk a listabol a mar hierarchikus elemeket */
		tmp_components.removeAll(ComponentList.get(0));

		/* Amig van elem a listaban */

		int iHierarchy = 0;
		while (!tmp_components.isEmpty()) {
			List<DigitalObject> components_next = new ArrayList<DigitalObject>();
			// Vegigmegyunk az aktualis szint osszes elemen
			for (DigitalObject obj : ComponentList.get(iHierarchy)) {
				// az aktualis szint aktualis elemenek kimenetein
				for (Wire out : obj.wireOut) {
					/*
					 * es az aktualis szint aktualis elemenek kimeno drotjai
					 * altal meghatarozott elemet hozzaadjuk a kovetkezo
					 * elemekhez
					 */
					for (DigitalObject wire_obj : out.objectsOut) {
						/*
						 * Itt meg kell nezni hogy korabi szinten szerepelt-e
						 * mar
						 */
						boolean contain = false;

						for (int i = 0; i < iHierarchy + 1; i++) {
							if (ComponentList.get(i).contains(wire_obj)) {
								contain = true;
							}
						}
						// ha nem volt meg korabbi szinten akkor most uj
						// szinthez adjuk
						if (!contain)
							if (!components_next.contains(wire_obj))
								components_next.add(wire_obj);
					}
				}
			}
			tmp_components.removeAll(components_next);
			ComponentList.add(new ArrayList<DigitalObject>());
			ComponentList.get(++iHierarchy).addAll(components_next);
		}
	}
	public void getFeedbacks(){
		// FEEDBACK
		/**
		 * Ha tartalmazza egy korabbi szint az obj elemet, akkor itt egy
		 * visszacsatolas van. Ha visszacsatolas, akkor meg FEEDBACK!
		 */
		int iHierarchy = 0;
		for (List<DigitalObject> sublist : ComponentList) { // vegig az osszes
															// hierarchia
															// szinten
			for (DigitalObject obj : sublist) { // vegig a reszlistakon
				DigitalObject feedback_start = null; // inicializaljuk a
														// feedbacket nullra
				for (Wire out : obj.wireOut) { // az aktualis elem osszes
												// kimenetet nezzuk
					for (DigitalObject wire_obj : out.objectsOut) { // ez az
																	// osszes
																	// elem
																	// amihez
																	// csatlakozik
						/*
						 * Itt meg kell nezni hogy korabi szinten szerepelt-e
						 * mar
						 */
						for (int i = 0; i < iHierarchy + 1; i++) {
							if (ComponentList.get(i).contains(wire_obj)) {
								/**
								 * a FEEDBACK kezdetebol elerheto elemek
								 * megtalalasa Elso lepesben a legalacsonyabb
								 * szintrol indulva felfele felepitunk egy
								 * reszgrafot. Akkor allunk meg ha feljebb
								 * elertuk az also elemet
								 */
								List<DigitalObject> tmp_list = new ArrayList<DigitalObject>();
								feedback_start = ComponentList.get(i).get(
										ComponentList.get(i).indexOf(wire_obj));
								int count = 0;

								DigitalObject listelement;
								tmp_list.add(feedback_start);
								while (!tmp_list.contains(obj)) {
									listelement = tmp_list.get(count++);
									for (Wire w : listelement.wireOut) {
										for (DigitalObject next_element : w.objectsOut) {
											tmp_list.add(next_element);
										}
									}
								}// reszgraf epitesenek vege

								/**
								 * a FEEDBACK vegebol visszafele elerheto elemek
								 * Ahol megegyezik a ket lista (ez es az elozo)
								 * az egy visszacsatolas
								 */
								feedback_start.Feedbacks.add(obj);
								count = 0;
								boolean added = false;
								while (true) {// feedback_start.Feedbacks.contains(feedback_start)){
									listelement = feedback_start.Feedbacks
											.get(count);
									if (listelement == feedback_start) {
										break;
									}
									for (Wire w : listelement.wireIn) {
										for (DigitalObject next_element : w.objectsIn) {
											if (tmp_list.contains(next_element)) {
												feedback_start.Feedbacks
														.add(next_element);
												added = true;
											}
										}
									}
									if (added)
										count++;

								}// feedback epitesenek vege
							}// if vege: szerepelt-e korabban
						}// vege: korabbi szintek vegignezese
					}// vege: kimeno drotokon levo objektumok
				}// vege:kimeno drotok
			}// vege: sublistban az elemek
			iHierarchy++;
		}// vege: hierarchiak		
	}	
	public void Debug(){
		/* KIIRATAS, DEBUG */
		int szint = 0;
		for (List<DigitalObject> sublist : ComponentList) {
			System.out.println();
			System.out.print("Szint: ");
			System.out.print(szint++);
			System.out.print("\t");
			for (DigitalObject o : sublist) {
				System.out.print(o.GetID() + ", ");
				if (o.Feedbacks != null && !o.Feedbacks.isEmpty()) {
					System.out.print("FEEDBACK [");
					for (DigitalObject f_o : o.Feedbacks) {
						System.out.print(" " + f_o.ID + ", ");
					}
					System.out.print("]");
				}
			}
		}
	}
	public List<DigitalObject> getFirstLevelOfComponentList(){
		return ComponentList.get(0);
	}
}