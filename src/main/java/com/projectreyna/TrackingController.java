package com.projectreyna;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

public class TrackingController {

    @FXML private Label statusLabel;
    @FXML private Label detailLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Button rateButton;

    // the ordered stages an order moves through
    private static final String[] STAGES =
            {"Confirmed", "Preparing", "On the way", "Delivered"};
    private int stageIndex = 0;

    private Timeline timeline;

    @FXML
    public void initialize() {
        Order order = Session.currentOrder;
        if (order == null) {
            statusLabel.setText("Status: No active order");
            return;
        }

        detailLabel.setText(String.format(
                "Order #%d from %s%nTotal: ₱%.2f",
                order.getOrderId(),
                order.getRestaurant().getName(),
                order.getTotal()));

        showStage();          // show the first stage immediately (Confirmed)
        startAutoTracking();  // then advance on its own
    }

    private void startAutoTracking() {
        // fire every 2.5 seconds; each tick moves to the next stage
        timeline = new Timeline(new KeyFrame(Duration.seconds(2.5), e -> advanceStage()));
        timeline.setCycleCount(STAGES.length - 1);   // 3 steps to reach the last stage
        timeline.play();
    }

    private void advanceStage() {
        if (stageIndex < STAGES.length - 1) {
            stageIndex++;
            Session.currentOrder.updateStatus(STAGES[stageIndex]);
            showStage();
        }
    }

    private void showStage() {
        String stage = STAGES[stageIndex];
        statusLabel.setText("Status: " + stage);
        progressBar.setProgress((double) stageIndex / (STAGES.length - 1));

        // enable rating only once the order is Delivered
        rateButton.setDisable(stageIndex != STAGES.length - 1);
    }

    @FXML
    private void handleRateOrder() {
        SceneSwitcher.switchTo("rating-view.fxml", statusLabel);
    }

    @FXML
    private void handleBackToRestaurants() {
        if (timeline != null) timeline.stop();   // stop the timer before leaving
        SceneSwitcher.switchTo("restaurants-view.fxml", statusLabel);
    }
}