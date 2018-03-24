package nanodegree.udacity.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nanodegree.udacity.popularmovies.R;
import nanodegree.udacity.popularmovies.model.Movie;
import nanodegree.udacity.popularmovies.model.Review;
import nanodegree.udacity.popularmovies.model.Trailer;

/**
 * Created by Ahmd on 12/03/2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder>{
    private List<Trailer> trailers;
    private Context context;

    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailer);
    }
    private TrailerAdapterOnClickHandler clickHandler;

    public TrailersAdapter(Context mContext, TrailerAdapterOnClickHandler mClickHandler) {
        context = mContext;
        clickHandler = mClickHandler;
    }

    @Override
    public TrailersAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForTrailerItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForTrailerItem, parent, shouldAttachToParentImmediately);

        return new TrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapterViewHolder holder, int position) {
        holder.trailerNameTv.setText(trailers.get(position).getName());
    }


    @Override
    public int getItemCount() {
        if (trailers == null || trailers.size() == 0) return 0;
        return trailers.size();
    }

    public void loadTrailers(List<Trailer> mTrailer) {
        trailers = mTrailer;
        notifyDataSetChanged();
    }

    public class TrailersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView trailerNameTv;
        final ImageView trailerLinkIv;

        public TrailersAdapterViewHolder(View itemView) {
            super(itemView);
            trailerNameTv = itemView.findViewById(R.id.trailer_name_tv);
            trailerLinkIv = itemView.findViewById(R.id.play_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Trailer currentTrailer = trailers.get(adapterPosition);
            clickHandler.onClick(currentTrailer);
        }
    }
}
