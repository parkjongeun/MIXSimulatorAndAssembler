package com.parkjongeun.mixsimulator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.TextView;

import com.parkjongeun.mixsimulator.mix.Executor;
import com.parkjongeun.mixsimulator.mix.Instruction;
import com.parkjongeun.mixsimulator.mix.Loader;
import com.parkjongeun.mixsimulator.mix.Memory;
import com.parkjongeun.mixsimulator.mix.Mix;
import com.parkjongeun.mixsimulator.mix.Word;

/**
 * Created by Parkjongeun on 03/10/2016.
 */

public class MixActivity extends AppCompatActivity {

    private Mix mMix;

    private RecyclerView mMemoryCells;

    private WordViewWrapper mRegisterA;
    private WordViewWrapper mRegisterX;
    private WordViewWrapper[] mRegisterIx;
    private WordViewWrapper mRegisterJ;
    private Checkable mOverflowToggle;
    private Checkable[] mComparisonIndicator;

    RecyclerView.Adapter mMemoryCellsAdaptor;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix);

        mMix = new Mix();
        mRegisterA = new WordViewWrapper(findViewById(R.id.registerA));
        mRegisterX = new WordViewWrapper(findViewById(R.id.registerX));
        mRegisterIx = new WordViewWrapper[7];
        mRegisterIx[1] = new WordViewWrapper(findViewById(R.id.registerI1));
        mRegisterIx[2] = new WordViewWrapper(findViewById(R.id.registerI2));
        mRegisterIx[3] = new WordViewWrapper(findViewById(R.id.registerI3));
        mRegisterIx[4] = new WordViewWrapper(findViewById(R.id.registerI4));
        mRegisterIx[5] = new WordViewWrapper(findViewById(R.id.registerI5));
        mRegisterIx[6] = new WordViewWrapper(findViewById(R.id.registerI6));
        mRegisterJ = new WordViewWrapper(findViewById(R.id.registerJ));

        mMemoryCells = (RecyclerView) findViewById(R.id.memory_cells);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMemoryCellsAdaptor = new MemoryCellsAdapter(getLayoutInflater(), mMix.mMemory);
        mMemoryCellsAdaptor.setHasStableIds(true);
        mMemoryCells.setLayoutManager(layoutManager);
        mMemoryCells.setAdapter(mMemoryCellsAdaptor);
        mMemoryCells.setHasFixedSize(true);

        final EditText gotoEdit = (EditText) findViewById(R.id.goto_edit);
        ((Button)findViewById(R.id.goto_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position;
                try {
                    position = Integer.parseInt(gotoEdit.getText().toString());
                } catch (NumberFormatException e) {
                    return;
                }
                if (0 <= position && position <= 3999) {
                    mMemoryCells.scrollToPosition(position);
                }
            }
        });
    }

    private void invalidate() {
        mRegisterA.setWord(mMix.getRegA());
        mRegisterX.setWord(mMix.getRegX());
        for (int i = 1; i < 7; ++i) {
            mRegisterIx[i].setWord(mMix.getRegIx()[i]);
        }
        mRegisterJ.setWord(mMix.getRegJ());
        mMemoryCellsAdaptor.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Executor executor = new Executor(mMix);

        Instruction[] pgm = new Instruction[]{
                new Instruction(false, 1, "STZ"),
                new Instruction(false, 1, "ENNX"),
                new Instruction(false, 1, 0, 1, "STX"),
                new Instruction(false, 1, "SLAX"),
                new Instruction(false, 1, "ENNA"),
                new Instruction(false, 1, "INCX"),
                new Instruction(false, 1, "ENT1"),
                new Instruction(false, 1, "SRC"),
                new Instruction(false, 1, "ADD"),
                new Instruction(true, 1, "DEC1"),
                new Instruction(false, 1, "STZ"),
                new Instruction(false, 1, "CMPA"),
                new Instruction(true, 1, 1, 1, "MOVE"),
                new Instruction(false, 1, "NUM"),
                new Instruction(false, 1, "CHAR"),
                new Instruction(false, 1, "HLT"),
        };
        new Loader(mMix).loadAt(1, pgm);
        executor.start(1);

        //mMemoryCells.scrollToPosition(3000);

        invalidate();
    }

    private static class MemoryCellsAdapter extends RecyclerView.Adapter<WordViewRVHolder> {

        private LayoutInflater mLayoutInflater;
        private Memory mMemory;

        public MemoryCellsAdapter(LayoutInflater layoutInflater, Memory memory) {
            mLayoutInflater = layoutInflater;
            mMemory = memory;
        }

        @Override
        public WordViewRVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = mLayoutInflater.inflate(R.layout.mix_word, parent, false);
            return new WordViewRVHolder(v);
        }

        @Override
        public void onBindViewHolder(WordViewRVHolder holder, int position) {
            final int address = position;
            Word word = mMemory.get(address);
            holder.getWordViewWrapper().setAddress(address);
            holder.getWordViewWrapper().setWord(word);
        }

        @Override
        public int getItemCount() {
            return mMemory.getSize();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    private static class WordViewRVHolder extends RecyclerView.ViewHolder {

        WordViewWrapper mWordViewWrapper;

        public WordViewRVHolder(View itemView) {
            super(itemView);
            mWordViewWrapper = new WordViewWrapper(itemView);
        }

        public WordViewWrapper getWordViewWrapper() {
            return mWordViewWrapper;
        }
    }

    private static class WordViewWrapper {

        private TextView mAddress;
        private TextView[] mCells;


        public WordViewWrapper(View wordView) {
            mAddress = (TextView) wordView.findViewById(R.id.address);
            mCells = new TextView[6];
            mCells[0] = (TextView) wordView.findViewById(R.id.cell0);
            mCells[1] = (TextView) wordView.findViewById(R.id.cell1);
            mCells[2] = (TextView) wordView.findViewById(R.id.cell2);
            mCells[3] = (TextView) wordView.findViewById(R.id.cell3);
            mCells[4] = (TextView) wordView.findViewById(R.id.cell4);
            mCells[5] = (TextView) wordView.findViewById(R.id.cell5);
            for (int i = 0; i < mCells.length; ++i) {
                if (mCells[i] == null) {
                    throw new NullPointerException("mCells[" + i + "] is null.");
                }
            }
        }

        public void setByte(int index, int value) {
            if (index == 0) {
                throw new IllegalArgumentException("Index == 0.");
            }
            mCells[index].setText(String.valueOf(value));
        }

        public void setSign(boolean isPlus) {
            mCells[0].setText(isPlus ? "+" : "-");
        }

        public void setWord(Word word) {
            mCells[0].setText(word.isSignPlus() ? "+" : "-");
            for (int i = 1; i < 6; ++i) {
                mCells[i].setText(String.valueOf(word.getField(i)));
            }
        }

        public void setAddress(int address) {
            mAddress.setText(String.format("%04d: ", address));
        }
    }
}
