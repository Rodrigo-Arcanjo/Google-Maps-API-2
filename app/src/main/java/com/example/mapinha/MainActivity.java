package com.example.mapinha;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout cod;
    private CardView cardOneView;
    private LinearLayout lnAllLayout, styleMap, lnClickMap, lnEditLogradouro, lnEditBairro, lnEditReferencia;

    private TextView titleAplication, titleTypeMap, titleStartMap, titleLogradouro, titleBairro, titleReferencia, tvLocal;
    private RadioGroup rgStyleMap;
    private RadioButton rbStyleMapNao, rbStyleMapSim;
    private ImageView imgMap;
    private ProgressBar progressMap;
    private ImageButton btnSearchLogradouro, btnSearchBairro;
    private EditText edtLogradouro, edtBairro, edtReferencia, edtNumero;

    private double latitudeD = 0, longitudeD = 0;

    private String snapShotMapa;
    public boolean style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cod = findViewById(R.id.cod);
        cardOneView = findViewById(R.id.cardOneView);

        lnAllLayout = findViewById(R.id.lnAllLayout);
        styleMap = findViewById(R.id.styleMap);
        lnClickMap = findViewById(R.id.lnClickMap);
        lnEditLogradouro = findViewById(R.id.lnEditLogradouro);
        lnEditBairro = findViewById(R.id.lnEditBairro);
        lnEditReferencia = findViewById(R.id.lnEditReferencia);

        titleAplication = findViewById(R.id.titleAplication);
        titleTypeMap = findViewById(R.id.titleTypeMap);
        titleStartMap = findViewById(R.id.titleStartMap);
        titleLogradouro = findViewById(R.id.titleLogradouro);
        titleBairro = findViewById(R.id.titleBairro);
        titleReferencia = findViewById(R.id.titleReferencia);

        rgStyleMap = findViewById(R.id.rgStyleMap);
        rbStyleMapNao = findViewById(R.id.rbStyleMapNao);
        rbStyleMapSim = findViewById(R.id.rbStyleMapSim);

        imgMap = findViewById(R.id.imgMap);
        btnSearchLogradouro = findViewById(R.id.btnSearchLogradouro);
        btnSearchBairro = findViewById(R.id.btnSearchBairro);

        edtLogradouro = findViewById(R.id.edtLogradouro);
        edtBairro = findViewById(R.id.edtBairro);
        edtReferencia = findViewById(R.id.edtReferencia);
        edtNumero = findViewById(R.id.edtNumero);

        progressMap = findViewById(R.id.progressMap);

        rgStyleMap.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                RadioButton radioBtn = (RadioButton) group.findViewById(checkedId);
                String texto = radioBtn.getText().toString();

                try {
                    if (texto.trim().equalsIgnoreCase("Sim")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "SIM!!!", Toast.LENGTH_SHORT);
                        toast.show();
                        style = true;
                    } else if (texto.trim().equalsIgnoreCase("Não")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "NÃO!!!", Toast.LENGTH_SHORT);
                        toast.show();
                        style = false;
                    }

                } catch (Exception e) {
                    Log.i("EXCEPTION RADIO GROUP: " , "EXCEPTION --> " + e.getMessage().toString());
                }
            }
        });

        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressMap.setVisibility(View.VISIBLE);

                try {
                    Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vb.vibrate(50);

                } catch (Exception e) {

                }

                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                i.putExtra("latitude", latitudeD);
                i.putExtra("longitude", longitudeD);
                i.putExtra("flag", style);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(i, 0);
            }
        });

        btnSearchLogradouro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.putExtra("FLAG", "LOGRADOURO");
                startActivityForResult(i, 2);
            }
        });

        btnSearchBairro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                i.putExtra("FLAG", "BAIRRO");
                startActivityForResult(i, 3);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        progressMap.setVisibility(View.GONE);

        if (resultCode == RESULT_OK && requestCode == 0) {
            Localizacao acidenteLocal1 = (Localizacao) data.getSerializableExtra("acidenteLocal");
            if (acidenteLocal1 != null) {

                edtBairro.setText(acidenteLocal1.getACI_Bairro());
                edtLogradouro.setText(acidenteLocal1.getACI_Logradouro());
                edtNumero.setText(acidenteLocal1.getACI_Numero());

                latitudeD = acidenteLocal1.getACI_Latitude();
                longitudeD = acidenteLocal1.getACI_Longitude();

                try {
                    snapShotMapa = acidenteLocal1.getACI_Local_Img();
                    imgMap.setImageBitmap(new Foto().StringToBitMap(snapShotMapa));
                    imgMap.setTag("image");
                    titleStartMap.setError(null);

                } catch (Exception e) {

                }
            } else {
                //snackBarUtil.createMessageError(getString(R.string.erro_s),null, 0, null);
            }
        }
    }

}