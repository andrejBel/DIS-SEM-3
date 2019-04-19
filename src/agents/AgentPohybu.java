package agents;

import Model.*;
import Model.Enumeracie.TYP_LINKY;
import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;

import java.util.ArrayList;
import java.util.HashMap;

//meta! id="86"
public class AgentPohybu extends Agent {

    private HashMap<TYP_LINKY, Linka> _linky;
    private ArrayList<Vozidlo> _vozidla;


    public AgentPohybu(int id, Simulation mySim, Agent parent, HashMap<TYP_LINKY, Linka> linky) {
		super(id, mySim, parent);
        this._linky = linky;
        this._vozidla = new ArrayList<>();
		init();
	}



	@Override
	public void prepareReplication() {
		super.prepareReplication();

		for (Vozidlo vozidlo: _vozidla) {
		    vozidlo.beforeReplication();
        }
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init() {
		new ManagerPohybu(Id.managerPohybu, mySim(), this);
		new AsistentPresunu(Id.asistentPresunu, mySim(), this);
		new AsistentVyjazdu(Id.asistentVyjazdu, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.presunVozidlaNaDalsiuZastavku);
		addOwnMessage(Mc.start);
		addOwnMessage(Mc.prichodVozidlaNaZastavku);
		addOwnMessage(Mc.finish);
	}



	public void inizializujVozidla(ArrayList<VozidloKonfiguracia> konfiguraciaVozidiel) {
        _vozidla.clear();
        long indexerVozidiel = 1;
        for (VozidloKonfiguracia konfiguracia: konfiguraciaVozidiel) {
            _vozidla.add(new Vozidlo(mySim(), indexerVozidiel++, konfiguracia.getCasPrijazduNaPrvuZastavku(), _linky.get(konfiguracia.getTypLinky()), konfiguracia.getTypVozidla()));
        }
	}

    public ArrayList<Vozidlo> getVozidla() {
        return _vozidla;
    }

    //meta! tag="end"
}