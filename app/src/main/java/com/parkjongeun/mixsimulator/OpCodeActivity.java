package com.parkjongeun.mixsimulator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Parkjongeun on 2016. 9. 13..
 */
public class OpCodeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mix);

        /*final List<String> opCode = new ArrayList<>();
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
        });*/
    }
}
