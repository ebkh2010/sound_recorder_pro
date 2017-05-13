/*
 * Copyright (C) 2016 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package ir.khorramnejad.advancesoundrecorder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.io.File;

import static ir.khorramnejad.advancesoundrecorder.G.REC_DIR;
import static ir.khorramnejad.advancesoundrecorder.G.dirName;

public class RecordingSettingActivity extends AppCompatActivity {

    RadioGroup RGSampleRate,RGChanels,RGMp3Bitrate,RGCodingQuality,RGBitDepth;
    EditText ETFileNAme,ETComment,ETAlbum,ETArtist,ETTitle,ETDir;

    public static String test="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ir.khorramnejad.advancesoundrecorder.R.layout.activity_setting);

        RGSampleRate    = (RadioGroup) findViewById(R.id.fqs_radiogroup);
        RGChanels       = (RadioGroup) findViewById(R.id.fch_radiogroup);
        RGMp3Bitrate    = (RadioGroup) findViewById(R.id.fcodB_rg);
        RGCodingQuality = (RadioGroup) findViewById(R.id.fcodQ_rg);
//        RGBitDepth      = (RadioGroup) findViewById(ir.khorramnejad.soundrecorderpro.R.id.fbd_rg);

        ETFileNAme      = (EditText) findViewById(R.id.set_name);
        ETDir           = (EditText) findViewById(R.id.set_dir);
//        ETComment       = (EditText) findViewById(R.id.set_comment);
//        ETAlbum         = (EditText) findViewById(R.id.set_album);
//        ETArtist        = (EditText) findViewById(R.id.set_artist);
//        ETTitle         = (EditText) findViewById(R.id.set_title);

        ETFileNAme.setHint(G.FILE_NAME);
        ETDir.setHint(G.dirName);

        ETFileNAme.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                G.FILE_NAME = ETFileNAme.getText().toString();
            }
        });

        ETDir.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                dirName = ETDir.getText().toString();
                REC_DIR = Environment.getExternalStorageDirectory() + "/"+dirName;


            }
        });

//        ETComment.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                inComment = ETComment.getText().toString();
//
//            }
//        });
//
//        ETAlbum.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                inAlbum = ETAlbum.getText().toString();
//            }
//        });
//
//        ETArtist.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                inArtist = ETArtist.getText().toString();
//            }
//        });
//
//        ETTitle.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                inTitle = ETTitle.getText().toString();
//            }
//        });

        RGSampleRate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (RGSampleRate.getCheckedRadioButtonId()){
                    case ir.khorramnejad.advancesoundrecorder.R.id.veryLowQFile:
                        G.inSamplerate = 16000;
                        break;
                    case ir.khorramnejad.advancesoundrecorder.R.id.lowQFile:
                        G.inSamplerate = 22050;
                        break;
                    case ir.khorramnejad.advancesoundrecorder.R.id.normalQFile:
                        G.inSamplerate = 44100;
                        break;
                    case ir.khorramnejad.advancesoundrecorder.R.id.HighQFile:
                        G.inSamplerate = 48000;
                        break;
                    default:
                        break;
                }
            }
        });

        switch (G.inSamplerate){
            case 16000:
                RGSampleRate.check(ir.khorramnejad.advancesoundrecorder.R.id.veryLowQFile);
                break;
            case 22050:
                RGSampleRate.check(ir.khorramnejad.advancesoundrecorder.R.id.lowQFile);
                break;
            case 44100:
                RGSampleRate.check(ir.khorramnejad.advancesoundrecorder.R.id.normalQFile);
                break;
            case 48000:
                RGSampleRate.check(ir.khorramnejad.advancesoundrecorder.R.id.HighQFile);
                break;
            default:
                break;
        }

//        RGBitDepth.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                switch (RGBitDepth.getCheckedRadioButtonId()){
//                    case ir.khorramnejad.soundrecorderpro.R.id.fbd8bit:
//                        G.inBitDept = 8;
//                        break;
//                    case ir.khorramnejad.soundrecorderpro.R.id.fbd16bit:
//                        G.inBitDept = 16;
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//
//        switch (G.inBitDept){
//            case 8:
//                RGBitDepth.check(ir.khorramnejad.soundrecorderpro.R.id.fbd8bit);
//                break;
//            case 16:
//                RGBitDepth.check(ir.khorramnejad.soundrecorderpro.R.id.fbd16bit);
//                break;
//            default:
//                break;
//        }

        RGChanels.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (RGChanels.getCheckedRadioButtonId()){
                    case ir.khorramnejad.advancesoundrecorder.R.id.mono:
                        G.inChanels = 1;
                        break;
                    case ir.khorramnejad.advancesoundrecorder.R.id.stereo:
                        G.inChanels = 2;
                        break;
                    default:
                        break;
                }
            }
        });

        switch (G.inChanels){
            case 1:
                RGChanels.check(ir.khorramnejad.advancesoundrecorder.R.id.mono);
                break;
            case 2:
                RGChanels.check(ir.khorramnejad.advancesoundrecorder.R.id.stereo);
                break;
        }

        RGMp3Bitrate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (RGMp3Bitrate.getCheckedRadioButtonId()){
                    case ir.khorramnejad.advancesoundrecorder.R.id.mp3B32:
                        G.inMp3Bitrate = 32;
                        break;
                    case ir.khorramnejad.advancesoundrecorder.R.id.mp3B64:
                        G.inMp3Bitrate = 64;
                        break;
                    case ir.khorramnejad.advancesoundrecorder.R.id.Mp3B128:
                        G.inMp3Bitrate = 128;
                        break;
                    case ir.khorramnejad.advancesoundrecorder.R.id.mp3B256:
                        G.inMp3Bitrate = 256;
                        break;
                    default:
                        break;
                }
            }
        });

        switch (G.inMp3Bitrate){
            case 32:
                RGMp3Bitrate.check(ir.khorramnejad.advancesoundrecorder.R.id.mp3B32);
                break;
            case 64:
                RGMp3Bitrate.check(ir.khorramnejad.advancesoundrecorder.R.id.mp3B64);
                break;
            case 128:
                RGMp3Bitrate.check(ir.khorramnejad.advancesoundrecorder.R.id.Mp3B128);
                break;
            case 256:
                RGMp3Bitrate.check(ir.khorramnejad.advancesoundrecorder.R.id.mp3B256);
                break;
            default:
                break;
        }

        RGCodingQuality.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (RGCodingQuality.getCheckedRadioButtonId()){
                    case ir.khorramnejad.advancesoundrecorder.R.id.mp3Qlow:
                        G.inQuality = 9;
                        break;
                    case ir.khorramnejad.advancesoundrecorder.R.id.mp3Qnormal:
                        G.inQuality = 5;
                        break;
                    case ir.khorramnejad.advancesoundrecorder.R.id.Mp3Qhigh:
                        G.inQuality = 2;
                        break;
                    case ir.khorramnejad.advancesoundrecorder.R.id.mp3Qbest:
                        G.inQuality = 0;
                        break;
                    default:
                        break;
                }
            }
        });

        switch (G.inQuality){
            case 9:
                RGCodingQuality.check(ir.khorramnejad.advancesoundrecorder.R.id.mp3Qlow);
                break;
            case 5:
                RGCodingQuality.check(ir.khorramnejad.advancesoundrecorder.R.id.mp3Qnormal);
                break;
            case 2:
                RGCodingQuality.check(ir.khorramnejad.advancesoundrecorder.R.id.Mp3Qhigh);
                break;
            case 0:
                RGCodingQuality.check(ir.khorramnejad.advancesoundrecorder.R.id.mp3Qbest);
                break;
        }

    }


    @Override
    public void onBackPressed() {
        // save data first

        Intent MainActivityIntent = new Intent(RecordingSettingActivity.this, Mp3AudioRecordActivity.class);
        startActivity(MainActivityIntent);
        super.onBackPressed();
        new File(REC_DIR).mkdir();
        G.filePath = REC_DIR + "/" + G.FILE_NAME +".mp3" ;
    }
}

