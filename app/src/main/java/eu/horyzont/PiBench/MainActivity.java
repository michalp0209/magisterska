package eu.horyzont.PiBench;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button piTest, primeTest;
    private ImageButton piTestSettings, primeTestSettings;
    private TextView resultWindow;
    private int threadsFinished = 0;
    private boolean testFinished = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        piTest = (Button) findViewById(R.id.pi_test);
        primeTest = (Button) findViewById(R.id.prime_test);
        piTestSettings = (ImageButton) findViewById(R.id.pi_test_settings);
        primeTestSettings = (ImageButton) findViewById(R.id.prime_test_settings);
        resultWindow = (TextView) findViewById(R.id.result_window);
        resultWindow.setMovementMethod(new ScrollingMovementMethod());
        resultWindow.setText("");

        piTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (testFinished == true) {
                    updateWindow("Precyzja: " + PiTestSettingsActivity.getPrecision() + "\n");

                    PiTestThread testThread = new PiTestThread(PiTestSettingsActivity.getIterations(), MainActivity.this);
                    Thread[] test = new Thread[PiTestSettingsActivity.getThreads()];
                    for (int i = 0; i < PiTestSettingsActivity.getThreads(); i++) {
                        test[i] = new Thread(testThread);
                        test[i].start();
                        updateWindow("Wątek " + i + " uruchomiony\n");
                    }
                    testFinished = false;
                }
                else Toast.makeText(getApplicationContext(),"Test wciąż trwa",Toast.LENGTH_SHORT).show();
            }
        });
        piTestSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (testFinished == true) {
                    Intent currentSettings = new Intent(MainActivity.this, PiTestSettingsActivity.class);
                    currentSettings.putExtra("Iterations", PiTestSettingsActivity.getIterations());
                    currentSettings.putExtra("Precision", PiTestSettingsActivity.getPrecision());
                    currentSettings.putExtra("Save", PiTestSettingsActivity.getSaveToFile());
                    currentSettings.putExtra("Threads", PiTestSettingsActivity.getThreads());
                    currentSettings.putExtra("CheckPrecision", PiTestSettingsActivity.getCheckPrecision());
                    startActivity(currentSettings);
                } else Toast.makeText(getApplicationContext(),"Test wciaz trwa", Toast.LENGTH_SHORT).show();
            }
        });
        primeTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (testFinished == true) {
                    updateWindow("Liczby Mersenne'a: " + PrimeTestSettingsActivity.getMersenne() + "\n");

                    PrimeTestController ptc = new PrimeTestController(PrimeTestSettingsActivity.getMersenne());
                    PrimeTestThread testThread = new PrimeTestThread(MainActivity.this,ptc);
                    Thread[] test = new Thread[PrimeTestSettingsActivity.getThreads()];
                    ptc.setTimer();
                    for (int i = 0; i < test.length; i++) {
                        test[i] = new Thread(testThread);
                        test[i].setName(""+i);
                        test[i].start();
                        updateWindow("Wątek " + i + " uruchomiony\n");
                    }
                    testFinished = false;
                }
                else Toast.makeText(getApplicationContext(),"Test wciąż trwa",Toast.LENGTH_SHORT).show();
            }
        });

        primeTestSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (testFinished == true) {
                    Intent currentSettings = new Intent(MainActivity.this, PrimeTestSettingsActivity.class);
                    currentSettings.putExtra("Threads",PrimeTestSettingsActivity.getThreads());
                    currentSettings.putExtra("Mersenne",PrimeTestSettingsActivity.getMersenne());
                    startActivity(currentSettings);
                } else Toast.makeText(getApplicationContext(),"Test wciaz trwa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public synchronized void setRamTestFinished(){
        threadsFinished++;
        if(threadsFinished== PiTestSettingsActivity.getThreads()){
            testFinished = true;
            threadsFinished = 0;
        }
    }

    public synchronized void setPrimeTestFinished(PrimeTestController ptc){
        threadsFinished++;
        if(threadsFinished==PrimeTestSettingsActivity.getThreads()){
            testFinished = true;
            threadsFinished = 0;
            ptc.setFinishTimer();
            updateWindow("Czas trwania: " + ptc.getTime().toString());
        }
    }

    public synchronized void updateWindow(String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultWindow.append(text);
            }
        });
        //resultWindow.append(text);
    }

    public synchronized void writeFileExternalStorage(String textToWrite, String filename) {

        //Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {

            //If it isn't mounted - we can't write into it.
            return;
        }

        //Create a new file that points to the root directory, with the given name:
        File file = new File(getExternalFilesDir(null), filename);
        file.delete();

        //This point and below is responsible for the write operation
        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            //second argument of FileOutputStream constructor indicates whether
            //to append or create new file if one exists
            outputStream = new FileOutputStream(file, true);
            outputStream.write(textToWrite.getBytes());
            outputStream.flush();
            outputStream.close();
            updateWindow("Zapisano\n");
        } catch (Exception e) {
            e.printStackTrace();
            updateWindow("Nie zapisano - błąd\n");
        }
    }
    public synchronized String readPiFromFile() {
        //Get the text file
        File file = new File(getExternalFilesDir(null), "pi512k.txt");

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                //text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
            e.printStackTrace();
            updateWindow("Nie wczytano pi z pliku");
        }
        return text.toString();
    }
}