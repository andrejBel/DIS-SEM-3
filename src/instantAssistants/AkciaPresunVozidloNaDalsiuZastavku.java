package instantAssistants;

import Model.Vozidlo;
import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="267"
public class AkciaPresunVozidloNaDalsiuZastavku extends Action {
	public AkciaPresunVozidloNaDalsiuZastavku(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message) {
		Sprava copy = (Sprava) message.createCopy(); // TODO rozhodnutie ci kopia ci nie
		Vozidlo vozidlo = copy.getVozidlo();
		if (vozidlo.getPocetNastupujucichCestujucich() != 0) {
			throw new RuntimeException("Nikto nemoze nastupovat, proces nastupu neukonceny");
		}
		if (vozidlo.getPocetVystupujucichCestujucich() != 0) {
			throw new RuntimeException("Nikto nemoze vystupovat, proces vystupu neukonceny");
		}

		copy.setAddressee(mySim().findAgent(Id.agentPohybu));
		copy.setCode(Mc.presunVozidlaNaDalsiuZastavku);
		myAgent().manager().notice(copy);
	}

	@Override
	public AgentPrepravy myAgent() {
		return (AgentPrepravy)super.myAgent();
	}

}
