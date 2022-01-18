package hasanali.example.tictactoemvp;

import android.util.Log;
import android.view.View;

public class BoardPresenter implements BoardListener {
    BoardView boardView;
    Board board;
    public BoardPresenter(BoardView boardView) {
        this.boardView = boardView;
        board = new Board(this);
    }

    @Override
    public void playedAt(byte player, byte row, byte col) {
        char symbol;
        if(player== BoardListener.PLAYER_1)
            symbol = BoardView.PLAYER_1_SYMBOL;
        else
            symbol = BoardView.PLAYER_2_SYMBOL;
        boardView.putSymbol(symbol,row ,col);
    }

    @Override
    public void gameEnded(byte winner) {

        switch (winner){
            case BoardListener.NO_ONE:
                boardView.gameEnded(BoardView.DRAW);
                break;
            case BoardListener.PLAYER_1:
                boardView.gameEnded(BoardView.PLAYER_1_WINNER);
                break;
            case BoardListener.PLAYER_2:
                boardView.gameEnded(BoardView.PLAYER_2_WINNER);
                break;
        }
    }

    @Override
    public void invalidPlay(byte row, byte col) {
        boardView.invalidPlay(row,col);
    }

    static class CellClickListener implements View.OnClickListener{
        BoardPresenter presenter;
        byte row;
        byte col;
        public CellClickListener( BoardPresenter presenter, byte row, byte col){
            this.row = row;
            this.col = col;
            this.presenter = presenter;
        }
        @Override
        public void onClick(View view){
            Log.d("CellClickListener", "at" + row + ", " + col);
            presenter.move(row,col);
        }
    }
    private  void move(byte row ,byte col){
        board.move(row,col);
    }
}

