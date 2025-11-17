package com.recenter.dto;

import java.util.List;

public class PromotionDetailDto {
    private PromotionDto promotion;
    private List<ServiceSummaryDto> applicableServices;
    private List<ServiceCategoryDto> applicableCategories;

    public PromotionDto getPromotion() { return promotion; }
    public void setPromotion(PromotionDto promotion) { this.promotion = promotion; }

    public List<ServiceSummaryDto> getApplicableServices() { return applicableServices; }
    public void setApplicableServices(List<ServiceSummaryDto> applicableServices) { this.applicableServices = applicableServices; }

    public List<ServiceCategoryDto> getApplicableCategories() { return applicableCategories; }
    public void setApplicableCategories(List<ServiceCategoryDto> applicableCategories) { this.applicableCategories = applicableCategories; }
}
