package engine;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp {
    private Date value;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss:SSS");

    public TimeStamp(Date timeStamp) {
        this.value = timeStamp;
    }

    @Override
    public String toString() {
        return simpleDateFormat.format(value);
    }

    public Date getValue() {
        return value;
    }
}
