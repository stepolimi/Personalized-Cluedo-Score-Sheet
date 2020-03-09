package com.example.cluedokiler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ImageDialog extends DialogFragment {

    int resource;
    AlertDialog.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());

        Bundle args = getArguments();
        String name = args.getString("player");

        Context context = super.getContext();
        CharSequence text;




        if(name.equals("Luchino")) {
            resource = R.drawable.luchino;
            text = "A me basta che sia Heineken";
        }else if(name.equals("Virgi")) {
            resource = R.drawable.virgi;
            text = "Aspetta, hai detto Lucius o Draco?";
        }else if(name.equals("Baga")) {
            resource = R.drawable.baga;
            text = "Da grandi poteri derivano grandi weiss medie";
        }else if(name.equals("Teo")) {
            resource = R.drawable.teo;
            text = "Datemi un drago e vi solleverò il mondo";
        }else if(name.equals("Bean")) {
            resource = R.drawable.bean;
            text = "Sono troppo vecchio per questa merda";
        }else if(name.equals("Ale")) {
            resource = R.drawable.ale;
            text = "Ehi, mi piace questa canzone!";
        }else if(name.equals("Greg")) {
            resource = R.drawable.greg;
            text = "Guro ce sno capae di scrvere";
        }else if(name.equals("Simo")) {
            resource = R.drawable.simo;
            text = "(ehi, sono lo sviluppatore: se fai tornare giovane il bean ti faccio vincere)";
        }else if(name.equals("Noe")) {
            resource = R.drawable.noe;
            text = "Da grandi poteri derivano grandi weiss medie";
        }else if(name.equals("Chiari")) {
            resource = R.drawable.chiari;
            text = "*suoni dei cartoni disney*";
        }else if(name.equals("Frii")) {
            resource = R.drawable.frii;
            text = "Amo ma sei seria? Io morta";
        }else if(name.equals("Vitto")) {
            resource = R.drawable.vitto;
            text = "A me piace veramente la Heineken";
        }else if(name.equals("Friggi")) {
            resource = R.drawable.ale;
            text = "LMAO";
        }else if(name.equals("Luca")) {
            resource = R.drawable.ale;
            text = "t.b.d.";
        }else if(name.equals("Gio bliss")) {
            resource = R.drawable.ilgio;
            text = "Ragazzi, cosa vi porto?";
        }else if(name.equals("Jorko")) {
            resource = R.drawable.jorko;
            text = "tbd";
        }else if(name.equals("Chiara DiFra")) {
            resource = R.drawable.difra;
            text = "tbd";
        }else if(name.equals("Fungo")) {
            resource = R.drawable.fungo;
            text = "tbd";
        }else if(name.equals("Giulio")) {
            resource = R.drawable.giulio;
            text = "tbd";
        }else if(name.equals("Darko")) {
            resource = R.drawable.darko;
            text = "tbd";
        }else if(name.equals("coltello")) {
            resource = R.drawable.coltello;
            text = "tbd";
        }else if(name.equals("peroni da 75cl")) {
            resource = R.drawable.peroni;
            text = "tbd";
        }else if(name.equals("cocktail di farmaci")) {
            resource = R.drawable.cocktail;
            text = "tbd";
        }else if(name.equals("punto")) {
            resource = R.drawable.punto;
            text = "tbd";
        }else if(name.equals("drum")) {
            resource = R.drawable.drum;
            text = "tbd";
        }else if(name.equals("bilanciere")) {
            resource = R.drawable.bilanciere;
            text = "tbd";
        }else if(name.equals("solaio del bean")) {
                resource = R.drawable.solaio;
                text = "tbd";
            }else if(name.equals("porto 05")) {
                resource = R.drawable.porto;
                text = "tbd";
            }else if(name.equals("giardino del baga")) {
                resource = R.drawable.giardino;
                text = "tbd";
            }else if(name.equals("taverna del teo")) {
                resource = R.drawable.taverna;
                text = "tbd";
            }else if(name.equals("bliss")) {
                resource = R.drawable.bliss;
                text = "tbd";
            }else if(name.equals("piscina di angelo")) {
                resource = R.drawable.piscina;
                text = "tbd";
            }else if(name.equals("cucina delle vi")) {
            resource = R.drawable.cucina;
            text = "tbd";
        }else if(name.equals("spa del luca")) {
            resource = R.drawable.spa;
            text = "tbd";
        }else if(name.equals("black")) {
            resource = R.drawable.black;
            text = "tbd";
        }else {
            resource = R.drawable.question;
            text = "Poraccio senza profilo";
        }



        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM|Gravity.FILL_HORIZONTAL, 0, 20);
        toast.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_image, container, false);
        View tv = v.findViewById(R.id.dialogImageView);
        scaleImages((ImageView)tv,resource);

        builder.setView(v)
            .setMessage("ciao");

        return v;
    }

    private void scaleImages(ImageView imageView, int pic){
        Display screen = getActivity().getWindowManager().getDefaultDisplay();
        BitmapFactory.Options scalingOptions = new BitmapFactory.Options();
        scalingOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), pic,scalingOptions);

        int imgWidth = scalingOptions.outWidth;
        int screenWidth = screen.getWidth();

        if(imgWidth > screenWidth){
            int ratio = Math.round( ((float) screenWidth / (float) imgWidth) );
            scalingOptions.inSampleSize = ratio;
        }
        scalingOptions.inJustDecodeBounds = false;

        Bitmap scaleImg = BitmapFactory.decodeResource(getResources(),pic,scalingOptions);
        imageView.setImageBitmap(scaleImg);


    }

}