package engine;

import java.beans.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp {
    private Date value;

    public TimeStamp(Date timeStamp) {
        this.value = timeStamp;
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("HH:mm:ss:SSS").format(value);
    }

    public Date getValue() {
        return value;
    }
}
