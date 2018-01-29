package com.example.mango;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.Card.OnCardClickListener;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.client.utils.AsyncBitmapLoader;
import com.example.client.utils.ExceptionHandler;
import com.example.client.utils.HttpUtils;
import com.example.client.utils.JsonParse;
import com.example.client.utils.NetworkUtils;
import com.example.widget.BookListCard;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

@SuppressLint("ValidFragment") public class SearchListFragment extends Fragment{

		Bitmap[] bookCoverBm;
		private ExceptionHandler classifyHandler = new ExceptionHandler();
		private List<Map<String,Object>> sortBook;
		private int[] bookType = new int[] {1,2,3,5,6,11,7,8,4};
		private AsyncBitmapLoader abl = new AsyncBitmapLoader();
		private String imgURL,imgName;
		View type = null;
		LinearLayout noneType,waitType = null;
		CardListView typeList = null;
		
		protected  int typeId ;
		
		public SearchListFragment(int typeId){
			this.typeId = typeId;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			type = inflater.inflate(R.layout.search_type, container, false);
			noneType  = (LinearLayout) type.findViewById(R.id.none_type);
			waitType = (LinearLayout) type.findViewById(R.id.wait_type);
			typeList = (CardListView) type.findViewById(R.id.type_list);
			
			
			return type;
		}
		Handler myHandler = new Handler() {
			public void handleMessage(Message msg) {
				
				waitType.setVisibility(View.GONE);
				
				if(sortBook.size() != 0){
				ArrayList<Card> cards = new ArrayList<Card>();
		        for (int i=0;i<sortBook.size();i++){
		            BookListCard card = new BookListCard(getActivity());
		            card.setTitle(sortBook.get(i).get("bookName").toString());
		            card.setWriter(sortBook.get(i).get("author").toString());
		            card.setPrice("$" + sortBook.get(i).get("price").toString());
		            card.setBookId( (Integer) sortBook.get(i).get("bookId"));
		            card.setBitmap(bookCoverBm[i]);
		            card.count=i;

		            //Only for test, change some icon
		            //flag = i;
		            card.init();
		            card.setOnClickListener(new OnCardClickListener() {
						
						@Override
						public void onClick(Card card, View view) {
							// TODO Auto-generated method stub
							//Log.d("flag", ""+flag);
							//search.setVisibility(View.INVISIBLE);
							BookFragment bookFragment = new BookFragment(((BookListCard)card).getBookId());
							FragmentTransaction transaction = getFragmentManager().beginTransaction();
							transaction.replace(R.id.mainfragment, bookFragment);
							//transaction.addToBackStack(null);
							transaction.commit();
						}
					});
		            card.setBackgroundResourceId(R.drawable.card_alfa);
		            cards.add(card);
		        }
		        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
		      

		        typeList = (CardListView) type.findViewById(R.id.list_list);
		        typeList.setVisibility(View.VISIBLE);
		        if (typeList!=null){
//		            list_lin.setAdapter(mCardArrayAdapter);
		            AnimationAdapter animCardArrayAdapter = new AlphaInAnimationAdapter(mCardArrayAdapter);
		            animCardArrayAdapter.setAbsListView(typeList);
		            typeList.setExternalAdapter(animCardArrayAdapter,mCardArrayAdapter);
		        }
				}else{
				noneType.setVisibility(View.VISIBLE);
				}
			};
		};
		
		public class sortBookThread extends Thread{
			public int mTypeid;
			public sortBookThread(int typeid){
				mTypeid = typeid;
			}
			public void run(){
				JSONObject params = new JSONObject();
				try{
					params.put("typeid", mTypeid);
				}catch(JSONException e1){
					classifyHandler.showMessage("信息获取错误");
				}
				sortBook = JsonParse.getListMap("books",HttpUtils.getJsonContent(NetworkUtils.DANGDANG_SEARCH_URL, classifyHandler, params));
				bookCoverBm = new Bitmap[sortBook.size()];
				for(int i=0;i<sortBook.size();i++){
				imgURL = sortBook.get(i).get("imagePath").toString();
				imgName = sortBook.get(i).get("imageName").toString();
				bookCoverBm[i] = abl.loadBitmap(NetworkUtils.DANGDANG_BASE_URL + imgURL
						+ imgName);
				}
				myHandler.sendEmptyMessage(1);
			}
		}

		
}
