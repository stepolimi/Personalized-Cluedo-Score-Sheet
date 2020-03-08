package com.example.cluedokiler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
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
        int duration = Toast.LENGTH_LONG;



        if(name.equals("Luca")) {
            resource = R.drawable.luca;
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
            resource = R.drawable.ale;
            text = "Guro ce sno capae di scrvere";
        }else if(name.equals("Simo")) {
            resource = R.drawable.ale;
            text = "(ehi, sono lo sviluppatore: se fai tornare giovane il bean ti faccio vincere)";
        }else if(name.equals("Noe")) {
            resource = R.drawable.ale;
            text = "Da grandi poteri derivano grandi weiss medie";
        }else if(name.equals("Chiari")) {
            resource = R.drawable.ale;
            text = "*suoni dei cartoni disney*";
        }else if(name.equals("Frii")) {
            resource = R.drawable.ale;
            text = "Amo sei seria? Io morta";
        }else if(name.equals("Vitto")) {
            resource = R.drawable.ale;
            text = "A me piace veramente la Heineken";
        }else if(name.equals("Friggi")) {
            resource = R.drawable.ale;
            text = "...leggero retrogusto di pesca e un sentore di ciliegio, mentre il colore è....";
        }else {
            resource = R.drawable.question;
            text = "Poraccio senza profilo";
        }
        Toast toast = Toast.makeText(context, text, duration);
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