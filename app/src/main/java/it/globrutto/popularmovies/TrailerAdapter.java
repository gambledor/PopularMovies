package it.globrutto.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.globrutto.popularmovies.model.Trailer;

/**
 * Created by giuseppelobrutto on 04/03/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    public static final String TAG = TrailerAdapter.class.getSimpleName();

    private List<Trailer> mTrailers = null;

    private Context mContext = null;

    private final TrailerAdapterOnClickHandler mClickHandler;

    /**
     * Listener for on click trailer item
     */
    public interface TrailerAdapterOnClickHandler {
        /**
         * The action to perform on click item
         *
         * @param trailer The trailer object
         */
        void onClickItem(Trailer trailer);
    }

    /**
     * Constructor
     *
     * @param context
     */
    public TrailerAdapter(Context context, TrailerAdapterOnClickHandler handler) {
        mContext = context;
        mClickHandler = handler;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForTrailerItem = R.layout.trailer_row;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForTrailerItem, parent, shouldAttachToParentImmediately);

        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        if (mTrailers == null || mTrailers.isEmpty()) {
            Log.d(TAG, "mTrailers is null or empty");
            return;
        }

        Trailer trailer = mTrailers.get(position);
        holder.mTrailerTitle.setText(trailer.getName());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mTrailers == null ? 0 : mTrailers.size();
    }

    public void setTrailers(List<Trailer> trailers) {
        this.mTrailers = trailers;
        notifyDataSetChanged();
    }

    public List<Trailer> getTrailers() {
        return mTrailers;
    }

    /**
     * Trailer adapter view holder
     */
    public class TrailerAdapterViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_trailer_title)
        TextView mTrailerTitle = null;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Trailer trailer = mTrailers.get(position);
            mClickHandler.onClickItem(trailer);
        }
    }
}
