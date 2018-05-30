package ttc.project.stoku.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import ttc.project.stoku.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Element versionElement = new Element();
        versionElement.setTitle("Versi 1.4");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.stoku_logo)
                .addItem(versionElement)
                .setDescription(getString(R.string.stoku_about))
                .addEmail("devarafikry@gmail.com")
                .addPlayStore("ttc.project.stoku")
                .create();
        setContentView(aboutPage);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.about_title));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
