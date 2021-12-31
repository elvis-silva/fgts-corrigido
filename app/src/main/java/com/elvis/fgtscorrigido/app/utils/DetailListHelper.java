package com.elvis.fgtscorrigido.app.utils;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.elvis.fgtscorrigido.app.FGTSCorrigido;
import com.elvis.fgtscorrigido.app.R;
import com.elvis.fgtscorrigido.app.context.Contexts;
import com.elvis.fgtscorrigido.app.data.DataHandler;
import com.elvis.fgtscorrigido.app.model.FGTSDataModel;
import com.elvis.fgtscorrigido.app.model.FGTSDataRow;
import com.elvis.fgtscorrigido.app.model.NamedItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by elvis on 23/06/15.
 */
public class DetailListHelper {

    private Context context = Contexts.getInstance().getContext();

    private SimpleAdapter listViewAdapter;
    private List<Map<String, Object>> listViewMapList = new ArrayList<>();
    private ListView listView;
    private Map<String, FGTSDataModel> accountMap = new HashMap<>();
    private List<FGTSDataModel> dataModelList;

    private static String[] from = {
            "layout", "layout_inner", "category", "subcategory", "value", "title", "date"
    };
    private static int[] to = {
            R.id.detlist_item_layout,
            R.id.detlist_item_layout_inner,
            R.id.detlist_item_category,
            R.id.detlist_item_subcategory,
            R.id.detlist_item_value,
            R.id.detlist_item_title,
            R.id.detlist_item_date
    };

    public DetailListHelper() {
    }

    public void setup(ListView pListView, String pNameSaved) {
        listViewAdapter = new SimpleAdapter(context, listViewMapList, R.layout.detail_list, from, to);
        listViewAdapter.setViewBinder(new ListViewBinder());
        listView = pListView;
        listView.setAdapter(listViewAdapter);

        DataHandler dataHandler = FGTSCorrigido.dataHandler;
        Cursor cursor = dataHandler.returnFGTSData();
        cursor.moveToFirst();
        int i = 0;
        int j = 0;
        while (i < cursor.getCount()) {
            FGTSDataModel fgtsDataModel = dataHandler.parseFGTSData(cursor);
            if(fgtsDataModel.getName().equals(pNameSaved)) {
                accountMap.put(String.valueOf(fgtsDataModel.getId()), fgtsDataModel);
//                FGTSDataRow dataRow = new FGTSDataRow(context, fgtsDataModel.getMonth(),
//                        fgtsDataModel.getYear(), fgtsDataModel.getDeposito(), fgtsDataModel.getTr(),
//                        fgtsDataModel.getInpc(), fgtsDataModel.getDiference(), i);
                if(j < 2) {
                    GUI.toastShort(String.valueOf(fgtsDataModel.getId()));
                    j++;
                }
            }
            cursor.moveToNext();
            i++;
        }
        cursor.close();
    }

    public void reloadData(List<FGTSDataModel> pDataModelList) {
        dataModelList = pDataModelList;
        listViewMapList.clear();
        for(FGTSDataModel dataModel : pDataModelList) {
            Map<String, Object> row = toDetailMap(dataModel);
            listViewMapList.add(row);
        }
        listViewAdapter.notifyDataSetChanged();
    }

    private Map<String, Object> toDetailMap(FGTSDataModel pModel) {
        Map<String, Object> row = new HashMap<>();
        row.put(from[0], new NamedItem(from[0], pModel, from[0]));
        row.put(from[1], new NamedItem(from[1], pModel, from[1]));
        row.put(from[2], new NamedItem(from[2], pModel, pModel.getDeposito()));
        row.put(from[3], new NamedItem(from[3], pModel, pModel.getTr()));
        row.put(from[4], new NamedItem(from[4], pModel, pModel.getInpc()));
        row.put(from[5], new NamedItem(from[5], pModel, pModel.getMonth()));
        row.put(from[6], new NamedItem(from[6], pModel, pModel.getYear()));
        return row;
    }

    class ListViewBinder implements SimpleAdapter.ViewBinder{
//        AccountType last, lastCategory, lastSubcategory;

        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
/*            NamedItem item = (NamedItem) data;
            String name = item.getName();
            Detail detail = (Detail) item.getDeposito();

            Account categoryAccount = accountMap.get(detail.getCategory());
            Account subcategoryAccount = accountMap.get(detail.getSubcategory());

            if(name.equals("layout")) {

                RelativeLayout layout = (RelativeLayout) view;

                int flag = 0;
                if(subcategoryAccount != null) {
                    if(subcategoryAccount.getType().equals(AccountType.EXPENSE.getType())) {
                        flag |= 1;
                    }
                    lastSubcategory = AccountType.find(subcategoryAccount.getType());
                } else {
                    lastSubcategory = AccountType.UNKONW;
                }

                if(categoryAccount != null) {
                    if(categoryAccount.getType().equals(AccountType.INCOME.getType())) {
                        flag |= 2;
                    }
                    lastCategory = AccountType.find(categoryAccount.getType());
                } else {
                    lastCategory = AccountType.UNKONW;
                }
*/
//                Drawable drawable = activity.getResources().getDrawable(R.drawable.selector_unknow);
//                layout.setBackgroundDrawable(drawable);
/*                return true;
            }

            if(name.equals("layout_inner")) {
                LinearLayout layoutInner = (LinearLayout) view;
                Date detailDate = detail.getDate();
                Date today = new Date();
                Date day3 = calendarHelper.dateBefore(today, 3);
                Date day7 = calendarHelper.dateBefore(today, 7);
//                Drawable drawable = null;
//                if(calendarHelper.isFutureDay(today, detailDate)) {
//                    drawable = new ColorDrawable(0x4FFFFFFF);
//                }else if(calendarHelper.isSameDay(today, detailDate)) {
//                    today
//                    drawable = new ColorDrawable(0x3FFFFFFF);
//                }else if(calendarHelper.isFutureDay(day3, detailDate)) {
//                    drawable = new ColorDrawable(0x1fFFFFFF);
//                }else if(calendarHelper.isFutureDay(day7, detailDate)) {
//                    drawable = new ColorDrawable(0x0FFFFFFF);
//                }
//                layoutInner.setBackgroundDrawable(drawable);
                return true;
            }

            if(!(view instanceof TextView)) {
                return false;
            }

            if(AccountType.INCOME.equals(last)) {
                ((TextView)view).setTextColor(activity.getResources().getColor(R.color.income_fg));
            } else if(AccountType.EXPENSE.equals(last)) {
                ((TextView)view).setTextColor(activity.getResources().getColor(R.color.expense_fg));
            } else {
                ((TextView)view).setTextColor(activity.getResources().getColor(R.color.unknow_fg));
            }

            if(name.equals("category")) {
                view.setBackgroundDrawable(null);
                if(detail.getCategoryType().equals(AccountType.INCOME.getType())) {
                    ((TextView) view).setTextColor(activity.getResources().getColor(R.color.income_fgd));
                } else {
                    ((TextView) view).setTextColor(activity.getResources().getColor(R.color.expense_fgd));
                }
                YoYo.with(Techniques.BounceInRight).duration(1000).playOn(view);
            }

            if(name.equals("subcategory")) {
                view.setBackgroundDrawable(null);
            }
*/            return false;
        }
    }
}
