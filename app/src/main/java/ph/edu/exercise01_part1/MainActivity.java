package ph.edu.exercise01_part1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvDepartment;
    private Button btnChangeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("ID: 13102918");

        tvDepartment = findViewById(R.id.tvDepartment);
        btnChangeName = findViewById(R.id.btnChangeName);

        tvDepartment.setText("Department of Computer, Information Sciences and Mathematics");

        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvDepartment.setText("Angie M. Ceniza-Canillo");
            }
        });
    }
}
