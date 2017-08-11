package com.keepics.mynetlyzer.support;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.keepics.mynetlyzer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupportFragment extends Fragment {


    private Button backButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public SupportFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_support, container, false);

        backButton = (Button) view.findViewById(R.id.back_button_in_setting);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportFragment.this.getActivity().onBackPressed();
            }
        });

        backButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Drawable drawableRes = getResources().getDrawable(R.drawable.home);
                    backButton.setBackground(drawableRes);
                }else{
                    Drawable drawableRes = getResources().getDrawable(R.drawable.home_focus);
                    backButton.setBackground(drawableRes);
                }
            }
        });
        backButton.requestFocus();

        return view;
    }

}
