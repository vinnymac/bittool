package com.vinnymac.bittool;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class NotifyDialog extends SherlockDialogFragment {

	private static final String TAG = NotifyDialog.class.getSimpleName();

	private final static String KEY_SOMEINFO = "SOME_INFO";

	public static NotifyDialog newInstance(String someInfo) {
		NotifyDialog dialog = new NotifyDialog();
		Bundle bundle = new Bundle();
		bundle.putString(KEY_SOMEINFO, someInfo);
		dialog.setArguments(bundle);
		return dialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("InlinedApi")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(getActivity(),
						android.R.style.Theme_Holo_Dialog));
		builder.setMessage(getResources().getString(R.string.alert_me));
		builder.setPositiveButton(R.string.add,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Toast.makeText(getActivity(), "Added",
								Toast.LENGTH_SHORT).show();
					}
				}).setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dismiss();
					}
				});
		return builder.create();
	}
}
