package com.example.android.boardingpass;

/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.example.android.boardingpass.databinding.ActivityMainBinding;
import com.example.android.boardingpass.utilities.FakeDataUtils;

import java.sql.DatabaseMetaData;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //COMPLETED (3) Create a data binding instance called mBinding of type ActivityMainBinding
    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        BoardingPassInfo fakeBoardingInfo = FakeDataUtils.generateFakeBoardingPassInfo();


        // COMPLETED (4) Set the Content View using DataBindingUtil to the activity_main layout

        // COMPLETED (5) Load a BoardingPassInfo object with fake data using FakeDataUtils

        // COMPLETED (9) Call displayBoardingPassInfo and pass the fake BoardingInfo instance
        displayBoardingPassInfo(fakeBoardingInfo);

    }

    private void displayBoardingPassInfo(BoardingPassInfo info) {

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        mBinding.textViewArrivalTime.setText(formatter.format(info.arrivalTime));
        mBinding.textViewDepartureTime.setText(formatter.format(info.departureTime));
        mBinding.textViewFlightCode.setText(info.flightCode);
        mBinding.textViewBoardingInCountdown.setText(String.valueOf(info.getMinutesUntilBoarding()) + " Mins");
        mBinding.textViewBoardingTime.setText(formatter.format(info.boardingTime));
        mBinding.textViewOriginAirport.setText(info.originCode);
        mBinding.textViewDestinationAirport.setText(info.destCode);
        mBinding.textViewPassengerName.setText(info.passengerName);
        mBinding.textViewGate.setText(info.departureGate);
        mBinding.textViewTerminal.setText(info.departureTerminal);
        mBinding.textViewSeat.setText(info.seatNumber);
        // COMPLETED (6) Use mBinding to set the Text in all the textViews using the data in info

        // COMPLETED (7) Use a SimpleDateFormat formatter to set the formatted value in time text views

        // COMPLETED (8) Use TimeUnit methods to format the total minutes until boarding

    }
}

