package com.osipova.tenzor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.osipova.tenzor.model.Version;
import com.osipova.tenzor.model.VersionsResponse;
import com.osipova.tenzor.network.VersionService;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * An activity representing a list of Versions. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link VersionDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class VersionListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    List<Version> versions = new ArrayList<>();
    private SimpleItemRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.version_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
        loadVersions();

        if (findViewById(R.id.version_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        adapter = new SimpleItemRecyclerViewAdapter(versions);
        recyclerView.setAdapter(adapter);
    }

    private void loadVersions() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://dl.dropboxusercontent.com")
                .addConverterFactory(GsonConverterFactory.create()).build();
        VersionService versionService = retrofit.create(VersionService.class);
        Call<VersionsResponse> versionsResponseCall = versionService.getVersions();
        versionsResponseCall.enqueue(new Callback<VersionsResponse>() {
            @Override
            public void onResponse(Response<VersionsResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    VersionsResponse versionsResponse = response.body();
                    versions.clear();
                    versions.addAll(versionsResponse.getBody());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Version> mValues;

        public SimpleItemRecyclerViewAdapter(List<Version> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.version_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            Picasso.with(holder.mImageView.getContext()).load(mValues.get(position).getImage()).into(holder.mImageView);
            holder.mIdView.setText(mValues.get(position).getName());
            holder.mContentView.setText(mValues.get(position).getVersion());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(VersionDetailFragment.ARG_ITEM_ID, Parcels.wrap(holder.mItem));
                        VersionDetailFragment fragment = new VersionDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.version_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(VersionDetailFragment.ARG_ITEM_ID, Parcels.wrap(holder.mItem));
                        Intent intent = new Intent(context, VersionDetailActivity.class);
                        intent.putExtra(VersionDetailFragment.ARG_ITEM_ID, arguments);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            // TODO: comma at the end
            return mValues.size() - 1;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Version mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.image);
                mIdView = (TextView) view.findViewById(R.id.name);
                mContentView = (TextView) view.findViewById(R.id.version);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
