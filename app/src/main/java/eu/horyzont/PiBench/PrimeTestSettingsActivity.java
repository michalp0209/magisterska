package eu.horyzont.PiBench;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PrimeTestSettingsActivity extends AppCompatActivity {

    private static int threads = 1;
    private static int mersenne = 1279;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime_test_settings);

        Spinner threadsSpinner = (Spinner) findViewById(R.id.threads);
        Spinner mersenneNumbersSpinner = (Spinner) findViewById(R.id.mersenneNumbers);

        ArrayAdapter<CharSequence> threadAdapter = ArrayAdapter.createFromResource(this, R.array.threads, android.R.layout.simple_spinner_item);
        threadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        threadsSpinner.setAdapter(threadAdapter);
        int val = getIntent().getIntExtra("Threads", 1);
        threadsSpinner.setSelection(threadAdapter.getPosition(Integer.toString(val)));

        ArrayAdapter<CharSequence> mersenneAdapter = ArrayAdapter.createFromResource(this, R.array.mersenneNumbers, android.R.layout.simple_spinner_item);
        mersenneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mersenneNumbersSpinner.setAdapter(mersenneAdapter);
        val = getIntent().getIntExtra("Mersenne",1279);
        mersenneNumbersSpinner.setSelection(mersenneAdapter.getPosition(Integer.toString(val)));

        threadsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                threads = Integer.parseInt((String)adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mersenneNumbersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mersenne = Integer.parseInt((String)adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public static int getThreads() {
        return threads;
    }

    public static int getMersenne() {
        return mersenne;
    }
}