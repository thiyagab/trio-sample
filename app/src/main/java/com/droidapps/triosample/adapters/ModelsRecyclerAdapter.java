package com.droidapps.triosample.adapters;

/**
 * Created by bt on 5/8/16.
 */


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.droidapps.triosample.BR;
import com.droidapps.triosample.R;
import com.droidapps.triosample.data.Device;
import com.droidapps.triosample.databinding.RowBinding;

import java.util.List;


public class ModelsRecyclerAdapter extends RecyclerView.Adapter<ModelsRecyclerAdapter.BindingHolder> {
    private final Context mContext;
    private List<Device> mDevices;

    public static class BindingHolder extends ViewHolder {
        private final RowBinding binding;

        BindingHolder(final RowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public ModelsRecyclerAdapter(Context context, List<Device> recyclerDevices) {
        this.mContext = context;
        this.mDevices = recyclerDevices;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final RowBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.row, viewGroup, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        final Device device = mDevices.get(position);
        holder.binding.setVariable(BR.device, device);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }
}