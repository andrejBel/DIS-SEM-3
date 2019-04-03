package Simulacia;

public class SimulaciaWrapper {


    private SimulaciaRestauracia simulacia_ = new SimulaciaRestauracia(1, 1000, 0, false, false, false,false, 0, 0);
    private SimulaciaRestauracia simulaciaTestZavislostiCasnici_ = new SimulaciaRestauracia(1, 0,0, true, false, false, false, 0, 0);
    private SimulaciaRestauracia simulaciaTestZavislostiKuchari_ = new SimulaciaRestauracia(1, 0,0, true, false, false, false, 0, 0);

    public void spustiSimulaciu(int pocetCasnikov, int pocetKucharov, double koncovyCasReplikacie, long jednotkovyPosunSimulacnehoCasu,long spomalenieCasu, long pocetReplikacii,  boolean zrychlenaSimulacia, boolean krokovanieSimulacie, boolean chladenie, boolean planujPosunCasu) {


        simulacia_
        .setPocetCasnikov(pocetCasnikov)
        .setPocetKucharov(pocetKucharov)
        .setKoncovyCasReplikacie(koncovyCasReplikacie)
        .setJednotkovyPosunSimulacnehoCasu(jednotkovyPosunSimulacnehoCasu)
        .setSpomalenieCasu(spomalenieCasu)
        .setPocetReplikacii(pocetReplikacii)
        .setZrychlenaSimulacia(zrychlenaSimulacia)
        .setKrokovanieSimulacie(krokovanieSimulacie)
        .setChladenie(chladenie)
        .setPlanujPosunCasu(planujPosunCasu);

        simulacia_.startAsync();
    }


    public void stop() {
        simulacia_.stop();
    }

    public void pause() {
        simulacia_.pause();
    }

    public void resume() {
        simulacia_.resume();
    }

    public SimulaciaRestauracia getSimulacia() {
        return simulacia_;
    }

    public void setSpomalenieCasu(long spomalenieCasu) {
        simulacia_.setSpomalenieCasu(spomalenieCasu);
    }

    public void spustiZavislostCasnici(long pocetReplikacii, long pocetKucharov, long minimalnyPocetCasnikov, long maximalnyPocetCasnikov, boolean chladenie) {

        int pocetSimulacii = (int) (maximalnyPocetCasnikov - minimalnyPocetCasnikov + 1);
        simulaciaTestZavislostiCasnici_
                .setPocetKucharov((int) pocetKucharov)
                .setPocetCasnikov((int) minimalnyPocetCasnikov)
                .setGrafZavislostiCasnici(true)
                .setPocetReplikacii(pocetReplikacii)
                .setPocetSimulacii(pocetSimulacii)
                .setChladenie(chladenie)
                ;

        simulaciaTestZavislostiCasnici_.startAsync();
    }

    public void spustiZavislostKuchari(long pocetReplikacii, long pocetCasnikov, long minimalnyPocetKucharov, long maximalnyPocetKucharov, boolean chladenie) {

        int pocetSimulacii = (int) (maximalnyPocetKucharov - minimalnyPocetKucharov + 1);

        simulaciaTestZavislostiKuchari_
                .setPocetKucharov((int) minimalnyPocetKucharov)
                .setPocetCasnikov((int) pocetCasnikov)
                .setGrafZavislostiKuchari(true)
                .setPocetReplikacii(pocetReplikacii)
                .setPocetSimulacii(pocetSimulacii)
                .setChladenie(chladenie)
        ;

        simulaciaTestZavislostiKuchari_.startAsync();
    }

    public SimulaciaRestauracia getSimulaciaTestZavislostiCasnici() {
        return simulaciaTestZavislostiCasnici_;
    }

    public SimulaciaRestauracia getSimulaciaTestZavislostiKuchari() {
        return simulaciaTestZavislostiKuchari_;
    }
}
