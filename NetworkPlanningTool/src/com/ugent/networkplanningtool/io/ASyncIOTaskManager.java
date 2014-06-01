package com.ugent.networkplanningtool.io;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;

/**
 * Manager of ASyncTasks used for input and output
 */
public class ASyncIOTaskManager {

    private final ProgressDialog progressDialog;

    /**
     * Default constructor creating the progress dialog for the application to use
     * @param context the context of the parent
     */
    public ASyncIOTaskManager(Context context) {
        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    /**
     * Executes a task in the background thread, while displaying a busy dialog (non cancellable).
     *
     * @param task                    {@link AbstractASyncTask}
     * @param request                 request for the background task
     * @param progressLabel           label to be displayed when the progress dialog is being displayed.
     * @param onTaskCompletedListener {@link OnAsyncTaskCompleteListener} to be notified once the task is completed.
     */
    @SuppressWarnings("unchecked")
    public <T, P> void executeTask(AbstractASyncTask<P, T> task, P request, CharSequence progressLabel,
                                   OnAsyncTaskCompleteListener<T> onTaskCompletedListener, boolean darkenBackground) {
        if(darkenBackground){
            progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        this.progressDialog.setMessage(progressLabel);

        task.setOnTaskCompletionListener(onTaskCompletedListener);
        task.setProgressTracker(this);
        task.execute(request);
    }

    /**
     * Handler for the start of the progress
     */
    public void onStartProgress() {
        progressDialog.show();
    }

    /**
     * Handler for the stop of the progress
     */
    public void onStopProgress() {
        progressDialog.dismiss();
    }

}
