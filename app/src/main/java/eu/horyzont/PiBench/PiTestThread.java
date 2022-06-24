package eu.horyzont.PiBench;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalTime;

import ch.obermuhlner.math.big.BigDecimalMath;

public class PiTestThread implements Runnable {
    private int iterations;
    private int scale = PiTestSettingsActivity.getPrecision();
    private MathContext precision = new MathContext(scale,RoundingMode.HALF_UP);
    private Boolean save = PiTestSettingsActivity.getSaveToFile();
    private Boolean checkPrecision = PiTestSettingsActivity.getCheckPrecision();
    private MainActivity main;

    private BigDecimal wynik = new BigDecimal(0);

    public PiTestThread(int i, MainActivity main){
        iterations = i;
        this.main = main;
    }
    @Override
    public void run() {
        LocalTime start = LocalTime.now();
        LocalTime koniec;
        BigDecimal a = new BigDecimal(1.0);

        BigDecimal b = new BigDecimal(2);
        b = BigDecimalMath.sqrt(b,precision);
        b = a.divide(b,scale, RoundingMode.HALF_UP);

        BigDecimal t = new BigDecimal(1.0);
        t = t.divide(BigDecimal.valueOf(4.0),scale,RoundingMode.HALF_UP);

        BigDecimal p = new BigDecimal(1.0);
        BigDecimal ap;
        int j=1;
        for(j=1; j<=iterations; j++) {
            ap = a;

            a = a.add(b);
            a = a.divide(BigDecimal.valueOf(2),scale,RoundingMode.HALF_UP);

            b = b.multiply(ap);
            b = BigDecimalMath.sqrt(b,precision);

            BigDecimal dod = new BigDecimal(0);
            dod = dod.add(ap.subtract(a));
            dod = dod.pow(2);
            dod = dod.multiply(p);
            t = t.subtract(dod);

            p = p.multiply(BigDecimal.valueOf(2));

            koniec = LocalTime.now();
            main.updateWindow("Iteracja " + j + " zakończona: " + koniec.minusSeconds(start.toSecondOfDay()).toString() + "\n");
        }

        wynik = new BigDecimal(0);
        wynik = wynik.add(a.add(b));
        wynik = wynik.pow(2);
        wynik = wynik.divide(t.multiply(BigDecimal.valueOf(4)),scale,RoundingMode.HALF_UP);
        main.updateWindow("Zakończono obliczanie Pi" + "\n");

        koniec = LocalTime.now();

        main.updateWindow("Długość: " + koniec.minusSeconds(start.toSecondOfDay()).toString() + "\n");

        if(save) main.writeFileExternalStorage(wynik.toString(),"pi.txt");

        if(checkPrecision) {
            main.updateWindow("Sprawdzanie precyzji..." + "\n");
            String w = wynik.toString();
            String pi = main.readPiFromFile();
            int c = 0;
            for(int i=0; i<w.length(); i++){
                if(w.charAt(i) == pi.charAt(i)) c++;
                else break;
                //if(c%10000==0) main.updateWindow("Precyzja: " + c + "\n");
            }
            main.updateWindow("Precyzja (test): " + c + "\n");
        }

        main.setRamTestFinished();
    }
}
