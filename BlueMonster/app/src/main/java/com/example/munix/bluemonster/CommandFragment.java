package com.example.munix.bluemonster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class CommandFragment extends Fragment {

    private final String LOG_TAG = CommandFragment.class.getSimpleName();

    public CommandFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_devices) {
            Intent scanIntent = new Intent(getActivity(), ScanActivity.class);
            startActivity(scanIntent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_command, container, false);

        // button setup
        Button sendButton = (Button) rootView.findViewById(R.id.send_button);
        final EditText commandText = (EditText) rootView.findViewById(R.id.commandText);
        final TextView commandsView = (TextView) rootView.findViewById(R.id.commandsView);

        sendButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), commandText.getText(), Toast.LENGTH_SHORT).show();
                commandsView.setText(commandText.getText());
            }
        });

        return rootView;
    }
}
