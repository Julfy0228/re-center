package com.recenter.mapper;

import com.recenter.model.dto.ActivityResponse;
import com.recenter.model.dto.CategoryResponse;
import com.recenter.model.dto.DiscountResponse;
import com.recenter.model.dto.NewsResponse;
import com.recenter.model.dto.NotificationResponse;
import com.recenter.model.dto.PaymentResponse;
import com.recenter.model.dto.PromotionResponse;
import com.recenter.model.dto.ReviewResponse;
import com.recenter.model.dto.ServiceResponse;
import com.recenter.model.dto.UserResponse;
import com.recenter.model.entity.Activity;
import com.recenter.model.entity.Category;
import com.recenter.model.entity.Discount;
import com.recenter.model.entity.News;
import com.recenter.model.entity.Notification;
import com.recenter.model.entity.Payment;
import com.recenter.model.entity.Promotion;
import com.recenter.model.entity.Review;
import com.recenter.model.entity.Service;
import com.recenter.model.entity.User;

public final class EntityDtoMapper {

    private EntityDtoMapper() {
    }

    public static ActivityResponse toActivityResponse(Activity activity) {
        return ActivityResponse.builder()
                .id(activity.getId())
                .userId(activity.getUser() != null ? activity.getUser().getId() : null)
                .type(activity.getType() != null ? activity.getType().name() : null)
                .details(activity.getDetails())
                .createdAt(activity.getCreatedAt())
                .build();
    }

    public static CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public static DiscountResponse toDiscountResponse(Discount discount) {
        return DiscountResponse.builder()
                .id(discount.getId())
                .title(discount.getTitle())
                .description(discount.getDescription())
                .startDate(discount.getStartDate())
                .endDate(discount.getEndDate())
                .type(discount.getType() != null ? discount.getType().name() : null)
                .amount(discount.getAmount())
                .build();
    }

    public static NewsResponse toNewsResponse(News news) {
        return NewsResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .imageUrl(news.getImageUrl())
                .authorId(news.getAuthor() != null ? news.getAuthor().getId() : null)
                .authorEmail(news.getAuthor() != null ? news.getAuthor().getEmail() : null)
                .status(news.getStatus() != null ? news.getStatus().name() : null)
                .publishedAt(news.getPublishedAt())
                .createdAt(null)
                .build();
    }

    public static NotificationResponse toNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUser() != null ? notification.getUser().getId() : null)
                .type(notification.getType() != null ? notification.getType().name() : null)
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public static PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(payment.getBooking() != null ? payment.getBooking().getId() : null)
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .status(payment.getStatus())
                .paymentMethod(payment.getPaymentMethod())
                .build();
    }

    public static PromotionResponse toPromotionResponse(Promotion promotion) {
        return PromotionResponse.builder()
                .id(promotion.getId())
                .title(promotion.getTitle())
                .description(promotion.getDescription())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .build();
    }

    public static ReviewResponse toReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .bookingId(review.getBooking() != null ? review.getBooking().getId() : null)
                .content(review.getContent())
                .rating(review.getRating())
                .status(review.getStatus() != null ? review.getStatus().name() : null)
                .createdAt(review.getCreatedAt())
                .build();
    }

    public static ServiceResponse toServiceResponse(Service service) {
        return ServiceResponse.builder()
                .id(service.getId())
                .title(service.getTitle())
                .description(service.getDescription())
                .imageUrl(service.getImageUrl())
                .duration(service.getDuration())
                .price(service.getPrice())
                .maxPeople(service.getMaxPeople())
                .categoryId(service.getCategory() != null ? service.getCategory().getId() : null)
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
