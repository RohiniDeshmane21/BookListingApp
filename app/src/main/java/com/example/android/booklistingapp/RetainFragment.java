package com.example.android.booklistingapp;

import android.app.Fragment;
import android.os.Bundle;

class RetainedFragment extends Fragment {

    // data object we want to retain
  //  private MyDataObject data;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    /*public void setData(MyDataObject data) {
        this.data = data;
    }

    public MyDataObject getData() {
        return data;
    }*/
}
