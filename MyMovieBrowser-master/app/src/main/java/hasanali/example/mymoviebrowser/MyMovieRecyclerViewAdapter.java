package hasanali.example.mymoviebrowser;


import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import hasanali.example.mymoviebrowser.databinding.FragmentMovieBinding;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMovieRecyclerViewAdapter extends RecyclerView.Adapter<MyMovieRecyclerViewAdapter.ViewHolder> {

    private final List<Movie> mValues;
    private final MovieFragment.MovieListener mListener;
    int selectedIndex;

    public MyMovieRecyclerViewAdapter(List<Movie> items, MovieFragment.MovieListener listener) {
        mValues = items;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentMovieBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(position + 1 + "");
        holder.mContentView.setText(mValues.get(position).getName());
        holder.itemView.setBackgroundColor(selectedIndex == position ? Color.GREEN : Color.TRANSPARENT);
        holder.mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.movieSelected(holder.mItem);
                    notifyItemChanged(selectedIndex);
                    selectedIndex = holder.getLayoutPosition();
                    notifyItemChanged(selectedIndex);
                }
            }
        });
        holder.itemView.setBackgroundColor(selectedIndex==position ? Color.GREEN: Color.TRANSPARENT);
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public Movie mItem;

        public ViewHolder(FragmentMovieBinding binding) {
            super(binding.getRoot());

            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}