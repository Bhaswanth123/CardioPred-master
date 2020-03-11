package com.example.stark.cardiopredictor;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {

    Interpreter tflite;
    Button Submitbtn;
    TextView Result;
    private MappedByteBuffer tfliteModel;
    private  Interpreter.Options tfliteOptions = new Interpreter.Options();
    EditText age, sex, cp, trestbps, chol, fbs, restecg, thalach, exang, oldpeak, slope, ca, thal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Submitbtn = (Button) findViewById(R.id.submit);
        age = (EditText) findViewById(R.id.agevalue);
        sex = (EditText) findViewById(R.id.gendervalue);
        cp = (EditText) findViewById(R.id.cpvalue);
        trestbps = (EditText) findViewById(R.id.tresbpsvalue);
        chol = (EditText) findViewById(R.id.cholvalue);
        fbs = (EditText) findViewById(R.id.fbsvalue);
        restecg = (EditText) findViewById(R.id.restecgvalue);
        thalach = (EditText) findViewById(R.id.thalchvalue);
        exang = (EditText) findViewById(R.id.exangvalue);
        oldpeak = (EditText) findViewById(R.id.oldpeakvalue);
        slope = (EditText) findViewById(R.id.slopevalue);
        ca = (EditText) findViewById(R.id.cavalue);
        thal = (EditText) findViewById(R.id.thalvalue);

        Result = (TextView) findViewById(R.id.result);
        tfliteOptions.setNumThreads(1);
        //tfliteModel = FileUtil.loadMappedFile(activity, getModelPath());


        try{
            tflite = new Interpreter(loadModelFile());
        }
        catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

        }

        Submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float[] input = new float[13];
                float prediction;

                input[0] = Float.parseFloat(age.getText().toString());
                input[1] = Float.parseFloat(sex.getText().toString());
                input[2] = Float.parseFloat(cp.getText().toString());
                input[3] = Float.parseFloat(trestbps.getText().toString());
                input[4] = Float.parseFloat(chol.getText().toString());
                input[5] = Float.parseFloat(fbs.getText().toString());
                input[6] = Float.parseFloat(restecg.getText().toString());
                input[7] = Float.parseFloat(thalach.getText().toString());
                input[8] = Float.parseFloat(exang.getText().toString());
                input[9] = Float.parseFloat(oldpeak.getText().toString());
                input[10] = Float.parseFloat(slope.getText().toString());
                input[11] = Float.parseFloat(ca.getText().toString());
                input[12] = Float.parseFloat(thal.getText().toString());
                //   float fltinput[] = Arrays.stream(input).mapToDouble(Float::newparseDouble);
                prediction = doInference(input);


                Result.setText(String.format(Locale.getDefault(),"%.3f",(prediction)));

            }
        });
    }

    private MappedByteBuffer loadModelFile() throws IOException {

        AssetFileDescriptor assetFileDescriptor = this.getAssets().openFd("cardio.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long declaredLength = assetFileDescriptor.getDeclaredLength();
        Toast.makeText(getApplicationContext(),"Model Loaded Successfully",Toast.LENGTH_LONG).show();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength) ;
    }
    public float doInference(float inputString []) {

        float[] inputVal = new float[13];
        inputVal[0]=inputString[0];
        inputVal[1]=inputString[1];
        inputVal[2]=inputString[2];
        inputVal[3]=inputString[3];
        inputVal[4]=inputString[4];
        inputVal[5]=inputString[5];
        inputVal[6]=inputString[6];
        inputVal[7]=inputString[7];
        inputVal[8]=inputString[8];
        inputVal[9]=inputString[9];
        inputVal[10]=inputString[10];
        inputVal[11]=inputString[11];
        inputVal[12]=inputString[12];

        float [][] outputVal = new float[1][1];
        outputVal[0][0] = 0;


        tflite.run(inputVal,outputVal);
        Toast.makeText(getApplicationContext(),"TF LITE RUN SUCCESSFULLY",Toast.LENGTH_SHORT).show();
        return outputVal[0][0];


    }
}
