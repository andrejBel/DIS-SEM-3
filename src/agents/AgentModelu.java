package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="1"
public class AgentModelu extends Agent {
	public AgentModelu(int id, Simulation mySim, Agent parent) {
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication() {
		super.prepareReplication();
		// Setup component for the next replication
	}

	private void init() {
		new ManagerModelu(Id.managerModelu, mySim(), this);
        addOwnMessage(Mc.prichodZakaznikaNaZastavku);
        addOwnMessage(Mc.init);
	}

    public void spustiSimulaciu() {
        Sprava sprava = new Sprava(mySim());
        sprava.setAddressee(this);
        sprava.setCode(Mc.init);
        manager().notice(sprava);
    }

    @Override
    public SimulaciaDopravy mySim() {
        return (SimulaciaDopravy) super.mySim();
    }

    //meta! tag="end"
}