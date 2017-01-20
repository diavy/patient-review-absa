/**
 * Created by xshuai on 1/20/17.
 */
public class Aspect {
    public enum Category {
        DOCTOR_PERFORMANCE,
        CLINIC_ENVIRONMENT,
        BILLING_INSURANCE,
        STAFF_HELPFULLNESS,
        APPOINTMENT_SCHEDULING,
        PRICE,
        WAITING_TIME,
        OTHERS
    }

    public enum Polarity {
        POSITIVE,
        NEGATIVE,
        NEUTRAL
    }

    protected String target;

}
