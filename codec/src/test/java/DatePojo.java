import java.util.Date;

/**
 * Created by xiaofei.wxf on 14-2-14.
 */
public class DatePojo extends Date{
    private Date privateField = new Date();
    protected Date protectedField = new Date();
    public Date publicField = new Date();

    public Date getPublicField() {
        return publicField;
    }

    public Date getPrivateField() {
        return privateField;
    }

    public Date getProtectedField() {
        return protectedField;
    }

    @Override
    public String toString() {
        return "[privateField=" + privateField + "] [protectedField=" + protectedField + "] [publicField=" + publicField + "]";
    }
}
