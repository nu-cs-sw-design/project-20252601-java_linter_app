package test;

public class LoyaltyDiscountPolicy implements DiscountPolicy {

    private int threshold;
   LoyaltyDiscountPolicy(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public double applyDiscount(double OriginalAmount) {
        if (OriginalAmount > threshold) {
            return OriginalAmount * 0.9;
        }
        return OriginalAmount;
    }
}
