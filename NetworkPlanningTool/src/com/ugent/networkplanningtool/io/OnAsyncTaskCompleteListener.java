package com.ugent.networkplanningtool.io;

/**
 * Observer for executing logic asynchronously.
 * https://github.com/thira/blog-demos/tree/master/android-kSOAP2-example
 * 
 * @author Thiranjith
 * 
 * @param <T>
 *            result type expected after invoking the web service call
 */

public interface OnAsyncTaskCompleteListener<T> {
	/**
     * Callback method that get invoked when the web service task is successfully completed.
     * 
     * @param result result of the task
     */
    void onTaskCompleteSuccess(T result);

    /**
     * Callback method that get invoked if the task failed.
     * 
     * @param cause
     *            {@link Exception} describing the cause for the failure
     */
    void onTaskFailed(Exception cause);
}
