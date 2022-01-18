package hasanali.example.tictactoemvp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BoardView {
    TableLayout board;
    BoardPresenter boardPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        board = findViewById(R.id.board);
        boardPresenter = new BoardPresenter(this);
        for (byte row = 0; row < 3; row++) {
            TableRow tableRow = (TableRow) board.getChildAt(row);
            for (byte col = 0; col < 3; col++) {
                Button button = (Button) tableRow.getChildAt(col);
                button.setOnClickListener(new BoardPresenter.CellClickListener(boardPresenter,row,col));
            }
        }
    }

    @Override
    public void newGame() {
        TableLayout boarView = findViewById(R.id.board);
        for (int row = 0; row < 3; row++) {
            TableRow tableRow = (TableRow) boarView.getChildAt(row);
            for (int col = 0; col < 3; col++) {
                Button button = (Button)tableRow.getChildAt(col);
                button.setText("");
                button.setEnabled(true);
            }
        }

    }

    @Override
    public void putSymbol(char symbol, byte row, byte col) {

        TableRow tableRow = (TableRow) board.getChildAt(row);
        Button button = (Button) tableRow.getChildAt(col);
        button.setText(symbol+"");



    }

    @Override
    public void gameEnded(byte winner) {

        for (int row = 0; row < 3; row++) {
            TableRow tableRow = (TableRow) board.getChildAt(row);
            for (int col = 0; col < 3; col++) {
                Button button = (Button) tableRow.getChildAt(col);
                button.setText("");
                button.setEnabled(false);
            }
        }
        String msg =  null;
        if (winner!=0)
            msg = "Player " + winner + " won the game";
        else
            msg = "It is a draw";

        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void invalidPlay(byte row, byte col) {
        Toast.makeText(this,"Invalid row",Toast.LENGTH_LONG).show();
    }
}