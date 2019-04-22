package instantAssistants;

import Model.Cestujuci;
import Model.Enumeracie.STAV_CESTUJUCI;
import Model.Vozidlo;
import OSPABA.*;
import simulation.*;
import agents.*;

//meta! id="277"
public class AkciaVystupCestujuceho extends Action {
	public AkciaVystupCestujuceho(int id, Simulation mySim, CommonAgent myAgent) {
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message) {
		Sprava copy = (Sprava) message.createCopy();
		Vozidlo vozidlo = copy.getVozidlo();
		Integer indexVolnychDveri = vozidlo.obsadPrvyIndexVolnychDveri();
		if (indexVolnychDveri == null) {
			throw new RuntimeException("Dvere musia byt volne!!!");
		}
		if (vozidlo.getCelkovyPocetCestujucichVoVozidle() <= 0) {
			throw new RuntimeException("Vo vozidle musi byt niekto!!!");
		}

		Cestujuci cestujuci = vozidlo.odstranCestujucehoVoVozidle().getCestujuci();
		copy.setCestujuci(cestujuci);
		cestujuci.setStavCestujuci(STAV_CESTUJUCI.VYSTUPUJE_Z_VOZIDLA);
		cestujuci.setIndexVytupnychDveri(indexVolnychDveri);
		vozidlo.pridajCestujucehoNaVystup(copy);

		copy.setAddressee(mySim().findAgent(Id.agentNastupuVystupu));
		copy.setCode(Mc.vystupCestujuceho);
		myAgent().manager().request(copy);
	}

	@Override
	public AgentPrepravy myAgent() {
		return (AgentPrepravy)super.myAgent();
	}

}
