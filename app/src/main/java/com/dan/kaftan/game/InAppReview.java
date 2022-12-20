package com.dan.kaftan.game;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.model.ReviewErrorCode;
import com.google.android.play.core.review.testing.FakeReviewManager;

public class InAppReview {

    private ReviewManager manager;
    private Context activityContext;
    private ReviewInfo reviewInfo;
    private Task<ReviewInfo> request;
    private Activity activity = (Activity) activityContext;


    public InAppReview(Context activityContext) {
        this.manager = ReviewManagerFactory.create(activityContext);
        this.activityContext = activityContext;
        request = manager.requestReviewFlow();
    }

    public void startReview(){
        initReview();
        if (reviewInfo != null){
            launchReview();
        }
    }

    private void initReview(){
        request.addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                reviewInfo = task.getResult();
            } else {
                // There was some problem, log or handle the error code.
            }
        });
    }

    private void launchReview(){
        Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
        flow.addOnCompleteListener(task -> {
            // The flow has finished. The API does not indicate whether the user
            // reviewed or not, or even whether the review dialog was shown. Thus, no
            // matter the result, we continue our app flow.
        });
    }


}
