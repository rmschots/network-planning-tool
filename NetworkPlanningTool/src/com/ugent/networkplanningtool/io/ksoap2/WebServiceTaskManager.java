package com.ugent.networkplanningtool.io.ksoap2;

import android.app.ProgressDialog;
import android.content.Context;

public class WebServiceTaskManager {
	
	private final ProgressDialog progressDialog;

	public WebServiceTaskManager(Context context) {
		this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}
	
	/**
     * Executes a task in the background thread, while displaying a busy dialog (non cancellable).
     * 
     * @param task
     *            {@link com.ugent.networkplanningtool.io.ksoap2.AbstractWebServiceTask}
     * @param request
     *            request for the background task
     * @param progressLabel
     *            label to be displayed when the progress dialog is being displayed.
     * @param onTaskCompletedListener
     *            {@link OnAsyncTaskCompleteListener} to be notified once the task is completed.
     */
    @SuppressWarnings("unchecked")
	public <T, P> void executeTask(AbstractWebServiceTask<P, T> task, P request, CharSequence progressLabel,
            OnAsyncTaskCompleteListener<T> onTaskCompletedListener) {
        this.progressDialog.setMessage(progressLabel);

        task.setOnTaskCompletionListener(onTaskCompletedListener);
        task.setProgressTracker(this);
        task.execute(request);
    }

    // ------------------------------------------------------------------------
    // Progress Handlers
    // ------------------------------------------------------------------------

    public void onStartProgress() {
        progressDialog.show();
    }

    public void onStopProgress() {
        progressDialog.dismiss();
    }

}
