package com.yasdalteam.yasdalege.Payments;

public enum PaymentCancellations
{
    call_issuer(10, "Оплата отклонена по неизвестным причинам. Обратитесь в организацию, выпустившую платежное средство."),
    card_expired(11, "Истек срок действия банковской карты. Используйте другое платежное средство."),
    country_forbidden(12, "Невозможно заплатить банковской картой, выпущенной в этой стране.  Используйте другое платежное средство."),
    fraud_suspected(13, "Платеж заблокирован из-за подозрения в мошенничестве.  Используйте другое платежное средство."),
    general_decline(14, "Что-то пошло не так..."),
    identification_required(15, "Вам следует идентифицировать кошелек или выбрать другое платежное средство."),
    insufficient_funds(16, "Не хватает денег для оплаты."),
    invalid_card_number(17, "Неправильно указан номер карты."),
    invalid_csc(18, "Неправильно указан код CVV2 (CVC2, CID)."),
    issuer_unavailable(19, "Эмитент платежного средства недоступен. Повторите платеж позже или используйте другое платежное средство."),
    payment_method_limit_exceeded(20, "Исчерпан лимит платежей."),
    payment_method_restricted(21, "Запрещены операции данным платежным средством.");

    private int code;
    private String description;

    PaymentCancellations(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode()
    {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
