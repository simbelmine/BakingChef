package com.example.android.bakingchef;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingchef.dummy.DummyContent;

import java.util.List;


public class RecipesListAdapter extends RecyclerView.Adapter<RecipesViewHolder> {

    private final List<DummyContent.DummyItem> mValues;
    private Context context;

    public RecipesListAdapter(Context context, List<DummyContent.DummyItem> items) {
        this.context = context;
        mValues = items;
    }

    @Override
    public RecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipesViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.recipePhoto.setImageResource(R.mipmap.ic_launcher);
        holder.recipeTitle.setText(mValues.get(position).content);

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mTwoPane) {
//                    Bundle arguments = new Bundle();
//                    arguments.putString(DetailFragment.ARG_ITEM_ID, holder.mItem.id);
//                    DetailFragment fragment = new DetailFragment();
//                    fragment.setArguments(arguments);
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.item_detail_container, fragment)
//                            .commit();
//                } else {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, DetailActivity.class);
//                    intent.putExtra(DetailFragment.ARG_ITEM_ID, holder.mItem.id);
//
//                    context.startActivity(intent);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}