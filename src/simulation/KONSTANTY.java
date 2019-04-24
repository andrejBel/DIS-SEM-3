package simulation;

import Utils.Helper;

public class KONSTANTY {

    public static final double NAJSKORSI_PRICHOD_NA_ZASTAVKU = 75.0 * 60.0;
    public static final double NAJNESKORSI_PRICHOD_NA_ZASTAVKU = 10.0 * 60.0;
    public static final String STADION = "ST";
    public static final double NULL_TIME = -1.0;
    public static final double KOLKO_CAKA_PO_NASTUPE_VSETKYCH_CESTUJUCICH = 90.0;
    public static final String DEFAULT_KONFIGURACIA = "default.csv";
    public static final double KEDY_JE_OCHOTNY_NASTUPIT_DO_MINIBUSU = Helper.CASOVE_JEDNOTKY.MINUTA.getPocetSekund() * 6;

}
