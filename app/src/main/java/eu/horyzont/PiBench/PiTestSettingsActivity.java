package eu.horyzont.PiBench;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

public class PiTestSettingsActivity extends AppCompatActivity {

    private static int iterations = 5;
    private static int precision = 8000;
    private static boolean saveSettings = false;
    private static int threads = 1;
    private static boolean checkPrecision = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pi_test_settings);

        Spinner iterationsSpinner = (Spinner) findViewById(R.id.pi_test_iterations);
        CheckBox saveToFile = (CheckBox) findViewById(R.id.save_pi_test_to_file);
        Spinner precisionSpinner = (Spinner) findViewById(R.id.pi_test_precision);
        Spinner threadsSpinner = (Spinner) findViewById(R.id.threads);
        CheckBox checkPrec = (CheckBox) findViewById(R.id.check_precision);

        ArrayAdapter<CharSequence> precAdapter = ArrayAdapter.createFromResource(this, R.array.pi_test_precision_values, android.R.layout.simple_spinner_item);
        precAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        precisionSpinner.setAdapter(precAdapter);
        int val = getIntent().getIntExtra("Precision",16000);
        precisionSpinner.setSelection(precAdapter.getPosition(Integer.toString(val)));

        ArrayAdapter<CharSequence> iterAdapter = ArrayAdapter.createFromResource(this, R.array.pi_test_iterations, android.R.layout.simple_spinner_item);
        iterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iterationsSpinner.setAdapter(iterAdapter);
        val = getIntent().getIntExtra("Iterations",5);
        iterationsSpinner.setSelection(iterAdapter.getPosition(Integer.toString(val)));

        ArrayAdapter<CharSequence> threadAdapter = ArrayAdapter.createFromResource(this, R.array.threads, android.R.layout.simple_spinner_item);
        threadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        threadsSpinner.setAdapter(threadAdapter);
        val = getIntent().getIntExtra("Threads", 1);
        threadsSpinner.setSelection(threadAdapter.getPosition(Integer.toString(val)));

        precisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                precision = Integer.parseInt((String)adapterView.getItemAtPosition(i));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {            }
        });

        iterationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                iterations = Integer.parseInt((String)adapterView.getItemAtPosition(i));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {            }
        });
        threadsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                threads = Integer.parseInt((String)adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveToFile.setChecked(getIntent().getBooleanExtra("Save", false));

        saveToFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveToFile.isChecked()) saveSettings = true;
                else saveSettings = false;
            }
        });

        checkPrec.setChecked(getIntent().getBooleanExtra("CheckPrecison", false));
        checkPrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPrec.isChecked()) checkPrecision = true;
                else checkPrecision = false;
            }
        });
    }

    public static int getIterations() {
        return iterations;
    }
    public static int getPrecision(){
        return precision;
    }
    public static boolean getSaveToFile(){
        return saveSettings;
    }
    public static int getThreads(){ return threads;}
    public static boolean getCheckPrecision() {
        return checkPrecision;
    }
}