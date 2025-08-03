/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comnieu.entity;

/**
 *
 * @author ThinkBook
 */
public class PromotionStats {
    private int promotionId;
    private String promotionName;
    private float discountRate;
    private int usedCount;

    public PromotionStats(int promotionId, String promotionName, float discountRate, int usedCount) {
        this.promotionId = promotionId;
        this.promotionName = promotionName;
        this.discountRate = discountRate;
        this.usedCount = usedCount;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public float getDiscountRate() {
        return discountRate;
    }

    public int getUsedCount() {
        return usedCount;
    }
}