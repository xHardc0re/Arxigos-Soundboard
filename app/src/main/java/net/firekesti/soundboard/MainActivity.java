/*
 * Copyright (C) 2014 Caleb Sabatini
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.firekesti.soundboard;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    private SoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("");

        FavStore.init(getPreferences(Context.MODE_PRIVATE));

        final RecyclerView grid = (RecyclerView) findViewById(R.id.grid_view);
        grid.setLayoutManager(new StaggeredGridLayoutManager(getResources().getInteger(R.integer.num_cols),
                StaggeredGridLayoutManager.VERTICAL));
        grid.setAdapter(new SoundAdapter(SoundStore.getAllSounds(this)));

        SwitchCompat favSwitch = (SwitchCompat) findViewById(R.id.fav_switch);
        favSwitch.setChecked(FavStore.getInstance().getShowFavorites());
        if (favSwitch.isChecked()) {
            ((SoundAdapter) grid.getAdapter()).onlyShowFavorites();
        } else {
            ((SoundAdapter) grid.getAdapter()).showAllSounds(MainActivity.this);
        }
        favSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ((SoundAdapter) grid.getAdapter()).onlyShowFavorites();
                } else {
                    ((SoundAdapter) grid.getAdapter()).showAllSounds(MainActivity.this);
                }
                FavStore.getInstance().setShowFavorites(isChecked);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        soundPlayer = new SoundPlayer(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        soundPlayer.release();
    }
}
