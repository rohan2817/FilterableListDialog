package me.rohanpeshkar.filterablelistdialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * FilterableListDialog showing list of items with EditText to filter items given in the list
 * Created by Rohan on 9/26/17.
 */

public class FilterableListDialog extends AlertDialog implements
        RecyclerItemClickListener.OnItemClickListener {

    /**
     * RecyclerView of the list of items
     */
    private RecyclerView revList;

    /**
     * EditText to enter filter query
     */
    private EditText edtFilter;

    /**
     * List Adapter for items
     */
    private ListAdapter mListAdapter;

    /**
     * List of items
     */
    private ArrayList<String> mItems;

    /**
     * Context of calling activity
     */
    private Context mContext;

    /**
     * Item selected listener
     */
    private OnListItemSelectedListener mOnListItemSelectedListener;

    /**
     * Private Custom constructor
     *
     * @param context                    - Context of calling activity
     * @param items                      - ArrayList of items, must be string type
     * @param onListItemSelectedListener - ItemSelectedListener Object
     */
    private FilterableListDialog(@NonNull Context context, ArrayList<String> items,
                                 OnListItemSelectedListener onListItemSelectedListener) {
        super(context);
        mItems = items;
        mContext = context;
        mOnListItemSelectedListener = onListItemSelectedListener;
    }

    /**
     * Method to create Filterable List Dialog, static public to access from any activity
     *
     * @param context                    - Context of calling activity
     * @param items                      - ArrayList of items, must be string type
     * @param onListItemSelectedListener - ItemSelectedListener instance
     * @return - FilterableListDialog instance
     */
    public static FilterableListDialog create(Context context, ArrayList<String> items,
                                              OnListItemSelectedListener onListItemSelectedListener) {
        return new FilterableListDialog(context, items, onListItemSelectedListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_dialog);
        handleKeyBoard();
        initViews();
        initList();
        setUpFilter();
    }

    /**
     * Method to handle showing keyboard for filter query
     */
    private void handleKeyBoard() {
        if (getWindow() != null) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
    }

    /**
     * Initialize views
     */
    private void initViews() {
        revList = (RecyclerView) findViewById(R.id.rev_list);
        edtFilter = (EditText) findViewById(R.id.edt_filter);
    }

    /**
     * Initialize list with given list of items
     */
    private void initList() {
        revList.setLayoutManager(new LinearLayoutManager(mContext));
        revList.setHasFixedSize(true);
        mListAdapter = new ListAdapter(mContext, mItems);
        revList.addOnItemTouchListener(new RecyclerItemClickListener(mContext, revList, this));
        revList.setAdapter(mListAdapter);
    }

    /**
     * Setup EditText text change listener to accept filter query
     */
    private void setUpFilter() {
        edtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence constraint, int i, int i1, int i2) {
                mListAdapter.getFilter().filter(constraint);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * OnItemClick called when item is clicked from list
     *
     * @param view     - View of the item
     * @param position - Position of the item
     */
    @Override
    public void OnItemClick(View view, int position) {
        mOnListItemSelectedListener.onItemSelected(mListAdapter.getItem(position));
        dismiss();
    }

    /**
     * OnItemLongClick not used at this moment
     *
     * @param view     - View of the item
     * @param position - Position of the item
     */
    @Override
    public void OnItemLongClick(View view, int position) {

    }

    /**
     * ListAdapter class implementing filterable interface
     */
    class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemViewHolder>
            implements Filterable {

        /**
         * List of items, need to be string
         */
        private ArrayList<String> mItems;

        /**
         * Context
         */
        private Context mContext;

        /**
         * List of filtered items
         */
        private ArrayList<String> mFilteredList;

        /**
         * Filter instance
         */
        private ItemFilter mItemFilter;

        /**
         * Constructor
         *
         * @param context - Context
         * @param items   - List of items
         */
        ListAdapter(Context context, ArrayList<String> items) {
            mContext = context;
            mItems = items;
            mFilteredList = items;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.layout_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.txtItem.setText(getItem(position));
        }

        String getItem(int pos) {
            return mItems.get(pos);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        /**
         * Method to update items from Filter
         *
         * @param items - List of updated items
         */
        void updateItems(ArrayList<String> items) {
            mItems = items;
            notifyDataSetChanged();
        }

        /**
         * Method to return filter
         *
         * @return - Filter instance
         */
        @Override
        public Filter getFilter() {
            if (mItemFilter == null) {
                mItemFilter = new ItemFilter(mFilteredList, this);
            }
            return mItemFilter;
        }

        /**
         * View Holder class
         */
        class ItemViewHolder extends RecyclerView.ViewHolder {

            TextView txtItem;

            ItemViewHolder(View itemView) {
                super(itemView);
                txtItem = itemView.findViewById(R.id.txt_item);
            }
        }

    }

    /**
     * Filter class extending Android Filter
     */
    private class ItemFilter extends Filter {

        private ArrayList<String> mItems;

        private ListAdapter mListAdapter;

        ItemFilter(ArrayList<String> items, ListAdapter listAdapter) {
            mItems = items;
            mListAdapter = listAdapter;
        }

        /**
         * Method to perform filtering
         *
         * @param constraint - Filter constraint
         * @return - Filtered results
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (TextUtils.isEmpty(constraint)) {
                filterResults.count = mItems.size();
                filterResults.values = mItems;
            } else {
                constraint = constraint.toString().toLowerCase();
                ArrayList<String> filteredItems = new ArrayList<>();
                for (String item : mItems) {
                    if (item.toLowerCase().contains(constraint)) {
                        filteredItems.add(item);
                    }
                }

                filterResults.count = filteredItems.size();
                filterResults.values = filteredItems;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mListAdapter.updateItems((ArrayList<String>) filterResults.values);
        }
    }

    /**
     * OnListItemSelected Listener Interface
     */
    public interface OnListItemSelectedListener {

        void onItemSelected(String item);
    }


}
