package com.vinnymac.bittool;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class AboutDialogFragment extends SherlockDialogFragment {
	
	private static final String TAG = AboutDialogFragment.class.getSimpleName();
	
	private static final String UNKNOWN = "unknown";

	public static AboutDialogFragment newInstance() {
		AboutDialogFragment f = new AboutDialogFragment();
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}

	@SuppressLint("InlinedApi")
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String version;
		try {
			version = getActivity().getPackageManager().getPackageInfo(
					getActivity().getPackageName(), 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			version = UNKNOWN;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog));
		builder.setTitle(String.format(getResources().getString(R.string.version), version));
		builder.setItems(R.array.attribution_labels,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String[] links = getResources().getStringArray(
								R.array.attribution_links);
						String link = links[which];
						Intent view = new Intent(Intent.ACTION_VIEW);
						Uri uri = Uri.parse(link);
						view.setData(uri);
						startActivity(view);
					}
				});
		builder.setNeutralButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dismiss();
					}
				});
		AlertDialog dialog = builder.create();
		return dialog;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		dismiss();
	}
}
