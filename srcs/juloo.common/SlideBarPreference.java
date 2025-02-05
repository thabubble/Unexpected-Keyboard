package juloo.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.SeekBar;

/*
 ** SideBarPreference
 ** -
 ** Open a dialog showing a seekbar
 ** -
 ** xml attrs:
 **   android:defaultValue  Default value (float)
 **   min                   min value (float)
 **   max                   max value (float)
 ** -
 ** Summary field allow to show the current value using %f or %s flag
 */
  
public class SlideBarPreference extends DialogPreference
  implements SeekBar.OnSeekBarChangeListener
{
  private LinearLayout _layout;
  private TextView _textView;
  private SeekBar _seekBar;

  private final int _min;
  private final float _pow;
  private float _value;

  private String _initialSummary;

  public SlideBarPreference(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    String min = attrs.getAttributeValue(null, "min");
    String max = attrs.getAttributeValue(null, "max");
    
    int minP = 0;
    if (min != null)
    {
      int i = min.indexOf(".");
      if (i > -1)
        minP = min.length() - i;
    }
    int maxP = 0;
    if (max != null)
    {
      int i = max.indexOf(".");
      if (i > -1)
        maxP = max.length() - i;
    }
    _pow = (float)Math.pow(10, Math.max(minP, maxP));

    _value = float_of_string(min);
    _min = (int)(_value * _pow);
    int maxI = (int)(Math.max(1f, float_of_string(max)) * _pow);

    _initialSummary = getSummary().toString();
    _textView = new TextView(context);
    _textView.setPadding(48, 40, 48, 40);
    _seekBar = new SeekBar(context);
    _seekBar.setOnSeekBarChangeListener(this);
    _seekBar.setMax(maxI-_min);
    _layout = new LinearLayout(getContext());
    _layout.setOrientation(LinearLayout.VERTICAL);
    _layout.addView(_textView);
    _layout.addView(_seekBar);
  }

  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
  {
    _value = (float)(_min + progress)/_pow;
    updateText();
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar)
  {
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar)
  {
  }

  @Override
  protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue)
  {
    if (restorePersistedValue)
    {
      _value = getPersistedFloat(_min);
    }
    else
    {
      _value = (Float)defaultValue;
      persistFloat(_value);
    }
    _seekBar.setProgress((int)(_value*_pow)-_min);
    updateText();
  }

  @Override
  protected Object onGetDefaultValue(TypedArray a, int index)
  {
    return (a.getFloat(index, (float)_min/_pow));
  }

  @Override
  protected void onDialogClosed(boolean positiveResult)
  {
    if (positiveResult)
      persistFloat(_value);
  }

  protected View onCreateDialogView()
  {
    ViewGroup parent = (ViewGroup)_layout.getParent();

    if (parent != null)
      parent.removeView(_layout);
    return (_layout);
  }

  private void updateText()
  {
    String f = String.format(_initialSummary, _value);

    _textView.setText(f);
    setSummary(f);
  }

  private static float float_of_string(String str)
  {
    if (str == null)
      return (0f);
    return (Float.parseFloat(str));
  }
}
