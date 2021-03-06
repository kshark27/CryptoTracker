package Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Adapters.CoinsAdapter;
import Objects.CoinItem;

/**
 * Created by Ultranova on 2/11/2018.
 */

public class Storage {

    private static final String SORTINGCT = "SortingMode";
    private static final String STORAGECT = "CoinStorage";
    private ArrayList<CoinItem> savedCoinList = null;
    public static final String COINLIST = "CoinListKey";
    private Context context;
    SharedPreferences sharedpreferences;

    public Storage(Context context) {
        this.context = context;
        this.savedCoinList = new ArrayList<CoinItem>();
        initArray();
    }

    public void initArray() {

        List<CoinItem> savedList =  new ArrayList<>();

      if(getCoinList() != null) {
          savedList = getCoinList();
          savedCoinList.addAll(savedList);
      }

    }


    public void addCoin(CoinItem newCoin) {

         if(canBeAdded(newCoin)) {
             savedCoinList.add(newCoin);
             saveCoinList();
         }
         else {

             Log.i("Duplicate: ", "Cannot be added");
         }

    }

    public void deleteCoin(String coinID) {



        int i;
        for(i = 0; i < savedCoinList.size(); i++) {
            String cid = savedCoinList.get(i).getID();
            if(cid.equals(coinID)) {

                savedCoinList.remove(i);

            }
        }


        saveCoinList();
    }

    public void updateCoin(CoinItem newCoin) {

          int i;
          for(i = 0; i < savedCoinList.size(); i++) {
          String cid = savedCoinList.get(i).getID();
          if(cid.equals(newCoin.getID())) {
              savedCoinList.set(i,newCoin);
            }
          }


         saveCoinList();



    }


    public void removeAllFromStaticList() {

        if(savedCoinList != null) {
            savedCoinList.clear();
        }

    }

    public void saveCoinList() {

        // stringify json
        String coinJSONList = new Gson().toJson(savedCoinList);
        sharedpreferences = context.getSharedPreferences(STORAGECT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(COINLIST,coinJSONList);
        editor.apply();

    }

    public void saveSortingCondition(Integer condition) {

        sharedpreferences = context.getSharedPreferences(SORTINGCT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(SORTINGCT,condition);
        editor.apply();

    }

    public Integer getSortingCondition() {

        sharedpreferences = context.getSharedPreferences(SORTINGCT, Context.MODE_PRIVATE);
        Integer condition = sharedpreferences.getInt(SORTINGCT,0);

        return condition;

    }


    public void clearCoins(CoinsAdapter currentAdapter, TextView balance){

        sharedpreferences = context.getSharedPreferences(STORAGECT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
        removeAllFromStaticList();

        if(currentAdapter != null) {
            currentAdapter.clearCoins();
        }

        setPortfolioBalance(balance);


    }

    public List<CoinItem> getCoinList() {

        sharedpreferences = context.getSharedPreferences(STORAGECT, Context.MODE_PRIVATE);
        String coinJSONList = sharedpreferences.getString(COINLIST,"");
        Type type = new TypeToken<List<CoinItem>>(){}.getType();

        List<CoinItem> newCoinList = new ArrayList<>();
        newCoinList =  new Gson().fromJson(coinJSONList, type );

        if(newCoinList != null) {
            return newCoinList;
        }
        else {
            return null;
        }


    }

    public boolean canBeAdded(CoinItem newCoin) {

        Boolean canAdd = true;

        int i;
        for(i = 0; i < savedCoinList.size(); i++) {
            String cid = savedCoinList.get(i).getID();
            if(cid.equals(newCoin.getID())) {
                canAdd = false;
            }
        }

        return canAdd;
    }


    public void setCoinsPrices(String prices, CoinsAdapter adapter, TextView balance) {

        try {

            JSONObject priceObj = new JSONObject(prices);

            int i;
            for(i = 0; i < savedCoinList.size(); i++) {

                CoinItem tempCoin = savedCoinList.get(i);
                String symbol = tempCoin.getSymbol();
                JSONObject priceArray = priceObj.getJSONObject(symbol);
                Double finalPrice = priceArray.getDouble(Util.CURRENCY);
                tempCoin.setPrice(finalPrice);

            }


            setPortfolioBalance(balance);

            saveCoinList();
            adapter.updateCoins(savedCoinList, getSortingCondition());

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void setPortfolioBalance(TextView balance) {

        Double totalBalance = 0.0;


        int i;
        for(i = 0; i < savedCoinList.size(); i++) {

            CoinItem tempCoin = savedCoinList.get(i);
            totalBalance += tempCoin.getTotalValue();

        }

        String val = "$" + Util.getFormattedNumber(totalBalance);

        balance.setText(val);

    }


}