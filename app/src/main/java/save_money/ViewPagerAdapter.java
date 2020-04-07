package save_money;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.intern.R;

public class ViewPagerAdapter extends PagerAdapter {
    LayoutInflater layoutInflater;
    private Context context;
    private int img[] = {R.drawable.pr, R.drawable.r1, R.drawable.pr};

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    //Return the number of views available.
    public int getCount() {
        return img.length;
    }

    @Override
    //Determines whether a page View is associated with a specific key object as returned by instantiateItem(ViewGroup, int). This method is required for a PagerAdapter to function properly.
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    //(returntype-object)  Returns an Object representing the new page
    //Create the page for the given position,   The adapter is responsible for adding the view to the container given here,

    public Object instantiateItem(@NonNull ViewGroup container, int position) {                                             //ViewGroup:(container) The containing View in which the page will be shown.
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);                           //int:(position) The page position to be instantiated.
        View view = layoutInflater.inflate(R.layout.image_slider, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(img[position]);      //passing array inside setImageREsource with its position i.e index

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view);                        //the view that we have inflated above is passed as an argument in addView
        return view;
    }

    @Override
    //Remove a page for the given position. The adapter is responsible for removing the view from its container
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {                   //ViewGroup: The containing View from which the page will be removed.
        //int: The page position to be removed.
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
    }
}
