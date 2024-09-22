package com.dudu.wearlauncher.ui.home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.blankj.utilcode.util.FileIOUtils;
import com.bumptech.glide.Glide;
import com.dudu.wearlauncher.R;
import com.dudu.wearlauncher.model.WatchFaceInfo;
import com.dudu.wearlauncher.ui.BaseActivity;
import com.dudu.wearlauncher.ui.ViewPagerFragmentAdapter;
import com.dudu.wearlauncher.ui.settings.HiddenActivitiesSettingsActivity;
import com.dudu.wearlauncher.ui.settings.ImportLocalWatchFaceActivity;
import com.dudu.wearlauncher.utils.DensityUtil;
import com.dudu.wearlauncher.utils.ILog;
import com.dudu.wearlauncher.utils.SharedPreferencesUtil;
import com.dudu.wearlauncher.utils.WatchFaceHelper;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ChooseWatchFaceActivity extends BaseActivity {
    public static int FILE_CHOOSER_CODE = 114514;
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
            view.setOnClickListener(v->{
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("application/zip");
                    requireActivity().startActivityForResult(intent, FILE_CHOOSER_CODE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(requireActivity(), "请安装FileManager!", Toast.LENGTH_SHORT).show();
                }
            });
            view.setOnLongClickListener(v->{
                Intent intent = new Intent(requireActivity(),HiddenActivitiesSettingsActivity.class);
                requireActivity().startActivity(intent);
                return false;
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == FILE_CHOOSER_CODE){
                try {
                    File cacheZip = new File(getCacheDir(),System.currentTimeMillis()+".zip");
                    FileIOUtils.writeFileFromIS(cacheZip,this.getContentResolver().openInputStream(data.getData()));
                    Intent intent = new Intent(this, ImportLocalWatchFaceActivity.class);
                    intent.putExtra("zip_path",cacheZip.getAbsolutePath());
                    startActivity(intent);
                    finish();
                } catch (FileNotFoundException e) {
                    ILog.e(e.getLocalizedMessage());
                    Toast.makeText(this,"File Not Found",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
