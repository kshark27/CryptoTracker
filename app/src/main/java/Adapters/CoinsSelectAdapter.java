package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.evmcstudios.cryptotracker.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Objects.CoinItem;
import Utilities.Util;

/**
 * Created by Ultranova on 2/11/2018.
 */

public class CoinsSelectAdapter extends ArrayAdapter<CoinItem> implements Filterable {

    private List<CoinItem> coinItem;
    public  ArrayList<CoinItem> tempArray;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView Title;
        TextView Symbol;
        ImageView Image;
    }


    public CoinsSelectAdapter(Context context, ArrayList<CoinItem> data ) {
        super(context, R.layout.lv_select_all_coins , data);

        this.coinItem = data;
        this.mContext = context;

        setTempArray(data);
    }

    public void  setTempArray(ArrayList<CoinItem> data) {

        tempArray = new ArrayList<>();
        tempArray.addAll(data);

    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        // OfferItem currentOffer = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.lv_select_all_coins, parent, false);
            viewHolder.Title = (TextView) convertView.findViewById(R.id.coinName);
            viewHolder.Symbol = (TextView) convertView.findViewById(R.id.coinSymbol);
            viewHolder.Image = (ImageView) convertView.findViewById(R.id.coinImg);



            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;

        // set information

        viewHolder.Title.setText(coinItem.get(position).getTitle());


        String symbol = "(" + coinItem.get(position).getSymbol() + ")";
        viewHolder.Symbol.setText(symbol);

        Picasso.with(mContext).load(coinItem.get(position).getImageUrl()).fit().centerCrop().into(viewHolder.Image);

        // Return the completed view to render on screen
        return convertView;
    }

    public void updateCoins(List<CoinItem> coins) {

        coinItem.clear();
        tempArray.clear();
        coinItem.addAll(coins);
        tempArray.addAll(coins);
        notifyDataSetChanged();

    }
    public void clearCoins() {

        if(coinItem != null) {
            coinItem.clear();
        }

        if(tempArray != null) {

            tempArray.clear();
        }


        notifyDataSetChanged();


    }

    public void filterList(String text, CoinsSelectAdapter adapter ) {

        String charSet =  text.toLowerCase(Locale.getDefault());



        coinItem.clear();


        if(charSet.length() == 0){
            coinItem.addAll(tempArray);
        }

        else {


            int i;
            for(i = 0; i < tempArray.size(); i++) {

                Boolean inTitle = tempArray.get(i).getTitle().toLowerCase(Locale.getDefault()).contains(charSet);
                Boolean inSymbol = tempArray.get(i).getSymbol().toLowerCase(Locale.getDefault()).contains(charSet);

                if(inTitle || inSymbol ) {

                    coinItem.add(tempArray.get(i));

                }


            }



        }

        adapter.notifyDataSetChanged();



    }



}
