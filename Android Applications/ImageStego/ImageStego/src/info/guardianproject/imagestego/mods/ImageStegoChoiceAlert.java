package info.guardianproject.imagestego.mods;

import java.util.ArrayList;
import java.util.List;

import info.guardianproject.imagestego.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageStegoChoiceAlert extends AlertDialog implements View.OnClickListener {
	View inner;
	TextView title, summary;
	LinearLayout choiceHolder;
	
	Activity a;
	
	LayoutInflater li;
	
	
	ImageStegoChoiceAlert isca;
	List<ChoiceButton> choices;
	
	public interface ChoiceAlertListener {
		public void onChoice(int choice);
	}
	
	public ImageStegoChoiceAlert(Activity a) {
		super(a);
		this.a = a;
		li = LayoutInflater.from(a);
		inner = li.inflate(R.layout.imagestegochoicealert, null);
		setView(inner);
		
		summary = (TextView) inner.findViewById(R.id.alert_summary);
		title = (TextView) inner.findViewById(R.id.alert_title);
		choiceHolder = (LinearLayout) inner.findViewById(R.id.alert_button_holder);
		
		choices = new ArrayList<ChoiceButton>();
		
		isca = this;
	}
	
	public ImageStegoChoiceAlert(Context c) {
		super(c);
	}
	
	@Override
	public void setMessage(CharSequence m) {
		summary.setText(m);
	}
	
	@Override
	public void setTitle(CharSequence t) {
		title.setText(t);
	}
	
	private void refresh() {
		choiceHolder.removeAllViews();
		for(Button b : choices)
			choiceHolder.addView(b);
	}
	
	public void addChoice(String b, int id) {
		ChoiceButton choice = new ChoiceButton(a, id);
		choice.setText(b);
		choice.setOnClickListener(this);
		choices.add(choice);
		refresh();
	}

	@Override
	public void onClick(View v) {
		
		((ChoiceAlertListener) a).onChoice(((ChoiceButton) v).id);
		isca.dismiss();
	}
	
	public class ChoiceButton extends Button {
		int id;
		
		public ChoiceButton(Context context) {
			super(context);
		}
		
		public ChoiceButton(Context context, int id) {
			super(context);
			this.id = id;
		}
		
	}
}
