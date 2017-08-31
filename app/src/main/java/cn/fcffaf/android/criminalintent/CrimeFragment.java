package cn.fcffaf.android.criminalintent;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;


public class CrimeFragment extends Fragment {

    private static final String TAG = "CrimeFragment";

    private Crime mCrime;
    private EditText mCrimeTitle;
    private Button mCrimeData;
    private CheckBox mCrimeSolved;
    private Button mCrimeDelete;

    private static final String DIALOG_DATE = "DialogDate";

    private static final String ARG_CRIME_ID = "crime_id";

    private static final int REQUEST_DATE = 0;


    public CrimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mCrime = new Crime();
        //UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        //mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        mCrimeTitle = (EditText)v.findViewById(R.id.crime_title);
        mCrimeTitle.setText(mCrime.getTitle());
        mCrimeTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mCrimeData = (Button) v.findViewById(R.id.crime_data);
        updateDate();
        //mCrimeData.setEnabled(false);
        mCrimeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                //DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mCrimeSolved = (CheckBox)v.findViewById(R.id.crime_solved);
        mCrimeSolved.setChecked(mCrime.isSolved());
        mCrimeSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
            }
        });

        mCrimeDelete = v.findViewById(R.id.crime_delete_button);
        mCrimeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
                CrimeLab.get(getActivity()).removeCrime(crimeId);
                getActivity().finish();

            }
        });

        return v;

    }

    private void updateDate() {
        mCrimeData.setText(mCrime.getDate().toString());
    }

}
