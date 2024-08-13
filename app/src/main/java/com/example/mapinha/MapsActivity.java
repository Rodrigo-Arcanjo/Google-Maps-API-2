package com.example.mapinha;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.AdvancedMarker;
import com.google.android.gms.maps.model.AdvancedMarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {


    private GoogleMap mMap; // assa
    Marker marker;
    MarkerOptions markerOp;
    AdvancedMarkerOptions markerAvancedOp;
    AdvancedMarker markerAvanced;

    Location location;
    Geocoder geocoder;

    FragmentActivity fragmentActivity = this;
    Context context = this;
    private View view;
    private SnackBarUtil snackBarUtil;

    LinearLayout imgGetLatLng;
    ImageView imgBackMap, imgActual;
    TextView tvMapEndereco;
    EditText placeACF;
    private ProgressBar progressBar;

    List<Address> addresses;
    List<Place.Field> fields;

    double latitude = 0, longitude = 0;
    String logradouro = "", bairro = "", numero = "";
    private final float ZOOM = 17.0f;
    private boolean execute;
    private boolean flagStyle;
    private int notRepetion = 0;
    int AUTOCOMPLETE_REQUEST_CODE = 1;

    private ReentrantLock lock = new ReentrantLock();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        imgGetLatLng = findViewById(R.id.imgGetLatLng);
        placeACF = findViewById(R.id.placeACF);
        imgBackMap = findViewById(R.id.imgBackMap);
        tvMapEndereco = findViewById(R.id.tvMapEndereco);
        imgActual = findViewById(R.id.imgActual);
        imgBackMap.setBackgroundResource(R.drawable.ic_baseline_arrow_back_24);
        progressBar = findViewById(R.id.progressBar);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));

        view = getWindow().getDecorView();
        snackBarUtil = new SnackBarUtil(view);

        location = getLastBestLocation();
        geocoder = new Geocoder(this, Locale.getDefault());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            latitude = (double) bundle.get("latitude");
            longitude = (double) bundle.get("longitude");
            flagStyle = (boolean) bundle.get("flag");
        }

        imgBackMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                } catch (Exception e) {

                }
            }
        });

        placeACF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fields = Arrays.asList(Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields)
                        .build(context);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        imgGetLatLng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vb.vibrate(50);
                } catch (Exception e) {

                }

                if (latitude == 0 || longitude == 0) {
                    snackBarUtil.createMessage("Toque em algum ponto no mapa", v);
                    return;

                } else if (marker != null) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            final Localizacao loc = new Localizacao();
                            final Intent data = new Intent();
                            final Bundle extras = new Bundle();
                            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                            GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
                                Bitmap bitmap;

                                @Override
                                public void onSnapshotReady(Bitmap snapshot) {
                                    bitmap = snapshot;
                                    try {
                                        if (snapshot != null) {
                                            Bitmap snapCut = Bitmap.createBitmap(snapshot,
                                                    0,
                                                    snapshot.getHeight() / 2 - snapshot.getWidth() / 2,
                                                    (int) snapshot.getWidth(),
                                                    (int) snapshot.getWidth());

                                            snapCut.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);

                                            String sLatitude = String.valueOf(latitude);
                                            String sLongitude = String.valueOf(longitude);

                                            try {
                                                sLatitude = sLatitude.substring(0, 22);
                                            } catch (Exception e) {

                                            }

                                            try {
                                                sLongitude = sLongitude.substring(0, 22);
                                            } catch (Exception e) {

                                            }

                                            loc.setACI_Bairro(bairro);
                                            loc.setACI_Logradouro(logradouro);
                                            loc.setACI_Numero(numero);
                                            loc.setACI_Latitude(Double.parseDouble(sLatitude));
                                            loc.setACI_Longitude(Double.parseDouble(sLongitude));
                                            loc.setACI_Local_Img(Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT));

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    extras.putSerializable("acidenteLocal", loc);
                                                    data.putExtras(extras);
                                                    setResult(RESULT_OK, data);
                                                    finish();
                                                }
                                            });
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            };
                            mMap.snapshot(callback);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(this);

        setAddress(latitude, longitude);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            snackBarUtil.createMessage("Necessário ativar permissões de localização", view );
            return;
        }

        mMap.setMyLocationEnabled(true);

        if (mMap != null) {
            if (latitude == 0 || longitude == 0) {
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location arg0) {
                        if (notRepetion == 0) {
                            latitude = arg0.getLatitude();
                            longitude = arg0.getLongitude();

                            setAddress(latitude, longitude);
                            notRepetion = 1;
                        }
                    }
                });
                return;
            }
            setAddress(latitude, longitude);

        } else {
            setAddress(latitude, longitude);
            progressBar.setVisibility(View.GONE);
            snackBarUtil.createMessage("Não foi possível localizar. Toque no local do acidente no mapa.", view );
        }
    }

    public Location getLastBestLocation() {
        LocationManager lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        Location locationGPS = lManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = lManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) {
            GPSLocationTime = locationGPS.getTime();
        }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if (0 < GPSLocationTime - NetLocationTime) {
            return locationGPS;
        } else {
            return locationNet;
        }
    }

    @Override
    public void onMapClick(final LatLng latLng) {
        progressBar.setVisibility(View.VISIBLE);

        if (!execute) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    execute = true;

                    try {
                        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                        if (addresses != null && addresses.size() != 0) {

                            final Address address = addresses.get(0);
                            if (address.getSubAdminArea() != null) {

                                latitude = latLng.latitude;
                                longitude = latLng.longitude;
                                bairro = address.getSubLocality() != null ? address.getSubLocality() : "";
                                logradouro = address.getThoroughfare() != null ? address.getThoroughfare() : "";
                                numero = address.getSubThoroughfare() != null ? address.getSubThoroughfare() : "";

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        execute = false;
                                        tvMapEndereco.setText(logradouro + " " + numero + " " + bairro);
                                        progressBar.setVisibility(View.INVISIBLE);

                                        if (marker != null) {
                                            marker.remove();
                                        }

                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude, latLng.longitude), ZOOM));

                                        //marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)));
                                        //markerAvanced = (AdvancedMarker) mMap.addMarker(new AdvancedMarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)));

                                        /*PinConfig.Builder pinConfigBuilder = PinConfig.builder();
                                        pinConfigBuilder.setBackgroundColor(Color.GREEN);
                                        PinConfig pinConfig = pinConfigBuilder.build();

                                        AdvancedMarkerOptions advancedMarkerOptions =
                                                new AdvancedMarkerOptions()
                                                        .icon(BitmapDescriptorFactory.fromPinConfig(pinConfig))
                                                        .position(new LatLng(latLng.latitude, latLng.longitude));

                                        marker = mMap.addMarker(advancedMarkerOptions);*/

                                        if (flagStyle == true) {
                                            marker = mMap.addMarker(new MarkerOptions()
                                                    .position(new LatLng(latLng.latitude, latLng.longitude))
                                                    .draggable(true)
                                                    .icon(bitmapDescriptorFromVector(R.drawable.ic_emergencia)));

                                        } else if (flagStyle == false) {
                                            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)));
                                        }

                                    }
                                });
                                return;
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    execute = false;
                                    snackBarUtil.createMessage("Local inválido.", view );
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                            return;
                        }

                        setAddress(latLng.latitude, latLng.longitude);

                    } catch (Exception e) {
                        Log.i("erro_e", e.getMessage());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                execute = false;
                                snackBarUtil.createMessage("Falha na conexão", view);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }
            });
        }
    }

    private BitmapDescriptor createCustomMarker(int markerColor, int borderColor) {

        int markerSize = 80; // Tamanho do marcador
        int borderWidth = 10; // Largura da borda
        int cor = getResources().getColor(R.color.colorOrange);

        Bitmap bitmap = Bitmap.createBitmap(markerSize, markerSize, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        Paint borderPaint = new Paint();
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        Paint markerPaint = new Paint();
        markerPaint.setColor(markerColor);
        markerPaint.setStyle(Paint.Style.FILL);

        // Desenha a borda
        canvas.drawCircle(markerSize / 2, markerSize / 2, markerSize / 2, borderPaint);

        // Desenha o marcador
        canvas.drawCircle(markerSize / 2, markerSize / 2, (markerSize - borderWidth) / 2, markerPaint);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @SuppressLint("ResourceAsColor")
    private BitmapDescriptor bitmapDescriptorFromVector(int vectorResId) {

        // Cria o VectorDrawable
        VectorDrawableCompat vectorDrawable = VectorDrawableCompat.create(getResources(), vectorResId, null);
        if (vectorDrawable == null) {
            return null;
        }

        // Define as dimensões do drawable
        int width = vectorDrawable.getIntrinsicWidth();
        int height = vectorDrawable.getIntrinsicHeight();

        vectorDrawable.setBounds(0, 0, width, height);

        // Cria o bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Cria o canvas e desenha o drawable no bitmap
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }

    private void setAddress(double lat, double lon) {
        lock.lock();

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try{
                        addresses = geocoder.getFromLocation(lat, lon, 1);
                        if (addresses != null && addresses.size() != 0) {
                            final Address address = addresses.get(0);
                            if (address.getSubLocality() != null) {

                                if (marker != null) {
                                    marker.remove();
                                }

                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), ZOOM));

                                if (flagStyle == true) {
                                    marker = mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(lat, lon))
                                            .draggable(true)
                                            .icon(bitmapDescriptorFromVector(R.drawable.ic_emergencia)));

                                } else if (flagStyle == false) {
                                    marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
                                }

                                latitude = lat;
                                longitude = lon;
                                bairro = address.getSubLocality() != null ? address.getSubLocality() : "";
                                logradouro = address.getThoroughfare() != null ? address.getThoroughfare() : "";
                                numero = address.getSubThoroughfare() != null ? address.getSubThoroughfare() : "";

                                tvMapEndereco.setText(logradouro + " " + numero + " " + bairro);
                                progressBar.setVisibility(View.GONE);
                                return;

                            }
                        }

                    } catch (IOException e){

                    }
                }
            });

        } catch (Exception e) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    execute = false;
                    snackBarUtil.createMessage("Falha na conexão.", view);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        } finally {
            lock.unlock();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imgGetLatLng.setEnabled(true);
                }
            });
        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                Place place = Autocomplete.getPlaceFromIntent(data);
                setAddress(place.getLatLng().latitude, place.getLatLng().longitude);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("uio", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

}