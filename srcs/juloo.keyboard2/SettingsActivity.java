package juloo.keyboard2;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity
{
  private static SettingsActivity _instance;
  
  public static void reloadPrefs()
  {
    if (_instance != null)
      _instance.recreate();
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.settings);
    _instance = this;
  }
  
  @Override
  public void onDestroy()
  {
    _instance = null;
    super.onDestroy();
  }
}
