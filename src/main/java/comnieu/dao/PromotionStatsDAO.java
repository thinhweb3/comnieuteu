/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package comnieu.dao;

import comnieu.entity.PromotionStats;
import java.util.Date;
import java.util.List;

public interface PromotionStatsDAO {
    List<PromotionStats> getPromotionsUsed(Date from, Date to);
}