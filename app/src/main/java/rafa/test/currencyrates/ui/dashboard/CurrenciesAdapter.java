package rafa.test.currencyrates.ui.dashboard;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import rafa.test.currencyrates.data.local.CurrencyRate;
import rafa.test.currencyrates.databinding.RateListItemBinding;
import rafa.test.currencyrates.ui.listener.ItemClickListener;
import rafa.test.currencyrates.utils.CommonUtils;

public class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.ViewHolder> implements Filterable {
    public ArrayList<CurrencyRate> mRateList;
    public ArrayList<CurrencyRate> originalList;
    ItemClickListener mClickListener;

    public CurrenciesAdapter(ArrayList<CurrencyRate> rates) {
        this.mRateList = rates;
    }

    public void setRateList(ArrayList<CurrencyRate> mRateList) {
        this.mRateList = mRateList;
        this.originalList = mRateList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrenciesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RateListItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CurrenciesAdapter.ViewHolder holder, final int position) {
        CurrencyRate rate = mRateList.get(position);
        holder.binding.txtRate.setText(String.format("%s $", CommonUtils.formatMoney(rate.getRate())));
        holder.binding.txtCurrency.setText(rate.getCurrency());
        holder.binding.txtCountry.setText(rate.getDescription());
        holder.itemView.setOnClickListener(view -> {
            if (mClickListener != null)
                mClickListener.onItemClick(rate.getCurrency());
        });
    }

    @Override
    public int getItemCount() {
        return mRateList == null ? 0 :
                mRateList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressLint("NotifyDataSetChanged")
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mRateList = (ArrayList<CurrencyRate>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<CurrencyRate> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = originalList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected ArrayList<CurrencyRate> getFilteredResults(String constraint) {
        ArrayList<CurrencyRate> results = new ArrayList<>();

        for (CurrencyRate item : originalList) {
            if (item.getCurrency().toLowerCase().contains(constraint) || item.getDescription().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RateListItemBinding binding;

        public ViewHolder(RateListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}