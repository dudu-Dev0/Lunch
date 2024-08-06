package com.dudu.wearlauncher.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.model.WatchFaceInfo;
import com.dudu.wearlauncher.ui.BaseActivity;
import com.dudu.wearlauncher.ui.ViewPagerFragmentAdapter;
import com.dudu.wearlauncher.utils.DensityUtil;
import com.dudu.wearlauncher.utils.SharedPreferencesUtil;
import com.dudu.wearlauncher.utils.WatchFaceHelper;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ChooseWatchFaceActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_watchface);
        ViewPager pager = findViewById(R.id.wf_choose_vp);
        pager.setPageMargin(DensityUtil.dip2px(this, 15));
        try {
            List<WatchFaceInfo> allWfList = WatchFaceHelper.getAllWatchFace();
            List<Fragment> fragmentList = new ArrayList<>();
            String nowWatchFaceName = (String) SharedPreferencesUtil.getData(SharedPreferencesUtil.NOW_WATCHFACE, "watchface-example");
            int nowWatchFacePosition = 0;
            for (WatchFaceInfo info : allWfList) {
                fragmentList.add(new WatchFacePreviewFragment(info.name));
                if (info.name.equals(nowWatchFaceName)) {
                    nowWatchFacePosition = allWfList.indexOf(info);
                }
            }
            fragmentList.add(new Choose2ImportWatchFaceFragment());
            ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
            pager.setAdapter(adapter);
            pager.setCurrentItem(nowWatchFacePosition);
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }

    public static class Choose2ImportWatchFaceFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_watchface_preview, container, false);
        }

        @Override
        public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            view.findViewById(R.id.wf_settings_button).setVisibility(View.INVISIBLE);
            Glide.with(requireActivity())
                    .load(AppCompatResources.getDrawable(requireActivity(), R.drawable.note_add))
                    .into((ImageView) view.findViewById(R.id.wf_pre_img));
            view.findViewById(R.id.wf_pre_img).setScaleX(0.5F);
            view.findViewById(R.id.wf_pre_img).setScaleY(0.5F);
            ((TextView) view.findViewById(R.id.wf_name_txt)).setText("从本地导入表盘");
        }
    }
}
