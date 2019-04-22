package instantAssistants;

import Model.Cestujuci;
import Model.Enumeracie.STAV_CESTUJUCI;
import Model.Vozidlo;
import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="280"
public class AkciaNastupCestujuceho extends Action {
	public AkciaNastupCestujuceho(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message) {
		Sprava copy = (Sprava) message.createCopy(); // todo ci kopia, skor kopia
		Vozidlo vozidlo = copy.getVozidlo();
		Cestujuci cestujuci = copy.getCestujuci();
		Integer volneDvere = vozidlo.obsadPrvyIndexVolnychDveri();
		if (volneDvere == null) {
			throw new RuntimeException("Dvere musia byt volne");
		}

		if (!vozidlo.jeVolneMiestoVoVozidle()) {
			throw new RuntimeException("Vo vozidle musi byt miesto");
		}
		cestujuci.setIndexNastupnychDveri(volneDvere);
		cestujuci.setVozidlo(vozidlo);
		cestujuci.setStavCestujuci(STAV_CESTUJUCI.NASTUPUJE_DO_VOZIDLA);
		vozidlo.pridajCestujucehoNaNastup(copy);
		copy.setAddressee(mySim().findAgent(Id.agentNastupuVystupu));
		copy.setCode(Mc.nastupCestujuceho);
		myAgent().manager().request(copy);
	}

	@Override
	public AgentPrepravy myAgent() {
		return (AgentPrepravy)super.myAgent();
	}

}
