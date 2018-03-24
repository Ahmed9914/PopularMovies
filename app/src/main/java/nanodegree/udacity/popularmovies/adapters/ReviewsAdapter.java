package nanodegree.udacity.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nanodegree.udacity.popularmovies.R;
import nanodegree.udacity.popularmovies.model.Review;

/**
 * Created by Ahmd on 12/03/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {

        private List<Review> reviews;
        private Context context;

        public ReviewsAdapter(Context mContext) {
            context = mContext;
        }

        @Override
        public ReviewsAdapter.ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layoutIdForReviewItem = R.layout.review_item;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForReviewItem, parent, shouldAttachToParentImmediately);

            return new ReviewsAdapter.ReviewsAdapterViewHolder(view);
        }

    @Override
    public void onBindViewHolder(ReviewsAdapterViewHolder holder, int position) {
        holder.authorTv.setText(reviews.get(position).getAuthor());
        holder.contentTv.setText(reviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if (reviews == null || reviews.size() == 0) return 0;
        return reviews.size();
    }

    public void loadReviews(List<Review> mReviews) {
        reviews = mReviews;
        notifyDataSetChanged();
    }

    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView authorTv;
        final TextView contentTv;

        public ReviewsAdapterViewHolder(View itemView) {
            super(itemView);
            authorTv = itemView.findViewById(R.id.review_author_tv);
            contentTv = itemView.findViewById(R.id.review_content_tv);
        }

    }
    }

