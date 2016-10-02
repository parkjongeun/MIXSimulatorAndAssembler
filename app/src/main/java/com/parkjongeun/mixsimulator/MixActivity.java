package com.parkjongeun.mixsimulator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Parkjongeun on 03/10/2016.
 */

public class MixActivity extends AppCompatActivity {

    RecyclerView mMemoryCells;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix);

        mMemoryCells = (RecyclerView) findViewById(R.id.memory_cells);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView.Adapter adapter = new MemoryCellsAdapter();
        mMemoryCells.setLayoutManager(layoutManager);
        mMemoryCells.setAdapter(adapter);
    }


    private class MemoryCellsAdapter extends RecyclerView.Adapter<WordViewHolder> {


        @Override
        public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.mix_word, parent, false);
            return new WordViewHolder(v);
        }

        @Override
        public void onBindViewHolder(WordViewHolder holder, int position) {
            holder.setAddress(position);
        }

        @Override
        public int getItemCount() {
            return 4000;
        }
    }

    private static class WordViewHolder extends RecyclerView.ViewHolder {

        TextView mAddress;

        public WordViewHolder(View itemView) {
            super(itemView);
            mAddress = (TextView) itemView.findViewById(R.id.address);
        }

        public void setAddress(int address) {
            mAddress.setText(String.format("%04d: ", address));
        }
    }
}
