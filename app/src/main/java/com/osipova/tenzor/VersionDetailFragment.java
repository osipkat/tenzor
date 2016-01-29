package com.osipova.tenzor;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.osipova.tenzor.model.Version;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

/**
 * A fragment representing a single Version detail screen.
 * This fragment is either contained in a {@link VersionListActivity}
 * in two-pane mode (on tablets) or a {@link VersionDetailActivity}
 * on handsets.
 */
public class VersionDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private Version mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VersionDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = Parcels.unwrap(getArguments().getParcelable(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
            final ImageView imageView = (ImageView) activity.findViewById(R.id.backdrop);
            if (imageView != null) {
                Picasso.with(imageView.getContext()).load(mItem.getImage()).into(imageView);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.version_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.name)).setText(mItem.getName());
            ((TextView) rootView.findViewById(R.id.version)).setText(mItem.getVersion());
        }

        return rootView;
    }
}
