package com.parkjongeun.mixsimulator;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public class OpCodeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.opcode_activity);

        final List<String> opCode = new ArrayList<>();
        for(OpCode e : OpCode.values()) {
            opCode.add(e.name() + " " + e.code + " " + e.fieldSpec);
        }


        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerview1);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false)) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText(opCode.get(position));
            }

            @Override
            public int getItemCount() {
                return opCode.size();
            }
        });
    }
}
