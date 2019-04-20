package simulation;

import Model.Cestujuci;
import Model.Vozidlo;
import Model.ZastavkaKonfiguracia;
import OSPABA.*;

public class Sprava extends MessageForm {

	// nenastavuj na null ani keby nic!!!
	private Cestujuci _cestujuci;
	private Vozidlo _vozidlo;
	private ZastavkaKonfiguracia _zastavkaKonfiguracie;

	public Sprava(Simulation sim) {
		super(sim);
	}

	public Sprava(Sprava original) {
		super(original);
		// copy() is called in superclass
	}

	@Override
	public MessageForm createCopy() {
		return new Sprava(this);
	}

	@Override
	protected void copy(MessageForm message) {
		super.copy(message);
		Sprava original = (Sprava)message;
		this._cestujuci = original._cestujuci;
		this._vozidlo = original._vozidlo;
		this._zastavkaKonfiguracie = original._zastavkaKonfiguracie;
	}

	@Override
	public SimulaciaDopravy mySim() {
		return (SimulaciaDopravy) super.mySim();
	}

	public Cestujuci getCestujuci() {
		return _cestujuci;
	}

	public void setCestujuci(Cestujuci cestujuci) {
		this._cestujuci = cestujuci;
	}

	public Vozidlo getVozidlo() {
		return _vozidlo;
	}

	public void setVozidlo(Vozidlo vozidlo) {
		this._vozidlo = vozidlo;
	}

	public ZastavkaKonfiguracia getZastavkaKonfiguracie() {
		return _zastavkaKonfiguracie;
	}

	public void setZastavkaKonfiguracie(ZastavkaKonfiguracia zastavkaKonfiguracie) {
		this._zastavkaKonfiguracie = zastavkaKonfiguracie;
	}

}