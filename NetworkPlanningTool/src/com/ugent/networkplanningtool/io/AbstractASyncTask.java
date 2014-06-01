package com.ugent.networkplanningtool.io;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Abstract class for an asynchronous task to be extended for specific tasks
 * @param <P> the input type for the task
 * @param <R> the output type for the task
 */
public abstract class AbstractASyncTask<P, R> extends AsyncTask<P, Void, R> {

    private final String TAG = getClass().getName();

    private OnAsyncTaskCompleteListener<R> taskCompletionListener;
    private ASyncIOTaskManager progressTracker;
    // Most recent exception (used to diagnose failures)
    private Exception mostRecentException;

    /**
     * Default constructor
     */
    public AbstractASyncTask() {
    }

    /**
     * Sets the listener that needs to be alerted when the task is complete
     * @param taskCompletionListener the listener that needs to be alerted when the task is complete
     */
    public final void setOnTaskCompletionListener(OnAsyncTaskCompleteListener<R> taskCompletionListener) {
        this.taskCompletionListener = taskCompletionListener;
    }

    /**
     * Sets the progress tracker that needs to be alerted when the task progress changed
     * @param progressTracker the progress tracker that needs to be alerted when the task progress changed
     */
    public final void setProgressTracker(ASyncIOTaskManager progressTracker) {
        if (progressTracker != null) {
            this.progressTracker = progressTracker;
        }
    }

    /**
     * Alerts the progress tracker before the task begins executing
     */
    @Override
    protected final void onPreExecute() {
        if (progressTracker != null) {
            this.progressTracker.onStartProgress();
        }
    }

    /**
     * Invokes the task (only the first parameter is used)
     * @param parameters the parameters for the task
     * @return the result of the task
     */
    @Override
    protected final R doInBackground(P... parameters) {
        mostRecentException = null;
        R result = null;

        try {
            result = performTaskInBackground(parameters[0]);
        } catch (Exception e) {
            Log.e(TAG, "Failed to invoke the web service: ", e);
            mostRecentException = e;
        }

        return result;
    }

    /**
     * Task performing method to be implemented by extending classes
     * @param parameter the task input parameter
     * @return the result of the task
     * @throws Exception The Exception when the task fails.
     */
    protected abstract R performTaskInBackground(P parameter) throws Exception;

    /**
     * Alerts the listener and progress tracker that the task is done.
     * @param result the task result
     */
    @Override
    protected final void onPostExecute(R result) {
        if (progressTracker != null) {
            progressTracker.onStopProgress();
        }

        if (taskCompletionListener != null) {
            if (result == null || mostRecentException != null) {
                taskCompletionListener.onTaskFailed(mostRecentException);

            } else {
                taskCompletionListener.onTaskCompleteSuccess(result);
            }
        }

        // clean up listeners since we are done with this task
        progressTracker = null;
        taskCompletionListener = null;
    }
}
