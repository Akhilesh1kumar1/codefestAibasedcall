package com.sr.capital.external.crif.Constant;

public enum CrifScoreRating {
    VERY_POOR(300, 449, "Very Poor"),
    POOR(450, 549, "Poor"),
    FAIR(550, 674, "Fair"),
    GOOD(675, 729, "Good"),
    VERY_GOOD(730, 829, "Very Good"),
    EXCELLENT(830, 900, "Excellent");

    private final int minScore;
    private final int maxScore;
    private final String label;

    CrifScoreRating(int minScore, int maxScore, String label) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.label = label;
    }

    public int getMinScore() {
        return minScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public String getLabel() {
        return label;
    }
}