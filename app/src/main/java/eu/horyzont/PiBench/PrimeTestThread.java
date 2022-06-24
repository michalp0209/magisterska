package eu.horyzont.PiBench;

import java.math.BigDecimal;
import java.math.MathContext;

import ch.obermuhlner.math.big.BigDecimalMath;

public class PrimeTestThread implements Runnable {

    MainActivity main;
    PrimeTestController controller;

    public PrimeTestThread(MainActivity main, PrimeTestController ptc){
        this.main = main;
        controller = ptc;
    }

    @Override
    public void run(){
        while(true) {
            int p = controller.takeValue();
            //main.updateWindow(p + "\n");
            if(p==0) break;

            BigDecimal checkNumber = BigDecimalMath.pow(BigDecimal.valueOf(2), p, new MathContext(32000));
            checkNumber = checkNumber.subtract(BigDecimal.valueOf(1));

            // First number of the series
            BigDecimal nextval = BigDecimal.valueOf(4).remainder(checkNumber);

            // Generate the rest (p-2) terms
            // of the series.
            for (int i = 1; i < p - 1; i++) {
                nextval = nextval.multiply(nextval);
                nextval = nextval.subtract(BigDecimal.valueOf(2));
                nextval = nextval.remainder(checkNumber);
            }
            //main.updateWindow("Rem: " + nextval.toString() + "\n");
            // now if the (p-1)th term is 0 return true else false.
            if (nextval.equals(BigDecimal.valueOf(0))) main.updateWindow("Wątek " + Thread.currentThread().getName() + ": 2^" + p + "-1 To liczba pierwsza\n");
            else main.updateWindow("Wątek " + Thread.currentThread().getName() + ": 2^" + p + "-1 To nie liczba pierwsza\n");
        }
        main.updateWindow("Wątek " + Thread.currentThread().getName() +" zakończony\n");
        main.setPrimeTestFinished(controller);
    }
}
